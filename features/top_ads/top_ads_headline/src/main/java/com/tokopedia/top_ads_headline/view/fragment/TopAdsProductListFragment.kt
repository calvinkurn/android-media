package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.TopAdsCategoryDataModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.IS_EDITED
import com.tokopedia.top_ads_headline.view.activity.SELECTED_PRODUCT_LIST
import com.tokopedia.top_ads_headline.view.adapter.CategoryListAdapter
import com.tokopedia.top_ads_headline.view.adapter.ProductListAdapter
import com.tokopedia.top_ads_headline.view.viewmodel.DEFAULT_RECOMMENDATION_CATEGORY_ID
import com.tokopedia.top_ads_headline.view.viewmodel.TopAdsProductListViewModel
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.data.util.SpaceItemDecoration
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.ProductSortSheetList
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_topads_product_list.*
import javax.inject.Inject

private const val ROW = 50
private const val MAX_PRODUCT_SELECTION = 10

class TopAdsProductListFragment : BaseDaggerFragment(), ProductListAdapter.ProductListAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface
    private var start = 0
    private lateinit var viewModel: TopAdsProductListViewModel
    private var topAdsCategoryList = ArrayList<TopAdsCategoryDataModel>()
    private lateinit var categoryListAdapter: CategoryListAdapter
    private lateinit var productsListAdapter: ProductListAdapter
    private var selectedCategoryDataModel: TopAdsCategoryDataModel? = null
    private var selectedTopAdsProduct = HashSet<ResponseProductList.Result.TopadsGetListProduct.Data>()
    private lateinit var sortProductList: ProductSortSheetList
    private var isProductSelectedListEdited = false
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private var isDataEnded = false
    private var linearLayoutManager = LinearLayoutManager(context)

    companion object {
        fun newInstance(): TopAdsProductListFragment = TopAdsProductListFragment()
    }

    override fun getScreenName(): String {
        return TopAdsProductListFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
        viewModel = ViewModelProvider(this, viewModelFactory).get(TopAdsProductListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topads_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpObservers()
        viewModel.getEtalaseList(userSession.shopId ?: "")
    }

    private fun getSelectedSortId() = sortProductList.getSelectedSortId()

    private fun getKeyword() = searchInputView.searchBarTextField.text.toString()

    private fun getSelectedProducts() = selectedTopAdsProduct.toCollection(ArrayList())

    private fun getDataFromArguments() {
        arguments?.run {
            getParcelableArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>(SELECTED_PRODUCT_LIST)?.toHashSet()?.let {
                selectedTopAdsProduct = it
            }
        }
    }

    private fun setUpUI() {
        categoryListAdapter = CategoryListAdapter(topAdsCategoryList, chipFilterClick = this::onChipFilterClick)
        productsListAdapter = ProductListAdapter(ArrayList(), selectedTopAdsProduct, this)
        quickFilterRecyclerView.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryListAdapter
            addItemDecoration(SpaceItemDecoration(LinearLayoutManager.HORIZONTAL))
        }
        productListRecyclerView.run {
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
        btnSort.setOnClickListener {
            sortProductList.show(childFragmentManager, "")
        }
        btnNext.setOnClickListener {
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
        view?.let { Utils.setSearchListener(searchInputView, activity, it, ::refreshProduct) }
        searchInputView.searchBarTextField.addTextChangedListener(object : TextWatcher {
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
        emptyResultText.hide()
        quickFilterRecyclerView.show()
        selectProductCheckBox.show()
        productListRecyclerView.show()
    }

    private fun showEmptyView() {
        if (getKeyword().isNotEmpty()) {
            emptyResultText.text = String.format(getString(R.string.topads_headline_search_result_with_keyword, getKeyword()))
        }
        emptyResultText.show()
        quickFilterRecyclerView.hide()
        selectProductCheckBox.hide()
        productListRecyclerView.hide()
    }

    private fun setUpSelectProductCheckBox() {
        selectProductCheckBox.setOnClickListener {
            val isChecked = selectProductCheckBox.isChecked
            if (isChecked) {
                productsListAdapter.list.let {
                    selectedTopAdsProduct.addAll(it.take(MAX_PRODUCT_SELECTION))
                }
            } else {
                selectedTopAdsProduct.clear()
            }
            setSelectProductText()
            productsListAdapter.notifyDataSetChanged()
        }
    }

    private fun setSelectProductText() {
        selectProductInfo.text = String.format(getString(R.string.format_selected_produk), selectedTopAdsProduct.size)
    }

    private fun setUpToolTip() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            val tvToolTipText = this.findViewById<Typography>(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.topads_headline_tooltip_text)

            val imgTooltipIcon = this.findViewById<ImageUnify>(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_tips))
        }
        tooltipBtn.addItem(tooltipView)
        tooltipBtn.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(R.string.topads_headline_tips_choose_product_with_most_reviews, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_choose_product_with_most_popularity, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_choose_product_with_same_category, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_name_and_photo_correct, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, getString(R.string.topads_headline_tips_choosing_product), tipsList) }
            tipsListSheet?.show(fragmentManager!!, "")
        }
    }

    private fun refreshProduct() {
        start = 0
        productsListAdapter.refreshList()
        fetchTopAdsProducts()
    }

    private fun setUpObservers() {
        viewModel.getEtalaseListLiveData().observe(viewLifecycleOwner, Observer {
            categoryListAdapter.setItems(it)
            it.first().let { topAdsCategory ->
                selectedCategoryDataModel = topAdsCategory
                setProductSelectCbText()
                fetchTopAdsProducts()
            }
        })
    }

    private fun setProductSelectCbText() {
        if (selectedCategoryDataModel?.id == DEFAULT_RECOMMENDATION_CATEGORY_ID) {
            selectProductCheckBox.text = getString(R.string.topads_headline_select_all_recommendation)
        } else {
            val count = selectedCategoryDataModel?.count?.takeIf {
                it <= MAX_PRODUCT_SELECTION
            } ?: MAX_PRODUCT_SELECTION
            selectProductCheckBox.text = String.format(getString(R.string.topads_headline_select_first_n_recommendation), count)
        }
        selectProductCheckBox.isChecked = false
    }

    private fun setSelectProductCbCheck(data: ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>) {
        if (data.size == selectedTopAdsProduct.size && selectedTopAdsProduct == HashSet(data)) {
            selectProductCheckBox.setIndeterminate(false)
            selectProductCheckBox.isChecked = true
        } else {
            if (selectedTopAdsProduct.isNotEmpty()) {
                for (product in data) {
                    if (selectedTopAdsProduct.contains(product)) {
                        selectProductCheckBox.setIndeterminate(true)
                        selectProductCheckBox.isChecked = true
                        break
                    }
                }
            }
        }
    }

    private fun fetchTopAdsProducts() {
        viewModel.getTopAdsProductList(userSession.shopId.toIntOrZero(), getKeyword(), selectedCategoryDataModel?.id
                ?: "", getSelectedSortId(), "", ROW, start,
                this::onSuccessGetProductList, this::onError)
    }

    private fun onChipFilterClick(topAdsCategoryDataModel: TopAdsCategoryDataModel) {
        selectedCategoryDataModel = topAdsCategoryDataModel
        setProductSelectCbText()
        fetchTopAdsProducts()
    }

    private fun onSuccessGetProductList(data: List<ResponseProductList.Result.TopadsGetListProduct.Data>, eof: Boolean) {
        if (data.isNotEmpty()) {
            productsListAdapter.setProductList(data as ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>)
            prepareForNextFetch(eof)
            setSelectProductCbCheck(data)
        } else {
            showEmptyView()
        }
    }

    private fun prepareForNextFetch(eof: Boolean) {
        isDataEnded = eof
        if (!isDataEnded)
            recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun onError(t: Throwable) {
        view?.let {
            Toaster.build(it, t.localizedMessage?:"", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, clickListener = View.OnClickListener {
                refreshProduct()
            })
        }
    }

    fun setResultAndFinish() {
        val intent = Intent()
        intent.putExtra(IS_EDITED, isProductSelectedListEdited)
        intent.putExtra(SELECTED_PRODUCT_LIST, getSelectedProducts())
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onProductOverSelect() {
        view?.let {
            Toaster.build(it, getString(R.string.topads_headline_over_product_selection), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun onProductClick(product: ResponseProductList.Result.TopadsGetListProduct.Data) {
        val count = selectedTopAdsProduct.size
        isProductSelectedListEdited = true
        selectProductCheckBox.setIndeterminate(true)
        setSelectProductText()
        btnNext.isEnabled = count > 0
    }
}