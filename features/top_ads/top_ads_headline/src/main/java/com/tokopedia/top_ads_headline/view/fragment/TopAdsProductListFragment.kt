package com.tokopedia.top_ads_headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.TopAdsCategoryDataModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.IS_EDITED
import com.tokopedia.top_ads_headline.view.activity.SELECTED_PRODUCT_LIST
import com.tokopedia.top_ads_headline.view.adapter.CategoryListAdapter
import com.tokopedia.top_ads_headline.view.adapter.ProductListAdapter
import com.tokopedia.top_ads_headline.view.viewmodel.TopAdsProductListViewModel
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.view.sheet.ProductSortSheetList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_topads_product_list.*
import javax.inject.Inject

private const val ROW = 50

class TopAdsProductListFragment : BaseDaggerFragment(), ProductListAdapter.ProductListAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var Start = 0

    private lateinit var viewModel: TopAdsProductListViewModel

    private var topAdsCategoryList = ArrayList<TopAdsCategoryDataModel>()

    private var categoryListAdapter: CategoryListAdapter? = null

    private var productsListAdapter: ProductListAdapter? = null

    private var selectedCategoryDataModel: TopAdsCategoryDataModel? = null

    private var selectedTopAdsProduct = HashSet<ResponseProductList.Result.TopadsGetListProduct.Data>()

    private lateinit var sortProductList: ProductSortSheetList

    private var isProductSelectedListEdited = false

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
        arguments?.run {
            getParcelableArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>(SELECTED_PRODUCT_LIST)?.toHashSet()?.let {
                selectedTopAdsProduct = it
            }
        }
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

    private fun setUpUI() {
        categoryListAdapter = CategoryListAdapter(topAdsCategoryList, chipFilterClick = this::onChipFilterClick)
        productsListAdapter = ProductListAdapter(ArrayList(), selectedTopAdsProduct, this)
        quickFilterRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        quickFilterRecyclerView.adapter = categoryListAdapter
        quickFilterRecyclerView.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.HORIZONTAL))
        productListRecyclerView.layoutManager = LinearLayoutManager(context)
        productListRecyclerView.adapter = productsListAdapter
        productListRecyclerView.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
        sortProductList = ProductSortSheetList.newInstance()
        sortProductList.onItemClick = { refreshProduct() }
        btnSort.setOnClickListener {
            sortProductList.show(childFragmentManager, "")
        }
        btnNext.setOnClickListener {
            setResultAndFinish()
        }
        selectProductInfo.text = String.format(getString(R.string.format_selected_produk), selectedTopAdsProduct.size)
        setUpToolTip()
    }

    private fun setUpToolTip() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            val tvToolTipText = this.findViewById<Typography>(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.topads_headline_tooltip_text)

            val imgTooltipIcon = this.findViewById<ImageUnify>(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_tips))
        }
        tooltipBtn?.addItem(tooltipView)
        tooltipBtn?.setOnClickListener {

        }
    }

    private fun refreshProduct() {
        Start = 0
        fetchTopAdsProducts()
    }

    private fun setUpObservers() {
        viewModel.getEtalaseListLiveData().observe(viewLifecycleOwner, Observer {
            categoryListAdapter?.setItems(it)
            it.first().let { topAdsCategory ->
                selectedCategoryDataModel = topAdsCategory
                fetchTopAdsProducts()
            }
        })
    }

    private fun fetchTopAdsProducts() {
        viewModel.getTopAdsProductList(userSession.shopId.toIntOrZero(), getKeyword(), selectedCategoryDataModel?.id
                ?: "", getSelectedSortId(), "", ROW, Start,
                this::onSuccessGetProductList, this::onError)
    }

    inner class SpaceItemDecoration(private val orientation: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount ?: 0) - 1) {
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.dp_8)
                } else {
                    outRect.bottom = view.context.resources.getDimensionPixelSize(R.dimen.dp_8)
                }
            }
        }
    }

    private fun onChipFilterClick(topAdsCategoryDataModel: TopAdsCategoryDataModel) {
        selectedCategoryDataModel = topAdsCategoryDataModel
        fetchTopAdsProducts()
    }

    private fun onSuccessGetProductList(data: List<ResponseProductList.Result.TopadsGetListProduct.Data>, eof: Boolean) {
        productsListAdapter?.setProductList(data as ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>)
    }

    private fun onError(t: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, t.localizedMessage) {
            refreshProduct()
        }
    }

    override fun onProductOverSelect() {
        view?.let { Toaster.make(it, getString(R.string.topads_headline_over_product_selection), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR) }
    }

    override fun onProductSelect(product: ResponseProductList.Result.TopadsGetListProduct.Data) {
        val count = selectedTopAdsProduct.size
        isProductSelectedListEdited = true
        selectProductInfo.text = String.format(getString(R.string.format_selected_produk), count)
        btnNext.isEnabled = count > 0
    }

    private fun getSelectedSortId() = sortProductList.getSelectedSortId()

    private fun getKeyword() = searchInputView.searchBarTextField.text.toString()

    private fun getSelectedProducts() = selectedTopAdsProduct.toCollection(ArrayList())

    fun setResultAndFinish() {
        val intent = Intent()
        intent.putExtra(IS_EDITED, isProductSelectedListEdited)
        intent.putExtra(SELECTED_PRODUCT_LIST, getSelectedProducts())
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}