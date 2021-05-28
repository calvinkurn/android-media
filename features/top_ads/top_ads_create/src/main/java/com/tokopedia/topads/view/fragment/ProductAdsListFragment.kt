package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseViewModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.ProductFilterSheetList
import com.tokopedia.topads.common.view.sheet.ProductSortSheetList
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.product.ProductListAdapter
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.view.model.ProductAdsListViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
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
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null
    var items = mutableListOf<EtalaseViewModel>()

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductAdsListViewModel::class.java)
        productListAdapter = ProductListAdapter(ProductListAdapterTypeFactoryImpl(this::onProductListSelected))
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_PRODUCT_IKLAN, getSelectedProduct().joinToString(","))
        if (stepperModel?.redirectionToSummary == false)
            stepperListener?.goToNextPage(stepperModel)
        else {
            stepperModel?.redirectionToSummary = false
            stepperListener?.getToFragment(UrlConstant.fragment_number_4, stepperModel)
        }
    }

    private fun getSelectedProductAdId(): MutableList<String> {
        val list = mutableListOf<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.adID)
        }
        return list
    }

    private fun getSelectedProduct(): MutableList<String> {
        val list = mutableListOf<String>()
        productListAdapter.getSelectedItems().forEach {
            list.add(it.productID)
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
        if (stepperModel?.redirectionToSummary == true) {
            btn_next?.text = getString(R.string.topads_common_save_butt)
        }
        context?.let {
            sortProductList = ProductSortSheetList.newInstance()
            filterSheetProductList = ProductFilterSheetList.newInstance()
        }
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(com.tokopedia.topads.common.R.string.tip_memilih_produk)

            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }

        tip_btn?.addItem(tooltipView)
        tip_btn.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(R.string.pilih_produk_yang_berada_dalam, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.pilih_produk_dengan_ulasan_terbanyak, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.pilih_produk_terpopuler, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.showHeader = true
            tipsListSheet?.showKnob = false
            tipsListSheet?.setTitle(getString(R.string.tip_memilih_produk))
            tipsListSheet?.show(childFragmentManager, "")
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_PRODUCT_IKLAN, "")
        }
        btn_sort.setOnClickListener {
            sortProductList.show(childFragmentManager, "")
        }
        btn_filter.setOnClickListener {
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
            not_promoted.chipType = ChipsUnify.TYPE_SELECTED
            promoted.chipType = ChipsUnify.TYPE_NORMAL
            refreshProduct()
        }
        promoted.setOnClickListener {
            not_promoted.chipType = ChipsUnify.TYPE_NORMAL
            promoted.chipType = ChipsUnify.TYPE_SELECTED
            refreshProduct()
        }
        Utils.setSearchListener(searchInputView, context, linearLayout10, ::refreshProduct)
        swipe_refresh_layout.setOnRefreshListener {
            refreshProduct()
        }
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
        viewModel.etalaseList(this::onSuccessGetEtalase, this::onError)
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
        if (promoted.chipType == ChipsUnify.TYPE_SELECTED) {
            stepperModel?.selectedPromo = getSelectedProduct()
            stepperModel?.adIdsPromo = getSelectedProductAdId()
            stepperModel?.selectedNonPromo?.let {
                stepperModel?.selectedProductIds = (getSelectedProduct() + it).toMutableList()
            }
            stepperModel?.adIdsNonPromo?.let {
                stepperModel?.adIds = ((getSelectedProductAdId() + it).toMutableList())
            }
        } else {
            stepperModel?.selectedNonPromo = getSelectedProduct()
            stepperModel?.adIdsNonPromo = getSelectedProductAdId()
            stepperModel?.selectedPromo?.let {
                stepperModel?.selectedProductIds = (getSelectedProduct() + it).toMutableList()
            }
            stepperModel?.adIdsPromo?.let {
                stepperModel?.adIds = ((getSelectedProductAdId() + it).toMutableList())
            }
        }
        val count = stepperModel?.selectedProductIds?.size ?: 0
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

    private fun onSuccessGetProductList(data: List<TopAdsProductModel>, eof: Boolean) {
        if (START == 0)
            clearShimmerList()
        prepareForNextFetch(eof)
        btn_next.isEnabled = false
        data.forEach { result ->
            if (promoted.chipType == ChipsUnify.TYPE_SELECTED) {
                if (result.adID.toFloat() > 0)
                    productListAdapter.items.add(ProductItemViewModel(result))

            } else {
                if (result.adID == "0")
                    productListAdapter.items.add(ProductItemViewModel(result))
            }
        }
        if (productListAdapter.items.isEmpty()) {
            productListAdapter.items.addAll(mutableListOf(ProductEmptyViewModel()))
        }
        if (productListAdapter.items[0] !is ProductEmptyViewModel) {
            stepperModel?.selectedProductIds?.let {
                productListAdapter.setSelectedList(it)
            }
        }
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
        items.clear()
        items.add(0, EtalaseItemViewModel(true, viewModel.addSemuaProduk()))
        data.forEachIndexed { index, result -> items.add(index + 1, EtalaseItemViewModel(false, result)) }
        filterSheetProductList.updateData(items)
    }
}
