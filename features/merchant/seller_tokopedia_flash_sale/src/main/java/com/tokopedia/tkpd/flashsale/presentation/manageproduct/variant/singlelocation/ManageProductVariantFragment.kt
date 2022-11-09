package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.ProductBulkApplyBottomSheet
import com.tokopedia.campaign.utils.extension.doOnDelayFinished
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.ManageProductVariantAdapter
import javax.inject.Inject
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.CampaignCriteriaCheckBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ToasterHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.mapper.BulkApplyMapper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.ManageProductMultiLocationVariantActivity
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.ManageProductVariantListener
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleRequestCodeConstant.REQUEST_CODE_MANAGE_PRODUCT_VARIANT_LOCATION
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL

class ManageProductVariantFragment :
    BaseCampaignManageProductDetailFragment<ManageProductVariantAdapter>(),
    ManageProductVariantListener {

    companion object {
        fun newInstance(
            product: ReservedProduct.Product?,
            campaignId: String
        ): ManageProductVariantFragment {
            val fragment = ManageProductVariantFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
            bundle.putString(BundleConstant.BUNDLE_FLASH_SALE_ID, campaignId)
            fragment.arguments = bundle
            return fragment
        }

        private const val MINIMUM_VARIANT_TO_BULK_APPLY = 2
        private const val DELAY = 1000L
    }

    //argument
    private val product by lazy {
        arguments?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
    }

    private val campaignId by lazy {
        arguments?.getString(BundleConstant.BUNDLE_FLASH_SALE_ID).orEmpty()
    }

    //viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductVariantViewModel::class.java) }

    private var criteriaCheckBottomSheet = CampaignCriteriaCheckBottomSheet()

    override fun getScreenName(): String =
        ManageProductVariantFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        product?.let { viewModel.setupInitiateProductData(it) }
        viewModel.setCampaignId(campaignId)
        super.onViewCreated(view, savedInstanceState)
        setupPage(viewModel.getProductData())
        setupWidgetBulkApply(
            getString(R.string.stfs_inactive_variant_bulk_apply_place_holder),
            false
        )
        setupObservers()
    }

    private fun setupPage(product: ReservedProduct.Product) {
        setupProductHeaderData(
            productImageUrl = product.picture,
            productName = product.name,
            productOriginalPriceFormatted = product.price.price.getCurrencyFormatted(),
            productTotalVariantFormatted = getString(
                R.string.stfs_avp_variant_count_placeholder,
                product.childProducts.count()
            ),
            productStockTextFormatted = product.stock.toString(),
            isShowWidgetBulkApply = true
        )
        textProductOriginalPrice?.gone()
        textTotalStock?.gone()
        imageIconProduct?.gone()
        buttonSubmit?.text = getString(R.string.manageproductnonvar_save)
    }

    private fun setupObservers() {
        viewModel.isInputPageValid.observe(viewLifecycleOwner) {
            buttonSubmit?.isEnabled = it
        }

        viewModel.criteriaCheckingResult.observe(viewLifecycleOwner) {
            showCriteriaCheckBottomSheet(it)
        }
    }

    private fun setupWidgetBulkApply(title: String, isReadyToBulkApply: Boolean) {
        if (isReadyToBulkApply) {
            enableWidgetBulkApply()
        } else {
            disableWidgetBulkApply()
        }
        setWidgetBulkApplyText(title)
    }

    override fun onBackArrowClicked() {
        activity?.finish()
    }

    override fun getHeaderUnifyTitle(): String {
        return getString(R.string.stfs_stfs_manage_variant_title)
    }

    override fun onSubmitButtonClicked() {
        viewModel.sendSaveClickEvent(campaignId)
        val bundle = Bundle()
        val intent = Intent()
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, viewModel.getProductData())
        intent.putExtras(bundle)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onWidgetBulkApplyClicked() {
        val product = viewModel.getProductData()
        val param = BulkApplyMapper.mapProductToBulkParam(context ?: return, product)
        val bSheet = ProductBulkApplyBottomSheet.newInstance(param)
        bSheet.setOnApplyClickListener {
            val appliedProduct = BulkApplyMapper.mapBulkResultToProduct(product, it)
            setProductListData(appliedProduct)
            displayToaster(appliedProduct)
        }
        bSheet.show(childFragmentManager, "")
        viewModel.sendManageAllClickEvent(campaignId)
    }

    override fun createAdapterInstance(): ManageProductVariantAdapter {
        return ManageProductVariantAdapter(this).apply {
            setDataList(viewModel.getProductData())
        }
    }

    private fun displayToaster(product: ReservedProduct.Product) {
        val variantWarehouses = product.childProducts
        val criteria = product.productCriteria

        val isValid = variantWarehouses.firstOrNull { variantProduct ->
            variantProduct.warehouses.firstOrNull { variantWarehouse ->
                viewModel.validateInput(criteria, variantWarehouse.discountSetup).isAllFieldValid()
            } != null
        } != null

        if (isValid) ToasterHelper.showToaster(
            buttonSubmit,
            getString(R.string.stfs_toaster_valid),
            TYPE_NORMAL
        )
        else ToasterHelper.showToaster(
            buttonSubmit,
            getString(R.string.stfs_toaster_error),
            TYPE_ERROR
        )
    }

    private fun setWidgetBulkApplyState() {
        var activeVariantCount = Int.ZERO
        val items = viewModel.getProductData().childProducts
        items.filter { it.isToggleOn }.map {
            activeVariantCount++
        }

        if (activeVariantCount >= MINIMUM_VARIANT_TO_BULK_APPLY) {
            setupWidgetBulkApply(
                getString(
                    R.string.stfs_active_variant_bulk_apply_place_holder,
                    activeVariantCount
                ), true
            )
        } else {
            setupWidgetBulkApply(
                getString(R.string.stfs_inactive_variant_bulk_apply_place_holder),
                false
            )
        }
    }

    private fun setProductListData(product: ReservedProduct.Product) {
        val adapter = ManageProductVariantAdapter(this).apply {
            setDataList(product)
        }
        rvManageProductDetail?.adapter = adapter
        viewModel.setupInitiateProductData(product)
    }

    private fun showCriteriaCheckBottomSheet(list: List<CriteriaCheckingResult>) {
        criteriaCheckBottomSheet.show(list, childFragmentManager, "")
    }

    override fun onDataInputChanged(
        index: Int,
        product: ReservedProduct.Product,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult {
        val items = adapter?.getDataList()
        val selectedItem = items?.get(index)
        if (selectedItem != null) {
            viewModel.validateInputPage(selectedItem.productCriteria)
        }
        setWidgetBulkApplyState()

        return viewModel.validateInput(
            criteria = product.productCriteria,
            discountSetup = discountSetup
        )
    }

    override fun onToggleSwitch(index: Int, isChecked: Boolean) {
        viewModel.setItemToggleValue(index, isChecked)
        setWidgetBulkApplyState()
        viewModel.sendAdjustToggleVariantEvent(campaignId)
    }

    override fun onDiscountChange(index: Int, priceValue: Long, discountValue: Int) {
        viewModel.setDiscountValue(index, priceValue, discountValue)
        doOnDelayFinished(DELAY) {
            viewModel.sendFillInDiscountPercentageEvent(campaignId)
            viewModel.sendFillInColumnPriceEvent(campaignId)
        }
    }

    override fun onStockChange(index: Int, stockValue: Long) {
        viewModel.setStockValue(index, stockValue)
    }

    override fun calculatePrice(percentInput: Long, adapterPosition: Int): String {
        val originalPrice =
            viewModel.getProductData().childProducts[adapterPosition].warehouses.firstOrNull()?.price.orZero()
        return viewModel.calculatePrice(percentInput, originalPrice)
    }

    override fun calculatePercent(priceInput: Long, adapterPosition: Int): String {
        val originalPrice =
            viewModel.getProductData().childProducts[adapterPosition].warehouses.firstOrNull()?.price.orZero()
        return viewModel.calculatePercent(priceInput, originalPrice)
    }

    override fun onMultiWarehouseClicked(
        adapterPosition: Int,
        selectedProduct: ReservedProduct.Product.ChildProduct
    ) {
        viewModel.sendManageAllLocationClickEvent(campaignId, selectedProduct.productId)
        val intent = ManageProductMultiLocationVariantActivity.start(
            context ?: return,
            viewModel.getProductData(),
            adapterPosition,
            campaignId
        )
        startActivityForResult(intent, REQUEST_CODE_MANAGE_PRODUCT_VARIANT_LOCATION)
    }

    override fun onIneligibleProductWithSingleWarehouseClicked(
        index: Int,
        selectedProduct: ReservedProduct.Product.ChildProduct,
        productCriteria: ReservedProduct.Product.ProductCriteria
    ) {
        criteriaCheckBottomSheet.setProductPreview(
            selectedProduct.name,
            selectedProduct.picture
        )
        viewModel.checkCriteria(selectedProduct, productCriteria)
        viewModel.sendCheckDetailClickEvent(campaignId, selectedProduct.productId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_MANAGE_PRODUCT_VARIANT_LOCATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    val appliedProduct =
                        data?.extras?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
                    if (appliedProduct != null) {
                        setProductListData(appliedProduct)
                        viewModel.validateInputPage(appliedProduct.productCriteria)
                    }
                }
            }
        }
    }
}
