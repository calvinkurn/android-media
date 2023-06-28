package com.tokopedia.addon.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.addon.presentation.listener.AddOnComponentListener
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product_bundle.common.util.Utility.animateExpand
import com.tokopedia.product_service_widget.databinding.FragmentBottomsheetAddonBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AddOnFragment: BaseDaggerFragment(), AddOnComponentListener {

    companion object {
        fun newInstance(
            productId: Long,
            pageSource: String,
            cartId: Long,
            selectedAddonIds: List<String>,
            warehouseId: Long,
            isTokocabang: Boolean,
            atcSource: String
        ): AddOnFragment {
            val fragment = AddOnFragment()
            val bundle = Bundle().apply {
                putLong(AddOnExtraConstant.PRODUCT_ID, productId)
                putString(AddOnExtraConstant.PAGE_SOURCE, pageSource)
                putStringArrayList(AddOnExtraConstant.SELECTED_ADDON_IDS, ArrayList(selectedAddonIds))
                putLong(AddOnExtraConstant.CART_ID, cartId)
                putLong(AddOnExtraConstant.WAREHOUSE_ID, warehouseId)
                putBoolean(AddOnExtraConstant.IS_TOKOCABANG, isTokocabang)
                putString(AddOnExtraConstant.ATC_SOURCE, atcSource)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private var binding by autoClearedNullable<FragmentBottomsheetAddonBinding>()
    private var onSaveAddonListener: (aggregatedData: AddOnPageResult) -> Unit = {}
    private var tempSelectedAddons: List<AddOnUIModel> = emptyList()
    private val productId by lazy { arguments?.getLong(AddOnExtraConstant.PRODUCT_ID) }
    private val pageSource by lazy { arguments?.getString(AddOnExtraConstant.PAGE_SOURCE) }
    private val cartId by lazy { arguments?.getLong(AddOnExtraConstant.CART_ID) }
    private val selectedAddonIds by lazy { arguments?.getStringArrayList(AddOnExtraConstant.SELECTED_ADDON_IDS) }
    private val warehouseId by lazy { arguments?.getLong(AddOnExtraConstant.WAREHOUSE_ID) }
    private val isTokocabang by lazy { arguments?.getBoolean(AddOnExtraConstant.IS_TOKOCABANG).orFalse() }
    private val atcSource by lazy { arguments?.getString(AddOnExtraConstant.ATC_SOURCE).orEmpty() }

    override fun getScreenName(): String = AddOnFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
            getAddonData(productId.toString(), warehouseId.toString(), isTokocabang)
        }
        binding?.btnSave?.setOnClickListener {
            binding?.addonWidget?.saveAddOnState(cartId.orZero(), atcSource)
        }
    }

    override fun onAddonComponentError(errorMessage: String) {
        showErrorToaster(errorMessage)
    }

    override fun onAddonComponentClick(index: Int, indexChild: Int, addOnGroupUIModels: List<AddOnGroupUIModel>) {

    }

    override fun onTotalPriceCalculated(price: Long) {
        binding?.tfAddonTotal?.text = price.getCurrencyFormatted()
    }

    override fun onDataEmpty() {
        println("empty")
    }

    override fun onAggregatedDataObtained(aggregatedData: AddOnPageResult.AggregatedData) {
        onSaveAddonListener(AddOnPageResult(tempSelectedAddons, aggregatedData))
    }

    override fun onSaveAddonSuccess(
        selectedAddonIds: List<String>,
        changedAddonSelections: List<AddOnUIModel>,
        addonGroups: List<AddOnGroupUIModel>
    ) {
        binding?.btnSave?.isLoading = false
        binding?.addonWidget?.getAddOnAggregatedData(selectedAddonIds)
        tempSelectedAddons = changedAddonSelections
    }

    override fun onSaveAddonLoading() {
        binding?.btnSave?.isLoading = true
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

    fun setOnSuccessSaveAddonListener(listener: (result: AddOnPageResult ) -> Unit) {
        onSaveAddonListener = listener
    }
}
