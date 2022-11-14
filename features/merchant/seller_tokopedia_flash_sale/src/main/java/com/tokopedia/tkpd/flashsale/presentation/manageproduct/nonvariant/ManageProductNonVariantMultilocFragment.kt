package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.ProductBulkApplyBottomSheet
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.LocationCriteriaCheckBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_FLASH_SALE_ID
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter.ManageProductNonVariantAdapterListener
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter.ManageProductNonVariantMultilocAdapter
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ToasterHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.mapper.BulkApplyMapper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.MULTI_LOCATION
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import javax.inject.Inject

class ManageProductNonVariantMultilocFragment :
    BaseCampaignManageProductDetailFragment<ManageProductNonVariantMultilocAdapter>(),
    ManageProductNonVariantAdapterListener {

    companion object {
        @JvmStatic
        fun newInstance(product: Product?, campaignId: Long): ManageProductNonVariantMultilocFragment {
            val fragment = ManageProductNonVariantMultilocFragment()
            val bundle = Bundle()
            bundle.putLong(BUNDLE_FLASH_SALE_ID, campaignId)
            bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ManageProductNonVariantViewModel
    private val product by lazy {
        arguments?.getParcelable<Product>(BUNDLE_KEY_PRODUCT)
    }
    private val campaignId by lazy {
        arguments?.getLong(BUNDLE_FLASH_SALE_ID).orZero()
    }
    var inputAdapter = ManageProductNonVariantMultilocAdapter()

    override fun getScreenName(): String = ManageProductNonVariantMultilocFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupPage()
        setupProductInput()
        setupObservers()
    }

    private fun setupProductInput() {
        viewModel.setProduct(product ?: return)
        viewModel.checkCriteria(product ?: return, campaignId = campaignId)
    }

    override fun createAdapterInstance() = inputAdapter.apply {
        product?.let {
            setDataList(it)
            setListener(this@ManageProductNonVariantMultilocFragment)
        }
    }

    override fun onBackArrowClicked() {
        activity?.finish()
    }

    override fun getHeaderUnifyTitle() = getString(R.string.manageproductnonvar_title)

    override fun onSubmitButtonClicked() {
        val bundle = Bundle()
        val intent = Intent()
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, viewModel.product.value)
        intent.putExtras(bundle)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onDataInputChanged(index: Int, criteria: ProductCriteria, discountSetup: DiscountSetup): ValidationResult {
        product?.let {
            val warehouses = inputAdapter.getDataList()
            warehouses.onEachIndexed { indexEdited, warehouse ->
                if (warehouse.isDilayaniTokopedia && indexEdited != index && warehouses.getOrNull(index)?.isDilayaniTokopedia == true) {
                    warehouse.discountSetup.apply {
                        price = discountSetup.price
                        discount = discountSetup.discount
                    }
                    if (rvManageProductDetail?.isComputingLayout == false && rvManageProductDetail?.scrollState == SCROLL_STATE_IDLE) {
                        rvManageProductDetail?.adapter?.notifyItemChanged(indexEdited)
                    }
                }
            }
            val newProduct = it.copy(warehouses = warehouses)
            viewModel.setProduct(newProduct)
        }
        return viewModel.validateInput(criteria, discountSetup)
    }

    override fun showDetailCriteria(position: Int, warehouse: Warehouse, product: Product) {
        val bottomSheetLocation = LocationCriteriaCheckBottomSheet()
        bottomSheetLocation.show(
            listOf(viewModel.getCriteria()?.locationResult?.get(position) ?: return),
            childFragmentManager, ""
        )
        viewModel.onCheckDetailButtonClicked(
            campaignId = campaignId.toString(),
            productId = product.productId.toString(),
            warehouseId = warehouse.warehouseId.toString(),
            locationType = MULTI_LOCATION
        )
    }

    override fun calculatePrice(percentInput: Long, adapterPosition: Int): String {
        val warehouses = (adapter as ManageProductNonVariantMultilocAdapter).getDataList()
        val originalPrice = warehouses.getOrNull(adapterPosition)?.price.orZero()
        return viewModel.calculatePrice(percentInput, originalPrice)
    }

    override fun calculatePercent(priceInput: Long, adapterPosition: Int): String {
        val warehouses = (adapter as ManageProductNonVariantMultilocAdapter).getDataList()
        val originalPrice = warehouses.getOrNull(adapterPosition)?.price.orZero()
        return viewModel.calculatePercent(priceInput, originalPrice)
    }

    override fun onWidgetBulkApplyClicked() {
        val product = viewModel.product.value
        val param = BulkApplyMapper.mapProductToBulkParam(context ?: return, product ?: return)
        val bSheet = ProductBulkApplyBottomSheet.newInstance(param)
        bSheet.setOnApplyClickListener{
            val appliedProduct = BulkApplyMapper.mapBulkResultToProduct(product, it)
            inputAdapter = ManageProductNonVariantMultilocAdapter().apply {
                setDataList(appliedProduct)
                setListener(this@ManageProductNonVariantMultilocFragment)
            }
            rvManageProductDetail?.adapter = inputAdapter
            viewModel.setProduct(appliedProduct)
            displayToaster(appliedProduct)
        }
        bSheet.show(childFragmentManager, "")
    }

    override fun trackOnClickPercent(percentInput: String) { /* Only Tracked On Single Location Product */ }

    override fun trackOnClickPrice(nominalInput: String) { /* Only Tracked On Single Location Product */ }

    override fun trackOnSwitchToggled(index: Int, criteria: ProductCriteria, discountSetup: DiscountSetup) {
        viewModel.onEditSwitchToggled(
            campaignId = campaignId.toString(),
            productId = product?.productId.toString(),
            warehouseId = inputAdapter.getDataList()[index].warehouseId.toString(),
            locationType = MULTI_LOCATION
        )
    }

    private fun setupObservers() {
        viewModel.isInputPageValid.observe(viewLifecycleOwner) {
            buttonSubmit?.isEnabled = it
        }
        viewModel.enableBulkApply.observe(viewLifecycleOwner) {
            if (it) enableWidgetBulkApply() else disableWidgetBulkApply()
        }
        viewModel.bulkApplyCaption.observe(viewLifecycleOwner) {
            setWidgetBulkApplyText(it)
        }
    }

    private fun setupPage() {
        product?.let {
            setupProductHeaderData(
                productImageUrl = it.picture,
                productName = it.name,
                productOriginalPriceFormatted = it.price.price.getCurrencyFormatted(),
                productStockTextFormatted = getString(R.string.manageproductnonvar_stock_total_format, it.stock),
                isShowWidgetBulkApply = true
            )
            buttonSubmit?.text = getString(R.string.manageproductnonvar_save)
        }
    }

    private fun displayToaster(product: Product) {
        val criteria = product.productCriteria

        val isValid = product.warehouses.firstOrNull {
            viewModel.validateInput(criteria, it.discountSetup).isAllFieldValid()
        } != null

        if (isValid) ToasterHelper.showToaster(buttonSubmit, getString(R.string.stfs_toaster_valid), TYPE_NORMAL)
        else ToasterHelper.showToaster(buttonSubmit, getString(R.string.stfs_toaster_error), TYPE_ERROR)
    }

}
