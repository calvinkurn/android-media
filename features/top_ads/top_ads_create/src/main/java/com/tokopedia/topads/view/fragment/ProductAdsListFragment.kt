package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.ResponseEtalase
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.view.adapter.etalase.viewmodel.EtalaseViewModel
import com.tokopedia.topads.view.adapter.product.ProductListAdapter
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.view.model.ProductAdsListViewModel
import com.tokopedia.topads.view.sheet.InfoSheetProductList
import com.tokopedia.topads.view.sheet.ProductFilterSheetList
import com.tokopedia.topads.view.sheet.ProductSortSheetList
import kotlinx.android.synthetic.main.topads_create_fragment_product_list.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_TIPS_PRODUCT_IKLAN = "click-tips memilih produk"
private const val CLICK_PRODUCT_IKLAN = "click-pilih produk"

class ProductAdsListFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var sortProductList: ProductSortSheetList
    private lateinit var filterSheetProductList: ProductFilterSheetList
    private lateinit var productListAdapter: ProductListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductAdsListViewModel
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var isDataEnded = false

    companion object {

        private const val ALL = "all"
        private const val ROW = 50
        private var START = -ROW

        fun createInstance(): Fragment {
            val fragment = ProductAdsListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductAdsListViewModel::class.java)
        productListAdapter = ProductListAdapter(ProductListAdapterTypeFactoryImpl(this::onProductListSelected))
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_PRODUCT_IKLAN, getSelectedProduct().joinToString(","))
    }

    private fun getSelectedProduct(): MutableList<Int> {
        var list = mutableListOf<Int>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productID)
        }
        return list
    }

    private fun getSelectedProductAdId(): MutableList<Int> {
        var list = mutableListOf<Int>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.adID)
        }
        return list
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.product_list_step))
    }

    override fun getScreenName(): String {
        return ProductAdsListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.topads_create_fragment_product_list, container, false)
        recyclerView = view.findViewById(R.id.product_list)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.itemAnimator = null
        recyclerView.adapter = productListAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isDataEnded) {
                    START += ROW
                    fetchNextPage()
                }
            }
        }
    }

    private fun fetchNextPage() {
        productListAdapter.initLoading()
        clearRefreshLoading()
        viewModel.productList(getKeyword(),
                getSelectedEtalaseId(),
                getSelectedSortId(),
                getPromoted(),
                ROW,
                START, this::onSuccessGetProductList, this::onEmptyProduct, this::onError)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            sortProductList = ProductSortSheetList.newInstance(it)
            filterSheetProductList = ProductFilterSheetList.newInstance(it)
        }
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            InfoSheetProductList.newInstance(it.context).show()
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_PRODUCT_IKLAN, "")
        }
        btn_sort.setOnClickListener {
            sortProductList.show()
        }
        btn_filter.setOnClickListener {
            if (filterSheetProductList.getSelectedFilter().isBlank()) {
                fetchEtalase()
            }
            filterSheetProductList.show()
        }
        filterSheetProductList.onItemClick = { refreshProduct() }
        sortProductList.onItemClick = { refreshProduct() }
        not_promoted.setOnClickListener { refreshProduct() }
        promoted.setOnClickListener { refreshProduct() }
        searchInputView.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                refreshProduct()
            }

            override fun onSearchTextChanged(text: String?) {
            }
        })
        swipe_refresh_layout.setOnRefreshListener {
            refreshProduct()
        }

    }

    private fun refreshProduct() {
        START = 0
        swipe_refresh_layout.isRefreshing = true
        productListAdapter.initLoading()
        clearRefreshLoading()
        viewModel.productList(getKeyword(),
                getSelectedEtalaseId(),
                getSelectedSortId(),
                getPromoted(),
                ROW,
                START, this::onSuccessGetProductList, this::onEmptyProduct, this::onError)
    }

    private fun clearShimmerList() {
        productListAdapter.items.clear()
        productListAdapter.notifyDataSetChanged()
    }

    private fun fetchEtalase() {
        viewModel.etalaseList(this::onSuccessGetEtalase, this::onError)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        not_promoted.isChecked = true
        refreshProduct()
    }

    private fun getSelectedEtalaseId() = filterSheetProductList.getSelectedFilter()

    private fun getSelectedSortId() = sortProductList.getSelectedSortId()

    private fun getKeyword() = searchInputView.searchText

    private fun getPromoted(): String {
        return ALL
    }

    private fun onProductListSelected() {
        if (promotedGroup.checkedRadioButtonId == R.id.promoted) {
            stepperModel?.selectedPromo = getSelectedProduct()
            stepperModel?.adIdsPromo = getSelectedProductAdId()
            stepperModel?.selectedProductIds = (getSelectedProduct() + stepperModel?.selectedNonPromo!!).toMutableList()
            stepperModel?.adIds = ((getSelectedProductAdId() + stepperModel?.adIdsNonPromo!!).toMutableList())
        } else {
            stepperModel?.selectedNonPromo = getSelectedProduct()
            stepperModel?.adIdsNonPromo = getSelectedProductAdId()
            stepperModel?.selectedProductIds = (getSelectedProduct() + stepperModel?.selectedPromo!!).toMutableList()
            stepperModel?.adIds = ((getSelectedProductAdId() + stepperModel?.adIdsPromo!!).toMutableList())
        }
        var count = stepperModel?.selectedProductIds!!.size
        select_product_info.text = String.format(getString(R.string.format_selected_produk), count)
        btn_next.isEnabled = count > 0
    }

    private fun onEmptyProduct() {
        clearShimmerList()
        btn_next.isEnabled = false
        productListAdapter.items = mutableListOf(ProductEmptyViewModel())
        productListAdapter.notifyDataSetChanged()
        select_product_info.text = String.format(getString(R.string.format_selected_produk), 0)
    }

    private fun onError(t: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, t.localizedMessage) { refreshProduct() }
    }

    private fun onSuccessGetProductList(data: List<ResponseProductList.Result.TopadsGetListProduct.Data>, eof: Boolean) {
        if (START == 0)
            clearShimmerList()
        prepareForNextFetch(eof)
        btn_next.isEnabled = false
        data.forEach { result ->
            if (promotedGroup.checkedRadioButtonId == R.id.promoted) {
                if (result.adID > 0)
                    productListAdapter.items.add(ProductItemViewModel(result))

            } else {
                if (result.adID == 0)
                    productListAdapter.items.add(ProductItemViewModel(result))
            }
        }
        if (productListAdapter.items.isEmpty()) {
            productListAdapter.items.addAll(mutableListOf(ProductEmptyViewModel()))
        }
        if (productListAdapter.items[0] !is ProductEmptyViewModel)
            productListAdapter.setSelectedList(stepperModel?.selectedProductIds!!)
        val count = stepperModel?.selectedProductIds?.size ?: 0
        select_product_info.text = String.format(getString(R.string.format_selected_produk), count)
        btn_next.isEnabled = count > 0
        productListAdapter.notifyDataSetChanged()
    }

    private fun prepareForNextFetch(eof: Boolean) {
        isDataEnded = eof
        if (!isDataEnded)
            recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun clearRefreshLoading() {
        swipe_refresh_layout.isRefreshing = false
    }

    private fun onSuccessGetEtalase(data: List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) {
        var items = mutableListOf<EtalaseViewModel>()
        items.add(0, EtalaseItemViewModel(true, viewModel.addSemuaProduk()))
        data.forEachIndexed { index, result -> items.add(index + 1, EtalaseItemViewModel(false, result)) }
        filterSheetProductList.updateData(items)
    }
}
