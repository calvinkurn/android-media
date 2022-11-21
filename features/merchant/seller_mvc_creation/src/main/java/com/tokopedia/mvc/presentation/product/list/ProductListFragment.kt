package com.tokopedia.mvc.presentation.product.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentProductListBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.ShopData
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.presentation.product.add.AddProductFragment
import com.tokopedia.mvc.presentation.product.list.adapter.ProductListDelegateAdapter
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEffect
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEvent
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListUiState
import com.tokopedia.mvc.presentation.product.variant.dialog.DeleteConfirmationDialog
import com.tokopedia.mvc.presentation.product.variant.review.ReviewVariantBottomSheet
import com.tokopedia.mvc.presentation.share.LinkerDataGenerator
import com.tokopedia.mvc.presentation.share.ShareComponentInstanceBuilder
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ProductListFragment : BaseDaggerFragment() {

    companion object {
        private const val ONE_PRODUCT = 1
        private const val REQUEST_CODE_ADD_NEW_PRODUCT = 101

        @JvmStatic
        fun newInstance(pageMode: PageMode, selectedProducts: List<SelectedProduct>): ProductListFragment {
            return ProductListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelableArrayList(
                        BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                        ArrayList(selectedProducts)
                    )
                }
            }
        }
    }

    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val selectedParentProducts by lazy { arguments?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS) }
    private var binding by autoClearedNullable<SmvcFragmentProductListBinding>()

    private val productAdapter by lazy {
        CompositeAdapter.Builder()
            .add(ProductListDelegateAdapter(onDeleteProductClick, onCheckboxClick, onVariantClick))
            .build()
    }

    @Inject
    lateinit var shareComponentInstanceBuilder: ShareComponentInstanceBuilder

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductListViewModel::class.java) }
    private var shareComponentBottomSheet : UniversalShareBottomSheet? = null

    override fun getScreenName(): String = AddProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentProductListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupView()
        observeUiEffect()
        observeUiState()

        viewModel.processEvent(
            ProductListEvent.FetchProducts(
                VoucherAction.CREATE,
                PromoType.CASHBACK,
                selectedParentProducts?.toList().orEmpty(),
                pageMode ?: PageMode.CREATE
            )
        )
    }

    private fun setupView() {
        setupToolbar()
        setupCheckbox()
        setupRecyclerView()
        setupButton()
        setupClickListener()
    }

    private fun setupToolbar() {
        binding?.header?.actionTextView?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapCtaChangeProduct)
        }
    }

    private fun setupClickListener() {
        binding?.tpgCtaAddProduct?.setOnClickListener {
            if (pageMode == PageMode.EDIT) {
                val voucherConfiguration = VoucherConfiguration(
                    benefitIdr = 25_000,
                    benefitMax = 500_000,
                    benefitPercent = 0,
                    benefitType = BenefitType.NOMINAL,
                    promoType = PromoType.FREE_SHIPPING,
                    isVoucherProduct = true,
                    minPurchase = 50_000,
                    productIds = emptyList()
                )

                val intent = AddProductActivity.buildEditModeIntent(
                    activity ?: return@setOnClickListener,
                    voucherConfiguration
                )
                startActivityForResult(intent, REQUEST_CODE_ADD_NEW_PRODUCT)
            }

            if (pageMode == PageMode.CREATE) {
                activity?.finish()
            }

        }

        binding?.iconBulkDelete?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapBulkDeleteProduct)
        }
    }

    private fun setupButton() {
        binding?.btnContinue?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapContinueButton)
        }
        binding?.btnBack?.setOnClickListener { 
            activity?.finish()
        }
    }

    private fun setupCheckbox() {
        binding?.checkbox?.setOnCheckedChangeListener { view, isChecked ->
            if (view.isClickTriggeredByUserInteraction()) {
                if (isChecked) {
                    viewModel.processEvent(ProductListEvent.EnableSelectAllCheckbox)
                } else {
                    viewModel.processEvent(ProductListEvent.DisableSelectAllCheckbox)
                }
            }
        }
    }
    

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            attachDividerItemDecoration()
            adapter = productAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleEffect(effect: ProductListEffect) {
        when (effect) {
            is ProductListEffect.ShowVariantBottomSheet -> {
                displayVariantBottomSheet(effect.isParentProductSelected, effect.selectedProduct, effect.originalVariantIds)
            }
            is ProductListEffect.ShowBulkDeleteProductConfirmationDialog -> {
                DeleteConfirmationDialog.show(
                    context = activity ?: return,
                    title = getString(R.string.smvc_placeholder_bulk_delete_product_confirmation, effect.toDeleteProductCount),
                    description = getString(R.string.smvc_delete_product_description),
                    primaryButtonTitle = getString(R.string.smvc_proceed_delete),
                    onPrimaryButtonClick = { viewModel.processEvent(ProductListEvent.ApplyBulkDeleteProduct) }
                )
            }
            is ProductListEffect.ShowDeleteProductConfirmationDialog -> {
                DeleteConfirmationDialog.show(
                    context = activity ?: return,
                    title = getString(R.string.smvc_delete_product),
                    description = getString(R.string.smvc_delete_product_description),
                    primaryButtonTitle = getString(R.string.smvc_proceed_delete),
                    onPrimaryButtonClick = { viewModel.processEvent(ProductListEvent.ApplyRemoveProduct(effect.productId)) }
                )
            }
            is ProductListEffect.BulkDeleteProductSuccess -> {
                binding?.cardUnify2.showToaster(
                    message = getString(
                        R.string.smvc_placeholder_bulk_delete_product,
                        effect.deletedProductCount
                    ),
                    ctaText = getString(R.string.smvc_ok)
                )
            }
            ProductListEffect.ProductDeleted -> {
                binding?.cardUnify2.showToaster(
                    message = getString(R.string.smvc_product_deleted),
                    ctaText = getString(R.string.smvc_ok)
                )
            }

            is ProductListEffect.ConfirmAddProduct -> {
                if (pageMode == PageMode.CREATE) {
                    //TODO: Navigate to voucher preview page
                } else {
                    sendResult(effect.selectedProducts)
                }
            }
        }
    }


    private fun handleUiState(uiState: ProductListUiState) {
        renderLoadingState(uiState.isLoading)
        renderList(uiState.products)
        renderEmptyState(
            uiState.products.count(),
            uiState.isLoading,
            uiState.pageMode,
            uiState.maxProductSelection
        )
        renderProductCounter(
            uiState.products.count(),
            uiState.selectedProductsIds.count(),
            uiState.pageMode
        )
        renderBulkDeleteIcon(uiState.selectedProductsIds.count())
        renderSelectAllCheckbox(
            uiState.products.count(),
            uiState.selectedProductsIds.count(),
            uiState.pageMode
        )
        renderButton()
        renderToolbar(uiState.pageMode)
    }

    private fun renderToolbar(pageMode: PageMode) {
        if (pageMode == PageMode.CREATE) {
            binding?.header?.actionTextView?.gone()
        } else {
            binding?.header?.actionTextView?.visible()

            binding?.header?.actionText = getString(R.string.smvc_update_product)
        }
    }

    private fun renderButton() {
        binding?.btnContinue?.text = if (pageMode == PageMode.CREATE) {
            getString(R.string.smvc_continue)
        } else {
            getString(R.string.smvc_save)
        }
    }

    private fun renderBulkDeleteIcon(selectedProductCount: Int) {
        binding?.iconBulkDelete?.isVisible = selectedProductCount > ONE_PRODUCT
    }

    private fun renderProductCounter(productCount: Int, selectedProductCount: Int, pageMode: PageMode) {
        binding?.tpgSelectedParentProductCount?.text =
            getString(R.string.smvc_placholder_selected_product_count, productCount)

        val selectedProductCountWording = if (selectedProductCount.isZero()) {
            getString(
                R.string.smvc_select_all,
                selectedProductCount,
                productCount
            )
        } else {
            getString(
                R.string.smvc_placeholder_review_selected_product_count,
                selectedProductCount,
                productCount
            )
        }

        binding?.tpgSelectAll?.text = selectedProductCountWording
        binding?.tpgSelectAll?.isVisible = pageMode == PageMode.CREATE
    }

    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
    }


    private fun renderList(products: List<DelegateAdapterItem> ) {
        productAdapter.submit(products)
    }

    private fun renderEmptyState(
        productCount: Int,
        isLoading: Boolean,
        pageMode: PageMode,
        maxProductSelection: Int
    ) {
        val isCreateMode = pageMode == PageMode.CREATE
        binding?.run {
            recyclerView.isVisible = productCount.isMoreThanZero()
            checkbox.isVisible = productCount.isMoreThanZero() && isCreateMode
            dividerList.isVisible = productCount.isMoreThanZero() && isCreateMode
            tpgSelectAll.isVisible = productCount.isMoreThanZero() && isCreateMode
            tpgSelectedParentProductCount.isVisible = productCount.isMoreThanZero() && isCreateMode
            emptyState.isVisible = productCount.isZero() && !isLoading
            emptyState.emptyStateCTAID.setOnClickListener { activity?.finish() }

            when {
                !isCreateMode -> cardUnify2.gone()
                productCount.isZero() -> cardUnify2.invisible()
                else -> cardUnify2.visible()
            }

            when {
                productCount >= maxProductSelection -> tpgCtaAddProduct.gone()
                productCount.isMoreThanZero() && isCreateMode -> tpgCtaAddProduct.visible()
                else -> tpgCtaAddProduct.gone()
            }
        }
    }

    private fun renderSelectAllCheckbox(productCount: Int, selectedProductCount: Int, pageMode: PageMode) {
        binding?.checkbox?.isChecked = selectedProductCount == productCount
        binding?.checkbox?.isVisible = pageMode == PageMode.CREATE
    }

    private val onDeleteProductClick: (Int) -> Unit = { selectedItemPosition ->
        val selectedItem = productAdapter.getItems()[selectedItemPosition]
        val selectedItemId = (selectedItem.id() as? Long).orZero()

        viewModel.processEvent(ProductListEvent.TapRemoveProduct(selectedItemId))
    }

    private val onCheckboxClick: (Int, Boolean) -> Unit = { selectedItemPosition, isChecked ->
        val selectedItem = productAdapter.getItems()[selectedItemPosition]
        val selectedItemId = (selectedItem.id() as? Long).orZero()

        if (isChecked) {
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(selectedItemId))
        } else {
            viewModel.processEvent(ProductListEvent.RemoveProductFromSelection(selectedItemId))
        }
    }

    private val onVariantClick: (Int) -> Unit = { selectedItemPosition ->
        val selectedItem = productAdapter.getItems()[selectedItemPosition]
        val selectedParentProduct = (selectedItem as? Product)

        selectedParentProduct?.run {
            viewModel.processEvent(ProductListEvent.TapVariant(this))
        }

    }

    private fun displayVariantBottomSheet(
        isParentProductSelected: Boolean,
        selectedProduct: SelectedProduct,
        originalVariantIds: List<Long>
    ) {
        val bottomSheet = ReviewVariantBottomSheet.newInstance(
            isParentProductSelected,
            selectedProduct,
            originalVariantIds
        )
        bottomSheet.setOnSelectButtonClick { selectedVariantIds ->
            viewModel.processEvent(ProductListEvent.VariantUpdated(selectedProduct.parentProductId, selectedVariantIds))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun CompoundButton.isClickTriggeredByUserInteraction() : Boolean {
        return isPressed
    }

    private fun displayShareBottomSheet(
        selectedProducts: List<Product>,
        selectedProductImageUrls: List<String>,
        shop: ShopData
    ) {
        val voucherStartDate = Date()
        val voucherEndDate = Date()

        val endDate = voucherEndDate.formatTo(DateConstant.DATE_YEAR_PRECISION)
        val endHour = voucherEndDate.formatTo(DateConstant.TIME_MINUTE_PRECISION)

        val formattedShopName = MethodChecker.fromHtml(shop.name).toString()
        val title = String.format(
            getString(R.string.smvc_placeholder_share_component_outgoing_title),
            formattedShopName
        )
        val description = String.format(
            getString(R.string.smvc_placeholder_share_component_text_description),
            formattedShopName,
            endDate,
            endHour
        )

        val imageGeneratorParam = ShareComponentInstanceBuilder.Param(
            voucherId = 1239,
            isPublic = true,
            voucherCode = "UNVRCUAN",
            voucherStartTime = voucherStartDate,
            voucherEndTime = voucherEndDate,
            promoType = PromoType.CASHBACK,
            benefitType = BenefitType.NOMINAL,
            shopLogo = shop.logo,
            shopName = formattedShopName,
            discountAmount = 500_000,
            discountAmountMax = 1_000_000,
            productImageUrls = selectedProductImageUrls
        )

        shareComponentBottomSheet = shareComponentInstanceBuilder.build(
            imageGeneratorParam,
            title,
            onShareOptionsClicked = { shareModel ->
                handleShareOptionSelection(
                    imageGeneratorParam.voucherId,
                    shareModel,
                    title,
                    description,
                    shop.domain
                )
            },
            onCloseOptionClicked = {

            })

        shareComponentBottomSheet?.show(childFragmentManager, shareComponentBottomSheet?.tag)
    }

    private fun handleShareOptionSelection(
        voucherId: Long,
        shareModel: ShareModel,
        title: String,
        description: String,
        shopDomain: String
    ) {
        val shareCallback = object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                val wording = "$description ${linkerShareData?.shareUri.orEmpty()}"
                SharingUtil.executeShareIntent(
                    shareModel,
                    linkerShareData,
                    activity,
                    view,
                    wording
                )
                shareComponentBottomSheet?.dismiss()
            }

            override fun onError(linkerError: LinkerError?) {}
        }

        val linkerDataGenerator = LinkerDataGenerator()
        val outgoingDescription = getString(R.string.smvc_share_component_outgoing_text_description)
        val linkerShareData = linkerDataGenerator.generate(
            voucherId,
            userSession.shopId,
            shopDomain,
            shareModel,
            title,
            outgoingDescription
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                Int.ZERO,
                linkerShareData,
                shareCallback
            )
        )
    }

    private fun sendResult(selectedProducts: List<SelectedProduct>) {
        val returnIntent = Intent()
        returnIntent.putParcelableArrayListExtra(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS, ArrayList(selectedProducts))
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_ADD_NEW_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                val newlySelectedProducts = data?.getParcelableArrayListExtra<Product>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS).orEmpty()
                viewModel.processEvent(ProductListEvent.AddNewProductToSelection(newlySelectedProducts))
            }
        }
    }
}

