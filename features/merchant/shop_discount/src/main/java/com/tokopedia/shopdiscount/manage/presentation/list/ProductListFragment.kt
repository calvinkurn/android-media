package com.tokopedia.shopdiscount.manage.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shopdiscount.databinding.FragmentProductListBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.utils.extension.applyUnifyBackgroundColor
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.cancel.CancelDiscountDialog
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.more_menu.MoreMenuBottomSheet
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.extension.showToaster

class ProductListFragment : BaseSimpleListFragment<ProductListAdapter, Product>() {

    companion object {
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "status"
        private const val PAGE_SIZE = 10
        private const val EMPTY_STATE_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

        @JvmStatic
        fun newInstance(discountStatusId : Int, onDiscountRemoved : () -> Unit = {}) : ProductListFragment {
            val fragment = ProductListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_DISCOUNT_STATUS_ID, discountStatusId)
            }
            fragment.onDiscountRemoved = onDiscountRemoved
            return fragment
        }

    }

    private val discountStatusId by lazy { arguments?.getInt(BUNDLE_KEY_DISCOUNT_STATUS_ID).orZero() }
    private var binding by autoClearedNullable<FragmentProductListBinding>()
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductListViewModel::class.java) }
    private var onDiscountRemoved : () -> Unit = {}

    override fun getScreenName() : String = ProductListFragment::class.java.canonicalName.orEmpty()
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
        applyUnifyBackgroundColor()
        setupViews()
        observeProducts()
        observeDeleteDiscount()
    }

    private fun setupViews() {
        binding?.run {

        }
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    handleProducts(it.data)
                    binding?.tpgTotalProduct?.text = String.format(getString(R.string.sd_total_product), it.data.totalProduct)
                }
                is Fail -> {
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun observeDeleteDiscount() {
        viewModel.deleteDiscount.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val deleteSuccess = it.data
                    if (deleteSuccess) {
                        binding?.recyclerView showToaster getString(R.string.sd_discount_deleted)
                        onDiscountRemoved()
                    }
                }
                is Fail -> {
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun handleProducts(data: ProductData) {
        if (data.totalProduct == 0) {
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

    private val onProductClicked : (Product) -> Unit = { product ->

    }

    private val onUpdateDiscountClicked : (Product) -> Unit = { product ->

    }


    private val onOverflowMenuClicked: (Product) -> Unit = { product ->
        displayMoreMenuBottomSheet(product)
    }


    override fun createAdapter(): ProductListAdapter {
        return ProductListAdapter(onProductClicked, onUpdateDiscountClicked, onOverflowMenuClicked)
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

    private fun displayError(errorMessage : String) {
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

}