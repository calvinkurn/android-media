package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.ManageProductVariantAdapter
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import javax.inject.Inject
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.ManageProductVariantListener
import timber.log.Timber

class ManageProductVariantFragment :
    BaseCampaignManageProductDetailFragment<ManageProductVariantAdapter>(),
    ManageProductVariantListener {

    companion object {
        fun newInstance(product: ReservedProduct.Product?): ManageProductVariantFragment {
            val fragment = ManageProductVariantFragment()
            val bundle = Bundle()
            bundle.putParcelable(BundleConstant.BUNDLE_KEY_PRODUCT, product)
            fragment.arguments = bundle
            return fragment
        }

        private const val MINIMUM_VARIANT_TO_BULK_APPLY = 2
    }

    //argument
    private val product by lazy {
        arguments?.getParcelable<ReservedProduct.Product>(BundleConstant.BUNDLE_KEY_PRODUCT)
    }

    //viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductVariantViewModel::class.java) }

    override fun getScreenName(): String =
        ManageProductVariantFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product?.let { viewModel.setupInitiateProductData(it) }
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
            productTotalVariantFormatted = product.childProducts.count().toString(),
            productStockTextFormatted = product.stock.toString(),
            isShowWidgetBulkApply = true
        )
        buttonSubmit?.text = getString(R.string.manageproductnonvar_save)
    }

    private fun setupObservers() {
        viewModel.isInputPageValid.observe(viewLifecycleOwner) {
            buttonSubmit?.isEnabled = it
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

    }

    override fun createAdapterInstance(): ManageProductVariantAdapter {
        return ManageProductVariantAdapter().apply {
            product?.let {
                setDataList(it)
                setListener(this@ManageProductVariantFragment)
            }
        }
    }

    private fun setWidgetBulkApplyState(items: List<ReservedProduct.Product.ChildProduct>) {
        var activeVariantCount = Int.ZERO
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

    override fun onDataInputChanged(
        index: Int,
        product: ReservedProduct.Product,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult {
        product.productCriteria.let {
            val warehouses =
                (adapter as ManageProductVariantAdapter).getDataList()[index].warehouses
            viewModel.validateInputPage(warehouses, it)
        }
        return viewModel.validateInput(product.productCriteria, discountSetup)
    }

    override fun onToggleSwitch(index: Int, isChecked: Boolean) {
        viewModel.setItemToggleValue(index, isChecked)
        val product = viewModel.getProductData()
        setWidgetBulkApplyState(product.childProducts)
    }

    override fun onDiscountChange(index: Int, priceValue: Long, discountValue: Int) {
        viewModel.setDiscountValue(index, priceValue, discountValue)
    }

    override fun onStockChange(index: Int, stockValue: Long) {
        viewModel.setStockValue(index, stockValue)
    }

    override fun calculatePrice(percentInput: Long, adapterPosition: Int): String {
        val originalPrice = viewModel.getProductData().childProducts[adapterPosition].price.price
        return viewModel.calculatePrice(percentInput, originalPrice)
    }

    override fun calculatePercent(priceInput: Long, adapterPosition: Int): String {
        val originalPrice = viewModel.getProductData().childProducts[adapterPosition].price.price
        return viewModel.calculatePercent(priceInput, originalPrice)
    }
}