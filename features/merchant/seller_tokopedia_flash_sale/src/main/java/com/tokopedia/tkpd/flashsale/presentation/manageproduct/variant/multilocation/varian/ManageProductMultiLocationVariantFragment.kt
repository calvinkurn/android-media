package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.ProductBulkApplyBottomSheet
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ToasterHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.mapper.BulkApplyMapper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter.ManageProductVariantAdapterListener
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter.ManageProductVariantMultiLocationAdapter
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import javax.inject.Inject

class ManageProductMultiLocationVariantFragment :
    BaseCampaignManageProductDetailFragment<ManageProductVariantMultiLocationAdapter>(),
    ManageProductVariantAdapterListener {

    companion object {
        fun newInstance(
            product: ReservedProduct.Product?,
            variantPositionOnProduct: Int
        ): ManageProductMultiLocationVariantFragment {
            val fragment = ManageProductMultiLocationVariantFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
            bundle.putInt(BundleConstant.VARIANT_POSITION, variantPositionOnProduct)
            fragment.arguments = bundle
            return fragment
        }
    }

    //argument
    private val product by lazy {
        arguments?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
    }

    private val variantPositionOnProduct by lazy {
        arguments?.getInt(BundleConstant.VARIANT_POSITION).toZeroIfNull()

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductMultiLocationVariantViewModel::class.java) }

    var inputAdapter = ManageProductVariantMultiLocationAdapter()

    override fun getScreenName(): String =
        ManageProductMultiLocationVariantFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupProductInput()
        setupPage()
        setupObservers()
    }

    private fun setupProductInput() {
        product?.let {
            viewModel.setProduct(it, variantPositionOnProduct)
        }
    }

    private fun setupPage() {
        setupButtonText()
        viewModel.product.value?.let {
            val variant = it.childProducts[variantPositionOnProduct]
            setupHeaderData(it, variant)
        }
    }

    private fun setupButtonText() {
        buttonSubmit?.text = getText(R.string.stfs_text_button_save)
    }

    private fun setupHeaderData(
        product: ReservedProduct.Product,
        variant: ReservedProduct.Product.ChildProduct
    ) {
        setupProductHeaderData(
            productImageUrl = product.picture,
            productName = product.name,
            productTotalVariantFormatted = getString(
                R.string.choose_product_variant_count_format,
                product.childProducts.count()
            ),
            productVariantName = variant.name,
            isShowWidgetBulkApply = true
        )
    }

    override fun onBackArrowClicked() {
        activity?.finish()
    }

    override fun getHeaderUnifyTitle(): String {
        return getString(R.string.stfs_stfs_manage_location_title)
    }

    override fun onSubmitButtonClicked() {
        activity?.let {
            (it as ManageProductMultiLocationVariantActivity).sendResultToRequester()
        }
    }

    override fun createAdapterInstance() = inputAdapter.apply {
        product?.let {
            setDataList(it.childProducts[variantPositionOnProduct])
            setListener(this@ManageProductMultiLocationVariantFragment)
        }
    }

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
    }

    private fun setupObservers() {
        viewModel.isInputPageValid.observe(viewLifecycleOwner) {
            buttonSubmit?.isEnabled = !it
        }
        viewModel.enableBulkApply.observe(viewLifecycleOwner) {
            if (it) enableWidgetBulkApply() else disableWidgetBulkApply()
        }
        viewModel.bulkApplyCaption.observe(viewLifecycleOwner) {
            setWidgetBulkApplyText(it)
        }
    }

    override fun onDataInputChanged(
        index: Int,
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup,
    ): ValidationResult {
        viewModel.product.value?.let {
            val variant = it.childProducts[variantPositionOnProduct]
            val warehouses = inputAdapter.getDataList()
            variant.warehouses =
                viewModel.valueAdjustmentOfServedByTokopediaWarehouseToRegister(warehouses, index)
            viewModel.setProduct(
                product = it,
                positionOfVariant = variantPositionOnProduct,
            )

            if(variant.warehouses[index].isDilayaniTokopedia) {
                val listServedByTokopedia =
                    viewModel.findPositionOfProductServedByTokopediaToRegister(warehouses)
                listServedByTokopedia?.forEach { warehouse ->
                    val (positionOnItem, dataWarehouse) = warehouse
                    if (positionOnItem != index) {
                        inputAdapter.setDataList(positionOnItem, dataWarehouse)
                    }
                }
            }
        }
        return viewModel.validateInput(criteria, discountSetup)
    }

    override fun validationItem(
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult {
        return viewModel.validateItem(criteria, discountSetup)
    }

    override fun calculatePrice(percentInput: Long, adapterPosition: Int): String {
        val warehouses = (adapter as ManageProductVariantMultiLocationAdapter).getDataList()
        val originalPrice = warehouses.getOrNull(adapterPosition)?.price.orZero()
        return viewModel.calculatePrice(percentInput, originalPrice)
    }

    override fun calculatePercent(priceInput: Long, adapterPosition: Int): String {
        val warehouses = (adapter as ManageProductVariantMultiLocationAdapter).getDataList()
        val originalPrice = warehouses.getOrNull(adapterPosition)?.price.orZero()
        return viewModel.calculatePercent(priceInput, originalPrice)
    }

    override fun onWidgetBulkApplyClicked() {
        val product = viewModel.product.value
        val param = BulkApplyMapper.mapProductToBulkParam(context ?: return, product ?: return)
        val bSheet = ProductBulkApplyBottomSheet.newInstance(param)
        bSheet.setOnApplyClickListener {
            val appliedProduct = BulkApplyMapper.mapBulkResultToProduct(product, it)
            inputAdapter = ManageProductVariantMultiLocationAdapter().apply {
                setDataList(appliedProduct.childProducts[variantPositionOnProduct])
                setListener(this@ManageProductMultiLocationVariantFragment)
            }
            showMessageToaster(appliedProduct)
            rvManageProductDetail?.adapter = inputAdapter
            viewModel.setProduct(appliedProduct, variantPositionOnProduct)
        }
        bSheet.show(childFragmentManager, "")
    }

    private fun showMessageToaster(product: ReservedProduct.Product) {
        val variantProduct = product.childProducts[variantPositionOnProduct]
        val warehouseToRegister = viewModel.findToggleOnInWarehouse(variantProduct)
        warehouseToRegister.forEach { warehouse ->
            val isValid = viewModel.validateInput(product.productCriteria, warehouse.discountSetup)
            if (!isValid.isAllFieldValid()) {
                ToasterHelper.showToaster(buttonSubmit, getString(R.string.stfs_toaster_error), TYPE_ERROR)
                return
            }
        }
        ToasterHelper.showToaster(buttonSubmit, getString(R.string.stfs_toaster_valid), TYPE_NORMAL)
    }

    fun getIntentResult(): Intent {
        val intent = Intent()
        val bundle = Bundle()
        val product = viewModel.setToggleOnOrOf(viewModel.product.value, variantPositionOnProduct)
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
        intent.putExtras(bundle)
        return intent
    }
}