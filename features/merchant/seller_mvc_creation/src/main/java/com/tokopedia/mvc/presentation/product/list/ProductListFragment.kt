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
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentProductListBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.presentation.product.add.AddProductFragment
import com.tokopedia.mvc.presentation.product.list.adapter.ProductListAdapter
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEffect
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEvent
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListUiState
import com.tokopedia.mvc.presentation.product.variant.dialog.ConfirmationDialog
import com.tokopedia.mvc.presentation.product.variant.review.ReviewVariantBottomSheet
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ProductListFragment : BaseDaggerFragment() {

    companion object {
        private const val ONE_PRODUCT = 1

        @JvmStatic
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct>
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
                }
            }
        }
    }

    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val selectedParentProducts by lazy { arguments?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS) }
    private val voucherConfiguration by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }
    private var binding by autoClearedNullable<SmvcFragmentProductListBinding>()

    private val productAdapter by lazy {
        ProductListAdapter(onDeleteProductClick, onCheckboxClick, onVariantClick)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductListViewModel::class.java) }


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
                pageMode ?: PageMode.CREATE,
                voucherConfiguration ?: return,
                selectedParentProducts?.toList().orEmpty()
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
        val showSubtitle = pageMode == PageMode.CREATE
        binding?.header?.actionTextView?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.TapCtaChangeProduct)
        }
        binding?.header?.subheaderView?.isVisible = showSubtitle
        binding?.header?.setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun setupClickListener() {
        binding?.tpgCtaAddProduct?.setOnClickListener {
            if (pageMode == PageMode.EDIT) {
                val intent = AddProductActivity.buildEditModeIntent(
                    activity ?: return@setOnClickListener,
                    voucherConfiguration ?: return@setOnClickListener
                )
                startActivityForResult(intent, NumberConstant.REQUEST_CODE_ADD_PRODUCT_TO_SELECTION)
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
            is ProductListEffect.ShowVariantBottomSheet -> displayVariantBottomSheet(effect.isParentProductSelected, effect.selectedProduct, effect.originalVariantIds, pageMode ?: return)
            is ProductListEffect.ShowBulkDeleteProductConfirmationDialog -> showBulkDeleteProductConfirmationDialog(effect.toDeleteProductCount)
            is ProductListEffect.ShowDeleteProductConfirmationDialog -> showDeleteProductConfirmationDialog(effect.productId)
            is ProductListEffect.BulkDeleteProductSuccess -> binding?.cardUnify2.showToaster(message = getString(
                        R.string.smvc_placeholder_bulk_delete_product,
                        effect.deletedProductCount
                    ), ctaText = getString(R.string.smvc_ok))
            ProductListEffect.ProductDeleted -> binding?.cardUnify2.showToaster(message = getString(R.string.smvc_product_deleted), ctaText = getString(R.string.smvc_ok))
            is ProductListEffect.ProceedToVoucherPreviewPage -> navigateToVoucherPreviewPage(effect.voucherConfiguration, effect.selectedProducts, effect.selectedParentProductImageUrls)
            is ProductListEffect.SendResultToCallerPage -> sendResultToCallerActivity(effect.selectedProducts)
            is ProductListEffect.ShowError -> binding?.cardUnify2?.showToasterError(effect.error)
        }
    }


    private fun handleUiState(uiState: ProductListUiState) {
        renderToolbar(uiState.pageMode)
        renderTopSection(uiState.pageMode, uiState.products.count(), uiState.maxProductSelection)
        renderLoadingState(uiState.isLoading)
        renderList(uiState.products)
        renderEmptyState(uiState.products.count(), uiState.isLoading, uiState.pageMode)
        renderProductCounter(uiState.products.count(), uiState.selectedProductsIds.count(), uiState.pageMode)
        renderBulkDeleteIcon(uiState.selectedProductsIds.count())
        renderSelectAllCheckbox(uiState.products.count(), uiState.selectedProductsIds.count(), uiState.pageMode)
        renderButton()
    }

    private fun renderToolbar(pageMode: PageMode) {
        if (pageMode == PageMode.CREATE) {
            binding?.header?.actionTextView?.gone()
        } else {
            binding?.header?.actionTextView?.visible()
            binding?.header?.actionText = getString(R.string.smvc_update_product)
            binding?.header?.subheaderView?.gone()
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
            emptyState.emptyStateCTAID.setOnClickListener { activity?.finish() }

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

        val bottomSheet = ReviewVariantBottomSheet.newInstance(
            isParentProductSelected,
            selectedProduct,
            originalVariantIds,
            isVariantCheckable,
            isVariantDeletable
        )
        bottomSheet.setOnSelectButtonClick { selectedVariantIds ->
            viewModel.processEvent(ProductListEvent.VariantUpdated(selectedProduct.parentProductId, selectedVariantIds))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun CompoundButton.isClickTriggeredByUserInteraction() : Boolean {
        return isPressed
    }


    private fun sendResultToCallerActivity(selectedProducts: List<SelectedProduct>) {
        val returnIntent = Intent()
        returnIntent.putParcelableArrayListExtra(
            BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS,
            ArrayList(selectedProducts)
        )
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NumberConstant.REQUEST_CODE_ADD_PRODUCT_TO_SELECTION) {
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
        selectedParentProductImageUrls: List<String>
    ) {
        SummaryActivity.start(context, voucherConfiguration, selectedProducts)
    }
}

