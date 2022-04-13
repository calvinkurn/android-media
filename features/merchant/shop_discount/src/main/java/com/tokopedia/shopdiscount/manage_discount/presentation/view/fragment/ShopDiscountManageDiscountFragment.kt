package com.tokopedia.shopdiscount.manage_discount.presentation.view.fragment

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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.databinding.FragmentManageDiscountBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountAdapter
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountTypeFactoryImpl
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountManageDiscountGlobalErrorViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountSetupProductItemViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.view.viewmodel.ShopDiscountManageDiscountViewModel
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.product_detail.presentation.ShopDiscountProductDetailDividerItemDecoration
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopDiscountManageDiscountFragment : BaseDaggerFragment(),
    ShopDiscountSetupProductItemViewHolder.Listener,
    ShopDiscountManageDiscountGlobalErrorViewHolder.Listener {

    companion object {
        const val REQUEST_ID_ARG = "request_id_arg"
        const val STATUS_ARG = "status_arg"
        const val MODE_ARG = "mode_arg"
        private const val URL_EDU_ABUSIVE_PRODUCT =
            "https://seller.tokopedia.com/edu/ketentuan-baru-diskon-toko/"

        fun createInstance(requestId: String, status: Int, mode: String) =
            ShopDiscountManageDiscountFragment().apply {
                arguments = Bundle().apply {
                    putString(REQUEST_ID_ARG, requestId)
                    putInt(STATUS_ARG, status)
                    putString(MODE_ARG, mode)
                }
            }
    }

    private var viewBinding by autoClearedNullable<FragmentManageDiscountBinding>()
    override fun getScreenName(): String =
        ShopDiscountManageDiscountFragment::class.java.canonicalName.orEmpty()

    private var rvProductList: RecyclerView? = null
    private var containerButtonSubmit: ViewGroup? = null
    private var buttonSubmit: UnifyButton? = null
    private var cardLabelBulkManage: CardUnify2? = null
    private var bulkManageTitle: Typography? = null
    private var headerUnify: HeaderUnify? = null
    private var tickerAbusiveProducts: Ticker? = null


    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var router: FragmentRouter

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ShopDiscountManageDiscountViewModel::class.java) }
    private var requestId: String = ""
    private var status: Int = -1
    private var mode: String = ""

    private val adapter by lazy {
        ShopDiscountManageDiscountAdapter(
            typeFactory = ShopDiscountManageDiscountTypeFactoryImpl(
                this,
                this
            )
        )
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
        init()
        setToolbarTitle()
        setupSubmitButton()
        setupRecyclerView()
        observeLiveData()
        getSetupProductListData()
    }

    private fun setToolbarTitle() {
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
                    hide()
                }
                setPrimaryCTAClickListener {
                    finishActivity()
                }
                show()
            }
        }
    }

    private fun setupSubmitButton() {
        buttonSubmit?.apply {
            isEnabled = false
            setOnClickListener {
                submitProductDiscount()
            }
        }
    }

    private fun submitProductDiscount() {
        viewModel.submitProductDiscount(adapter.getAllProductData(), mode)
    }

    private fun getArgumentsData() {
        arguments?.apply {
            requestId = getString(REQUEST_ID_ARG).orEmpty()
            status = getInt(STATUS_ARG).orZero()
            mode = getString(MODE_ARG).orEmpty()
        }
    }

    private fun init() {
        rvProductList = viewBinding?.rvSetupProductList
        containerButtonSubmit = viewBinding?.containerButtonSubmit
        buttonSubmit = viewBinding?.buttonSubmit
        cardLabelBulkManage = viewBinding?.labelBulkApply?.cardLabelBulkManage
        bulkManageTitle = viewBinding?.labelBulkApply?.textLabelTitle
        headerUnify = viewBinding?.headerUnify
        tickerAbusiveProducts = viewBinding?.tickerAbusiveProducts
    }

    private fun setupRecyclerView() {
        rvProductList?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@ShopDiscountManageDiscountFragment.adapter
            itemAnimator = null
            setDecoration()
        }
    }

    private fun RecyclerView.setDecoration() {
        val dividerDrawable = MethodChecker.getDrawable(
            context,
            R.drawable.shop_discount_manage_discount_bg_line_separator_thin
        )
        val dividerItemDecoration = ShopDiscountProductDetailDividerItemDecoration(dividerDrawable)
        if (itemDecorationCount > 0)
            removeItemDecorationAt(0)
        addItemDecoration(dividerItemDecoration)
    }

    private fun getSetupProductListData() {
        showLoading()
        viewModel.getSetupProductListData(requestId)
    }

    private fun observeLiveData() {
        observeSetupProductListLiveData()
        observeBulkApplyProductListResult()
        observeEnableButtonSubmitLiveData()
        observeResultSubmitProductSlashPriceLiveData()
        observeResultDeleteSlashPriceProductLiveData()
    }

    private fun observeResultDeleteSlashPriceProductLiveData() {
        viewModel.resultDeleteSlashPriceProductLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    val responseHeaderData = it.data.responseHeader
                    if (!responseHeaderData.success) {
                        showToasterError(responseHeaderData.errorMessages.joinToString())
                    } else {
                        showToasterSuccess(getString(R.string.shop_discount_manage_discount_delete_product_message))
                        removeProduct(it.data.productId)
                        checkProductSize()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterError(errorMessage)
                }
            }
        })
    }

    private fun checkProductSize() {
        val totalProduct = getTotalProductSize()
        setupLabelBulkApply(totalProduct)
        if (totalProduct.isZero()) {
            finishActivity()
        }
    }

    private fun showToasterSuccess(message: String) {
        activity?.run {
            view?.let {
                Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
            }
        }
    }

    private fun showToasterError(message: String) {
        activity?.run {
            view?.let { Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
        }
    }

    private fun removeProduct(productId: String) {
        adapter.removeProduct(productId)
    }

    private fun observeResultSubmitProductSlashPriceLiveData() {
        viewModel.resultSubmitProductSlashPriceLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    val responseHeaderData = it.data.responseHeader
                    if (!responseHeaderData.success) {
                        showToasterError(responseHeaderData.errorMessages.joinToString())
                    } else {
                        showToasterSuccess(getString(R.string.shop_discount_manage_discount_submit_product_message))
                        finishActivity()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterError(errorMessage)
                }
            }
        })
    }

    private fun finishActivity() {
        activity?.finish()
    }

    private fun observeEnableButtonSubmitLiveData() {
        viewModel.enableButtonSubmitLiveData.observe(viewLifecycleOwner, {
            buttonSubmit?.isEnabled = it
        })
    }

    private fun observeBulkApplyProductListResult() {
        viewModel.bulkApplyProductListResult.observe(viewLifecycleOwner, {
            clearProductListData()
            setListSetupProductData(it)
            checkButtonSubmit()
        })
    }

    private fun checkButtonSubmit() {
        viewModel.checkShouldEnableButtonSubmit(adapter.getAllProductData())
    }

    private fun clearProductListData() {
        adapter.clearData()
    }

    private fun observeSetupProductListLiveData() {
        viewModel.setupProductListLiveData.observe(viewLifecycleOwner, {
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
                        configTickerAbusiveProducts()
                    }
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        })
    }

    private fun configTickerAbusiveProducts() {
        val totalAbusiveProduct = adapter.getTotalAbusiveProduct()
        if (!totalAbusiveProduct.isZero()) {
            tickerAbusiveProducts?.apply {
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
            }
        }
    }

    private fun redirectToWebView(linkUrl: CharSequence) {
        RouteManager.route(
            context, String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW, linkUrl.toString()
            )
        )
    }

    private fun showErrorState(throwable: Throwable) {
        adapter.showGlobalErrorView(throwable)
    }

    private fun getTotalProductSize(): Int {
        return adapter.getAllProductData().size
    }

    private fun setupLabelBulkApply(totalProduct: Int) {
        cardLabelBulkManage?.apply {
            show()
            cardType = CardUnify2.TYPE_CLEAR
            setOnClickListener {
                openBottomSheetBulkApply()
            }
        }
        bulkManageTitle?.text = String.format(
            getString(R.string.shop_discount_manage_discount_bulk_manage_label_format),
            totalProduct
        )
    }

    private fun openBottomSheetBulkApply() {
        val bottomSheet = DiscountBulkApplyBottomSheet.newInstance()
        bottomSheet.setOnApplyClickListener { bulkApplyDiscountResult ->
            onApplyBulkManage(bulkApplyDiscountResult)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onApplyBulkManage(bulkApplyDiscountResult: DiscountSettings) {
        bulkUpdateProductData(bulkApplyDiscountResult)
    }

    private fun bulkUpdateProductData(bulkApplyDiscountResult: DiscountSettings) {
        val listProductData = adapter.getAllProductData()
        viewModel.applyBulkUpdate(listProductData, bulkApplyDiscountResult)
    }

    private fun showButtonSubmit() {
        containerButtonSubmit?.show()
    }

    private fun setListSetupProductData(data: List<ShopDiscountSetupProductUiModel.SetupProductData>) {
        adapter.addListSetupProductData(data)
    }

    private fun showLoading() {
        adapter.showLoading()
    }

    private fun hideLoading() {
        adapter.hideLoading()
    }

    override fun onClickManageDiscount(model: ShopDiscountSetupProductUiModel.SetupProductData) {
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
                    hide()
                }
                setPrimaryCTAClickListener {
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

}