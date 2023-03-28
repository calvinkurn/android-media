package com.tokopedia.shopdiscount.manage_discount.presentation.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.common.widget.ShopDiscountLabelBulkApply
import com.tokopedia.shopdiscount.databinding.FragmentManageDiscountBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.presentation.container.DiscountedProductManageActivity
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSlashPriceProductSubmissionUiModel
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountAdapter
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountTypeFactoryImpl
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountManageDiscountGlobalErrorViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountSetupProductItemViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.view.viewmodel.ShopDiscountManageViewModel
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountManageProductActivity
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountManageVariantActivity
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showErrorLongDuration
import com.tokopedia.shopdiscount.utils.extension.showToaster
import com.tokopedia.shopdiscount.utils.rv_decoration.ShopDiscountDividerItemDecoration
import com.tokopedia.shopdiscount.utils.tracker.ShopDiscountTracker
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopDiscountManageFragment :
    BaseDaggerFragment(),
    ShopDiscountSetupProductItemViewHolder.Listener,
    ShopDiscountManageDiscountGlobalErrorViewHolder.Listener {

    companion object {
        private const val REQUEST_ID_ARG = "request_id_arg"
        private const val SELECTED_SLASH_PRICE_STATUS_ARG = "selected_slash_price_status_arg"
        private const val MODE_ARG = "mode_arg"
        private const val SELECTED_PRODUCT_VARIANT_ID = "selected_product_variant_id"
        private const val URL_EDU_ABUSIVE_PRODUCT =
            "https://seller.tokopedia.com/edu/ketentuan-baru-diskon-toko/"
        private const val MAX_SUBMITTED_PRODUCT_ERROR_MESSAGES = 5

        fun createInstance(
            requestId: String,
            status: Int,
            mode: String,
            selectedProductVariantId: String
        ) =
            ShopDiscountManageFragment().apply {
                arguments = Bundle().apply {
                    putString(REQUEST_ID_ARG, requestId)
                    putInt(SELECTED_SLASH_PRICE_STATUS_ARG, status)
                    putString(MODE_ARG, mode)
                    putString(SELECTED_PRODUCT_VARIANT_ID, selectedProductVariantId)
                }
            }
    }

    private var viewBinding by autoClearedNullable<FragmentManageDiscountBinding>()
    override fun getScreenName(): String =
        ShopDiscountManageFragment::class.java.canonicalName.orEmpty()

    private var rvProductList: RecyclerView? = null
    private var containerButtonSubmit: ViewGroup? = null
    private var buttonSubmit: UnifyButton? = null
    private var labelBulkApply: ShopDiscountLabelBulkApply? = null
    private var headerUnify: HeaderUnify? = null
    private var tickerAbusiveProducts: Ticker? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracker: ShopDiscountTracker

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ShopDiscountManageViewModel::class.java) }
    private var requestId: String = ""
    private var selectedSlashPriceStatusId: Int = -1
    private var mode: String = ""
    private var selectedProductVariantId: String = ""

    private val adapter by lazy {
        ShopDiscountManageDiscountAdapter(
            typeFactory = ShopDiscountManageDiscountTypeFactoryImpl(
                this,
                this
            )
        )
    }

    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentManageDiscountBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentsData()
        initView()
        configToolbar()
        setupSubmitButton()
        setupRecyclerView()
        observeLiveData()
        getSetupProductListData()
    }

    private fun configToolbar() {
        headerUnify?.apply {
            title = when (mode) {
                ShopDiscountManageDiscountMode.CREATE -> {
                    getString(R.string.shop_discount_manage_discount_manage_toolbar_title)
                }
                ShopDiscountManageDiscountMode.UPDATE -> {
                    getString(R.string.shop_discount_manage_discount_edit_toolbar_title)
                }
                else -> {
                    ""
                }
            }
            setNavigationOnClickListener {
                showDialogOnBackPressed()
            }
        }
    }

    private fun showDialogOnBackPressed() {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val title =
                    getString(R.string.shop_discount_manage_discount_back_press_dialog_title)
                val description =
                    getString(R.string.shop_discount_manage_discount_back_press_dialog_description)
                val primaryCtaText =
                    getString(R.string.shop_discount_manage_discount_back_press_button_cta)
                val secondaryCtaText =
                    getString(R.string.shop_discount_manage_discount_cancel_button_cta)
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCtaText)
                setSecondaryCTAText(secondaryCtaText)
                setSecondaryCTAClickListener {
                    dismiss()
                }
                setPrimaryCTAClickListener {
                    finishActivity()
                    dismiss()
                }
                show()
            }
        }
    }

    private fun setupSubmitButton() {
        buttonSubmit?.apply {
            isEnabled = false
            setOnClickListener {
                if (buttonSubmit?.isLoading == false) {
                    showButtonSubmitLoading()
                    submitProductDiscount()
                }
            }
        }
    }

    private fun showButtonSubmitLoading() {
        buttonSubmit?.isLoading = true
    }

    private fun submitProductDiscount() {
        viewModel.submitProductDiscount(adapter.getAllProductData(), mode)
    }

    private fun getArgumentsData() {
        arguments?.apply {
            requestId = getString(REQUEST_ID_ARG).orEmpty()
            selectedSlashPriceStatusId = getInt(SELECTED_SLASH_PRICE_STATUS_ARG).orZero()
            mode = getString(MODE_ARG).orEmpty()
            selectedProductVariantId = getString(SELECTED_PRODUCT_VARIANT_ID).orEmpty()
        }
    }

    private fun initView() {
        rvProductList = viewBinding?.rvSetupProductList
        containerButtonSubmit = viewBinding?.containerButtonSubmit
        buttonSubmit = viewBinding?.buttonSubmit
        labelBulkApply = viewBinding?.labelBulkApply
        headerUnify = viewBinding?.headerUnify
        tickerAbusiveProducts = viewBinding?.tickerAbusiveProducts
    }

    private fun setupRecyclerView() {
        rvProductList?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@ShopDiscountManageFragment.adapter
            itemAnimator = null
            setDecoration()
        }
    }

    private fun RecyclerView.setDecoration() {
        val dividerDrawable = MethodChecker.getDrawable(
            context,
            R.drawable.shop_discount_manage_discount_bg_line_separator
        )
        val dividerItemDecoration = ShopDiscountDividerItemDecoration(dividerDrawable)
        if (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(dividerItemDecoration)
    }

    private fun getSetupProductListData() {
        showLoading()
        viewModel.getSetupProductListData(requestId, selectedProductVariantId, mode, selectedSlashPriceStatusId)
    }

    private fun observeLiveData() {
        observeSetupProductListLiveData()
        observeBulkApplyProductListResult()
        observeEnableButtonSubmitLiveData()
        observeResultSubmitProductSlashPriceLiveData()
        observeResultDeleteSlashPriceProductLiveData()
    }

    private fun observeResultDeleteSlashPriceProductLiveData() {
        viewModel.resultDeleteSlashPriceProductLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    val responseHeaderData = it.data.responseHeader
                    if (!responseHeaderData.success) {
                        updateProductList()
                        showToasterError(responseHeaderData.errorMessages.joinToString())
                    } else {
                        showToasterSuccess(getString(R.string.shop_discount_manage_discount_delete_product_message))
                        removeProduct(it.data.productId)
                        checkProductSize()
                        updateLabelBulkApply()
                        configTickerAbusiveProducts()
                        checkButtonSubmit()
                    }
                }
                is Fail -> {
                    updateProductList()
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterError(errorMessage)
                }
            }
        }
    }

    private fun updateProductList() {
        adapter.updateProductList()
    }

    private fun checkProductSize() {
        val totalProduct = getTotalProductSize()
        if (totalProduct.isZero()) {
            redirectToManageProductPage(selectedSlashPriceStatusId, "")
        }
    }

    private fun showToasterSuccess(message: String) {
        view.showToaster(message)
    }

    private fun showToasterError(message: String) {
        view.showError(message)
    }

    private fun showToasterErrorSubmitProduct(submittedProductData: ShopDiscountSlashPriceProductSubmissionUiModel) {
        val reason = submittedProductData.responseHeader.reason
        val listError = submittedProductData.listSubmittedProductData.filter {
            !it.success
        }.map {
            val errorMessage = if (it.message.isNotEmpty()) {
                it.message
            } else {
                it.listSubmittedWarehouse.firstOrNull { listSubmittedWarehouse ->
                    listSubmittedWarehouse.message.isNotEmpty()
                }?.message.orEmpty()
            }
            "- ${it.name}, $errorMessage"
        }.take(MAX_SUBMITTED_PRODUCT_ERROR_MESSAGES)
        val errorMessage = "$reason\n${listError.joinToString("\n")}"
        view.showErrorLongDuration(errorMessage)
    }

    private fun removeProduct(productId: String) {
        adapter.removeProduct(productId)
    }

    private fun observeResultSubmitProductSlashPriceLiveData() {
        viewModel.resultSubmitProductSlashPriceLiveData.observe(viewLifecycleOwner) {
            hideButtonSubmitLoading()
            when (it) {
                is Success -> {
                    val responseHeaderData = it.data.responseHeader
                    if (!responseHeaderData.success) {
                        showToasterErrorSubmitProduct(it.data)
                        checkProductDiscountStartDateError()
                    } else {
                        val slashPriceStatusId: Int
                        val successSubmitToasterWording: String
                        tracker.sendClickSaveSlashPrice(mode)
                        when (mode) {
                            ShopDiscountManageDiscountMode.CREATE -> {
                                slashPriceStatusId = DiscountStatus.SCHEDULED
                                successSubmitToasterWording =
                                    getString(R.string.shop_discount_manage_discount_success_create)
                            }
                            else -> {
                                slashPriceStatusId = selectedSlashPriceStatusId
                                successSubmitToasterWording =
                                    getString(R.string.shop_discount_manage_discount_success_update)
                            }
                        }
                        redirectToManageProductPage(
                            slashPriceStatusId,
                            successSubmitToasterWording
                        )
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterError(errorMessage)
                }
            }
        }
    }

    private fun checkProductDiscountStartDateError() {
        viewModel.checkStartDateError(
            adapter.getAllProductData(),
            mode,
            selectedSlashPriceStatusId
        )
    }

    private fun hideButtonSubmitLoading() {
        buttonSubmit?.isLoading = false
    }

    private fun redirectToManageProductPage(
        selectedPriceStatusId: Int,
        successSubmitToasterWording: String
    ) {
        context?.let {
            DiscountedProductManageActivity.start(
                it,
                selectedPriceStatusId,
                successSubmitToasterWording
            )
            finishActivity()
        }
    }

    private fun finishActivity() {
        activity?.finish()
    }

    private fun observeEnableButtonSubmitLiveData() {
        viewModel.enableButtonSubmitLiveData.observe(viewLifecycleOwner) {
            buttonSubmit?.isEnabled = it
        }
    }

    private fun observeBulkApplyProductListResult() {
        viewModel.updatedProductListData.observe(viewLifecycleOwner) {
            clearProductListData()
            setListSetupProductData(it)
            checkButtonSubmit()
        }
    }

    private fun checkButtonSubmit() {
        viewModel.checkShouldEnableButtonSubmit(adapter.getAllProductData())
    }

    private fun clearProductListData() {
        adapter.clearData()
    }

    private fun observeSetupProductListLiveData() {
        viewModel.setupProductListLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    if (!it.data.responseHeader.success) {
                        val errorMessage = it.data.responseHeader.errorMessages.joinToString()
                        showErrorState(Throwable(errorMessage))
                    } else {
                        setListSetupProductData(it.data.listSetupProductData)
                        showButtonSubmit()
                        checkButtonSubmit()
                        checkProductSize()
                        updateLabelBulkApply()
                        configTickerAbusiveProducts()
                    }
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        }
    }

    private fun configTickerAbusiveProducts() {
        val totalAbusiveProduct = adapter.getTotalAbusiveProduct()
        tickerAbusiveProducts?.apply {
            if (!totalAbusiveProduct.isZero()) {
                show()
                setHtmlDescription(
                    String.format(
                        getString(R.string.shop_discount_manage_discount_ticker_abusive_products),
                        totalAbusiveProduct,
                        URL_EDU_ABUSIVE_PRODUCT
                    )
                )
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        redirectToWebView(linkUrl)
                    }

                    override fun onDismiss() {
                    }
                })
            } else {
                hide()
            }
        }
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

    private fun showErrorState(throwable: Throwable) {
        adapter.showGlobalErrorView(throwable)
    }

    private fun getTotalProductSize(): Int {
        return adapter.getAllProductData().size
    }

    private fun updateLabelBulkApply() {
        val totalProduct = adapter.getAllProductData().size
        val labelTitleFormat = getLabelBulkApplyTitleFormat(mode)
        val labelTitle = String.format(labelTitleFormat, totalProduct)
        labelBulkApply?.apply {
            show()
            setTitle(labelTitle)
            setOnClickListener {
                openBottomSheetBulkApply()
            }
        }
    }

    private fun getLabelBulkApplyTitleFormat(mode: String): String {
        return when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                getString(R.string.shop_discount_manage_discount_bulk_manage_label_format_manage)
            }
            ShopDiscountManageDiscountMode.UPDATE -> {
                getString(R.string.shop_discount_manage_discount_bulk_manage_label_format_edit)
            }
            else -> ""
        }
    }

    private fun openBottomSheetBulkApply() {
        val minStartDate = adapter.getMinStartDateOfProductList()
        val maxStartDate = adapter.getMaxStartDateOfProductList()
        val bulkApplyBottomSheetTitle = getBulkApplyBottomSheetTitle(mode)
        val bulkApplyBottomSheetMode = getBulkApplyBottomSheetMode(mode)
        val bottomSheet = DiscountBulkApplyBottomSheet.newInstance(
            bulkApplyBottomSheetTitle,
            bulkApplyBottomSheetMode,
            minStartDate,
            maxStartDate,
            selectedSlashPriceStatusId
        )
        bottomSheet.setOnApplyClickListener { bulkApplyDiscountResult ->
            bulkUpdateProductData(bulkApplyDiscountResult)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun getBulkApplyBottomSheetMode(mode: String): DiscountBulkApplyBottomSheet.Mode {
        return when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                DiscountBulkApplyBottomSheet.Mode.BULK_APPLY
            }
            else -> {
                DiscountBulkApplyBottomSheet.Mode.BULK_UPDATE
            }
        }
    }

    private fun getBulkApplyBottomSheetTitle(mode: String): String {
        return when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                getString(R.string.shop_discount_manage_discount_bulk_apply_create_title)
            }
            ShopDiscountManageDiscountMode.UPDATE -> {
                getString(R.string.shop_discount_manage_discount_bulk_apply_update_title)
            }
            else -> {
                ""
            }
        }
    }

    private fun bulkUpdateProductData(bulkApplyDiscountResult: DiscountSettings) {
        val listProductData = adapter.getAllProductData()
        viewModel.applyBulkUpdate(listProductData, bulkApplyDiscountResult, mode, selectedSlashPriceStatusId)
    }

    private fun showButtonSubmit() {
        containerButtonSubmit?.show()
    }

    private fun setListSetupProductData(data: List<ShopDiscountSetupProductUiModel.SetupProductData>) {
        adapter.addListSetupProductData(data)
    }

    private fun showLoading() {
        adapter.showLoading()
        hideButtonSubmit()
    }

    private fun hideButtonSubmit() {
        containerButtonSubmit?.hide()
    }

    private fun hideLoading() {
        adapter.hideLoading()
        showButtonSubmit()
    }

    override fun onClickManageDiscount(model: ShopDiscountSetupProductUiModel.SetupProductData) {
        if (model.productStatus.isVariant) {
            redirectToManageProductVariantDiscountPage(model)
        } else {
            redirectToManageProductDiscountPage(model)
        }
    }

    private fun redirectToManageProductVariantDiscountPage(model: ShopDiscountSetupProductUiModel.SetupProductData) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalSellerapp.SHOP_DISCOUNT_MANAGE_PRODUCT_VARIANT_DISCOUNT
        )
        intent.putExtra(ShopDiscountManageVariantActivity.MODE_PARAM, mode)
        intent.putExtra(
            ShopDiscountManageVariantActivity.PRODUCT_MANAGE_UI_MODEL,
            model.copy()
        )
        startActivityForResult(
            intent,
            ShopDiscountManageVariantActivity.REQUEST_CODE_APPLY_PRODUCT_VARIANT_DISCOUNT
        )
    }

    private fun redirectToManageProductDiscountPage(model: ShopDiscountSetupProductUiModel.SetupProductData) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalSellerapp.SHOP_DISCOUNT_MANAGE_PRODUCT_DISCOUNT
        )
        intent.putExtra(ShopDiscountManageProductActivity.MODE_PARAM, mode)
        intent.putExtra(
            ShopDiscountManageProductActivity.PRODUCT_MANAGE_UI_MODEL,
            model.copy()
        )
        startActivityForResult(
            intent,
            ShopDiscountManageProductActivity.REQUEST_CODE_APPLY_PRODUCT_DISCOUNT
        )
    }

    override fun onClickDeleteProduct(
        model: ShopDiscountSetupProductUiModel.SetupProductData,
        position: Int
    ) {
        showDialogDeleteProduct(model, position)
    }

    private fun showDialogDeleteProduct(
        model: ShopDiscountSetupProductUiModel.SetupProductData,
        position: Int
    ) {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val title =
                    getString(R.string.shop_discount_manage_discount_delete_product_dialog_title)
                val description =
                    getString(R.string.shop_discount_manage_discount_delete_product_dialog_description)
                val primaryCtaText =
                    getString(R.string.shop_discount_manage_discount_delete_button_cta)
                val secondaryCtaText =
                    getString(R.string.shop_discount_manage_discount_cancel_button_cta)
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCtaText)
                setSecondaryCTAText(secondaryCtaText)
                setSecondaryCTAClickListener {
                    dismiss()
                }
                setPrimaryCTAClickListener {
                    dismiss()
                    showLoading()
                    deleteProductFromList(model.productId, position.toString())
                }
                show()
            }
        }
    }

    private fun deleteProductFromList(productId: String, position: String) {
        viewModel.deleteSlashPriceProduct(productId, position, requestId, mode)
    }

    override fun onGlobalErrorActionClickRetry() {
        getSetupProductListData()
    }

    fun onBackPressed() {
        showDialogOnBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ShopDiscountManageProductActivity.REQUEST_CODE_APPLY_PRODUCT_DISCOUNT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.extras?.getParcelable<ShopDiscountSetupProductUiModel.SetupProductData>(
                        ShopDiscountManageProductActivity.PRODUCT_DATA_RESULT
                    )?.let {
                        applyUpdatedProductData(it)
                    }
                }
            }
            ShopDiscountManageVariantActivity.REQUEST_CODE_APPLY_PRODUCT_VARIANT_DISCOUNT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.extras?.getParcelable<ShopDiscountSetupProductUiModel.SetupProductData>(
                        ShopDiscountManageProductActivity.PRODUCT_DATA_RESULT
                    )?.let {
                        applyUpdatedProductData(it)
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun applyUpdatedProductData(updatedProductData: ShopDiscountSetupProductUiModel.SetupProductData) {
        viewModel.updateSingleProductData(
            adapter.getAllProductData(),
            updatedProductData,
            mode,
            selectedSlashPriceStatusId
        )
    }
}
