package com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountSubsidyOptOutReasonBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountManageProductSubsidyUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountOptOutReasonUiModel
import com.tokopedia.shopdiscount.subsidy.presentation.adapter.ShopDiscountOptOutReasonAdapter
import com.tokopedia.shopdiscount.subsidy.presentation.adapter.viewholder.ShopDiscountOptOutOptionItemViewHolder
import com.tokopedia.shopdiscount.subsidy.presentation.viewmodel.ShopDiscountOptOutReasonBottomSheetViewModel
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.tracker.ShopDiscountTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopDiscountSubsidyOptOutReasonBottomSheet : BottomSheetUnify(),
    ShopDiscountOptOutOptionItemViewHolder.Listener {

    private var viewBinding by autoClearedNullable<LayoutBottomSheetShopDiscountSubsidyOptOutReasonBinding>()
    private val textDescription: Typography?
        get() = viewBinding?.textOptOutDescription
    private val textOptionTitle: Typography?
        get() = viewBinding?.textOptOutOptionTitle
    private val rvReasonList: RecyclerView?
        get() = viewBinding?.rvReasonList
    private val buttonCancel: UnifyButton?
        get() = viewBinding?.btnCancel
    private val buttonSubmit: UnifyButton?
        get() = viewBinding?.btnSubmit
    private val loader: View?
        get() = viewBinding?.loader
    private val globalError: GlobalError?
        get() = viewBinding?.globalError
    private val rvAdapter: ShopDiscountOptOutReasonAdapter by lazy {
        ShopDiscountOptOutReasonAdapter(this)
    }
    private var onDismissBottomSheetAfterFinishActionListener: ((String, List<String>, String) -> Unit)? =
        null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracker: ShopDiscountTracker
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider[ShopDiscountOptOutReasonBottomSheetViewModel::class.java]
    }
    private var data: ShopDiscountManageProductSubsidyUiModel =
        ShopDiscountManageProductSubsidyUiModel()

    companion object {
        private const val DATA = "data"
        private const val DISMISS_FROM_CANCEL_BUTTON = "batal"
        private const val DISMISS_FROM_BOTTOM_SHEET_CLOSE_BUTTON = "x"
        private const val DISMISS_FROM_OVERLAY = "empty"

        fun newInstance(
            data: ShopDiscountManageProductSubsidyUiModel
        ): ShopDiscountSubsidyOptOutReasonBottomSheet {
            return ShopDiscountSubsidyOptOutReasonBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, data)
                }
            }
        }
    }

    fun setOnDismissBottomSheetAfterFinishActionListener(listener: (String, List<String>, String) -> Unit) {
        onDismissBottomSheetAfterFinishActionListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        getArgumentData()
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    @SuppressLint("DeprecatedMethod")
    private fun getArgumentData() {
        arguments?.let {
            data = it.getParcelable(DATA) ?: ShopDiscountManageProductSubsidyUiModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDescriptionSection()
        if (data.selectedProductToOptOut.isNotEmpty()) {
            sendImpressionSubsidyOptOutReasonBottomSheetTracker()
            observeListOptOutReasonLiveData()
            observeDoOptOutSubsidyResultLiveData()
            setRecyclerView()
            getOptOutReasonList()
            setButtonSubmit()
            setButtonCancel()
        } else {
            sendImpressionNonSubsidyOptOutReasonBottomSheetTracker()
            showDescriptionSection()
        }
    }

    private fun sendImpressionSubsidyOptOutReasonBottomSheetTracker() {
        tracker.sendImpressionSubsidyOptOutReasonBottomSheetEvent(data.entrySource.value)
    }

    private fun sendImpressionNonSubsidyOptOutReasonBottomSheetTracker() {
        tracker.sendImpressionNonSubsidyOptOutReasonBottomSheetEvent(
            data.listProductDetailData.map { it.productId },
        )
    }

    private fun observeDoOptOutSubsidyResultLiveData() {
        viewModel.doOptOutSubsidyResultLiveData.observe(viewLifecycleOwner) {
            hideLoadingButtonSubmit()
            when (it) {
                is Success -> {
                    onSuccessDoOptOutSubsidy()
                }

                is Fail -> {
                    onFailDoOptOutSubsidy(it.throwable)
                }
            }
        }
    }

    private fun onSuccessDoOptOutSubsidy() {
        onDismissBottomSheetAfterFinishActionListener?.invoke(
            data.mode,
            data.getListProductParentId(),
            "Produkmu berhasil keluar dari subsidi"
        )
        dismiss()
    }

    private fun onFailDoOptOutSubsidy(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        view?.rootView?.showError(errorMessage)
    }

    private fun setButtonCancel() {
        buttonCancel?.setOnClickListener {
            sendDismissOptOutReasonBottomSheetTracker(DISMISS_FROM_CANCEL_BUTTON)
            dismiss()
        }
    }

    private fun setButtonSubmit() {
        buttonSubmit?.apply {
            setOnClickListener {
                sendClickButtonSubmitOptOutReasonTracker()
                doOptOutProductSubsidy()
            }
            text = if (data.isAllSelectedProductFullSubsidy()) {
                getString(R.string.sd_subsidy_opt_out_reason_bottom_sheet_delete_mode_submit_text)
            } else {
                when (data.mode) {
                    ShopDiscountManageDiscountMode.UPDATE -> {
                        getString(R.string.sd_subsidy_opt_out_reason_bottom_sheet_edit_mode_submit_text)
                    }

                    ShopDiscountManageDiscountMode.DELETE -> {
                        getString(R.string.sd_subsidy_opt_out_reason_bottom_sheet_delete_mode_submit_text)
                    }

                    else -> {
                        getString(R.string.sd_subsidy_opt_out_reason_bottom_sheet_default_mode_submit_text)

                    }
                }
            }

        }
        configButtonState()
    }

    private fun sendClickButtonSubmitOptOutReasonTracker() {
        tracker.sendClickButtonSubmitOptOutReasonEvent(
            data.entrySource.value,
            buttonSubmit?.text.toString(),
            data.selectedProductToOptOut.map { it.productId }
        )
    }

    private fun doOptOutProductSubsidy() {
        showLoadingButtonSubmit()
        viewModel.doOptOutProductSubsidy(
            data.getSelectedProductToOptOutEventId(),
            rvAdapter.getSelectedReason(),
        )
    }

    private fun showLoadingButtonSubmit() {
        buttonSubmit?.apply {
            isLoading = true
        }
    }

    private fun hideLoadingButtonSubmit() {
        buttonSubmit?.apply {
            isLoading = false
        }
    }

    private fun configButtonState() {
        buttonSubmit?.apply {
            isEnabled = rvAdapter.isReasonSelected()
        }
    }

    private fun showDescriptionSection() {
        textDescription?.show()
    }

    private fun observeListOptOutReasonLiveData() {
        viewModel.listOptOutReasonLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetListOptOutReason(it.data)
                }

                is Fail -> {
                    onFailGetListOptOutReason(it.throwable)
                }
            }
        }
    }

    private fun onFailGetListOptOutReason(throwable: Throwable) {
        hideContent()
        setGlobalError(throwable)
    }

    private fun setGlobalError(throwable: Throwable) {
        context?.let {
            globalError?.apply {
                errorSecondaryAction.hide()
                val errorMessage = ErrorHandler.getErrorMessage(it, throwable)
                errorTitle.text = errorMessage
                setActionClickListener {
                    getOptOutReasonList()
                }
            }
        }
    }

    private fun hideContent() {
        globalError?.show()
        textDescription?.hide()
        textOptionTitle?.hide()
        rvReasonList?.hide()
        buttonCancel?.hide()
        buttonSubmit?.hide()
    }

    private fun onSuccessGetListOptOutReason(listReason: List<ShopDiscountOptOutReasonUiModel>) {
        showContent()
        setReasonData(listReason)
    }

    private fun showContent() {
        globalError?.hide()
        textDescription?.show()
        textOptionTitle?.show()
        rvReasonList?.show()
        buttonCancel?.show()
        buttonSubmit?.show()
    }

    private fun setReasonData(listReason: List<ShopDiscountOptOutReasonUiModel>) {
        rvAdapter.setListReasonData(listReason)
    }


    private fun getOptOutReasonList() {
        if (data.selectedProductToOptOut.isEmpty()) return
        showLoading()
        viewModel.getListOptOutReason()
    }

    private fun showLoading() {
        loader?.show()
    }

    private fun hideLoading() {
        loader?.hide()
    }

    private fun setRecyclerView() {
        rvReasonList?.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = null
        }
    }

    private fun setDescriptionSection() {
        textDescription?.apply {
            if (data.selectedProductToOptOut.isEmpty()) {
                text = getString(R.string.sd_subsidy_opt_out_bottom_sheet_desc_no_product)
            }else{
                val htmlDescription = HtmlLinkHelper(
                    context,
                    getString(
                        R.string.sd_subsidy_opt_out_bottom_sheet_desc_with_product,
                        data.getCtaLink()
                    )
                )
                movementMethod = LinkMovementMethod.getInstance()
                text = htmlDescription.spannedString
                htmlDescription.urlList.firstOrNull()?.setOnClickListener {
                    sendClickEduArticleTracker()
                    redirectToWebView(htmlDescription.urlList.firstOrNull()?.linkUrl.orEmpty())
                }
            }
        }
    }

    private fun sendClickEduArticleTracker() {
        tracker.sendClickEduArticleOptOutReasonBottomSheetEvent(data.entrySource.value)
    }

    private fun redirectToWebView(linkUrl: CharSequence) {
        RouteManager.route(
            context,
            String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW,
                linkUrl.toString()
            )
        )
    }

    private fun getDescriptionText(): CharSequence {
        return if (data.selectedProductToOptOut.isEmpty()) {
            getString(R.string.sd_subsidy_opt_out_bottom_sheet_desc_no_product)
        } else {
            context?.let {
                HtmlLinkHelper(
                    it,
                    getString(
                        R.string.sd_subsidy_opt_out_bottom_sheet_desc_with_product,
                        data.getCtaLink()
                    )
                ).spannedString
            } ?: ""
        }
    }

    private fun setupBottomSheet() {
        viewBinding = LayoutBottomSheetShopDiscountSubsidyOptOutReasonBinding.inflate(
            LayoutInflater.from(context)
        ).apply {
            val bottomSheetTitle = getBottomSheetTitle()
            setTitle(bottomSheetTitle)
            val rootView = this.root
            setChild(rootView)
            setCloseClickListener {
                sendDismissOptOutReasonBottomSheetTracker(DISMISS_FROM_BOTTOM_SHEET_CLOSE_BUTTON)
                dismiss()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        sendDismissOptOutReasonBottomSheetTracker(DISMISS_FROM_OVERLAY)
        super.onCancel(dialog)
    }

    private fun sendDismissOptOutReasonBottomSheetTracker(dismissSource: String) {
        tracker.sendDismissOptOutReasonBottomSheetEvent(
            data.entrySource.value,
            dismissSource,
            data.selectedProductToOptOut.map { it.productId }
        )
    }

    private fun getBottomSheetTitle(): String {
        return if (data.selectedProductToOptOut.isEmpty()) {
            getString(R.string.sd_subsidy_opt_out_bottom_sheet_title_no_product)
        } else {
            getString(R.string.sd_subsidy_opt_out_bottom_sheet_title_with_product)
        }
    }

    override fun onReasonSelected(uiModel: ShopDiscountOptOutReasonUiModel, isSelected: Boolean) {
        rvAdapter.selectReason(uiModel, isSelected)
        configButtonState()
    }

    override fun onCustomReasonChanged(
        uiModel: ShopDiscountOptOutReasonUiModel,
        customReason: String,
        inputError: Boolean
    ) {
        rvAdapter.setCustomReason(uiModel, customReason, inputError)
        configButtonState()
    }

}
