package com.tokopedia.tkpd.flashsale.presentation.avp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.avp.adapter.ManageProductVariantDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.avp.adapter.item.ManageProductVariantItem
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import javax.inject.Inject
import com.tokopedia.seller_tokopedia_flash_sale.R
import timber.log.Timber

class ManageProductVariantFragment : BaseCampaignManageProductDetailFragment<CompositeAdapter>() {

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
        product?.let {
            viewModel.setupInitiateProductData(it)
        }
        setupHeaderData(viewModel.getFinalProductData())
        setupWidgetBulkApply(
            getString(R.string.stfs_inactive_variant_bulk_apply_place_holder),
            false
        )
    }

    private fun setupHeaderData(product: ReservedProduct.Product) {
        setupProductHeaderData(
            productImageUrl = product.picture,
            productName = product.name,
            productOriginalPriceFormatted = product.price.price.getCurrencyFormatted(),
            productTotalVariantFormatted = product.childProducts.count().toString(),
            productStockTextFormatted = product.stock.toString(),
            isShowWidgetBulkApply = true
        )
        product.toItem()?.let { adapter?.submit(it) }
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

    override fun createAdapterInstance(): CompositeAdapter {
        return CompositeAdapter.Builder()
            .add(ManageProductVariantDelegateAdapter(
                onToggleSwitched = { position, isChecked ->
                    onToggleSwitched(position, isChecked)
                },
                onDiscountAmountChanged = { position, value ->
                    viewModel.setDiscountAmount(position, value)
                    Timber.tag("Masuk").d(value.toString())
                }
            ))
            .build()
    }

    private fun onToggleSwitched(itemPosition: Int, isChecked: Boolean) {
        viewModel.setItemToggleValue(itemPosition, isChecked)
        viewModel.getFinalProductData().toItem()?.let {
            adapter?.submit(it)
            setWidgetBulkApplyState(it)
        }
    }

    private fun setWidgetBulkApplyState(newItems: List<ManageProductVariantItem>) {
        var activeVariantCount = Int.ZERO
        newItems.filter { it.isToggleOn }.map {
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

    private fun ReservedProduct.Product.toItem(): List<ManageProductVariantItem>? {
        return this.childProducts.map { child ->
            ManageProductVariantItem(
                disabledReason = child.disabledReason,
                isDisabled = child.isDisabled,
                isMultiwarehouse = child.isMultiwarehouse,
                isToggleOn = child.isToggleOn,
                name = child.name,
                picture = child.picture,
                price = child.price,
                productId = child.productId,
                sku = child.sku,
                stock = child.stock,
                url = child.url,
                warehouses = child.warehouses,
            )
        }
    }
}