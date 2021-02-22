package com.tokopedia.topads.edit.view.fragment.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseViewModel
import com.tokopedia.topads.common.view.sheet.ProductFilterSheetList
import com.tokopedia.topads.common.view.sheet.ProductSortSheetList
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.ALL
import com.tokopedia.topads.edit.utils.Constants.EXISTING_IDS
import com.tokopedia.topads.edit.utils.Constants.RESULT_IMAGE
import com.tokopedia.topads.edit.utils.Constants.RESULT_NAME
import com.tokopedia.topads.edit.utils.Constants.RESULT_PRICE
import com.tokopedia.topads.edit.utils.Constants.RESULT_PROUCT
import com.tokopedia.topads.edit.utils.Constants.ROW
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapter
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.edit.view.model.ProductAdsListViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.topads_edit_fragment_product_list.*
import javax.inject.Inject

private const val CLICK_BELUM_DIIKLANKAN = "click - belum diiklankan"
private const val CLICK_SUDAH_DIIKLANKAN = "click - sudah diiklankan"
private const val CLICK_SORT = "click - sort"
private const val CLICK_FILTER = "click - filter"
class ProductAdsListFragment : BaseDaggerFragment() {

    private lateinit var sortProductList: ProductSortSheetList
    private lateinit var filterSheetProductList: ProductFilterSheetList
    private lateinit var productListAdapter: ProductListAdapter
    private var selectedPrevPro: List<TopAdsProductModel> = listOf()
    private var selectedPrevNonPro: List<TopAdsProductModel> = listOf()
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var isDataEnded = false
    var items = mutableListOf<EtalaseViewModel>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductAdsListViewModel

    companion object {
        private var START = -ROW
        fun createInstance(extras: Bundle?): Fragment {
            val fragment = ProductAdsListFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductAdsListViewModel::class.java)
        productListAdapter = ProductListAdapter(ProductListAdapterTypeFactoryImpl(this::onProductListSelected))
    }

    private fun getSelectedProduct(): MutableList<String> {
        val list = mutableListOf<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productID)
        }
        return list
    }

    private fun getSelectedList(): ArrayList<String> {
        val list = ArrayList<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productID)
        }
        if (not_promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            selectedPrevPro.forEach {
                list.add(it.productID)
            }
        } else {
            selectedPrevNonPro.forEach {
                list.add(it.productID)
            }
        }
        return list
    }

    private fun getSelectedPrice(): ArrayList<String> {
        val list = ArrayList<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productPrice)
        }
        if (not_promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            selectedPrevPro.forEach {
                list.add(it.productPrice)
            }
        } else {
            selectedPrevNonPro.forEach {
                list.add(it.productPrice)
            }
        }

        return list
    }

    private fun getSelectedName(): ArrayList<String> {
        val list = ArrayList<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productName)
        }
        if (not_promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            selectedPrevPro.forEach {
                list.add(it.productName)
            }
        } else {
            selectedPrevNonPro.forEach {
                list.add(it.productName)
            }
        }
        return list
    }

    private fun getSelectedImage(): ArrayList<String> {
        val list = ArrayList<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productImage)
        }
        if (not_promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            selectedPrevPro.forEach {
                list.add(it.productImage)
            }
        } else {
            selectedPrevNonPro.forEach {
                list.add(it.productImage)
            }
        }
        return list
    }

    override fun getScreenName(): String {
        return ProductAdsListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.topads_edit_fragment_product_list, container, false)
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
                    fetchList()
                }
            }
        }
    }

    private fun fetchList() {
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
            sortProductList = ProductSortSheetList.newInstance()
            filterSheetProductList = ProductFilterSheetList.newInstance()
        }
        btn_next.setOnClickListener {
            val returnIntent = getPassingIntent()
            activity?.setResult(Activity.RESULT_OK, returnIntent)
            activity?.finish()
        }
        btn_sort.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_SORT, "")
            sortProductList.show(childFragmentManager, "")
        }
        btn_filter.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_FILTER, "")
            if (filterSheetProductList.getSelectedFilter().isBlank()) {
                fetchEtalase()
            } else {
                filterSheetProductList.updateData(items)
            }
            filterSheetProductList.show(childFragmentManager, "filterList")
        }
        filterSheetProductList.onItemClick = { refreshProduct() }
        sortProductList.onItemClick = { refreshProduct() }
        not_promoted.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_BELUM_DIIKLANKAN, "")
            not_promoted.chipType = ChipsUnify.TYPE_SELECTED
            promoted.chipType = ChipsUnify.TYPE_NORMAL
            refreshProduct()
        }
        promoted.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_SUDAH_DIIKLANKAN, "")
            promoted.chipType = ChipsUnify.TYPE_SELECTED
            not_promoted.chipType = ChipsUnify.TYPE_NORMAL
            refreshProduct()
        }
        Utils.setSearchListener(searchInputView, context, view, ::refreshProduct)
        swipe_refresh_layout.setOnRefreshListener {
            refreshProduct()
        }
    }

    private fun getPassingIntent(): Intent {
        val returnIntent = Intent()
        returnIntent.putStringArrayListExtra(RESULT_PRICE, getSelectedPrice())
        returnIntent.putStringArrayListExtra(RESULT_PROUCT, getSelectedList())
        returnIntent.putStringArrayListExtra(RESULT_NAME, getSelectedName())
        returnIntent.putStringArrayListExtra(RESULT_IMAGE, getSelectedImage())
        return returnIntent
    }

    private fun refreshProduct() {
        START = 0
        swipe_refresh_layout.isRefreshing = true
        productListAdapter.initLoading()
        clearRefreshLoading()
        fetchList()
    }

    private fun clearShimmerList() {
        productListAdapter.items.clear()
        productListAdapter.notifyDataSetChanged()
    }

    private fun fetchEtalase() {
        viewModel.etalaseList(this::onSuccessGetEtalase)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        not_promoted.chipType = ChipsUnify.TYPE_SELECTED
        refreshProduct()
    }

    private fun getSelectedEtalaseId() = filterSheetProductList.getSelectedFilter()

    private fun getSelectedSortId() = sortProductList.getSelectedSortId()

    private fun getKeyword() = searchInputView.searchBarTextField.text.toString()

    private fun getPromoted(): String {
        return ALL
    }

    private fun onProductListSelected() {
        if (not_promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            selectedPrevNonPro = productListAdapter.getSelectedItems()
        } else {
            selectedPrevPro = productListAdapter.getSelectedItems()
        }
        val count: Int = if (not_promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            getSelectedProduct().size + selectedPrevPro.size
        } else {
            getSelectedProduct().size + selectedPrevNonPro.size
        }

        select_product_info.text = String.format(getString(R.string.format_selected_produk), count)
        btn_next.isEnabled = count > 0
    }

    private fun onEmptyProduct() {
        clearShimmerList()
        btn_next.isEnabled = false
        productListAdapter.items = mutableListOf(ProductEmptyViewModel())
        productListAdapter.notifyDataSetChanged()
    }

    private fun onError(t: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, t.localizedMessage) { refreshProduct() }
    }

    private fun onSuccessGetProductList(data: List<TopAdsProductModel>, eof: Boolean) {
        if (START == 0)
            clearShimmerList()
        prepareForNextFetch(eof)
        btn_next.isEnabled = false
        data.forEach { result ->
            if (promoted.chipType == ChipsUnify.TYPE_SELECTED) {
                if (result.adID.toFloat() > 0 && !ifExists(result.productID)) {
                    productListAdapter.items.add(ProductItemViewModel(result))
                }

            } else {
                if (result.adID == "0" && !ifExists(result.productID)) {
                    productListAdapter.items.add(ProductItemViewModel(result))
                }
            }
        }
        if (productListAdapter.items.size == 0) {
            productListAdapter.items.addAll(mutableListOf(ProductEmptyViewModel()))
        }
        productListAdapter.notifyDataSetChanged()
        if (not_promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            productListAdapter.setSelectedList(selectedPrevNonPro)
        } else {
            productListAdapter.setSelectedList(selectedPrevPro)
        }
        val count = selectedPrevNonPro.size + selectedPrevPro.size

        select_product_info.text = String.format(getString(R.string.format_selected_produk), count)
        btn_next.isEnabled = count > 0
    }

    private fun prepareForNextFetch(eof: Boolean) {
        isDataEnded = eof
        if (!isDataEnded)
            recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun ifExists(productID: String): Boolean {
        val existingIds = arguments?.getStringArrayList(EXISTING_IDS)
        return existingIds?.find { id -> productID == id } != null

    }

    private fun clearRefreshLoading() {
        swipe_refresh_layout.isRefreshing = false
    }

    private fun onSuccessGetEtalase(data: List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) {
        items.clear()
        items.add(0, EtalaseItemViewModel(true, viewModel.addSemuaProduk()))
        data.forEachIndexed { index, result -> items.add(index + 1, EtalaseItemViewModel(false, result)) }
        filterSheetProductList.updateData(items)
    }

}
