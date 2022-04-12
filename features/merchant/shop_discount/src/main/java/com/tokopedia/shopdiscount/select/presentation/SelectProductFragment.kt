package com.tokopedia.shopdiscount.select.presentation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.FragmentSelectProductBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.presentation.view.activity.ShopDiscountManageDiscountActivity
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.search.presentation.SearchProductFragment
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
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
import java.util.*
import javax.inject.Inject

class SelectProductFragment : BaseSimpleListFragment<SelectProductAdapter, ReservableProduct>() {

    companion object {
        private const val ANIMATION_DURATION_IN_MILLIS : Long = 500
        private const val SCROLL_WIDGET_MARGIN = 24F
        private const val BACK_TO_ORIGINAL_POSITION : Float = 0F
        private const val PAGE_SIZE = 10
        private const val FIRST_PAGE = 1
        private const val DECELERATOR_FACTOR: Float = 2.0F
        private const val MAX_PRODUCT_SELECTION = 5
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/search_not_found.png"

        @JvmStatic
        fun newInstance(): SelectProductFragment {
            return SelectProductFragment()
        }

    }

    private var binding by autoClearedNullable<FragmentSelectProductBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore

    @Inject
    lateinit var userSession : UserSessionInterface

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SelectProductViewModel::class.java) }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestId = userSession.shopId + Date().time
        viewModel.setRequestId(requestId)
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

                }

                override fun onDismiss() {
                    preferenceDataStore.markTickerMultiSelectAsDismissed()
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
                val requestId = viewModel.getRequestId()
                val selectedProducts = viewModel.getSelectedProduct()
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
                    ShopDiscountManageDiscountActivity.start(
                        requireActivity(),
                        viewModel.getRequestId(),
                        2,
                        "RESERVE"
                    )
                }
                is Fail -> {
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun handleProducts(data: List<ReservableProduct>) {
        if (data.size == ZERO) {
            binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
            binding?.emptyState?.setTitle(getString(R.string.sd_search_result_not_found_title))
            binding?.emptyState?.setDescription(getString(R.string.sd_search_result_not_found_description))

            binding?.recyclerView?.gone()
            binding?.emptyState?.visible()

        } else {
            binding?.recyclerView?.visible()
            binding?.emptyState?.gone()


            renderList(data, data.size == getPerPage())
        }
    }

    private val onProductClicked: (ReservableProduct) -> Unit = { product ->
        val isDisabled = product.disableClick || product.disabled
        guard(isDisabled, product.disabledReason) {
            showProductDetailBottomSheet(product)
        }
    }

    private val onProductSelectionChange: (ReservableProduct, Boolean) -> Unit = { selectedProduct, isSelected ->
        if (isSelected) {
            viewModel.addProductToSelection(selectedProduct)
        } else {
            viewModel.removeProductFromSelection(selectedProduct)
        }

        val updatedProduct = selectedProduct.copy(isCheckboxTicked = isSelected)
        adapter?.update(selectedProduct, updatedProduct)

        val items = adapter?.getItems() ?: emptyList()
        val selectedProductCount = viewModel.getSelectedProduct().size

        val shouldDisableSelection = selectedProductCount >= MAX_PRODUCT_SELECTION
        viewModel.setDisableProductSelection(shouldDisableSelection)

        binding?.btnManage?.text =
            String.format(getString(R.string.sd_manage_with_counter),  selectedProductCount)
        binding?.btnManage?.isEnabled = selectedProductCount > 0

        if (shouldDisableSelection) {
            showTickerWithAnimation()
            disableProductSelection(items)
        } else {
            hideTickerWithAnimation()
            enableProductSelection(items)
        }
    }

    private fun disableProductSelection(products : List<ReservableProduct>) {
        val toBeDisabledProducts = viewModel.disableProducts(products)
        adapter?.refresh(toBeDisabledProducts)
    }

    private fun enableProductSelection(products : List<ReservableProduct>) {
        val toBeEnabledProducts = viewModel.enableProduct(products)
        adapter?.refresh(toBeEnabledProducts)
    }

    private fun guard(isDisabled: Boolean, disableReason : String, block : () -> Unit) {
        if (isDisabled) {
            val reason = disableReason.ifEmpty {
                getString(R.string.sd_select_product_max_count_reached)
            }

            Toaster.build(
                binding?.recyclerView ?: return,
                reason,
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                getString(R.string.sd_ok)
            ).show()
        } else {
            block()
        }
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
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        binding?.globalError?.gone()
        binding?.emptyState?.gone()
        val keyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()
        val requestId = viewModel.getRequestId()
        viewModel.getProducts(
            requestId,
            page,
            keyword,
            viewModel.shouldDisableProductSelection()
        )
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        binding?.emptyState?.gone()
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        binding?.emptyState?.gone()
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
        binding?.emptyState?.visible()
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

    private fun showProductDetailBottomSheet(product: ReservableProduct) {
        val bottomSheet = ShopDiscountProductDetailBottomSheet.newInstance(
            product.id,
            product.name,
            DiscountStatus.ALL
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun clearSearchBar() {
        val selectedProductCount = viewModel.getSelectedProduct().size
        val shouldDisableSelection = selectedProductCount >= MAX_PRODUCT_SELECTION
        viewModel.setDisableProductSelection(shouldDisableSelection)

        clearAllData()
        onShowLoading()
        binding?.shimmer?.content?.visible()
        val requestId = viewModel.getRequestId()
        viewModel.getProducts(
            requestId,
            FIRST_PAGE,
            EMPTY_STRING,
            viewModel.shouldDisableProductSelection(),
        )
    }

    private fun showTickerWithAnimation() {
        binding?.run {
            ticker
                .animate()
                .translationY(BACK_TO_ORIGINAL_POSITION)
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setInterpolator(AccelerateInterpolator(DECELERATOR_FACTOR))
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        ticker.visible()
                    }
                }).start()
        }
    }

    private fun hideTickerWithAnimation() {
        binding?.run {
            ticker.animate()
                .translationY(ticker.height.toFloat() + SCROLL_WIDGET_MARGIN)
                .setInterpolator(DecelerateInterpolator(DECELERATOR_FACTOR))
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        ticker.gone()
                    }
                })
                .start()
        }
    }
}