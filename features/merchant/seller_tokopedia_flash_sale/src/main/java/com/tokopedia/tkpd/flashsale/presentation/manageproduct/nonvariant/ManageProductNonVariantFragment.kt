package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter.ManageProductNonVariantAdapter
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter.ManageProductNonVariantAdapterListener
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantActivity.Companion.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import javax.inject.Inject

class ManageProductNonVariantFragment :
    BaseCampaignManageProductDetailFragment<ManageProductNonVariantAdapter>(),
    ManageProductNonVariantAdapterListener {

    companion object {
        private const val MULTILOC_FRAGMENT_TAG = "multiloc"
        @JvmStatic
        fun newInstance(product: ReservedProduct.Product?): ManageProductNonVariantFragment {
            val fragment = ManageProductNonVariantFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PRODUCT, product)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ManageProductNonVariantViewModel
    private val product by lazy {
        arguments?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
    }

    override fun getScreenName(): String = ManageProductNonVariantFragment::class.java.canonicalName.orEmpty()

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
        setupObserver()
    }

    override fun createAdapterInstance() = ManageProductNonVariantAdapter().apply {
        product?.let {
            setDataList(it)
            setListener(this@ManageProductNonVariantFragment)
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
            val warehouses = (adapter as ManageProductNonVariantAdapter).getWarehouses()
            val newProduct = it.copy(warehouses = warehouses)
            viewModel.setProduct(newProduct)
        }
        return viewModel.validateInput(criteria, discountSetup)
    }

    override fun calculatePrice(percentInput: Long, adapterPosition: Int): String {
        val originalPrice = product?.warehouses?.firstOrNull()?.price.orZero()
        return viewModel.calculatePrice(percentInput, originalPrice)
    }

    override fun calculatePercent(priceInput: Long, adapterPosition: Int): String {
        val originalPrice = product?.warehouses?.firstOrNull()?.price.orZero()
        return viewModel.calculatePercent(priceInput, originalPrice)
    }

    private fun setupObserver() {
        viewModel.isMultiloc.observe(viewLifecycleOwner) {
            if (it) moveToMultilocPage()
        }
        viewModel.isInputPageValid.observe(viewLifecycleOwner) {
            buttonSubmit?.isEnabled = it
        }
    }

    private fun moveToMultilocPage() {
        val multilocPage = ManageProductNonVariantMultilocFragment.newInstance(product)
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, multilocPage, MULTILOC_FRAGMENT_TAG)
            .commit()
    }

    private fun setupPage() {
        product?.let {
            setupProductHeaderData(
                productImageUrl = it.picture,
                productName = it.name,
                productOriginalPriceFormatted = it.price.price.getCurrencyFormatted(),
                productStockTextFormatted = getString(R.string.manageproductnonvar_stock_total_format, it.stock)
            )
            buttonSubmit?.text = getString(R.string.manageproductnonvar_save)
            viewModel.setProduct(it)
        }
    }

}