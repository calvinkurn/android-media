package com.tokopedia.shopdiscount.select.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.FragmentSelectProductBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.presentation.view.activity.ShopDiscountManageActivity
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.search.presentation.SearchProductFragment
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.shopdiscount.select.domain.entity.ShopBenefit
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.UrlConstant
import com.tokopedia.shopdiscount.utils.constant.ZERO
import com.tokopedia.shopdiscount.utils.extension.setFragmentToUnifyBgColor
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.preference.SharedPreferenceDataStore
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.URLEncoder
import java.util.Date
import javax.inject.Inject

class SelectProductFragment : BaseDaggerFragment() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val FIRST_PAGE = 1
        private const val MAX_PRODUCT_SELECTION = 5
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/search_not_found.png"
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "status_id"
        private const val DISABLED_REASON_PRODUCT_ALREADY_HAS_DISCOUNT = "Produk sudah memiliki diskon"

        @JvmStatic
        fun newInstance(discountStatusId: Int): SelectProductFragment {
            val fragment = SelectProductFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_DISCOUNT_STATUS_ID, discountStatusId)
            }
            return fragment
        }
    }

    private var binding by autoClearedNullable<FragmentSelectProductBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SelectProductViewModel::class.java) }
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var endlessLayoutManagerListener: EndlessLayoutManagerListener? = null

    private val productAdapter by lazy {
        SelectProductAdapter(
            onProductClicked,
            onProductSelectionChange
        )
    }

    override fun getScreenName(): String = SearchProductFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setFragmentToUnifyBgColor()
        observeProducts()
        observeReserveProducts()
        observeShopBenefits()
        viewModel.getSellerBenefits()
    }

    private fun setupView() {
        setupRecyclerView()
        setupButton()
        setupToolbar()
        setupSearchBar()
        setupTicker()
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.adapter = productAdapter
        enableLoadMore()
    }

    private fun setupTicker() {
        binding?.run {
            ticker.setHtmlDescription(getString(R.string.sd_ticker_search_product_announcement_wording))
            ticker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    redirectToDesktopPage()
                }

                override fun onDismiss() {
                }
            })
        }
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadFirstPage()
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
            binding?.btnManage?.text =
                String.format(getString(R.string.sd_manage_with_counter), ZERO)
            btnManage.setOnClickListener {
                binding?.btnManage?.isLoading = true
                binding?.btnManage?.loadingText = getString(R.string.sd_wait)

                val requestId = generateRequestId()
                viewModel.setRequestId(requestId)

                val selectedProducts = viewModel.getSelectedProducts()
                viewModel.reserveProduct(requestId, selectedProducts)
            }
        }
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.groupContent?.visible()
                    binding?.shimmer?.content?.gone()
                    handleProducts(it.data)
                }
                is Fail -> {
                    binding?.cardView?.gone()
                    binding?.groupContent?.gone()
                    binding?.shimmer?.content?.gone()
                    displayError()
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun observeReserveProducts() {
        viewModel.reserveProduct.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val isReservationSuccess = it.data
                    if (isReservationSuccess) {
                        redirectToApplyDiscountPage()
                    } else {
                        binding?.btnManage?.isLoading = false
                        binding?.root showError getString(R.string.sd_error_reserve_product)
                    }
                }
                is Fail -> {
                    binding?.btnManage?.isLoading = false
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun observeShopBenefits() {
        viewModel.benefit.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.shimmer?.content?.gone()
                    handleRemainingQuota(it.data)
                }
                is Fail -> {
                    displayError()
                    binding?.shimmer?.content?.gone()
                    binding?.recyclerView?.gone()
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun handleRemainingQuota(shopBenefit: ShopBenefit) {
        val unlimited = Int.MAX_VALUE

        val remainingQuota = if (shopBenefit.isUseVps) {
            val vpsPackage = findVpsPackage(shopBenefit.benefits)
            vpsPackage?.remainingQuota.orZero()
        } else {
            unlimited
        }

        viewModel.setRemainingQuota(remainingQuota)

        if (remainingQuota == ZERO) {
            showNoMoreRemainingQuota()
        }

        loadFirstPage()
    }

    private fun handleProducts(data: List<ReservableProduct>) {
        val currentItemCount = productAdapter.getItems().size.orZero()
        val isScrolling = currentItemCount > ZERO

        if (data.isEmpty() && !isScrolling) {
            binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
            binding?.emptyState?.setTitle(getString(R.string.sd_search_result_not_found_title))
            binding?.emptyState?.setDescription(getString(R.string.sd_search_result_not_found_description))

            binding?.recyclerView?.gone()
            binding?.emptyState?.visible()
        } else {
            renderList(data, data.size >= PAGE_SIZE)
            binding?.recyclerView?.visible()
            binding?.emptyState?.gone()
        }
    }

    private val onProductClicked: (ReservableProduct, Int) -> Unit = { product, _ ->
        val isDisabled = product.disableClick || product.disabled
        handleProductClick(isDisabled, product.disabledReason)
    }

    private val onProductSelectionChange: (ReservableProduct, Boolean) -> Unit =
        { selectedProduct, isSelected ->

            if (isSelected) {
                val currentSelectedProductCount = viewModel.getSelectedProducts().size
                handleAddProductToSelection(currentSelectedProductCount, selectedProduct)
            } else {
                handleRemoveProductFromSelection(selectedProduct)
            }

            val selectedProductCount = viewModel.getSelectedProducts().size
            handleTickerAppearance(selectedProductCount)
        }

    private fun handleAddProductToSelection(
        currentSelectedProductCount: Int,
        selectedProduct: ReservableProduct
    ) {
        val nextCounter = currentSelectedProductCount + 1
        if (nextCounter > MAX_PRODUCT_SELECTION) {
            untickProduct(selectedProduct)
            showDisableReason(getString(R.string.sd_select_product_max_count_reached))
        } else {
            val remainingSelection = viewModel.getRemainingQuota() - viewModel.getSelectedProducts().size

            val isPartiallyDiscountProduct = selectedProduct.countVariant > ZERO
            if (remainingSelection > ZERO || isPartiallyDiscountProduct) {
                tickProduct(selectedProduct)
                viewModel.addProductToSelection(selectedProduct)
            } else {
                untickProduct(selectedProduct)
                showNoMoreRemainingQuota()
            }
        }

        refreshButtonTitle()
    }

    private fun handleRemoveProductFromSelection(selectedProduct: ReservableProduct) {
        viewModel.removeProductFromSelection(selectedProduct)
        untickProduct(selectedProduct)

        refreshButtonTitle()
    }

    private fun handleProductClick(
        isDisabled: Boolean,
        disableReason: String
    ) {
        val selectedProductCount = viewModel.getSelectedProducts().size
        val reachedMaxAllowedSelection = selectedProductCount >= MAX_PRODUCT_SELECTION
        when {
            reachedMaxAllowedSelection -> showDisableReason(getString(R.string.sd_select_product_max_count_reached))
            isDisabled -> showDisableReason(disableReason)
            else -> {}
        }
    }

    private fun showDisableReason(disableReason: String) {
        val wording: String = when {
            disableReason == DISABLED_REASON_PRODUCT_ALREADY_HAS_DISCOUNT -> getString(R.string.sd_product_already_on_discount)
            disableReason.isNotEmpty() -> disableReason
            else -> getString(R.string.sd_product_already_on_discount)
        }

        Toaster.build(
            binding?.recyclerView ?: return,
            wording,
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_NORMAL,
            getString(R.string.sd_ok)
        ).show()
    }

    private fun showNoMoreRemainingQuota() {
        Toaster.build(
            binding?.cardView ?: return,
            getString(R.string.sd_no_remaining_quota),
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_ERROR,
            getString(R.string.sd_ok)
        ).show()
    }

    private fun addElementToAdapter(list: List<ReservableProduct>) {
        productAdapter.addData(list)
    }

    fun loadData(page: Int) {
        binding?.globalError?.gone()
        binding?.emptyState?.gone()
        val keyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()

        val requestId = generateRequestId()
        viewModel.setRequestId(requestId)

        viewModel.getReservableProducts(
            requestId,
            page,
            keyword,
            viewModel.shouldDisableProductSelection()
        )
    }

    private fun displayError() {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener {
                binding?.globalError?.gone()
                binding?.shimmer?.content?.visible()
                viewModel.getSellerBenefits()
            }
        }
    }

    private fun clearSearchBar() {
        clearPreviousData()
        showLoading()
        viewModel.getReservableProducts(
            viewModel.getRequestId(),
            FIRST_PAGE,
            EMPTY_STRING,
            viewModel.shouldDisableProductSelection()
        )
    }

    private fun redirectToDesktopPage() {
        if (!isAdded) return
        val url = UrlConstant.SELLER_HOSTNAME + UrlConstant.SHOP_DISCOUNT
        val encodedUrl = URLEncoder.encode(url, "utf-8")
        val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
        RouteManager.route(requireActivity(), route)
    }

    private fun generateRequestId(): String {
        return userSession.shopId + Date().time
    }

    private fun redirectToApplyDiscountPage() {
        binding?.btnManage?.isLoading = false
        ShopDiscountManageActivity.start(
            requireActivity(),
            viewModel.getRequestId(),
            ZERO,
            ShopDiscountManageDiscountMode.CREATE
        )
    }

    private fun findVpsPackage(benefits: List<ShopBenefit.Benefit>): ShopBenefit.Benefit? {
        return benefits.firstOrNull { it.packageId.toInt() > ZERO }
    }

    private fun handleTickerAppearance(selectedProductCount: Int) {
        if (selectedProductCount >= MAX_PRODUCT_SELECTION) {
            binding?.ticker?.visible()
        } else {
            binding?.ticker?.gone()
        }
    }

    private fun loadFirstPage() {
        binding?.shimmer?.content?.visible()
        clearPreviousData()
        showLoading()
        loadData(FIRST_PAGE)
    }

    private fun enableLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            endlessRecyclerViewScrollListener?.setEndlessLayoutManagerListener(
                endlessLayoutManagerListener
            )
        }
        endlessRecyclerViewScrollListener?.apply {
            binding?.recyclerView?.addOnScrollListener(this)
        }
    }

    private fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(binding?.recyclerView?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    private fun updateScrollListenerState(hasNextPage: Boolean) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun showLoading() {
        productAdapter.showLoading()
    }

    private fun hideLoading() {
        productAdapter.hideLoading()
    }

    private fun renderList(list: List<ReservableProduct>, hasNextPage: Boolean) {
        hideLoading()
        addElementToAdapter(list)

        updateScrollListenerState(hasNextPage)

        if (productAdapter.itemCount.orZero() < PAGE_SIZE && hasNextPage && endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener?.loadMoreNextPage()
        }
    }

    private fun clearPreviousData() {
        productAdapter.clearData()
        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun tickProduct(selectedProduct: ReservableProduct) {
        val updatedProduct = selectedProduct.copy(isCheckboxTicked = true)
        productAdapter.update(selectedProduct, updatedProduct)
    }

    private fun untickProduct(selectedProduct: ReservableProduct) {
        val updatedProduct = selectedProduct.copy(isCheckboxTicked = false)
        productAdapter.update(selectedProduct, updatedProduct)
    }

    private fun refreshButtonTitle() {
        val selectedProductCount = viewModel.getSelectedProducts().size
        binding?.btnManage?.text =
            String.format(getString(R.string.sd_manage_with_counter), selectedProductCount)
        binding?.btnManage?.isEnabled = selectedProductCount > ZERO
    }
}
