package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.ResponseEtalase
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.di.CreateAdsComponent
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
import kotlinx.android.synthetic.main.topads_create_fragment_product_list.tip_btn
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */
class ProductAdsListFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var sortProductList: ProductSortSheetList
    private lateinit var filteSheetProductList: ProductFilterSheetList
    private lateinit var productListAdapter: ProductListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductAdsListViewModel

    companion object {

        private val NOT_PROMOTED = "0"
        private val PROMOTED = "4"
        private val ALL = "1"
        private val ROW = 10
        private val START = 0

        fun createInstance(): Fragment {

            val fragment = ProductAdsListFragment()
            val args = Bundle()
            fragment.setArguments(args)
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
    }

    override fun populateView(stepperModel: CreateManualAdsStepperModel) {
    }

    override fun getScreenName(): String {
        return CreateGroupAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            sortProductList = ProductSortSheetList.newInstance(it)
            filteSheetProductList = ProductFilterSheetList.newInstance(it)
        }
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            InfoSheetProductList.newInstance(it.context).show()
        }
        btn_sort.setOnClickListener {
            sortProductList.show()
        }
        btn_filter.setOnClickListener {
            if(filteSheetProductList.getSelectedFilter().isBlank()) {
                fetchEtalase()
            }
            filteSheetProductList.show()
        }
        filteSheetProductList.onItemClick = { refreshProduct() }
        sortProductList.onItemClick = { refreshProduct() }
        not_promoted.setOnClickListener { refreshProduct() }
        promoted.setOnClickListener { refreshProduct() }
        searchInputView.setListener(object : SearchInputView.Listener{
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

    private fun refreshProduct(){
        swipe_refresh_layout.isRefreshing = true
        productListAdapter.items.clear()
        viewModel.productList(getKeyword(),
                getSelectedEtalaseId(),
                getSelectedSortId(),
                getPromoted(),
                ROW,
                START, this::onSuccessGetProductList, this::onEmptyProduct, this::onError)
    }

    private fun fetchEtalase() {
        viewModel.etalaseList(this::onSuccessGetEtalase, this::onError)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        refreshProduct()
    }

    private fun getSelectedEtalaseId() = filteSheetProductList.getSelectedFilter()

    private fun getSelectedSortId() = sortProductList.getSelectedSortId()

    private fun getKeyword() = searchInputView.searchText

    private fun getPromoted(): String {
        return when(promotedGroup.checkedRadioButtonId){
            R.id.not_promoted -> NOT_PROMOTED
            R.id.promoted -> PROMOTED
            else -> ALL
        }
    }

    private fun onProductListSelected() {
        select_product_info.setText(String.format(getString(R.string.format_selected_produk), productListAdapter.getSelectedItems().size))
    }

    private fun onEmptyProduct() {
        clearRefreshLoading()
        btn_next.isEnabled = false
        productListAdapter.items = mutableListOf(ProductEmptyViewModel())
        productListAdapter.notifyDataSetChanged()
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

    private fun onSuccessGetProductList(data: List<ResponseProductList.Data>) {
        clearRefreshLoading()
        btn_next.isEnabled = true
        data.forEach { result -> productListAdapter.items.add(ProductItemViewModel(result)) }
        productListAdapter.notifyDataSetChanged()
    }

    private fun clearRefreshLoading(){
        swipe_refresh_layout.isRefreshing = false
    }

    fun onSuccessGetEtalase(data: List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) {
        var items = mutableListOf<EtalaseViewModel>()
        data.forEachIndexed { index, result -> items.add(index, EtalaseItemViewModel(index == 0, result)) }
        filteSheetProductList.updateData(items)
    }

}
