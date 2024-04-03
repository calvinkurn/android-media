package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_2
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.etalase.uimodel.EtalaseItemUiModel
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseUiModel
import com.tokopedia.topads.common.view.sheet.ProductFilterSheetList
import com.tokopedia.topads.common.view.sheet.ProductSortSheetList
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.product.ProductListAdapter
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemUiModel
import com.tokopedia.topads.view.model.ProductAdsListViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject
import com.tokopedia.topads.common.R as topadscommonR

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_PRODUCT_IKLAN = "click-pilih produk"
private const val PRODUCT_PAGE_NAME = "android.topads_create"

class ProductAdsListFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private var rootLayout: ConstraintLayout? = null
    private var searchInputView: SearchBarUnify? = null
    private var btnSort: IconUnify? = null
    private var btnFilter: IconUnify? = null
    private var notPromoted: ChipsUnify? = null
    private var promoted: ChipsUnify? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var btnNext: UnifyButton? = null
    private var selectProductInfo: Typography? = null

    private lateinit var sortProductList: ProductSortSheetList
    private lateinit var filterSheetProductList: ProductFilterSheetList
    private lateinit var productListAdapter: ProductListAdapter
    private var items = mutableListOf<EtalaseUiModel>()
    private val productData = mutableMapOf(
        CONST_1 to mutableListOf<ProductItemUiModel>(),
        CONST_2 to mutableListOf<ProductItemUiModel>()
    )

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
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductAdsListViewModel::class.java)
        productListAdapter =
            ProductListAdapter(ProductListAdapterTypeFactoryImpl(this::onProductListSelected))
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_PRODUCT_IKLAN,
            getSelectedProduct().joinToString(","))
        if (stepperModel?.redirectionToSummary == false)
            stepperListener?.goToNextPage(stepperModel)
        else {
            stepperModel?.redirectionToSummary = false
            stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_4, stepperModel)
        }

        (activity as? StepperActivity)?.removeIcon()
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
            (activity as StepperActivity).apply {
                updateToolbarTitle(getString(R.string.product_list_step))
                addIcon()
            }
    }

    override fun getScreenName(): String {
        return ProductAdsListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.topads_create_fragment_product_list, container, false)
        recyclerView = view.findViewById(R.id.product_list)
        rootLayout = view.findViewById(R.id.linearLayout10)
        searchInputView = view.findViewById(R.id.searchInputView)
        btnSort = view.findViewById(R.id.btn_sort)
        btnFilter = view.findViewById(R.id.btn_filter)
        notPromoted = view.findViewById(R.id.not_promoted)
        promoted = view.findViewById(R.id.promoted)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        btnNext = view.findViewById(R.id.btn_next)
        selectProductInfo = view.findViewById(R.id.select_product_info)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.itemAnimator = null
        recyclerView.adapter = productListAdapter
        recyclerView.layoutManager = layoutManager
        context?.let {
            recyclerView.addItemDecoration(object : DividerItemDecoration(it, VERTICAL) {
                override fun shouldDrawOnLastItem(): Boolean {
                    return false
                }
            })
        }
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
            getSelectedEtalaseId(), getSelectedSortId(), getPromoted(), ROW, START,
            PRODUCT_PAGE_NAME, this::onSuccessGetProductList, this::onEmptyProduct, this::onError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (stepperModel?.redirectionToSummary == true) {
            btnNext?.text = getString(com.tokopedia.topads.common.R.string.topads_common_save_butt)
        }
        context?.let {
            sortProductList = ProductSortSheetList.newInstance()
            filterSheetProductList = ProductFilterSheetList.newInstance()
        }
        btnNext?.setOnClickListener {
            gotoNextPage()
        }

        btnSort?.setOnClickListener {
            sortProductList.show(childFragmentManager, "")
        }
        btnFilter?.setOnClickListener {
            if (filterSheetProductList.getSelectedFilter().isBlank()) {
                fetchEtalase()
            } else {
                filterSheetProductList.updateData(items)
            }
            filterSheetProductList.show(childFragmentManager, "filterList")
        }
        filterSheetProductList.onItemClick = { refreshProduct() }
        sortProductList.onItemClick = { refreshProduct() }
        notPromoted?.setOnClickListener {
            notPromoted?.chipType = ChipsUnify.TYPE_SELECTED
            promoted?.chipType = ChipsUnify.TYPE_NORMAL
            setProducts(2)
        }
        promoted?.setOnClickListener {
            notPromoted?.chipType = ChipsUnify.TYPE_NORMAL
            promoted?.chipType = ChipsUnify.TYPE_SELECTED
            setProducts(1)
        }

        if (searchInputView != null && rootLayout != null)
            Utils.setSearchListener(searchInputView!!, context, rootLayout!!, ::refreshProduct)

        swipeRefreshLayout?.setOnRefreshListener {
            refreshProduct()
        }
    }

    private fun setProducts(i: Int) {
        productListAdapter.items.clear()
        productData[i]?.let { productListAdapter.items.addAll(it) }
        if (productListAdapter.items.isEmpty()) {
            productListAdapter.items.addAll(mutableListOf(ProductEmptyUiModel()))
        }
        if (productListAdapter.items[0] !is ProductEmptyUiModel) {
            stepperModel?.selectedProductIds?.let {
                productListAdapter.setSelectedList(it)
            }
        }
        productListAdapter.notifyDataSetChanged()
    }

    private fun refreshProduct() {
        START = 0
        swipeRefreshLayout?.isRefreshing = true
        productListAdapter.initLoading()
        clearRefreshLoading()
        fetchList()
    }

    private fun clearShimmerList() {
        productListAdapter.items.clear()
        productListAdapter.notifyDataSetChanged()
        productData[1]?.clear()
        productData[2]?.clear()
    }

    private fun fetchEtalase() {
        viewModel.etalaseList(this::onSuccessGetEtalase, this::onError)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notPromoted?.chipType = ChipsUnify.TYPE_SELECTED
        refreshProduct()
    }

    private fun getSelectedEtalaseId() = filterSheetProductList.getSelectedFilter()

    private fun getSelectedSortId() = sortProductList.getSelectedSortId()

    private fun getKeyword() = searchInputView?.searchBarTextField?.text.toString()

    private fun getPromoted(): String {
        return ALL
    }

    private fun onProductListSelected() {
        if (promoted?.chipType == ChipsUnify.TYPE_SELECTED) {
            stepperModel?.selectedPromo = getSelectedProduct()
            stepperModel?.adIdsPromo = getSelectedProductAdId()
            stepperModel?.productListPromo = productListAdapter.getSelectedItems().toMutableList()
            stepperModel?.selectedNonPromo?.let {
                stepperModel?.selectedProductIds = (getSelectedProduct() + it).toMutableList()
            }
            stepperModel?.productListNonPromo?.let {
                stepperModel?.productList = (productListAdapter.getSelectedItems() + it).toMutableList()
            }
            stepperModel?.adIdsNonPromo?.let {
                stepperModel?.adIds = ((getSelectedProductAdId() + it).toMutableList())
            }
        } else {
            stepperModel?.selectedNonPromo = getSelectedProduct()
            stepperModel?.adIdsNonPromo = getSelectedProductAdId()
            stepperModel?.productListNonPromo = productListAdapter.getSelectedItems().toMutableList()
            stepperModel?.selectedPromo?.let {
                stepperModel?.selectedProductIds = (getSelectedProduct() + it).toMutableList()
            }
            stepperModel?.productListPromo?.let {
                stepperModel?.productList = (productListAdapter.getSelectedItems() + it).toMutableList()
            }
            stepperModel?.adIdsPromo?.let {
                stepperModel?.adIds = ((getSelectedProductAdId() + it).toMutableList())
            }
        }
        val count = stepperModel?.selectedProductIds?.size ?: 0
        selectProductInfo?.text = MethodChecker.fromHtml(String.format(
            getString(topadscommonR.string.format_selected_produk),
            count
        ))
        btnNext?.isEnabled = count > 0
    }

    private fun onEmptyProduct() {
        clearShimmerList()
        btnNext?.isEnabled = false
        productListAdapter.items = mutableListOf(ProductEmptyUiModel())
        productListAdapter.notifyDataSetChanged()
        selectProductInfo?.text = MethodChecker.fromHtml(String.format(
            getString(topadscommonR.string.format_selected_produk),
            Int.ZERO
        ))
        promoted?.chip_text?.text = String.format(getString(R.string.topads_ads_chip_title_one), productData[1]?.size)

        notPromoted?.chip_text?.text = String.format(getString(R.string.topads_ads_chip_title_two), productData[2]?.size)
    }

    private fun onError(t: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity,
            t.localizedMessage) { refreshProduct() }
    }

    private fun onSuccessGetProductList(data: List<TopAdsProductModel>, eof: Boolean) {
        if (START == 0)
            clearShimmerList()
        prepareForNextFetch(eof)
        btnNext?.isEnabled = false
        data.forEach { result ->
            if (result.adID.toFloat() > Int.ZERO) {
                productData[1]?.add(ProductItemUiModel(result))
            } else if (result.adID == Int.ZERO.toString()) {
                productData[2]?.add(ProductItemUiModel(result))
            }
        }
        if (promoted?.chipType == ChipsUnify.TYPE_SELECTED) {
            setProducts(CONST_1)
        } else {
            setProducts(CONST_2)
        }
        val count = stepperModel?.selectedProductIds?.size ?: 0
        selectProductInfo?.text = MethodChecker.fromHtml(String.format(
            getString(topadscommonR.string.format_selected_produk),
            count
        ))
        btnNext?.isEnabled = count > 0
        productListAdapter.notifyDataSetChanged()
        promoted?.chip_text?.text = String.format(getString(R.string.topads_ads_chip_title_one), productData[CONST_1]?.size)
        notPromoted?.chip_text?.text = String.format(getString(R.string.topads_ads_chip_title_two), productData[CONST_2]?.size)
    }

    private fun prepareForNextFetch(eof: Boolean) {
        isDataEnded = eof
        if (!isDataEnded)
            recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun clearRefreshLoading() {
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun onSuccessGetEtalase(data: List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) {
        items.clear()
        items.add(0, EtalaseItemUiModel(true, viewModel.addSemuaProduk()))
        data.forEachIndexed { index, result ->
            items.add(index + 1, EtalaseItemUiModel(false, result))
        }
        filterSheetProductList.updateData(items)
    }
}
