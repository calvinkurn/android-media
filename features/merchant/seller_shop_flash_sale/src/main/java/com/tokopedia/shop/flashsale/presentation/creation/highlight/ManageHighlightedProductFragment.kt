package com.tokopedia.shop.flashsale.presentation.creation.highlight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.FragmentSsfsManageHighlightedProductBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.extension.showLoading
import com.tokopedia.shop.flashsale.common.extension.showToaster
import com.tokopedia.shop.flashsale.common.preference.SharedPreferenceDataStore
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.presentation.creation.highlight.adapter.HighlightedProductAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ManageHighlightedProductFragment :
    BaseSimpleListFragment<HighlightedProductAdapter, HighlightableProduct>() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val ONE_PRODUCT = 1
        private const val MAX_PRODUCT_SELECTION = 5
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"

        @JvmStatic
        fun newInstance(campaignId: Long): ManageHighlightedProductFragment {
            val fragment = ManageHighlightedProductFragment()
            fragment.arguments = Bundle().apply {
                putLong(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            }
            return fragment
        }

    }

    private var binding by autoClearedNullable<FragmentSsfsManageHighlightedProductBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore

    private val campaignId by lazy {
        arguments?.getLong(BUNDLE_KEY_CAMPAIGN_ID).orZero()
    }
    private var isFirstLoad = true
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageHighlightedProductViewModel::class.java) }


    private val productAdapter by lazy {
        HighlightedProductAdapter(
            onProductClicked,
            onProductSelectionChange
        )
    }

    override fun getScreenName(): String = ManageHighlightedProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSsfsManageHighlightedProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setFragmentToUnifyBgColor()
        observeProducts()
    }

    private fun setupView() {
        setupButton()
        setupToolbar()
        setupSearchBar()
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearAllData()
                    binding?.groupNoSearchResult?.gone()
                    getProducts(Constant.FIRST_PAGE)
                    return@setOnEditorActionListener false
                }
                return@setOnEditorActionListener false
            }
            searchBar.clearListener = { clearSearchBar() }
        }
    }

    private fun setupToolbar() {
        binding?.run {
            header.setOnClickListener { activity?.onBackPressed() }
        }
    }

    private fun setupButton() {
        binding?.run {
            btnCreateCampaign.setOnClickListener {
                binding?.btnCreateCampaign?.showLoading()
            }
        }
    }


    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    isFirstLoad = false
                    binding?.groupContent?.visible()
                    handleProducts(it.data)
                }
                is Fail -> {
                    binding?.cardView?.gone()
                    binding?.groupContent?.gone()
                    binding?.root showError it.throwable
                }
            }
        }
    }


    private fun handleProducts(data: List<HighlightableProduct>) {
        val currentItemCount = productAdapter.getItems().size.orZero()
        val hasData = currentItemCount > Int.ZERO

        if (data.isEmpty() && !hasData) {
            binding?.groupNoSearchResult?.visible()
            binding?.recyclerView?.gone()

        } else {
            renderList(data, data.size >= PAGE_SIZE)
            binding?.recyclerView?.visible()
            binding?.groupNoSearchResult?.gone()
        }
    }

    private val onProductClicked: (HighlightableProduct, Int) -> Unit = { product, _ ->
        val isDisabled = product.disableClick || product.disabled
        handleProductClick(isDisabled, "")
    }

    private val onProductSelectionChange: (
        HighlightableProduct,
        Boolean
    ) -> Unit = { selectedProduct, isSelected ->

        if (isSelected) {
            val currentSelectedProductCount = viewModel.getSelectedProductIds().size
            handleAddProductToSelection(currentSelectedProductCount, selectedProduct)
        } else {
            handleRemoveProductFromSelection(selectedProduct)
        }

    }

    private fun handleAddProductToSelection(
        currentSelectedProductCount: Int,
        selectedProduct: HighlightableProduct
    ) {
         val nextCounter = currentSelectedProductCount + ONE_PRODUCT
         if (nextCounter > MAX_PRODUCT_SELECTION) {
             untickProduct(selectedProduct)

         } else {
             val remainingSelection = MAX_PRODUCT_SELECTION - viewModel.getSelectedProductIds().size

             if (remainingSelection > Int.ZERO) {
                 binding?.root showToaster getString(R.string.sfs_successfully_highlighted)
                 tickProduct(selectedProduct)
                 viewModel.addProductToSelection(selectedProduct)
             } else {
                 untickProduct(selectedProduct)

             }
         }

         refreshButton()
    }

    private fun handleRemoveProductFromSelection(selectedProduct: HighlightableProduct) {
        viewModel.removeProductFromSelection(selectedProduct)
        untickProduct(selectedProduct)

        refreshButton()
    }

    private fun handleProductClick(
        isDisabled: Boolean,
        disableReason: String
    ) {
        val selectedProductCount = viewModel.getSelectedProductIds().size

    }

    private fun tickProduct(selectedProduct: HighlightableProduct) {
        val products = adapter?.getItems() ?: return
        val updatedProduct = viewModel.markAsSelected(selectedProduct, products)
        productAdapter.submit(updatedProduct)
    }

    private fun untickProduct(selectedProduct: HighlightableProduct) {
        val products = adapter?.getItems() ?: return
        val updatedProduct = viewModel.markAsUnselected(selectedProduct, products)
        productAdapter.submit(updatedProduct)
    }

    override fun createAdapter() = productAdapter
    override fun getRecyclerView(view: View) = binding?.recyclerView
    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage() = PAGE_SIZE

    override fun addElementToAdapter(list: List<HighlightableProduct>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        getProducts(page)
    }

    private fun getProducts(page: Int) {
        val offset = if (isFirstLoad) {
            Constant.FIRST_PAGE
        } else {
            page * PAGE_SIZE
        }

        val searchKeyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()

        viewModel.getProducts(campaignId, searchKeyword, offset)
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
        adapter?.hideLoading()
    }

    override fun onGetListError(message: String) {
        adapter?.hideLoading()
    }

    override fun onScrolled(xScrollAmount: Int, yScrollAmount: Int) {

    }

    private fun clearSearchBar() {
        clearAllData()
        getProducts(Constant.FIRST_PAGE)
    }

    private fun refreshButton() {
        val selectedProductCount = viewModel.getSelectedProductIds().size
        binding?.btnCreateCampaign?.isEnabled = selectedProductCount > Int.ZERO
    }
}