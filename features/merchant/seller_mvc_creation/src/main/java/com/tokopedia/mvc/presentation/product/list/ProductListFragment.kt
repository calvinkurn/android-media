package com.tokopedia.mvc.presentation.product.list

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
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentProductListBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.presentation.product.add.AddProductFragment
import com.tokopedia.mvc.presentation.product.list.adapter.ProductReviewDelegateAdapter
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEffect
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEvent
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListUiState
import com.tokopedia.mvc.presentation.product.variant.SelectVariantBottomSheet
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ProductListFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(selectedProducts: List<SelectedProduct>): ProductListFragment {
            return ProductListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(
                        BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                        ArrayList(selectedProducts)
                    )
                }
            }
        }
    }

    private val selectedParentProducts by lazy { arguments?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS) }
    private var binding by autoClearedNullable<SmvcFragmentProductListBinding>()

    private val productAdapter by lazy {
        CompositeAdapter.Builder()
            .add(ProductReviewDelegateAdapter(onDeleteProductClick, onCheckboxClick, onVariantClick))
            .build()
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

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
                VoucherAction.CREATE,
                PromoType.CASHBACK,
                selectedParentProducts?.toList().orEmpty()
            )
        )
    }

    private fun setupView() {
        setupCheckbox()
        setupRecyclerView()
        setupButton()
        setupCta()
    }

    private fun setupCta() {
        binding?.tpgCtaAddProduct?.setOnClickListener { activity?.finish() }
    }

    private fun setupButton() {
        binding?.btnContinue?.setOnClickListener {
            viewModel.processEvent(ProductListEvent.ConfirmAddProduct)
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
                displayVariantBottomSheet(effect.selectedParentProduct)
            }
            is ProductListEffect.ConfirmAddProduct -> {

            }
        }
    }


    private fun handleUiState(uiState: ProductListUiState) {
        renderLoadingState(uiState.isLoading)
        renderList(uiState.products)
        renderEmptyState(uiState.products.count(), uiState.isLoading)
        renderProductCounter(uiState.products.count())
        renderSelectAllCheckbox(uiState)
    }

    private fun renderProductCounter(productCount: Int) {
        binding?.tpgSelectedParentProductCount?.text =
            getString(R.string.smvc_placholder_selected_product_count, productCount)
    }

    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
    }


    private fun renderList(products: List<DelegateAdapterItem> ) {
        productAdapter.submit(products)
    }

    private fun renderEmptyState(productCount: Int, isLoading: Boolean) {
        binding?.run {
            recyclerView.isVisible = productCount.isMoreThanZero()
            cardUnify2.isVisible = productCount.isMoreThanZero()
            checkbox.isVisible = productCount.isMoreThanZero()
            dividerList.isVisible = productCount.isMoreThanZero()
            tpgSelectAll.isVisible = productCount.isMoreThanZero()
            tpgSelectedParentProductCount.isVisible = productCount.isMoreThanZero()
            tpgCtaAddProduct.isVisible = productCount.isMoreThanZero()
            emptyState.isVisible = productCount.isZero() && !isLoading
            emptyState.emptyStateCTAID.setOnClickListener { activity?.finish() }
        }
    }

    private fun renderSelectAllCheckbox(uiState: ProductListUiState) {
        val selectedProductCount = uiState.selectedProductsIds.count()
        binding?.checkbox?.isChecked = selectedProductCount == uiState.products.count()
    }

    private val onDeleteProductClick: (Int) -> Unit = { selectedItemPosition ->
        val selectedItem = productAdapter.getItems()[selectedItemPosition]
        val selectedItemId = (selectedItem.id() as? Long).orZero()

        viewModel.processEvent(ProductListEvent.RemoveProduct(selectedItemId))
    }

    private val onCheckboxClick: (Int, Boolean) -> Unit = { selectedItemPosition, isChecked ->
        val selectedItem = productAdapter.getItems()[selectedItemPosition]
        val selectedItemId = (selectedItem.id() as? Long).orZero()

        if (isChecked) {
            viewModel.processEvent(ProductListEvent.AddProductToSelection(selectedItemId))
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

    private fun displayVariantBottomSheet(selectedParentProduct: Product) {
        val bottomSheet = SelectVariantBottomSheet.newInstance(selectedParentProduct)
        bottomSheet.setOnSelectButtonClick { selectedVariantIds ->
            viewModel.processEvent(ProductListEvent.VariantUpdated(selectedParentProduct.id, selectedVariantIds))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun CompoundButton.isClickTriggeredByUserInteraction() : Boolean {
        return isPressed
    }
}

