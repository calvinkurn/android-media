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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.ALL
import com.tokopedia.topads.edit.utils.Constants.EXISTING_IDS
import com.tokopedia.topads.edit.utils.Constants.RESULT_IMAGE
import com.tokopedia.topads.edit.utils.Constants.RESULT_NAME
import com.tokopedia.topads.edit.utils.Constants.RESULT_PRICE
import com.tokopedia.topads.edit.utils.Constants.RESULT_PROUCT
import com.tokopedia.topads.edit.utils.Constants.ROW
import com.tokopedia.topads.edit.utils.Constants.START
import com.tokopedia.topads.edit.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.edit.view.adapter.etalase.viewmodel.EtalaseViewModel
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapter
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.edit.view.model.ProductAdsListViewModel
import com.tokopedia.topads.edit.view.sheet.ProductFilterSheetList
import com.tokopedia.topads.edit.view.sheet.ProductSortSheetList
import kotlinx.android.synthetic.main.topads_edit_fragment_product_list.*
import javax.inject.Inject

class ProductAdsListFragment : BaseDaggerFragment() {

    private lateinit var sortProductList: ProductSortSheetList
    private lateinit var filterSheetProductList: ProductFilterSheetList
    private lateinit var productListAdapter: ProductListAdapter
    private var selectedPrevPro: List<ResponseProductList.Result.TopadsGetListProduct.Data> = listOf()
    private var selectedPrevNonPro: List<ResponseProductList.Result.TopadsGetListProduct.Data> = listOf()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductAdsListViewModel

    companion object {

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

    private fun getSelectedProduct(): MutableList<Int> {
        var list = mutableListOf<Int>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productID)
        }
        return list
    }

    private fun getSelectedList(): ArrayList<Int> {
        var list = ArrayList<Int>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productID)
        }
        if (not_promoted.isChecked) {
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
        var list = ArrayList<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productPrice)
        }
        if (not_promoted.isChecked) {
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
        var list = ArrayList<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productName)
        }
        if (not_promoted.isChecked) {
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
        var list = ArrayList<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productImage)
        }
        if (not_promoted.isChecked) {
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
        return inflater.inflate(R.layout.topads_edit_fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            sortProductList = ProductSortSheetList.newInstance(it)
            filterSheetProductList = ProductFilterSheetList.newInstance(it)
        }
        btn_next.setOnClickListener {
            val returnIntent = getPassingIntent()
            activity?.setResult(Activity.RESULT_OK, returnIntent)
            activity?.finish()
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
        not_promoted.setOnClickListener {
            refreshProduct()
        }
        promoted.setOnClickListener {
            refreshProduct()
        }
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
        product_list.adapter = productListAdapter
        product_list.layoutManager = LinearLayoutManager(context)

    }

    private fun getPassingIntent(): Intent {
        val returnIntent = Intent()
        returnIntent.putStringArrayListExtra(RESULT_PRICE, getSelectedPrice())
        returnIntent.putIntegerArrayListExtra(RESULT_PROUCT, getSelectedList())
        returnIntent.putStringArrayListExtra(RESULT_NAME, getSelectedName())
        returnIntent.putStringArrayListExtra(RESULT_IMAGE, getSelectedImage())
        return returnIntent
    }

    private fun refreshProduct() {
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

    private fun clearList(){
        productListAdapter.items.clear()
        productListAdapter.notifyDataSetChanged()
    }

    private fun fetchEtalase() {
        viewModel.etalaseList(this::onSuccessGetEtalase)
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
        if (not_promoted.isChecked) {
            selectedPrevNonPro = productListAdapter.getSelectedItems()
        } else {
            selectedPrevPro = productListAdapter.getSelectedItems()
        }
        val count: Int = if (not_promoted.isChecked) {
            getSelectedProduct().size + selectedPrevPro.size
        } else {
            getSelectedProduct().size + selectedPrevNonPro.size
        }

        select_product_info.text = String.format(getString(R.string.format_selected_produk), count)
        btn_next.isEnabled = count > 0
    }

    private fun onEmptyProduct() {
        clearList()
        btn_next.isEnabled = false
        productListAdapter.items = mutableListOf(ProductEmptyViewModel())
        productListAdapter.notifyDataSetChanged()
    }

    private fun onError(t: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, t.localizedMessage) { refreshProduct() }
    }

    private fun onSuccessGetProductList(data: List<ResponseProductList.Result.TopadsGetListProduct.Data>) {
        clearList()
        btn_next.isEnabled = false
        data.forEach { result ->
            if (promotedGroup.checkedRadioButtonId == R.id.promoted) {
                if (result.adID > 0 && !ifExists(result.productID)) {
                    productListAdapter.items.add(ProductItemViewModel(result))
                }

            } else {
                if (result.adID == 0 && !ifExists(result.productID)) {
                    productListAdapter.items.add(ProductItemViewModel(result))
                }
            }
        }
        if (productListAdapter.items.size == 0) {
            productListAdapter.items.addAll(mutableListOf(ProductEmptyViewModel()))
        }
        productListAdapter.notifyDataSetChanged()
        if (not_promoted.isChecked)
            productListAdapter.setSelectedList(selectedPrevNonPro)
        else
            productListAdapter.setSelectedList(selectedPrevPro)
        val count = selectedPrevNonPro.size + selectedPrevPro.size

        select_product_info.text = String.format(getString(R.string.format_selected_produk), count)
        btn_next.isEnabled = count > 0

    }

    private fun ifExists(productID: Int): Boolean {
        val existingIds = arguments?.getIntegerArrayList(EXISTING_IDS)
        return existingIds?.find { id -> productID == id } != null

    }

    private fun clearRefreshLoading() {
        swipe_refresh_layout.isRefreshing = false
    }

    private fun onSuccessGetEtalase(data: List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) {
        val items = mutableListOf<EtalaseViewModel>()
        items.add(0, EtalaseItemViewModel(true, viewModel.addSemuaProduk()))
        data.forEachIndexed { index, result -> items.add(index + 1, EtalaseItemViewModel(false, result)) }
        filterSheetProductList.updateData(items)
    }

}
