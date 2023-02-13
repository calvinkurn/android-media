package com.tokopedia.mvc.presentation.product.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentProductListBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.presentation.product.list.adapter.ProductListAdapter
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEffect
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEvent
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListUiState
import com.tokopedia.mvc.presentation.product.variant.dialog.ConfirmationDialog
import com.tokopedia.mvc.presentation.product.variant.review.ReviewVariantBottomSheet
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.mvc.util.tracker.ProductListPageTracker
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ProductListFragment : BaseDaggerFragment() {

    companion object {

        @JvmStatic
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct>,
            showCtaChangeProductOnToolbar: Boolean,
            isEntryPointFromVoucherSummaryPage: Boolean,
            selectedWarehouseId: Long
        ): ProductListFragment {
            return ProductListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelableArrayList(
                        BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                        ArrayList(selectedProducts)
                    )
                    putParcelable(
                        BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION,
                        voucherConfiguration
                    )
                    putBoolean(
                        BundleConstant.BUNDLE_KEY_SHOW_CTA_CHANGE_PRODUCT_ON_TOOLBAR,
                        showCtaChangeProductOnToolbar
                    )
                    putBoolean(
                        BundleConstant.BUNDLE_KEY_IS_ENTRY_POINT_FROM_VOUCHER_SUMMARY_PAGE,
                        isEntryPointFromVoucherSummaryPage
                    )
                    putLong(
                        BundleConstant.BUNDLE_KEY_SELECTED_WAREHOUSE_ID,
                        selectedWarehouseId
                    )
                }
            }
        }
    }

    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val selectedParentProducts by lazy { arguments?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS) }
    private val voucherConfiguration by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }
    private val showCtaChangeProductOnToolbar by lazy { arguments?.getBoolean(BundleConstant.BUNDLE_KEY_SHOW_CTA_CHANGE_PRODUCT_ON_TOOLBAR).orFalse() }
    private val isEntryPointFromVoucherSummaryPage by lazy { arguments?.getBoolean(BundleConstant.BUNDLE_KEY_IS_ENTRY_POINT_FROM_VOUCHER_SUMMARY_PAGE).orFalse() }
    private val selectedWarehouseId by lazy { arguments?.getLong(BundleConstant.BUNDLE_KEY_SELECTED_WAREHOUSE_ID).orZero() }

    private var binding by autoClearedNullable<SmvcFragmentProductListBinding>()

    private val productAdapter by lazy {
        ProductListAdapter(onDeleteProductClick, onCheckboxClick, onVariantClick)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracker: ProductListPageTracker

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductListViewModel::class.java) }


    override fun getScreenName(): String = ProductListFragment::class.java.canonicalName.orEmpty()

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
        setupView()
        observeUiEffect()
        observeUiState()

        viewModel.processEvent(
            ProductListEvent.FetchProducts(
                pageMode ?: PageMode.CREATE,
                voucherConfiguration ?: return,
                selectedParentProducts?.toList().orEmpty(),
                showCtaChangeProductOnToolbar,
                isEntryPointFromVoucherSummaryPage,
                selectedWarehouseId
            )
        )
    }

    private fun setupView() {
        setupToolbar()
        setupCheckbox()
        setupRecyclerView()
        setupButton()
        setupClickListener()
        binding?.cardUnify2?.cardType = CardUnify2.TYPE_CLEAR
    }

    private fun setupToolbar() {
        binding?.header?.actionTextView?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapCtaChangeProduct)
        }
        binding?.header?.setNavigationOnClickListener { backToPreviousPage() }
    }

    private fun setupClickListener() {
        binding?.tpgCtaAddProduct?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapCtaAddProduct)
        }

        binding?.iconBulkDelete?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapBulkDeleteProduct)
        }
    }

    private fun setupButton() {
        binding?.btnContinue?.setOnClickListener {
            tracker.sendButtonContinueClickEvent(pageMode ?: return@setOnClickListener)
            viewModel.processEvent(ProductListEvent.TapContinueButton)
        }
        binding?.btnBack?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapBackButton)
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
            is ProductListEffect.ShowVariantBottomSheet -> displayVariantBottomSheet(effect.isParentProductSelected, effect.selectedProduct, effect.originalVariantIds, effect.pageMode)
            is ProductListEffect.ShowBulkDeleteProductConfirmationDialog -> showBulkDeleteProductConfirmationDialog(effect.toDeleteProductCount)
            is ProductListEffect.ShowDeleteProductConfirmationDialog -> showDeleteProductConfirmationDialog(effect.productId)
            is ProductListEffect.BulkDeleteProductSuccess -> binding?.cardUnify2.showToaster(message = getString(
                        R.string.smvc_placeholder_bulk_delete_product,
                        effect.deletedProductCount
                    ), ctaText = getString(R.string.smvc_ok))
            ProductListEffect.ProductDeleted -> binding?.cardUnify2.showToaster(message = getString(R.string.smvc_product_deleted), ctaText = getString(R.string.smvc_ok))
            is ProductListEffect.ProceedToVoucherPreviewPage -> navigateToVoucherPreviewPage(effect.voucherConfiguration, effect.selectedProducts, effect.selectedParentProductImageUrls, effect.pageMode)
            is ProductListEffect.ShowError -> binding?.cardUnify2?.showToasterError(effect.error)
            ProductListEffect.BackToPreviousPage -> backToPreviousPage()
            is ProductListEffect.RedirectToAddProductPage -> redirectToAddProductPage(effect.voucherConfiguration)
            is ProductListEffect.RedirectToPreviousPage -> redirectToPreviousPage(effect.selectedProductCount, effect.pageMode)
            is ProductListEffect.TapBackButton -> {
                tracker.sendClickBackButtonEvent(effect.originalPageMode)
            }
        }
    }

    private fun redirectToPreviousPage(selectedProductCount: Int, pageMode: PageMode) {
        if (selectedProductCount.isZero()) {
            tracker.sendClickBackButtonEvent(pageMode)
        } else {
            tracker.sendClickToolbarBackButtonWithProductSelectedEvent(pageMode)
        }

        activity?.finish()
    }


    private fun handleUiState(uiState: ProductListUiState) {
        renderToolbar(uiState.originalPageMode, uiState.currentPageMode, uiState.showCtaChangeProductOnToolbar)
        renderTopSection(uiState.currentPageMode, uiState.products.count(), uiState.maxProductSelection)
        renderLoadingState(uiState.isLoading)
        renderList(uiState.products)
        renderEmptyState(uiState.products.count(), uiState.isLoading, uiState.currentPageMode)
        renderProductCounter(uiState.products.count(), uiState.selectedProductsIdsToBeRemoved.count(), uiState.currentPageMode)
        renderBulkDeleteIcon(uiState.selectedProductsIdsToBeRemoved.count())
        renderSelectAllCheckbox(uiState.products.count(), uiState.selectedProductsIdsToBeRemoved.count(), uiState.currentPageMode)
        renderButton(uiState.originalPageMode)
    }

    private fun renderToolbar(originalPageMode: PageMode, currentPageMode: PageMode, showCtaChangeProductOnToolbar: Boolean) {
        when {
            currentPageMode == PageMode.CREATE -> {
                binding?.header?.actionTextView?.gone()
                binding?.header?.isShowBackButton = true
            }
            showCtaChangeProductOnToolbar -> {
                binding?.header?.actionTextView?.visible()
                binding?.header?.actionText = getString(R.string.smvc_update_product)
                binding?.header?.useCloseIcon()
            }
        }

        val showSubtitle = originalPageMode == PageMode.CREATE
        binding?.header?.subheaderView?.isVisible = showSubtitle
    }

    private fun renderButton(originalPageMode: PageMode) {
        binding?.btnContinue?.text = if (originalPageMode == PageMode.CREATE) {
            getString(R.string.smvc_continue)
        } else {
            getString(R.string.smvc_save)
        }
    }

    private fun renderBulkDeleteIcon(selectedProductCount: Int) {
        binding?.iconBulkDelete?.isVisible = selectedProductCount.isMoreThanZero()
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


    private fun renderList(products: List<Product> ) {
        binding?.recyclerView?.isVisible = products.count().isMoreThanZero()
        productAdapter.submit(products)
    }

    private fun renderTopSection(pageMode: PageMode, productCount: Int, maxProductSelection: Int) {
        val isCreateMode = pageMode == PageMode.CREATE
        binding?.run {
            dividerList.isVisible = productCount.isMoreThanZero() && isCreateMode
            tpgSelectedParentProductCount.isVisible = productCount.isMoreThanZero() && isCreateMode

            when {
                productCount >= maxProductSelection -> tpgCtaAddProduct.gone()
                productCount.isMoreThanZero() && isCreateMode -> tpgCtaAddProduct.visible()
                else -> tpgCtaAddProduct.gone()
            }
        }
    }

    private fun renderEmptyState(productCount: Int, isLoading: Boolean, pageMode: PageMode) {
        val isCreateMode = pageMode == PageMode.CREATE

        binding?.run {
            emptyState.isVisible = productCount.isZero() && !isLoading
            emptyState.emptyStateCTAID.setOnClickListener {
                viewModel.processEvent(ProductListEvent.TapCtaAddProduct)
            }

            when {
                !isCreateMode -> cardUnify2.gone()
                productCount.isZero() -> cardUnify2.invisible()
                else -> cardUnify2.visible()
            }
        }
    }

    private fun renderSelectAllCheckbox(productCount: Int, selectedProductCount: Int, pageMode: PageMode) {
        val isCreateMode = pageMode == PageMode.CREATE

        binding?.run {
            checkbox.isChecked = selectedProductCount == productCount
            checkbox.isVisible = productCount.isMoreThanZero() && isCreateMode
            tpgSelectAll.isVisible = productCount.isMoreThanZero() && isCreateMode
            checkbox.isVisible = productCount.isMoreThanZero() && isCreateMode
        }

    }

    private val onDeleteProductClick: (Int) -> Unit = { selectedItemPosition ->
        val selectedItem = productAdapter.snapshot()[selectedItemPosition]

        viewModel.processEvent(ProductListEvent.TapRemoveProduct(selectedItem.id))
    }

    private val onCheckboxClick: (Int, Boolean) -> Unit = { selectedItemPosition, isChecked ->
        val selectedItem = productAdapter.snapshot()[selectedItemPosition]

        if (isChecked) {
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(selectedItem.id))
        } else {
            viewModel.processEvent(ProductListEvent.RemoveProductFromSelection(selectedItem.id))
        }
    }

    private val onVariantClick: (Int) -> Unit = { selectedItemPosition ->
        val selectedItem = productAdapter.snapshot()[selectedItemPosition]
        val selectedParentProduct = (selectedItem as? Product)

        selectedParentProduct?.run {
            viewModel.processEvent(ProductListEvent.TapVariant(this))
        }

    }

    private fun displayVariantBottomSheet(
        isParentProductSelected: Boolean,
        selectedProduct: SelectedProduct,
        originalVariantIds: List<Long>,
        pageMode: PageMode
    ) {
        val isVariantCheckable = pageMode == PageMode.CREATE
        val isVariantDeletable = pageMode == PageMode.CREATE
        val enableBulkDeleteProduct = pageMode == PageMode.CREATE
        val bottomSheetTitle = if (pageMode == PageMode.CREATE) {
            getString(R.string.smvc_select_variant)
        } else {
            getString(R.string.smvc_variant_list)
        }
        val showPrimaryButton = pageMode == PageMode.CREATE

        val bottomSheet = ReviewVariantBottomSheet.newInstance(
            isParentProductSelected,
            selectedProduct,
            originalVariantIds,
            isVariantCheckable,
            isVariantDeletable,
            enableBulkDeleteProduct,
            bottomSheetTitle,
            showPrimaryButton
        )

        bottomSheet.setOnSelectButtonClick { selectedVariantIds ->
            viewModel.processEvent(ProductListEvent.VariantUpdated(selectedProduct.parentProductId, selectedVariantIds))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun CompoundButton.isClickTriggeredByUserInteraction() : Boolean {
        return isPressed
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NumberConstant.REQUEST_CODE_ADD_PRODUCT_TO_EXISTING_SELECTION) {
            if (resultCode == Activity.RESULT_OK) {
                val newlySelectedProducts = data?.getParcelableArrayListExtra<Product>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS).orEmpty()
                viewModel.processEvent(ProductListEvent.AddNewProductToSelection(newlySelectedProducts))
            }
        }
    }

    private fun showBulkDeleteProductConfirmationDialog(productCountToBeDeleted: Int) {
        if (!isAdded) return

        ConfirmationDialog.show(
            context = activity ?: return,
            title = getString(R.string.smvc_placeholder_bulk_delete_product_confirmation, productCountToBeDeleted),
            description = getString(R.string.smvc_delete_product_description),
            primaryButtonTitle = getString(R.string.smvc_proceed_delete),
            onPrimaryButtonClick = { viewModel.processEvent(ProductListEvent.ApplyBulkDeleteProduct) }
        )
    }

    private fun showDeleteProductConfirmationDialog(productId: Long){
        if (!isAdded) return

        ConfirmationDialog.show(
            context = activity ?: return,
            title = getString(R.string.smvc_delete_product),
            description = getString(R.string.smvc_delete_product_description),
            primaryButtonTitle = getString(R.string.smvc_proceed_delete),
            onPrimaryButtonClick = { viewModel.processEvent(ProductListEvent.ApplyRemoveProduct(productId)) }
        )
    }

    private fun navigateToVoucherPreviewPage(
        voucherConfiguration: VoucherConfiguration,
        selectedProducts: List<SelectedProduct>,
        selectedParentProductImageUrls: List<String>,
        pageMode: PageMode
    ) {
        SummaryActivity.start(context, voucherConfiguration, selectedProducts)
        val returnIntent = Intent()
        returnIntent.putParcelableArrayListExtra(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS, ArrayList(selectedProducts))
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()
    }

    private fun backToPreviousPage() {
        viewModel.processEvent(ProductListEvent.TapToolbarBackIcon)
        activity?.finish()
    }

    private fun redirectToAddProductPage(voucherConfiguration: VoucherConfiguration) {
        val intent = AddProductActivity.buildEditModeIntent(
            activity ?: return,
            voucherConfiguration
        )
        startActivityForResult(intent, NumberConstant.REQUEST_CODE_ADD_PRODUCT_TO_EXISTING_SELECTION)
    }

    private fun HeaderUnify.useCloseIcon() {
        isShowBackButton = false
        navigationIcon = ContextCompat.getDrawable(activity ?: return, R.drawable.ic_smvc_close)
    }

}

