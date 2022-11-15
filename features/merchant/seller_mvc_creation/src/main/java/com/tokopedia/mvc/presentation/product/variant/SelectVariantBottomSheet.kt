package com.tokopedia.mvc.presentation.product.variant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetSelectVariantBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.presentation.product.variant.uimodel.SelectVariantEffect
import com.tokopedia.mvc.presentation.product.variant.uimodel.SelectVariantEvent
import com.tokopedia.mvc.presentation.product.variant.uimodel.SelectVariantUiState
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SelectVariantBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_PARENT_PRODUCT_ID = "parent_product"

        @JvmStatic
        fun newInstance(parentProduct: Product): SelectVariantBottomSheet {
            return SelectVariantBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_SELECTED_PARENT_PRODUCT_ID, parentProduct)
                }
            }
        }
    }


    private var binding by autoClearedNullable<SmvcBottomsheetSelectVariantBinding>()
    private val parentProduct by lazy { arguments?.getParcelable(BUNDLE_KEY_SELECTED_PARENT_PRODUCT_ID) as? Product }
    private var onSelectButtonClick: (List<Product.Variant>) -> Unit = {}

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val variantAdapter by lazy { VariantAdapter() }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SelectVariantViewModel::class.java) }

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
        binding = SmvcBottomsheetSelectVariantBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_select_variant))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        applyUnifyBackgroundColor()
        observeUiEffect()
        observeUiState()

        viewModel.processEvent(SelectVariantEvent.FetchProductVariants(parentProduct ?: return))
    }

    private fun setupDependencyInjection() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupView() {
        setupButton()
        setupCheckbox()
        setupList()
    }

    private fun setupButton() {
        binding?.btnSelect?.setOnClickListener {
            viewModel.processEvent(SelectVariantEvent.TapSelectButton)
        }
    }

    private fun setupList() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            attachDividerItemDecoration()
            variantAdapter.setOnVariantClick { selectedItemPosition, isSelected ->
                val selectedVariant = variantAdapter.snapshot()[selectedItemPosition]

                if (isSelected) {
                    viewModel.processEvent(SelectVariantEvent.AddProductToSelection(selectedVariant.variantProductId))
                } else {
                    viewModel.processEvent(SelectVariantEvent.RemoveProductFromSelection(selectedVariant.variantProductId))
                }

            }
            adapter = variantAdapter
        }
    }

    private fun setupCheckbox() {
        binding?.checkbox?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.processEvent(SelectVariantEvent.EnableSelectAllCheckbox)
            } else {
                viewModel.processEvent(SelectVariantEvent.DisableSelectAllCheckbox)
            }
        }
    }


    fun setOnSelectButtonClick(onSelectButtonClick: (List<Product.Variant>) -> Unit) {
        this.onSelectButtonClick = onSelectButtonClick
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

    private fun handleEffect(effect: SelectVariantEffect) {
        when (effect) {
            is SelectVariantEffect.ConfirmUpdateVariant -> {
                dismiss()
            }
        }
    }


    private fun handleUiState(uiState: SelectVariantUiState) {
        renderLoadingState(uiState.isLoading)
        renderSelectAllCheckbox(uiState)
        renderList(uiState)
        renderParentProduct(uiState.parentProduct)
        renderButton(uiState.parentProduct.modifiedVariants)
    }

    private fun renderButton(modifiedVariants: List<Product.Variant>) {
        val selectedProductsSize = modifiedVariants.count { it.isSelected }
        binding?.btnSelect?.isEnabled = selectedProductsSize.isMoreThanZero()

        val wording = if (selectedProductsSize.isZero()) {
            getString(R.string.smvc_select)
        } else {
            getString(R.string.smvc_placeholder_selected_variant_count, selectedProductsSize)
        }

        binding?.btnSelect?.text = wording
    }

    private fun renderParentProduct(parentProduct: Product) {
        binding?.run {
            tpgProductName.text = parentProduct.name
            tpgPrice.text = if (parentProduct.price.min == parentProduct.price.max) {
                context?.getString(
                    R.string.smvc_placeholder_product_price,
                    parentProduct.price.min.splitByThousand()
                )
            } else {
                context?.getString(
                    R.string.smvc_placeholder_product_price_range,
                    parentProduct.price.min.splitByThousand(),
                    parentProduct.price.max.splitByThousand()
                )
            }
            tpgStock.text = context?.getString(R.string.smvc_placeholder_total_stock, parentProduct.stock.splitByThousand())
            tpgSoldCount.text = context?.getString(R.string.smvc_placeholder_product_sold_count, parentProduct.txStats.sold.splitByThousand())
            imgParentProduct.loadImage(parentProduct.picture)
        }
    }

    private fun renderList(uiState: SelectVariantUiState) {
        variantAdapter.submit(uiState.parentProduct.modifiedVariants)
    }

    private fun renderSelectAllCheckbox(uiState: SelectVariantUiState) {
        binding?.checkbox?.isChecked = uiState.isSelectAllActive
    }

    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
    }

}
