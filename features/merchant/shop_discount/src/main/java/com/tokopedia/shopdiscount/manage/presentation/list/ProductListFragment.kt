package com.tokopedia.shopdiscount.manage.presentation.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.cancel.CancelDiscountDialog
import com.tokopedia.shopdiscount.databinding.FragmentProductListBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.presentation.container.RecyclerViewScrollListener
import com.tokopedia.shopdiscount.more_menu.MoreMenuBottomSheet
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showToaster
import com.tokopedia.shopdiscount.utils.extension.smoothSnapToPosition
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ProductListFragment : BaseSimpleListFragment<ProductListAdapter, Product>() {

    companion object {
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "status"
        private const val PAGE_SIZE = 10
        private const val DECELERATOR_FACTOR: Float = 2.0F
        private const val ANIMATION_DURATION_IN_MILLIS : Long = 500
        private const val SCROLL_WIDGET_MARGIN = 24F

        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

        @JvmStatic
        fun newInstance(
            discountStatusId: Int,
            onDiscountRemoved: (Int, Int) -> Unit = { _, _ -> }
        ): ProductListFragment {
            val fragment = ProductListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_DISCOUNT_STATUS_ID, discountStatusId)
            }
            fragment.onDiscountRemoved = onDiscountRemoved
            return fragment
        }

    }

    private val discountStatusId by lazy {
        arguments?.getInt(BUNDLE_KEY_DISCOUNT_STATUS_ID).orZero()
    }
    private var binding by autoClearedNullable<FragmentProductListBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductListViewModel::class.java) }
    private var onDiscountRemoved: (Int, Int) -> Unit = { _, _ -> }
    private val productAdapter by lazy {
        ProductListAdapter(
            onProductClicked,
            onUpdateDiscountClicked,
            onOverflowMenuClicked
        )
    }
    private var onScrollDown: () -> Unit = {}
    private var onScrollUp: () -> Unit = {}

    override fun getScreenName(): String = ProductListFragment::class.java.canonicalName.orEmpty()
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
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScrollListener()
        setupView()
        observeProducts()
        observeDeleteDiscount()
    }

    private fun setupScrollListener() {
        binding?.recyclerView?.addOnScrollListener(
            RecyclerViewScrollListener(
                onScrollDown = {
                    showScrollButtonWithAnimation()
                    onScrollDown()
                },
                onScrollUp = {
                    hideScrollButtonWithAnimation()
                    onScrollUp()
                }
            )
        )
    }

    private fun setupView() {
        binding?.run {
            imgScrollUp.setOnClickListener { recyclerView.smoothSnapToPosition(0) }
        }
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    handleProducts(it.data)
                    viewModel.setTotalProduct(it.data.totalProduct)
                    binding?.tpgTotalProduct?.text =
                        String.format(getString(R.string.sd_total_product), it.data.totalProduct)
                    binding?.swipeRefresh?.isRefreshing = false
                }
                is Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun observeDeleteDiscount() {
        viewModel.deleteDiscount.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    handleDeleteDiscountResult(it.data)
                }
                is Fail -> {
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun handleProducts(data: ProductData) {
        if (data.totalProduct == Int.ZERO) {
            val title = if (discountStatusId == DiscountStatus.PAUSED) {
                getString(R.string.sd_no_paused_discount_title)
            } else {
                getString(R.string.sd_no_paused_discount_description)
            }

            val description = if (discountStatusId == DiscountStatus.PAUSED) {
                getString(R.string.sd_no_discount_title)
            } else {
                getString(R.string.sd_no_discount_description)
            }

            binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
            binding?.emptyState?.setTitle(title)
            binding?.emptyState?.setDescription(description)

            binding?.recyclerView?.gone()
            binding?.tpgTotalProduct?.gone()
            binding?.emptyState?.visible()
        } else {
            renderList(data.products, data.products.size == getPerPage())
        }
    }

    private fun handleDeleteDiscountResult(isDeletionSuccess: Boolean) {
        if (isDeletionSuccess) {
            binding?.recyclerView showToaster getString(R.string.sd_discount_deleted)
            onDiscountRemoved(discountStatusId, viewModel.getTotalProduct())
            productAdapter.delete(viewModel.getSelectedProduct() ?: return)
            val updatedTotalProduct = viewModel.getTotalProduct() - 1
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_total_product), updatedTotalProduct)
        } else {
            binding?.root showError getString(R.string.sd_error_delete_discount)
        }
    }

    private val onProductClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
    }

    private val onUpdateDiscountClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
    }


    private val onOverflowMenuClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
        displayMoreMenuBottomSheet(product)
    }


    override fun createAdapter(): ProductListAdapter {
        return productAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.recyclerView
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return binding?.swipeRefresh
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<Product>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        binding?.globalError?.gone()
        binding?.emptyState?.gone()
        viewModel.getSlashPriceProducts(page, discountStatusId)
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
        displayError(message)
    }

    private fun displayError(errorMessage: String) {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { loadInitialData() }
            root showError errorMessage
        }

    }

    private fun displayMoreMenuBottomSheet(product: Product) {
        val bottomSheet = MoreMenuBottomSheet()
        bottomSheet.setOnDeleteMenuClicked { displayDeleteConfirmationDialog(product) }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayDeleteConfirmationDialog(product: Product) {
        val dialog = CancelDiscountDialog(requireContext())
        dialog.setOnDeleteConfirmed {
            viewModel.deleteDiscount(discountStatusId, product.id)
        }
        dialog.show()
    }

    fun setOnScrollDownListener(onScrollDown: () -> Unit = {}) {
        this.onScrollDown = onScrollDown
    }

    fun setOnScrollUpListener(onScrollUp: () -> Unit = {}) {
        this.onScrollUp = onScrollUp
    }


    private fun showScrollButtonWithAnimation() {
        binding?.run {
            imgScrollUp.animate()
                .translationY(imgScrollUp.height.toFloat() + SCROLL_WIDGET_MARGIN)
                .setInterpolator(DecelerateInterpolator(DECELERATOR_FACTOR))
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        imgScrollUp.gone()
                    }
                })
                .start()
        }

    }

    private fun hideScrollButtonWithAnimation() {
        binding?.run {
            imgScrollUp
                .animate()
                .translationY(0f)
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setInterpolator(AccelerateInterpolator(DECELERATOR_FACTOR))
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        imgScrollUp.visible()
                    }
                }).start()
        }

    }

}