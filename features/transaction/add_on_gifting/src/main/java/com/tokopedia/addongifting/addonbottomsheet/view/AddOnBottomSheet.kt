package com.tokopedia.addongifting.addonbottomsheet.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.addonbottomsheet.data.saveaddonstate.SaveAddOnStateResponse
import com.tokopedia.addongifting.addonbottomsheet.view.adapter.AddOnListAdapter
import com.tokopedia.addongifting.addonbottomsheet.view.adapter.AddOnListAdapterTypeFactory
import com.tokopedia.addongifting.addonbottomsheet.view.di.AddOnComponent
import com.tokopedia.addongifting.addonbottomsheet.view.di.DaggerAddOnComponent
import com.tokopedia.addongifting.addonbottomsheet.view.mapper.AddOnResultMapper
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.FragmentUiModel
import com.tokopedia.addongifting.addongallery.AddOnGalleryActivity
import com.tokopedia.addongifting.databinding.LayoutAddOnBottomSheetBinding
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class AddOnBottomSheet(val addOnProductData: AddOnProductData) : BottomSheetUnify(), AddOnActionListener, HasComponent<AddOnComponent> {

    companion object {
        const val DELAY_ADJUST_RECYCLER_VIEW_MARGIN = 200L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var adapter: AddOnListAdapter? = null
    private var viewBinding: LayoutAddOnBottomSheetBinding? = null
    private var measureRecyclerViewPaddingDebounceJob: Job? = null
    private var delayScrollJob: Job? = null
    private var isKeyboardOpened = false

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddOnViewModel::class.java)
    }

    override fun getComponent(): AddOnComponent {
        return DaggerAddOnComponent
                .builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AddOnBottomSheetStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = initializeView(addOnProductData)
        this.viewBinding = viewBinding

        initializeObserver(viewBinding)
        initializeData(addOnProductData)

        val view = super.onCreateView(inflater, container, savedInstanceState)
        addViewTreeObserver(view, viewBinding)

        return view
    }

    private fun addViewTreeObserver(view: View?, viewBinding: LayoutAddOnBottomSheetBinding) {
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff = (view.rootView?.height ?: 0) - view.height
            val displayMetrics = DisplayMetrics()
            val windowManager = context?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            windowManager?.let {
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val heightDiffInDp = heightDiff.pxToDp(displayMetrics)
                if (heightDiffInDp > 150) {
                    if (!isKeyboardOpened) {
                        viewBinding.totalAmount.gone()
                        delayScrollJob?.cancel()
                        delayScrollJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                            delay(200)
                            viewBinding.rvAddOn.smoothScrollBy(0, 200)
                        }
                    }
                    isKeyboardOpened = true
                } else if (isKeyboardOpened) {
                    viewBinding.totalAmount.show()
                    isKeyboardOpened = false
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            return object : BottomSheetDialog(it, theme) {
                override fun onBackPressed() {
                    viewModel.validateCloseAction()
                }
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initializeView(addOnProductData: AddOnProductData): LayoutAddOnBottomSheetBinding {
        val viewBinding = LayoutAddOnBottomSheetBinding.inflate(LayoutInflater.from(context))
        initializeBottomSheet(viewBinding, addOnProductData)
        initializeRecyclerView(viewBinding)

        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutAddOnBottomSheetBinding, addOnProductData: AddOnProductData) {
        var bottomSheetTitle = addOnProductData.bottomSheetTitle
        if (bottomSheetTitle.isEmpty()) {
            bottomSheetTitle = getString(R.string.add_on_bottomsheet_title)
        }
        setTitle(bottomSheetTitle)
        showCloseIcon = true
        showHeader = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setShowListener {
            bottomSheetClose.setOnClickListener {
                viewModel.validateCloseAction()
            }
        }
        setChild(viewBinding.root)
    }

    private fun initializeRecyclerView(viewBinding: LayoutAddOnBottomSheetBinding) {
        val adapterTypeFactory = AddOnListAdapterTypeFactory(this)
        adapter = AddOnListAdapter(adapterTypeFactory)
        viewBinding.rvAddOn.adapter = adapter
        viewBinding.rvAddOn.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initializeData(addOnProductData: AddOnProductData) {
        // Todo : remove mock data before merge to release
        val mockAddOnResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_add_on_response)
        val mockAddOnSavedStateResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_add_on_saved_state_response)
        showLoading()
        viewModel.loadAddOnData(addOnProductData, mockAddOnResponse, mockAddOnSavedStateResponse)
    }

    private fun initializeObserver(viewBinding: LayoutAddOnBottomSheetBinding) {
        observeGlobalEvent(viewBinding)
        observeFragmentData(viewBinding)
    }

    private fun observeGlobalEvent(viewBinding: LayoutAddOnBottomSheetBinding) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when (it.state) {
                    UiEvent.STATE_SUCCESS_LOAD_ADD_ON_DATA -> {
                        hideLoading()
                        renderLayoutNormal(viewBinding)
                    }
                    UiEvent.STATE_FAILED_LOAD_ADD_ON_DATA -> {
                        hideLoading()
                        renderLayoutError(viewBinding, it.throwable)
                    }
                    UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION -> {
                        showCloseConfirmationDialog()
                    }
                    UiEvent.STATE_SUCCESS_SAVE_ADD_ON -> {
                        setResultOnSaveAddOn(it)
                        dismiss()
                    }
                    UiEvent.STATE_FAILED_SAVE_ADD_ON -> {
                        viewBinding.totalAmount.amountCtaView.isLoading = false
                        Toaster.build(viewBinding.bottomsheetContainer,
                                getString(R.string.add_on_toaster_message_failed_save_add_on),
                                Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                                .show()
                    }
                    UiEvent.STATE_DISMISS_BOTTOM_SHEET -> {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun setResultOnSaveAddOn(uiEvent: UiEvent) {
        val data = uiEvent.data
        if (data != null && data is SaveAddOnStateResponse) {
            val resultData = AddOnResultMapper.mapResult(data)
            val result = Intent().apply {
                putExtra(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA_RESULT, resultData)
            }
            activity?.setResult(Activity.RESULT_OK, result)
        }
    }

    private fun observeFragmentData(viewBinding: LayoutAddOnBottomSheetBinding) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.fragmentUiModel.collectLatest {
                if (it.hasLoadedData) {
                    hideLoading()
                    adapter?.updateList(it.recyclerViewItems)
                    renderTotalAmount(viewBinding, it)
                }
            }
        }
    }

    private fun renderTotalAmount(viewBinding: LayoutAddOnBottomSheetBinding, fragmentUiModel: FragmentUiModel) {
        with(viewBinding.totalAmount) {
            setLabelTitle(context.getString(R.string.add_on_label_total_amount))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.totalAmount.addOnTotalPrice, false).removeDecimalSuffix())
            if (fragmentUiModel.totalAmount.addOnTotalQuantity > 0) {
                setCtaText(String.format(context.getString(R.string.add_on_label_total_amount_cta_save_with_quantity), fragmentUiModel.totalAmount.addOnTotalQuantity))
            } else {
                setCtaText(context.getString(R.string.add_on_label_total_amount_cta_save))
            }
            amountCtaView.show()
            amountCtaView.setOnClickListener {
                if (viewModel.hasChangedState()) {
                    // Todo : remove mock data before merge to release
                    val mockSaveAddOnStateResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_save_add_on_state_response)
                    viewBinding.totalAmount.amountCtaView.isLoading = true
                    viewModel.saveAddOnState(addOnProductData, mockSaveAddOnStateResponse)
                } else {
                    dismiss()
                }
            }
        }
    }

    private fun showLoading() {
        adapter?.let {
            it.setLoadingModel(LoadingModel())
            it.showLoading()
        }
        viewBinding?.totalAmount?.isTotalAmountLoading = true
    }

    private fun hideLoading() {
        adapter?.hideLoading()
        viewBinding?.totalAmount?.isTotalAmountLoading = false
    }

    private fun adjustRecyclerViewPaddingBottom(viewBinding: LayoutAddOnBottomSheetBinding) {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        measureRecyclerViewPaddingDebounceJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(DELAY_ADJUST_RECYCLER_VIEW_MARGIN)
            adjustRecyclerViewPadding(viewBinding)
        }
    }

    private fun adjustRecyclerViewPadding(viewBinding: LayoutAddOnBottomSheetBinding) {
        with(viewBinding) {
            if (rvAddOn.canScrollVertically(-1) || rvAddOn.canScrollVertically(1) || isBottomSheetFullPage(viewBinding)) {
                rvAddOn.setPadding(0, 0, 0, rvAddOn.resources?.getDimensionPixelSize(R.dimen.dp_32)
                        ?: 0)
            } else {
                rvAddOn.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun isBottomSheetFullPage(viewBinding: LayoutAddOnBottomSheetBinding): Boolean {
        val displayMetrics = Resources.getSystem().displayMetrics
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val bottomSheetHeight = (bottomSheetWrapper.parent as? View)?.height ?: 0
        val recyclerViewPaddingBottom = viewBinding.rvAddOn.resources?.getDimensionPixelSize(R.dimen.dp_32)
                ?: 0
        val displayHeight = displayMetrics?.heightPixels ?: 0
        return bottomSheetHeight != 0 && displayHeight != 0 && (bottomSheetHeight + (recyclerViewPaddingBottom / 2)) >= displayHeight
    }

    private fun renderLayoutNormal(viewBinding: LayoutAddOnBottomSheetBinding) {
        with(viewBinding) {
            globalError.gone()
            totalAmount.show()
            rvAddOn.show()
        }
    }

    private fun renderLayoutError(viewBinding: LayoutAddOnBottomSheetBinding, throwable: Throwable?) {
        with(viewBinding) {
            throwable?.let {
                globalError.setType(getGlobalErrorType(throwable))
                globalError.setActionClickListener {
                    renderLayoutNormal(viewBinding)
                    initializeData(addOnProductData)
                }
            }
            globalError.show()
            totalAmount.gone()
            rvAddOn.gone()
        }
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun showCloseConfirmationDialog() {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.add_on_label_dialog_confirmation_title))
                setDescription(getString(R.string.add_on_label_dialog_confirmation_description))
                setPrimaryCTAText(getString(R.string.add_on_label_dialog_confirmation_primary_cta))
                setSecondaryCTAText(getString(R.string.add_on_label_dialog_confirmation_secondary_cta))
                setPrimaryCTAClickListener {
                    this@AddOnBottomSheet.dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        measureRecyclerViewPaddingDebounceJob?.cancel()
        delayScrollJob?.cancel()
        activity?.finish()
        activity?.overridePendingTransition(android.R.anim.fade_in, R.anim.push_down)
        super.onDismiss(dialog)
    }

    override fun onCheckBoxCheckedChanged(addOnUiModel: AddOnUiModel) {
        viewBinding?.let {
            adjustRecyclerViewPaddingBottom(it)
        }
        viewModel.updateFragmentUiModel(addOnUiModel)
    }

    override fun onAddOnImageClicked(addOnUiModel: AddOnUiModel) {
        val intent = Intent(context, AddOnGalleryActivity::class.java)
        val addOnImages = arrayListOf<String>()
        addOnImages.addAll(addOnUiModel.addOnAllImageUrls)
        intent.putStringArrayListExtra(AddOnConstant.EXTRA_ADD_ON_IMAGES, addOnImages)
        intent.putExtra(AddOnConstant.EXTRA_ADD_ON_NAME, addOnUiModel.addOnName)
        intent.putExtra(AddOnConstant.EXTRA_ADD_ON_PRICE, addOnUiModel.addOnPrice)
        startActivity(intent)
    }
}