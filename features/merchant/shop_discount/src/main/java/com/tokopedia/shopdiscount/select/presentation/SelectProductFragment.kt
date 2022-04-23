package com.tokopedia.shopdiscount.select.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
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
import com.tokopedia.shopdiscount.manage_discount.presentation.view.activity.ShopDiscountManageDiscountActivity
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.search.presentation.SearchProductFragment
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.shopdiscount.select.domain.entity.ShopBenefit
import com.tokopedia.shopdiscount.utils.animator.ViewAnimator
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.UrlConstant
import com.tokopedia.shopdiscount.utils.constant.ZERO
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.shopdiscount.utils.preference.SharedPreferenceDataStore
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject

class SelectProductFragment : BaseSimpleListFragment<SelectProductAdapter, ReservableProduct>() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val FIRST_PAGE = 1
        private const val MAX_PRODUCT_SELECTION = 5
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/search_not_found.png"
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "status_id"
        private const val PAGE_REDIRECTION_DELAY_IN_MILLIS : Long = 1000

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
    lateinit var userSession : UserSessionInterface

    @Inject
    lateinit var viewAnimator: ViewAnimator

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SelectProductViewModel::class.java) }

    private val discountStatusId by lazy {
        arguments?.getInt(BUNDLE_KEY_DISCOUNT_STATUS_ID).orZero()
    }

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
        observeProducts()
        observeReserveProducts()
        observeShopBenefits()
        viewModel.getSellerBenefits()
    }

    private fun setupView() {
        setupButton()
        setupToolbar()
        setupSearchBar()
        setupTicker()
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
                    loadInitialData()
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
            binding?.btnManage?.text = String.format(getString(R.string.sd_manage_with_counter), ZERO)
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
                        redirectToUpdateDiscountPage()
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
                    handleRemainingQuota(it.data)
                }
                is Fail -> {
                    displayError()
                    binding?.recyclerView?.gone()
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun handleRemainingQuota(shopBenefit: ShopBenefit) {
        val remainingQuota = if (shopBenefit.isUseVps) {
            val vpsPackage = findVpsPackage(shopBenefit.benefits)
            vpsPackage?.remainingQuota.orZero()
        } else {
            MAX_PRODUCT_SELECTION
        }

        viewModel.setRemainingQuota(remainingQuota)

        if (remainingQuota == ZERO) {
            showNoMoreRemainingQuota()
        }
    }

    private fun handleProducts(data: List<ReservableProduct>) {
        val currentItemCount = adapter?.getItems()?.size.orZero()
        val isScrolling = currentItemCount > ZERO

        if (data.isEmpty() && !isScrolling) {
            binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
            binding?.emptyState?.setTitle(getString(R.string.sd_search_result_not_found_title))
            binding?.emptyState?.setDescription(getString(R.string.sd_search_result_not_found_description))

            binding?.recyclerView?.gone()
            binding?.emptyState?.visible()

        } else {
            val dataSize = data.size
            val perPage = getPerPage()
            val hasNextPage = (dataSize == perPage) || (dataSize > perPage)
            renderList(data, hasNextPage)
            binding?.recyclerView?.visible()
            binding?.emptyState?.gone()
        }
    }

    private val onProductClicked: (ReservableProduct, Int) -> Unit = { product, position ->
        val isDisabled = product.disableClick || product.disabled
        val hasVariant = product.countVariant > ZERO
        handleProductClick(isDisabled, product.disabledReason, hasVariant, product, position)
    }

    private val onProductSelectionChange: (ReservableProduct, Boolean) -> Unit = { selectedProduct, isSelected ->
        if (isSelected) {
            viewModel.addProductToSelection(selectedProduct)
        } else {
            viewModel.removeProductFromSelection(selectedProduct)
        }

        val selectedProductCount = viewModel.getSelectedProducts().size
        handleTickerAppearance(selectedProductCount)

        val updatedProduct = selectedProduct.copy(isCheckboxTicked = isSelected)
        productAdapter.update(selectedProduct, updatedProduct)


        val remainingQuota = viewModel.getRemainingQuota()
        val shouldDisableSelection = selectedProductCount >= remainingQuota
        viewModel.setDisableProductSelection(shouldDisableSelection)

        val items = productAdapter.getItems()
        if (shouldDisableSelection) {
            disableProductSelection(items)
        } else {
            enableProductSelection(items)
        }

        val remainingSelection = viewModel.getRemainingQuota() - viewModel.getSelectedProducts().size
        if (remainingSelection < ZERO) {
            showNoMoreRemainingQuota()
        }

        binding?.btnManage?.text =
            String.format(getString(R.string.sd_manage_with_counter),  selectedProductCount)
        binding?.btnManage?.isEnabled = selectedProductCount > ZERO
    }

    private fun disableProductSelection(products : List<ReservableProduct>) {
        val toBeDisabledProducts = viewModel.disableProducts(products)
        productAdapter.refresh(toBeDisabledProducts)
    }

    private fun enableProductSelection(products : List<ReservableProduct>) {
        val toBeEnabledProducts = viewModel.enableProduct(products)
        productAdapter.refresh(toBeEnabledProducts)
    }

    private fun handleProductClick(
        isDisabled: Boolean,
        disableReason: String,
        hasVariant: Boolean,
        product: ReservableProduct,
        position: Int
    ) {
        val selectedProductCount = viewModel.getSelectedProducts().size
        val reachedMaxAllowedSelection = selectedProductCount == MAX_PRODUCT_SELECTION
        val remainingAllowedSelection = viewModel.getRemainingQuota() - selectedProductCount
        when {
            remainingAllowedSelection <= ZERO -> showNoMoreRemainingQuota()
            reachedMaxAllowedSelection -> showDisableReason(getString(R.string.sd_select_product_max_count_reached))
            !isDisabled && hasVariant -> showProductDetailBottomSheet(product, position)
            isDisabled -> showDisableReason(disableReason)
            else -> {}
        }
    }

    private fun showDisableReason(disableReason: String) {
        if (disableReason.isNotEmpty()) {
            Toaster.build(
                binding?.recyclerView ?: return,
                disableReason,
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                getString(R.string.sd_ok)
            ).show()
        } else {
            Toaster.build(
                binding?.recyclerView ?: return,
                getString(R.string.sd_already_discount),
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                getString(R.string.sd_ok)
            ).show()
        }
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

    override fun createAdapter(): SelectProductAdapter {
        return productAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        val recyclerView = binding?.recyclerView
        recyclerView?.addItemDecoration(ProductListItemDecoration(requireActivity()))
        return recyclerView
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<ReservableProduct>) {
        productAdapter.addData(list)
    }

    override fun loadData(page: Int) {
        binding?.globalError?.gone()
        binding?.emptyState?.gone()
        val keyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()

        val requestId = generateRequestId()
        viewModel.setRequestId(requestId)

        viewModel.getReservableProducts(
            requestId,
            page,
            keyword,
            viewModel.shouldDisableProductSelection(),
        )
    }

    override fun clearAdapterData() {
        productAdapter.clearData()
    }

    override fun onShowLoading() {
        binding?.emptyState?.gone()
        productAdapter.showLoading()
    }

    override fun onHideLoading() {
        binding?.emptyState?.gone()
        productAdapter.hideLoading()
    }

    override fun onDataEmpty() {
        binding?.emptyState?.visible()
        productAdapter.hideLoading()
    }

    override fun onGetListError(message: String) {
        displayError()
    }

    private fun displayError() {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { loadInitialData() }
        }

    }

    private fun clearSearchBar() {
        val selectedProductCount = viewModel.getSelectedProducts().size
        val remainingQuota = viewModel.getRemainingQuota()
        val shouldDisableSelection = selectedProductCount >= remainingQuota
        viewModel.setDisableProductSelection(shouldDisableSelection)

        clearAllData()
        onShowLoading()
        binding?.shimmer?.content?.visible()
        viewModel.getReservableProducts(
            viewModel.getRequestId(),
            FIRST_PAGE,
            EMPTY_STRING,
            viewModel.shouldDisableProductSelection(),
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

    private fun redirectToUpdateDiscountPage() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(PAGE_REDIRECTION_DELAY_IN_MILLIS)
            binding?.btnManage?.isLoading = false
            ShopDiscountManageDiscountActivity.start(
                requireActivity(),
                viewModel.getRequestId(),
                discountStatusId,
                ShopDiscountManageDiscountMode.CREATE
            )
        }
    }

    private fun findVpsPackage(benefits: List<ShopBenefit.Benefit>): ShopBenefit.Benefit? {
        return benefits.firstOrNull { it.packageId.toInt() > ZERO }
    }

    override fun onSwipeRefreshPulled() {

    }

    private fun handleTickerAppearance(selectedProductCount: Int) {
        if (selectedProductCount >= MAX_PRODUCT_SELECTION) {
            viewAnimator.showWithAnimation(binding?.ticker)
        } else {
            viewAnimator.hideWithAnimation(binding?.ticker)
        }
    }

    private fun showProductDetailBottomSheet(product: ReservableProduct, position: Int) {
        val bottomSheet = ShopDiscountProductDetailBottomSheet.newInstance(
            product.id,
            product.name,
            discountStatusId,
            position
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }
}