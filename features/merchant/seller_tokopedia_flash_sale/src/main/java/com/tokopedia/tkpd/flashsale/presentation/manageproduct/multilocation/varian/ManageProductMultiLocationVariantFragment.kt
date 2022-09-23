package com.tokopedia.tkpd.flashsale.presentation.manageproduct.multilocation.varian

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import javax.inject.Inject
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.multilocation.varian.adapter.ManageProductMultiLocationVariantDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.multilocation.varian.adapter.ManageProductMultiLocationVariantItem
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.multilocation.varian.adapter.ManageProductMultiLocationVariantItem.BundleConstant.toProductCriteriaInWarehouse

//TODO WILLYBRODUS : CHANGE ALL logic method into ViewModel
class ManageProductMultiLocationVariantFragment :
    BaseCampaignManageProductDetailFragment<CompositeAdapter>() {

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

        private const val INPUT_NOT_EMPTY_STATE = false
        private const val MINIMUM_VARIANT_TO_BULK_APPLY = 2
    }

    //argument
    private val product by lazy {
        arguments?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
    }

    private val variantPositionOnProduct by lazy {
        arguments?.getInt(BundleConstant.VARIANT_POSITION).toZeroIfNull()

    }

    //viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductMultiLocationVariantViewModel::class.java) }

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
        product?.let {
            viewModel.setupInitiateProductData(it, variantPositionOnProduct)
        }
        observeButtonSubmitState()
        setupButtonText()
        setupHeaderData(viewModel.getFinalProductData(), viewModel.getVariantData())
        setupWidgetBulkApply(
            getString(R.string.stfs_inactive_location_bulk_apply_place_holder),
            false
        )
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
        viewModel.getFinalProductData().toItem(viewModel.getVariantData())
            .let { adapter?.submit(it) }
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
        return getString(R.string.stfs_stfs_manage_location_title)
    }

    override fun onSubmitButtonClicked() {
        activity?.let {
            (it as ManageProductMultiLocationVariantActivity).sendResultToRequester()
        }
    }

    override fun createAdapterInstance(): CompositeAdapter {
        return CompositeAdapter.Builder()
            .add(ManageProductMultiLocationVariantDelegateAdapter(
                onToggleSwitched = { position, isChecked ->
                    onToggleSwitched(position, isChecked)
                    viewModel.checkAllValidationOfInputUser(position, isChecked)
                },
                onDiscountAmountChanged = { position, amount, discount ->
                    viewModel.setDiscountAmount(position, amount)
                    viewModel.setDiscountPercentage(position, discount)
                    viewModel.checkAllValidationOfInputUser(position ,INPUT_NOT_EMPTY_STATE)
                },
                onDiscountPercentageChange = { position, discount, amount ->
                    viewModel.setDiscountPercentage(position, discount)
                    viewModel.setDiscountAmount(position, amount)
                    viewModel.checkAllValidationOfInputUser(position ,INPUT_NOT_EMPTY_STATE)
                },
                onValidationInputText = { position, isPriceToPercentage, action ->
                    val isValid =
                        when {
                            isPriceToPercentage -> viewModel.checkValidationDiscountAmountInWarehouseWithPosition(
                                position
                            )
                            else -> viewModel.checkValidationDiscountPercentInWarehouseWithPosition(
                                position
                            )
                        }

                    val (messageAmount, messageDiscount) =
                        viewModel.getMessageOfHintInputField(position)

                    action.invoke(
                        isValid,
                        messageAmount,
                        messageDiscount
                    )
                },
                onValidationQuantity = { position, stock, isNeedToCheck, action ->
                    viewModel.setStockAmount(position, stock)

                    viewModel.checkAllValidationOfInputUser(position ,isNeedToCheck)

                    val isStockInCriteria =
                        viewModel.checkValidationStockInWarehouseWithPosition(position)
                    val message = viewModel.getMessageOfHintStockField(position)
                    action.invoke(isStockInCriteria, message)
                }
            ))
            .build()
    }

    override fun onDestroy() {
        removeObservers(viewModel.buttonEnableState)
        viewModel.flush()
        super.onDestroy()
    }

    private fun observeButtonSubmitState() {
        observe(viewModel.buttonEnableState) { state ->
            when {
                state -> {
                    enableButtonSubmit()
                }
                else -> {
                    disableButtonSubmit()
                }
            }
        }
    }

    private fun onToggleSwitched(itemPosition: Int, isChecked: Boolean) {
        viewModel.setItemToggleValue(itemPosition, isChecked)
        val mapper = viewModel.getFinalProductData().toItem(viewModel.getVariantData())
        adapter?.submit(mapper)
        setWidgetBulkApplyState(mapper)
    }

    private fun setWidgetBulkApplyState(newItems: List<ManageProductMultiLocationVariantItem>) {
        var activeVariantCount = Int.ZERO
        newItems.filter { it.isToggleOn }.map {
            activeVariantCount++
        }

        if (activeVariantCount >= MINIMUM_VARIANT_TO_BULK_APPLY) {
            setupWidgetBulkApply(
                getString(
                    R.string.stfs_active_location_bulk_apply_place_holder,
                    activeVariantCount
                ), true
            )
        } else {
            setupWidgetBulkApply(
                getString(R.string.stfs_inactive_location_bulk_apply_place_holder),
                false
            )
        }
    }

    private fun ReservedProduct.Product.toItem(variant: ReservedProduct.Product.ChildProduct): List<ManageProductMultiLocationVariantItem> {
        return variant.warehouses.map { Warehouse ->
            ManageProductMultiLocationVariantItem(
                isToggleOn = Warehouse.isToggleOn,
                name = Warehouse.name,
                priceInWarehouse = Warehouse.price.toString(),
                priceInStore = ManageProductMultiLocationVariantItem.Price(
                    price = variant.price.price,
                    lowerPrice = variant.price.lowerPrice,
                    upperPrice = variant.price.upperPrice
                ),
                warehouseId = Warehouse.warehouseId,
                stock = Warehouse.stock,
                productCriteria = this.productCriteria.toProductCriteriaInWarehouse()
            )
        }
    }


    fun getIntentResult() : Intent {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, viewModel.getProductResult())
        intent.putExtras(bundle)
        return intent
    }
}