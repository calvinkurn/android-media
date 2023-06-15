package com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.common.widget.ShopDiscountLabelBulkApply
import com.tokopedia.shopdiscount.databinding.FragmentManageProductVariantDiscountBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountManageVariantActivity
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountMultiLocEduActivity
import com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter.ShopDiscountManageProductVariantDiscountAdapter
import com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter.ShopDiscountManageProductVariantDiscountTypeFactoryImpl
import com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter.viewholder.ShopDiscountManageProductVariantItemViewHolder
import com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel.ShopDiscountManageVariantViewModel
import com.tokopedia.shopdiscount.utils.rv_decoration.ShopDiscountDividerItemDecoration
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopDiscountManageVariantFragment :
    BaseDaggerFragment(),
    ShopDiscountManageProductVariantItemViewHolder.Listener,
    ShopDiscountManageVariantFragmentListener {

    companion object {
        const val PRODUCT_DATA_ARG = "product_data_arg"
        const val MODE_ARG = "mode_arg"

        fun createInstance(
            mode: String,
            productData: ShopDiscountSetupProductUiModel.SetupProductData
        ) = ShopDiscountManageVariantFragment().apply {
            arguments = Bundle().apply {
                putString(MODE_ARG, mode)
                putParcelable(PRODUCT_DATA_ARG, productData)
            }
        }
    }

    private var viewBinding by autoClearedNullable<FragmentManageProductVariantDiscountBinding>()
    override fun getScreenName(): String =
        ShopDiscountManageVariantFragment::class.java.canonicalName.orEmpty()

    private val adapter by lazy {
        ShopDiscountManageProductVariantDiscountAdapter(
            typeFactory = ShopDiscountManageProductVariantDiscountTypeFactoryImpl(
                this,
                this,
                mode
            )
        )
    }

    private var mode: String = ""
    private var headerUnify: HeaderUnify? = null
    private var rvContent: RecyclerView? = null
    private var buttonApply: UnifyButton? = null

    private var loadingSpinner: View? = null
    private var containerProductDataLayout: View? = null
    private var tickerMultiLoc: Ticker? = null
    private var imageProduct: ImageUnify? = null
    private var textProductName: Typography? = null
    private var labelTotalVariant: Label? = null
    private var textStockAndLocation: Typography? = null
    private var imageIconProduct: ImageUnify? = null
    private var labelBulkApply: ShopDiscountLabelBulkApply? = null
    private var divider: DividerUnify? = null
    private var layoutGlobalError: GlobalError? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider.get(
            ShopDiscountManageVariantViewModel::class.java
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
        viewBinding =
            FragmentManageProductVariantDiscountBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentsData()
        observeLiveData()
        initView()
        setupRecyclerView()
        setupToolbar()
        setupButtonApply()
        getSellerSlashPriceBenefit()
    }

    private fun setupRecyclerView() {
        rvContent?.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@ShopDiscountManageVariantFragment.adapter
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

    private fun setupButtonApply() {
        buttonApply?.setOnClickListener {
            val listVariantUiModel = adapter.getListVariantItemUiModel()
            viewModel.updateProductDataBasedOnVariantUiModel(listVariantUiModel)
            val productData = viewModel.getProductData()
            val intentData = Intent().apply {
                putExtra(
                    ShopDiscountManageVariantActivity.PRODUCT_DATA_RESULT,
                    productData
                )
            }
            activity?.setResult(Activity.RESULT_OK, intentData)
            activity?.finish()
        }
    }

    private fun configDiscountPeriodData(slashPriceBenefitData: ShopDiscountSellerInfoUiModel) {
        viewModel.updateProductVariantDiscountPeriodData(slashPriceBenefitData)
    }

    private fun observeLiveData() {
        observeSellerBenefitLiveData()
        observeIsEnableSubmitButton()
        observeIsEnableBulkApplyVariant()
        observeBulkApplyListVariantItemUiModel()
    }

    private fun observeBulkApplyListVariantItemUiModel() {
        viewModel.bulkApplyListVariantItemUiModel.observe(viewLifecycleOwner) { updatedListVariantProductData ->
            updatedListVariantProductData?.let {
                clearListVariantProductData()
                adapter.updateListVariantProductData(it)
                checkShouldEnableButtonSubmit()
            }
        }
    }

    private fun clearListVariantProductData() {
        adapter.clearData()
    }

    private fun observeIsEnableBulkApplyVariant() {
        viewModel.totalEnableBulkApplyVariant.observe(viewLifecycleOwner) { totalEnabledVariant ->
            totalEnabledVariant?.let {
                setLabelBulkApply(it)
            }
        }
    }

    private fun observeIsEnableSubmitButton() {
        viewModel.isEnableSubmitButton.observe(viewLifecycleOwner) {
            buttonApply?.isEnabled = it
        }
    }

    private fun observeSellerBenefitLiveData() {
        viewModel.slashPriceBenefitLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    checkShouldShowLabelBulkApply(viewModel.getProductData())
                    showProductDataContent()
                    setProductHeaderSection()
                    setVariantSection(it.data)
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showErrorState(errorMessage)
                }
            }
        }
    }

    private fun checkShouldShowLabelBulkApply(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val isShowLabelBulkApply = productData.listProductVariant.size > Int.ONE
        labelBulkApply?.shouldShowWithAction(isShowLabelBulkApply) {}
        divider?.shouldShowWithAction(isShowLabelBulkApply) {}
    }

    private fun setVariantSection(slashPriceBenefitData: ShopDiscountSellerInfoUiModel) {
        configDiscountPeriodData(slashPriceBenefitData)
        val productData = viewModel.getProductData()
        adapter.addVariantSectionData(productData)
    }

    private fun setProductHeaderSection() {
        val productData = viewModel.getProductData()
        configTickerMultiLoc(productData)
        imageProduct?.loadImage(productData.productImageUrl)
        textProductName?.text = productData.productName
        labelTotalVariant?.apply {
            show()
            setLabel(
                String.format(
                    getString(R.string.shop_discount_manage_discount_total_variant_format),
                    productData.mappedResultData.totalVariant.toString()
                )
            )
        }
        setTotalStockAndLocationData(productData)
    }

    private fun configTickerMultiLoc(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val isMultiLoc = productData.productStatus.isMultiLoc
        tickerMultiLoc?.apply {
            if (isMultiLoc) {
                show()
                val description = getString(
                    R.string.shop_discount_manage_product_discount_multi_loc_ticker_desc
                )
                closeButtonVisibility = View.VISIBLE
                setHtmlDescription(description)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        redirectToMultiLocEduPage()
                    }

                    override fun onDismiss() {
                    }
                })
            } else {
                hide()
            }
        }
    }

    private fun setTotalStockAndLocationData(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val totalStock = productData.stock.toIntOrZero()
        if (totalStock.isZero()) {
            imageIconProduct?.hide()
            textStockAndLocation?.hide()
        } else {
            imageIconProduct?.show()
            textStockAndLocation?.apply {
                show()
                text = MethodChecker.fromHtml(getTotalStockAndLocationData(productData))
            }
        }
    }

    private fun setLabelBulkApply(totalSelectedVariant: Int) {
        val isLabelEnabled: Boolean
        val title: String
        if (totalSelectedVariant.isMoreThanZero()) {
            isLabelEnabled = true
            val titleFormat = getLabelBulkApplyTitleFormat(mode)
            title = String.format(
                titleFormat,
                totalSelectedVariant
            )
        } else {
            isLabelEnabled = false
            title = getLabelBulkApplyTitleDisabled(mode)
        }
        labelBulkApply?.apply {
            isEnabled = isLabelEnabled
            setTitle(title)
            setOnClickListener {
                if (isEnabled) {
                    openBottomSheetBulkApply()
                }
            }
        }
    }

    private fun getLabelBulkApplyTitleDisabled(mode: String): String {
        return when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                getString(R.string.shop_discount_manage_product_variant_discount_label_bulk_apply_disabled_manage)
            }
            ShopDiscountManageDiscountMode.UPDATE -> {
                getString(R.string.shop_discount_manage_product_variant_discount_label_bulk_apply_disabled_edit)
            }
            else -> ""
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
        val slashPriceStatus = viewModel.getProductData().productStatus.selectedSlashPriceStatus
        val bulkApplyBottomSheetMode = getBulkApplyBottomSheetMode(mode)
        val bottomSheet = DiscountBulkApplyBottomSheet.newInstance(
            bulkApplyBottomSheetTitle,
            bulkApplyBottomSheetMode,
            minStartDate,
            maxStartDate,
            slashPriceStatus
        )
        bottomSheet.setOnApplyClickListener { bulkApplyDiscountResult ->
            onApplyBulkManage(bulkApplyDiscountResult)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onApplyBulkManage(bulkApplyDiscountResult: DiscountSettings) {
        bulkUpdateProductData(bulkApplyDiscountResult)
    }

    private fun bulkUpdateProductData(bulkApplyDiscountResult: DiscountSettings) {
        val listVariantItemUiModel = adapter.getListVariantItemUiModel()
        viewModel.applyBulkUpdate(listVariantItemUiModel, bulkApplyDiscountResult)
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

    private fun getTotalStockAndLocationData(
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ): String {
        val totalStock = productData.stock
        return when {
            shouldUseVariantMultiLocTotalStockFormat(productData) -> {
                String.format(
                    getString(R.string.sd_total_stock_various_multiple_location),
                    totalStock
                )
            }
            else -> {
                String.format(
                    getString(R.string.sd_total_stock),
                    totalStock
                )
            }
        }
    }

    private fun shouldUseVariantMultiLocTotalStockFormat(
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return productData.productStatus.isVariant && productData.productStatus.isMultiLoc
    }

    private fun redirectToMultiLocEduPage() {
        context?.let {
            val intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalSellerapp.SHOP_DISCOUNT_MANAGE_PRODUCT_MULTI_LOC_DISCOUNT
            )
            intent.putExtra(ShopDiscountMultiLocEduActivity.MODE_PARAM, mode)
            startActivity(intent)
        }
    }

    private fun showErrorState(errorMessage: String) {
        layoutGlobalError?.apply {
            show()
            errorSecondaryAction.hide()
            errorTitle.text = errorMessage
            setActionClickListener {
                getSellerSlashPriceBenefit()
            }
        }
    }

    private fun showProductDataContent() {
        containerProductDataLayout?.show()
    }

    private fun hideLoading() {
        loadingSpinner?.hide()
    }

    private fun getSellerSlashPriceBenefit() {
        showLoading()
        hideErrorState()
        viewModel.getSlashPriceBenefit()
    }

    private fun hideErrorState() {
        layoutGlobalError?.hide()
    }

    private fun showLoading() {
        loadingSpinner?.show()
    }

    private fun getArgumentsData() {
        arguments?.let {
            mode = it.getString(MODE_ARG).orEmpty()
            viewModel.setProductData(
                it.getParcelable(PRODUCT_DATA_ARG)
                    ?: ShopDiscountSetupProductUiModel.SetupProductData()
            )
        }
    }

    private fun initView() {
        viewBinding?.let {
            headerUnify = it.headerUnify
            containerProductDataLayout = it.containerProductDataLayout
            viewBinding?.shopDiscountVariantHeaderLayout?.let { headerLayoutBinding ->
                tickerMultiLoc = headerLayoutBinding.layoutTickerMultiLoc.tickerMultiLoc
                headerLayoutBinding.layoutProductDiscountHeader.let { headerContainerBinding ->
                    imageProduct = headerContainerBinding.imgProduct
                    textProductName = headerContainerBinding.textProductName
                    labelTotalVariant = headerContainerBinding.labelTotalVariant
                    textStockAndLocation = headerContainerBinding.textTotalStockAndLocation
                    imageIconProduct = headerContainerBinding.iconProduct
                }
                labelBulkApply = headerLayoutBinding.labelBulkApply
                divider = headerLayoutBinding.divider
            }
            rvContent = it.rvManageProductVariantDiscount
            loadingSpinner = it.loadingSpinner
            layoutGlobalError = it.layoutGlobalError
            buttonApply = it.buttonApply
        }
    }

    private fun setupToolbar() {
        headerUnify?.apply {
            title = when (mode) {
                ShopDiscountManageDiscountMode.CREATE -> {
                    getString(R.string.shop_discount_manage_product_variant_discount_manage_toolbar_title)
                }
                ShopDiscountManageDiscountMode.UPDATE -> {
                    getString(R.string.shop_discount_manage_product_variant_discount_edit_toolbar_title)
                }
                else -> {
                    ""
                }
            }
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    fun onBackPressed() {
        activity?.finish()
    }

    override fun checkShouldEnableButtonSubmit() {
        val listVariantItemUiModel = adapter.getListVariantItemUiModel()
        viewModel.checkShouldEnableSubmitButton(listVariantItemUiModel)
    }

    override fun checkShouldEnableBulkApplyVariant() {
        val listVariantItemUiModel = adapter.getListVariantItemUiModel()
        viewModel.checkShouldEnableBulkApplyVariant(listVariantItemUiModel)
    }

    override fun getFragmentChildManager(): FragmentManager {
        return childFragmentManager
    }
}
