package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter.ManageProductNonVariantAdapterListener
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter.ManageProductNonVariantMultilocAdapter
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantActivity.Companion.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import javax.inject.Inject

class ManageProductNonVariantMultilocFragment :
    BaseCampaignManageProductDetailFragment<ManageProductNonVariantMultilocAdapter>(),
    ManageProductNonVariantAdapterListener {

    companion object {
        @JvmStatic
        fun newInstance(product: ReservedProduct.Product?): ManageProductNonVariantMultilocFragment {
            val fragment = ManageProductNonVariantMultilocFragment()
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
        }
    }

    override fun createAdapterInstance() = ManageProductNonVariantMultilocAdapter().apply {
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
        activity?.finish()
    }

    override fun onDataInputChanged(index: Int, criteria: ProductCriteria, discountSetup: DiscountSetup): ValidationResult {
        return viewModel.validateInput(criteria, discountSetup)
    }

}