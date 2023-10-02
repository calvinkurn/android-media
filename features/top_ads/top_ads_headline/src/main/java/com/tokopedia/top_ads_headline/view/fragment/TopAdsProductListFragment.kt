package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.Category
import com.tokopedia.top_ads_headline.data.TopAdsHeadlineTabModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.GROUP_NAME
import com.tokopedia.top_ads_headline.view.activity.IS_EDITED
import com.tokopedia.top_ads_headline.view.activity.SELECTED_PRODUCT_LIST
import com.tokopedia.top_ads_headline.view.adapter.CategoryListAdapter
import com.tokopedia.top_ads_headline.view.adapter.ProductListAdapter
import com.tokopedia.top_ads_headline.view.adapter.SINGLE_SELECTION
import com.tokopedia.top_ads_headline.view.viewmodel.DEFAULT_RECOMMENDATION_TAB_ID
import com.tokopedia.top_ads_headline.view.viewmodel.TopAdsProductListViewModel
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.data.util.SpaceItemDecoration
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.ProductSortSheetList
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

private const val ROW = 50
const val MAX_PRODUCT_SELECTION = 10
private const val MIN_CATEGORY_SHOWN = 2
private const val RECOMMENDATION_TAB_POSITION = 0
private const val ALL_PRODUCTS_TAB_POSITION = 1
private const val VIEW_PILIH_PRODUK = "view - pilih produk"
private const val CLICK_TIPS_PILIH_PRODUK = "click - tips on pilih produk page"
private const val CLICK_SIMPAN_PILIH_PRODUK = "click - simpan on pilih produk page"
private const val PRODUCT_NAME_HEADLINE = "android.topads_headline"

class TopAdsProductListFragment : BaseDaggerFragment(),
    ProductListAdapter.ProductListAdapterListener {

    private var searchInputView: SearchBarUnify? = null
    private var btnSort: UnifyImageButton? = null
    private var emptyResultText: Typography? = null
    private var quickFilterRecyclerView: RecyclerView? = null
    private var ticker: Ticker? = null
    private var selectProductCheckBox: CheckboxUnify? = null
    private var productListRecyclerView: RecyclerView? = null
    private var tooltipBtn: FloatingButtonUnify? = null
    private var btnNext: UnifyButton? = null
    private var selectProductInfo: Typography? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface
    private var start = 0
    private lateinit var viewModel: TopAdsProductListViewModel
    private var topAdsCategoryList = ArrayList<TopAdsHeadlineTabModel>()
    private lateinit var categoryListAdapter: CategoryListAdapter
    private lateinit var productsListAdapter: ProductListAdapter
    private var selectedTabModel: TopAdsHeadlineTabModel? = null
    private lateinit var sortProductList: ProductSortSheetList
    private var isProductSelectedListEdited = false
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private var isDataEnded = false
    private var linearLayoutManager = LinearLayoutManager(context)
    private var selectedTopAdsProductMap: HashMap<Category, ArrayList<TopAdsProductModel>> =
        HashMap()
    private var selectedProductList = mutableListOf<TopAdsProductModel>()

    companion object {
        fun newInstance(): TopAdsProductListFragment = TopAdsProductListFragment()
    }

    override fun getScreenName(): String {
        return TopAdsProductListFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(TopAdsProductListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topads_product_list, container, false)
        searchInputView = view.findViewById(R.id.searchInputView)
        btnSort = view.findViewById(R.id.btnSort)
        emptyResultText = view.findViewById(R.id.emptyResultText)
        quickFilterRecyclerView = view.findViewById(R.id.quickFilterRecyclerView)
        ticker = view.findViewById(R.id.ticker)
        selectProductCheckBox = view.findViewById(R.id.selectProductCheckBox)
        productListRecyclerView = view.findViewById(R.id.productListRecyclerView)
        tooltipBtn = view.findViewById(R.id.tooltipBtn)
        btnNext = view.findViewById(R.id.btnNext)
        selectProductInfo = view.findViewById(R.id.selectProductInfo)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        fetchTopAdsProducts()
    }

    private fun getSelectedSortId() = sortProductList.getSelectedSortId()

    private fun getKeyword() = searchInputView?.searchBarTextField?.text.toString()

    private fun getDataFromArguments() {
        arguments?.run {
            getSerializable(SELECTED_PRODUCT_LIST)?.let { it ->
                (it as? HashMap<Category, ArrayList<TopAdsProductModel>>?)?.let { map ->
                    selectedTopAdsProductMap = map
                }
            }
        }
    }

    private fun setUpUI() {
        topAdsCategoryList = viewModel.getTopAdsCategoryList()
        selectedTabModel = topAdsCategoryList.first()
        categoryListAdapter =
            CategoryListAdapter(topAdsCategoryList, chipFilterClick = this::onChipFilterClick)
        productsListAdapter = ProductListAdapter(ArrayList(), selectedTopAdsProductMap, this)
        quickFilterRecyclerView?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryListAdapter
            addItemDecoration(SpaceItemDecoration(LinearLayoutManager.HORIZONTAL))
        }
        productListRecyclerView?.run {
            layoutManager = linearLayoutManager
            adapter = productsListAdapter
            addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
            recyclerviewScrollListener = onRecyclerViewListener()
            addOnScrollListener(recyclerviewScrollListener)
        }
        sortProductList = ProductSortSheetList.newInstance()
        sortProductList.onItemClick = { refreshProduct() }
        setSelectProductText()
        setUpSelectProductCheckBox()
        setUpSearchBarUnify()
        setUpToolTip()
        btnSort?.setOnClickListener {
            sortProductList.show(childFragmentManager, "")
        }
        btnNext?.setOnClickListener {
            setResultAndFinish()
        }
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isDataEnded) {
                    start += ROW
                    fetchTopAdsProducts()
                }
            }
        }
    }

    private fun setUpSearchBarUnify() {
        view?.let {
            searchInputView?.let { it1 ->
                Utils.setSearchListener(it1,
                    activity,
                    it,
                    ::refreshProduct)
            }
        }
        searchInputView?.searchBarTextField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isBlank()) {
                    hideEmptyView()
                    refreshProduct()
                }
            }
        })
    }

    private fun hideEmptyView() {
        emptyResultText?.hide()
        quickFilterRecyclerView?.show()
        selectProductCheckBox?.show()
        productListRecyclerView?.show()
    }

    private fun showEmptyView() {
        if (getKeyword().isNotEmpty()) {
            emptyResultText?.text =
                String.format(getString(R.string.topads_headline_search_result_with_keyword,
                    getKeyword()))
            emptyResultText?.show()
            quickFilterRecyclerView?.hide()
        }
        ticker?.hide()
        selectProductCheckBox?.hide()
        productListRecyclerView?.hide()
    }

    private fun setUpSelectProductCheckBox() {
        selectProductCheckBox?.setOnClickListener {
            val isChecked = selectProductCheckBox?.isChecked == true
            if (isChecked) {
                productsListAdapter.list.let {
                    var itemsToAdd = 0
                    it.forEach { product ->
                        val category =
                            Category(product.departmentID.toString(), product.departmentName)
                        if (selectedTopAdsProductMap[category] == null || selectedTopAdsProductMap[category]?.contains(
                                product) == false
                        ) {
                            itemsToAdd++
                        }
                    }
                    var alreadyAdded = 0
                    selectedTopAdsProductMap.values.forEach { selectedValues ->
                        alreadyAdded += selectedValues.size
                    }
                    if (itemsToAdd + alreadyAdded <= MAX_PRODUCT_SELECTION) {
                        it.forEach { product ->
                            val category =
                                Category(product.departmentID.toString(), product.departmentName)
                            if (selectedTopAdsProductMap[category] == null) {
                                val value = ArrayList<TopAdsProductModel>()
                                value.add(product)
                                selectedTopAdsProductMap[category] = value
                            } else if (selectedTopAdsProductMap[category]?.contains(product) == false) {
                                selectedTopAdsProductMap[category]?.add(product)
                            }
                        }
                    } else {
                        onProductOverSelect()
                        selectProductCheckBox?.isChecked = false
                    }
                }
            } else {
                productsListAdapter.list.forEach {
                    val category = Category(it.departmentID, it.departmentName)
                    selectedTopAdsProductMap[category]?.remove(it)
                }
            }
            setSelectProductText()
            productsListAdapter.notifyDataSetChanged()
            isProductSelectedListEdited = true
            if (btnNext?.isEnabled == false) {
                setTickerAndBtn()
            }
        }
    }

    private fun setTickerAndBtn() {
        val descriptionText = getDescriptionTextIfAny()
        if (descriptionText.isNotEmpty()) {
            ticker?.show()
            ticker?.setTextDescription(descriptionText)
            productsListAdapter.notifyDataSetChanged()
        } else {
            ticker?.hide()
        }
        btnNext?.isEnabled = ticker?.visibility == View.GONE
    }

    private fun getDescriptionTextIfAny(): String {
        var descriptionText = ""
        var categoryCount = -1
        selectedTopAdsProductMap.forEach { (category, arrayList) ->
            if (arrayList.size == SINGLE_SELECTION) {
                arrayList.first().isSingleSelect = true
                ++categoryCount
                when {
                    categoryCount == 0 -> {
                        descriptionText += category.name
                    }
                    categoryCount <= MIN_CATEGORY_SHOWN -> {
                        descriptionText += ", " + category.name
                    }
                }
            }
        }
        if (categoryCount > MIN_CATEGORY_SHOWN) {
            val others = categoryCount - MIN_CATEGORY_SHOWN
            descriptionText += " " + getString(R.string.topads_headline_category_count_error,
                others)
        }
        return descriptionText
    }

    private fun setSelectProductText() {
        var totalCount = 0
        selectedTopAdsProductMap.values.forEach {
            totalCount += it.size
        }
        selectProductInfo?.text =
            String.format(getString(com.tokopedia.topads.common.R.string.format_selected_produk), totalCount)
    }

    private fun setUpToolTip() {
        val tooltipView =
            layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null)
                .apply {
                    val tvToolTipText = this.findViewById<Typography>(com.tokopedia.topads.common.R.id.tooltip_text)
                    tvToolTipText?.text = getString(R.string.topads_headline_tooltip_text)

                    val imgTooltipIcon = this.findViewById<ImageUnify>(com.tokopedia.topads.common.R.id.tooltip_icon)
                    imgTooltipIcon?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
                }
        tooltipBtn?.addItem(tooltipView)
        tooltipBtn?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormClickEvent(
                CLICK_TIPS_PILIH_PRODUK,
                "{${userSession.shopId} - {${arguments?.getString(GROUP_NAME)}}",
                userSession.userId)
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiHeaderModel(R.string.topads_headline_tips_choosing_product))
                add(TipsUiRowModel(R.string.topads_headline_tips_choose_product_with_most_reviews,
                    com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_choose_product_with_most_popularity,
                    com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_choose_product_with_same_category,
                    com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_name_and_photo_correct,
                    com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList) }
            tipsListSheet?.show(childFragmentManager, "")
        }
    }

    private fun refreshProduct() {
        start = 0
        productsListAdapter.refreshList()
        fetchTopAdsProducts()
    }

    private fun setProductSelectCbText() {
        if (selectedTabModel?.id == DEFAULT_RECOMMENDATION_TAB_ID) {
            selectProductCheckBox?.text =
                getString(R.string.topads_headline_select_all_recommendation)
        } else {
            val count = selectedTabModel?.count?.takeIf {
                it <= MAX_PRODUCT_SELECTION
            } ?: MAX_PRODUCT_SELECTION
            selectProductCheckBox?.text =
                String.format(getString(R.string.topads_headline_select_first_n_recommendation),
                    count)
        }
        selectProductCheckBox?.isChecked = false
    }

    private fun setSelectProductCbCheck(data: ArrayList<TopAdsProductModel>) {
        if (data.isEmpty()) {
            selectProductCheckBox?.hide()
        } else {
            selectProductCheckBox?.show()
            checkIfDeterminate()
        }
    }

    private fun checkIfDeterminate() {
        var totalCount = 0
        productsListAdapter.list.forEach {
            val category = Category(it.departmentID, it.departmentName)
            totalCount += selectedTopAdsProductMap[category]?.size ?: 0
        }
        selectProductCheckBox?.isChecked = totalCount != 0
        if (totalCount == productsListAdapter.list.size) {
            selectProductCheckBox?.setIndeterminate(false)
        } else if (totalCount != 0 && totalCount < productsListAdapter.list.size) {
            selectProductCheckBox?.setIndeterminate(true)
        }
    }

    private fun fetchTopAdsProducts() {
        viewModel.getTopAdsProductList(userSession.shopId,
            getKeyword(),
            "",
            getSelectedSortId(),
            "",
            ROW,
            start,
            selectedTabModel?.id,
            PRODUCT_NAME_HEADLINE,
            this::onSuccessGetProductList,
            this::onError)
    }

    private fun onChipFilterClick(topAdsCategoryDataModel: TopAdsHeadlineTabModel) {
        selectedTabModel = topAdsCategoryDataModel
        productsListAdapter.refreshList()
        fetchTopAdsProducts()
    }

    private fun onSuccessGetProductList(data: List<TopAdsProductModel>, eof: Boolean) {
        if (data.isNotEmpty()) {
            selectedProductList.clear()
            selectedProductList.addAll(data)
            hideEmptyView()
            productsListAdapter.setProductList(data as ArrayList<TopAdsProductModel>)
            setTabCount(data.size)
            prepareForNextFetch(eof)
            showCbIfRecommendation(data)
            if (btnNext?.isEnabled == false) {
                setTickerAndBtn()
            }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormEcommerceViewEvent(
                VIEW_PILIH_PRODUK,
                "{${userSession.shopId}} - {${arguments?.getString(GROUP_NAME)}}",
                data,
                userSession.userId)
        } else {
            showEmptyView()
        }
    }

    private fun showCbIfRecommendation(data: ArrayList<TopAdsProductModel>) {
        if (selectedTabModel?.id == DEFAULT_RECOMMENDATION_TAB_ID) {
            selectProductCheckBox?.show()
            setProductSelectCbText()
            setSelectProductCbCheck(data)
        } else {
            selectProductCheckBox?.hide()
        }
    }

    private fun setTabCount(size: Int) {
        selectedTabModel?.count = size
        if (selectedTabModel?.id == DEFAULT_RECOMMENDATION_TAB_ID) {
            categoryListAdapter.setItem(size, RECOMMENDATION_TAB_POSITION)
        } else {
            categoryListAdapter.setItem(size, ALL_PRODUCTS_TAB_POSITION)
        }
    }

    private fun prepareForNextFetch(eof: Boolean) {
        isDataEnded = eof
        if (!isDataEnded)
            recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun onError(t: Throwable) {
        view?.let {
            Toaster.build(it,
                t.localizedMessage
                    ?: "",
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                clickListener = View.OnClickListener {
                    refreshProduct()
                }).show()
        }
    }

    private fun setResultAndFinish() {
        setTickerAndBtn()
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormEcommerceCLickEvent(
            CLICK_SIMPAN_PILIH_PRODUK,
            "{${userSession.shopId} - {${arguments?.getString(GROUP_NAME)}}",
            selectedProductList,
            userSession.userId)
        if (ticker?.isVisible == true) {
            return
        }
        val intent = Intent()
        intent.putExtra(IS_EDITED, isProductSelectedListEdited)
        intent.putExtra(SELECTED_PRODUCT_LIST, selectedTopAdsProductMap)
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onProductOverSelect() {
        view?.let {
            Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_60)
            Toaster.build(it,
                getString(R.string.topads_headline_over_product_selection),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR).show()
        }
    }

    override fun onProductClick(product: TopAdsProductModel) {
        val category = Category(product.departmentID, product.departmentName)
        isProductSelectedListEdited = true
        if (selectedTabModel?.id == DEFAULT_RECOMMENDATION_TAB_ID) {
            checkIfDeterminate()
        }
        setSelectProductText()
        if (btnNext?.isEnabled == false) {
            changeBackGround(category, product)
            setTickerAndBtn()
        }
    }

    private fun changeBackGround(category: Category, product: TopAdsProductModel) {
        selectedTopAdsProductMap[category]?.let { list ->
            if (list.isNotEmpty()) {
                list.first().isSingleSelect = list.size == SINGLE_SELECTION
                productsListAdapter.notifyItemChanged(list.first().positionInRv)
            } else {
                product.isSingleSelect = false
                productsListAdapter.notifyItemChanged(product.positionInRv)
            }
        }
    }
}
