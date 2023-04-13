package com.tokopedia.mvc.presentation.product.variant.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetReviewVariantBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.presentation.product.variant.dialog.ConfirmationDialog
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEffect
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEvent
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantUiState
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class ReviewVariantBottomSheet: BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_PRODUCT = "selected_product"
        private const val BUNDLE_PARENT_PRODUCT_IS_SELECTED = "is_parent_product_selected"
        private const val BUNDLE_ORIGINAL_VARIANT_IDS = "original_variant_ids"
        private const val BUNDLE_KEY_IS_VARIANT_CHECKABLE = "is_variant_checkable"
        private const val BUNDLE_KEY_IS_VARIANT_DELETABLE = "is_variant_deletable"
        private const val BUNDLE_KEY_ENABLE_BULK_DELETE_PRODUCT = "enable_bulk_delete_product"
        private const val BUNDLE_KEY_BOTTOM_SHEET_TITLE = "bottom_sheet_title"
        private const val BUNDLE_KEY_SHOW_PRIMARY_BUTTON = "show_primary_button"
        private const val DIVIDER_MARGIN_LEFT = 16
        private const val ONE_VARIANT = 1

        @JvmStatic
        fun newInstance(
            isParentProductSelected: Boolean,
            selectedProduct: SelectedProduct,
            originalVariantIds: List<Long>,
            isVariantCheckable: Boolean,
            isVariantDeletable: Boolean,
            enableBulkDeleteProduct: Boolean,
            bottomSheetTitle: String,
            showPrimaryButton: Boolean
        ): ReviewVariantBottomSheet {
            return ReviewVariantBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(BUNDLE_PARENT_PRODUCT_IS_SELECTED, isParentProductSelected)
                    putParcelable(BUNDLE_KEY_SELECTED_PRODUCT, selectedProduct)
                    putLongArray(BUNDLE_ORIGINAL_VARIANT_IDS, originalVariantIds.toLongArray())
                    putBoolean(BUNDLE_KEY_IS_VARIANT_CHECKABLE, isVariantCheckable)
                    putBoolean(BUNDLE_KEY_IS_VARIANT_DELETABLE, isVariantDeletable)
                    putBoolean(BUNDLE_KEY_ENABLE_BULK_DELETE_PRODUCT, enableBulkDeleteProduct)
                    putString(BUNDLE_KEY_BOTTOM_SHEET_TITLE, bottomSheetTitle)
                    putBoolean(BUNDLE_KEY_SHOW_PRIMARY_BUTTON, showPrimaryButton)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcBottomsheetReviewVariantBinding>()
    private val isParentProductSelected by lazy { arguments?.getBoolean(BUNDLE_PARENT_PRODUCT_IS_SELECTED, false).orFalse() }
    private val parentProduct by lazy { arguments?.getParcelable(BUNDLE_KEY_SELECTED_PRODUCT) as? SelectedProduct }
    private val originalVariantIds by lazy { arguments?.getLongArray(BUNDLE_ORIGINAL_VARIANT_IDS) }
    private val isVariantCheckable by lazy { arguments?.getBoolean(BUNDLE_KEY_IS_VARIANT_CHECKABLE) }
    private val isVariantDeletable by lazy { arguments?.getBoolean(BUNDLE_KEY_IS_VARIANT_DELETABLE) }
    private val enableBulkDeleteProduct by lazy { arguments?.getBoolean(BUNDLE_KEY_ENABLE_BULK_DELETE_PRODUCT).orTrue() }
    private val bottomSheetHeaderTitle by lazy { arguments?.getString(BUNDLE_KEY_BOTTOM_SHEET_TITLE).orEmpty() }
    private val showPrimaryButton by lazy { arguments?.getBoolean(BUNDLE_KEY_SHOW_PRIMARY_BUTTON).orTrue() }
    private var onSaveButtonClick: (Set<Long>) -> Unit = {}

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val reviewVariantAdapter by lazy { ReviewVariantAdapter() }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ReviewVariantViewModel::class.java) }

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetReviewVariantBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(bottomSheetHeaderTitle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        applyUnifyBackgroundColor()
        observeUiEffect()
        observeUiState()

        viewModel.processEvent(
            ReviewVariantEvent.FetchProductVariants(
                isParentProductSelected,
                parentProduct ?: return,
                originalVariantIds?.toList().orEmpty(),
                isVariantCheckable.orFalse(),
                isVariantDeletable.orFalse()
            )
        )
    }

    private fun setupDependencyInjection() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupView() {
        setupClickListener()
        setupBottomSheetWidgetAppearance()
        setupList()
    }

    private fun setupClickListener() {
        binding?.btnSave?.setOnClickListener {
            viewModel.processEvent(ReviewVariantEvent.TapSelectButton)
        }
        binding?.iconBulkDelete?.setOnClickListener { viewModel.processEvent(ReviewVariantEvent.TapBulkDeleteVariant) }
    }

    private fun setupList() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            attachDividerItemDecoration(insetLeft = DIVIDER_MARGIN_LEFT)
            reviewVariantAdapter.setOnVariantClick { selectedItemPosition, isSelected ->
                val selectedVariant = reviewVariantAdapter.snapshot()[selectedItemPosition]

                if (isSelected) {
                    viewModel.processEvent(ReviewVariantEvent.AddVariantToSelection(selectedVariant.variantId))
                } else {
                    viewModel.processEvent(ReviewVariantEvent.RemoveVariantFromSelection(selectedVariant.variantId))
                }

            }

            reviewVariantAdapter.setOnDeleteVariantClick { selectedItemPosition ->
                val selectedVariant = reviewVariantAdapter.snapshot()[selectedItemPosition]
                viewModel.processEvent(ReviewVariantEvent.TapRemoveVariant(selectedVariant.variantId))
            }

            adapter = reviewVariantAdapter
        }
    }

    private fun setupBottomSheetWidgetAppearance() {
        binding?.dividerList?.isVisible = enableBulkDeleteProduct
        binding?.tpgSelectAll?.isVisible = enableBulkDeleteProduct
        binding?.iconBulkDelete?.isVisible = enableBulkDeleteProduct
        binding?.btnSave?.isVisible = showPrimaryButton

        binding?.checkbox?.isVisible = enableBulkDeleteProduct
        binding?.checkbox?.setOnCheckedChangeListener { view, isChecked ->
            if (view.isClickTriggeredByUserInteraction()) {
                if (isChecked) {
                    viewModel.processEvent(ReviewVariantEvent.EnableSelectAllCheckbox)
                } else {
                    viewModel.processEvent(ReviewVariantEvent.DisableSelectAllCheckbox)
                }
            }
        }
    }


    fun setOnSelectButtonClick(onSelectButtonClick: (Set<Long>) -> Unit) {
        this.onSaveButtonClick = onSelectButtonClick
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

    private fun handleEffect(effect: ReviewVariantEffect) {
        when (effect) {
            is ReviewVariantEffect.ConfirmUpdateVariant -> {
                onSaveButtonClick(effect.selectedVariantIds)
                dismiss()
            }
            is ReviewVariantEffect.ShowBulkDeleteVariantConfirmationDialog -> {
                if (!isAdded) return

                ConfirmationDialog.show(
                    context = activity ?: return,
                    title = getString(R.string.smvc_placeholder_bulk_delete_variant_confirmation, effect.toDeleteProductCount),
                    description = getString(R.string.smvc_delete_variant_description),
                    primaryButtonTitle = getString(R.string.smvc_proceed_delete),
                    onPrimaryButtonClick = { viewModel.processEvent(ReviewVariantEvent.ApplyBulkDeleteVariant) }
                )
            }
            is ReviewVariantEffect.ShowDeleteVariantConfirmationDialog -> {
                if (!isAdded) return

                ConfirmationDialog.show(
                    context = activity ?: return,
                    title = getString(R.string.smvc_delete_variant),
                    description = getString(R.string.smvc_delete_variant_description),
                    primaryButtonTitle = getString(R.string.smvc_proceed_delete),
                    onPrimaryButtonClick = { viewModel.processEvent(ReviewVariantEvent.ApplyRemoveVariant(effect.productId)) }
                )
            }
            is ReviewVariantEffect.ShowError -> {
                binding?.cardUnify2?.showToasterError(effect.error)
            }
        }
    }


    private fun handleUiState(uiState: ReviewVariantUiState) {
        renderLoadingState(uiState.isLoading)
        renderSelectAllCheckbox(uiState)
        renderBulkDeleteIcon(uiState.selectedVariantIds)
        renderList(uiState)
        renderParentProduct(uiState)
        renderButton(uiState.originalVariantIds.count(), uiState.variants.count(), uiState.selectedVariantIds.count())
        observeVariantDeletion(uiState.variants.count(), uiState.isLoading)
    }

    private fun renderBulkDeleteIcon(selectedVariantIds: Set<Long>) {
        binding?.iconBulkDelete?.isVisible = selectedVariantIds.count() >= ONE_VARIANT
    }

    private fun renderButton(originalVariantCount: Int, variantCount: Int, selectedVariantCount: Int) {
        val anyVariantSelected = selectedVariantCount.isMoreThanZero()
        val anyVariantDeleted = variantCount != originalVariantCount
        binding?.btnSave?.isEnabled = anyVariantDeleted || anyVariantSelected
    }

    private fun renderParentProduct(uiState: ReviewVariantUiState) {
        binding?.run {
            tpgProductName.text = uiState.parentProductName
            tpgStock.text = context?.getString(R.string.smvc_placeholder_total_stock, uiState.parentProductStock.splitByThousand())
            tpgSoldCount.text = context?.getString(R.string.smvc_placeholder_product_sold_count, uiState.parentProductStock.splitByThousand())
            imgParentProduct.loadImage(uiState.parentProductImageUrl)
        }
    }

    private fun renderList(uiState: ReviewVariantUiState) {
        reviewVariantAdapter.submit(uiState.variants)
    }

    private fun renderSelectAllCheckbox(uiState: ReviewVariantUiState) {
        if (uiState.isLoading) return

        val selectedVariantCount = uiState.selectedVariantIds.count()
        val allVariantsCount = uiState.variants.count()

        when {
            selectedVariantCount.isZero() -> {
                binding?.checkbox?.isChecked = false
            }
            selectedVariantCount < allVariantsCount -> {
                binding?.checkbox?.setIndeterminate(true)
                binding?.checkbox?.isChecked = true
            }
            selectedVariantCount == allVariantsCount -> {
                binding?.checkbox?.isChecked = true
                binding?.checkbox?.setIndeterminate(false)
            }
        }

        binding?.checkbox?.skipAnimation()
    }

    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
    }

    private fun CompoundButton.isClickTriggeredByUserInteraction() : Boolean {
        return isPressed
    }

    private fun observeVariantDeletion(variantCount: Int, isLoading: Boolean) {
        if (isLoading) return

        if (variantCount.isZero()) {
            onSaveButtonClick(emptySet())
            dismiss()
        }
    }

}
