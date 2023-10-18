package com.tokopedia.addon.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.addon.di.DaggerAddOnComponent
import com.tokopedia.addon.presentation.listener.AddOnComponentListener
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnMapper
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.addon.presentation.uimodel.AddOnParam
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.addon.tracking.AddOnBottomsheetTracking
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product_bundle.common.util.Utility.animateExpand
import com.tokopedia.product_service_widget.databinding.FragmentBottomsheetAddonBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AddOnFragment : BaseDaggerFragment(), AddOnComponentListener {

    companion object {
        fun newInstance(
            addOnWidgetParam: AddOnParam,
            pageSource: String,
            cartId: Long,
            selectedAddonIds: List<String>,
            deselectedAddonIds: List<String>,
            atcSource: String
        ): AddOnFragment {
            val fragment = AddOnFragment()
            val bundle = Bundle().apply {
                putParcelable(AddOnExtraConstant.ADDON_WIDGET_PARAM, addOnWidgetParam)
                putString(AddOnExtraConstant.PAGE_SOURCE, pageSource)
                putStringArrayList(AddOnExtraConstant.SELECTED_ADDON_IDS, ArrayList(selectedAddonIds))
                putStringArrayList(AddOnExtraConstant.DESELECTED_ADDON_IDS, ArrayList(deselectedAddonIds))
                putLong(AddOnExtraConstant.CART_ID, cartId)
                putString(AddOnExtraConstant.ATC_SOURCE, atcSource)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private var binding by autoClearedNullable<FragmentBottomsheetAddonBinding>()
    private var onSaveAddonListener: (aggregatedData: AddOnPageResult) -> Unit = {}
    private var tempChangedAddons: List<AddOnUIModel> = emptyList()
    private val addOnWidgetParam: AddOnParam? by lazy {
        arguments?.getParcelable(AddOnExtraConstant.ADDON_WIDGET_PARAM) }
    private val pageSource by lazy { arguments?.getString(AddOnExtraConstant.PAGE_SOURCE) }
    private val cartId by lazy { arguments?.getLong(AddOnExtraConstant.CART_ID) }
    private val selectedAddonIds by lazy { arguments?.getStringArrayList(AddOnExtraConstant.SELECTED_ADDON_IDS) }
    private val deselectedAddonIds by lazy { arguments?.getStringArrayList(AddOnExtraConstant.DESELECTED_ADDON_IDS) }
    private val atcSource by lazy { arguments?.getString(AddOnExtraConstant.ATC_SOURCE).orEmpty() }

    @Inject
    lateinit var tracker: AddOnBottomsheetTracking

    override fun getScreenName(): String = AddOnFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerAddOnComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomsheetAddonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.addonWidget?.apply {
            setListener(this@AddOnFragment)
            setSelectedAddons(selectedAddonIds.orEmpty())
            setDeselectedAddons(deselectedAddonIds.orEmpty())
            getAddonData(addOnWidgetParam ?: AddOnParam())
        }
        binding?.btnSave?.setOnClickListener {
            binding?.addonWidget?.saveAddOnState(cartId.orZero(), atcSource)
        }
    }

    override fun onAddonComponentError(errorMessage: String) {
        showErrorToaster(errorMessage)
    }

    override fun onAddonComponentClick(index: Int, indexChild: Int, addOnGroupUIModels: List<AddOnGroupUIModel>) {
        tracker.sendClickAddonOptionEvent(addOnGroupUIModels, cartId.orZero().toString(), pageSource.orEmpty())
    }

    override fun onTotalPriceCalculated(price: Long) {
        binding?.tfAddonTotal?.text = price.getCurrencyFormatted()
    }

    override fun onDataEmpty() {
        // no-op
    }

    override fun onAggregatedDataObtained(aggregatedData: AddOnPageResult.AggregatedData) {
        onSaveAddonListener(AddOnPageResult(tempChangedAddons, aggregatedData))
    }

    override fun onSaveAddonSuccess(
        selectedAddonIds: List<String>,
        changedAddonSelections: List<AddOnUIModel>,
        addonGroups: List<AddOnGroupUIModel>
    ) {

        val addOnTypes = AddOnMapper.getSelectedAddonsTypes(addonGroups)
        tempChangedAddons = changedAddonSelections
        binding?.btnSave?.isLoading = false
        binding?.addonWidget?.getAddOnAggregatedData(
            selectedAddonIds,
            addOnTypes,
            addOnWidgetParam ?: AddOnParam()
        )
    }

    override fun onSaveAddonLoading() {
        binding?.btnSave?.isLoading = true
        val addonList = binding?.addonWidget?.getFetchedAddon().orEmpty()
        tracker.sendClickSimpanAddonEvent(addonList, cartId.orZero().toString(), pageSource.orEmpty())
    }

    override fun onSaveAddonFailed(errorMessage: String) {
        binding?.btnSave?.isLoading = false
        showErrorToaster(errorMessage)
    }

    private fun showErrorToaster(errorMessage: String) {
        binding?.apply {
            layoutError.animateExpand()
            tfError.text = errorMessage
            tfErrorAction.setOnClickListener {
                layoutError.gone()
            }
        }
    }

    fun setOnSuccessSaveAddonListener(listener: (result: AddOnPageResult) -> Unit) {
        onSaveAddonListener = listener
    }

    fun triggerCloseButtonTracker() {
        val addonList = binding?.addonWidget?.getFetchedAddon().orEmpty()
        tracker.sendClickCloseBottomsheetEvent(addonList, cartId.orZero().toString(), pageSource.orEmpty())
    }
}
