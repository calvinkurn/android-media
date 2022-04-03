package com.tokopedia.shopdiscount.manage.presentation

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
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class ProductListFragment : BaseSimpleListFragment<ProductListAdapter, Product>() {

    companion object {
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "status"
        private const val PAGE_SIZE = 10

        @JvmStatic
        fun newInstance(discountStatusId : Int) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putInt(BUNDLE_KEY_DISCOUNT_STATUS_ID, discountStatusId)
                }
            }
    }

    private val discountStatusId by lazy { arguments?.getInt(BUNDLE_KEY_DISCOUNT_STATUS_ID).orZero() }
    private var binding by autoClearedNullable<FragmentProductListBinding>()
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductListViewModel::class.java) }

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
        setupViews()
        observeProducts()

    }

    private fun setupViews() {
        binding?.run {

        }
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val b= it.data.size
                    renderList(it.data, it.data.size == getPerPage())
                }
                is Fail -> {
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private val onProductClick : (Product) -> Unit = { product ->

    }

    override fun createAdapter(): ProductListAdapter {
        return ProductListAdapter(onProductClick)
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
        if (adapter?.itemCount.isMoreThanZero()) {
           // loadingList?.show()
        }
        binding?.emptyState?.gone()
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        //loadingList?.gone()
        binding?.emptyState?.gone()
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
        binding?.emptyState?.visible()
    }

    override fun onGetListError(message: String) {
        displayError(message)
    }

    /*private fun createTabs(data: List<DiscountStatusMeta>) {
      val adapter =
      TabsUnifyMediator()
      data.forEach {
          val tab = TabsUnify()
          val tabName = "${it.name} (${it.productCount})"
          binding?.tabs?.addNewTab(tabName)
          binding.tabs.customTabMode = TabLayout.MODE_SCROLLABLE
      }
  }*/

    private fun displayError(errorMessage : String) {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { loadInitialData() }
            root showError errorMessage
        }

    }

}