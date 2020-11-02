package com.tokopedia.top_ads_headline.view.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.TopAdsCategoryDataModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.adapter.CategoryListAdapter
import com.tokopedia.top_ads_headline.view.adapter.ProductListAdapter
import com.tokopedia.top_ads_headline.view.viewmodel.TopAdsProductListViewModel
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_topads_product_list.*
import javax.inject.Inject

private const val START = 0
private const val ROW = 50

class TopAdsProductListFragment : BaseDaggerFragment(), ProductListAdapter.ProductItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: TopAdsProductListViewModel

    private var topAdsCategoryList = ArrayList<TopAdsCategoryDataModel>()

    private var topadsProductList = ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>()

    private var categoryListAdapter: CategoryListAdapter? = null

    private var productsListAdapter: ProductListAdapter? = null

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
        productsListAdapter = ProductListAdapter(topadsProductList, this)
        quickFilterRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        quickFilterRecyclerView.adapter = categoryListAdapter
        quickFilterRecyclerView.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.HORIZONTAL))
        productListRecyclerView.layoutManager = LinearLayoutManager(context)
        productListRecyclerView.adapter = productsListAdapter
        productListRecyclerView.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
    }

    private fun setUpObservers() {
        viewModel.getEtalaseListLiveData().observe(viewLifecycleOwner, Observer {
            categoryListAdapter?.setItems(it)
            it.first().let {topAdsCategory ->
                viewModel.getTopAdsProductList(userSession.shopId.toIntOrZero(), "", topAdsCategory.id, "", "", ROW, START,
                        this::onSuccessGetProductList, this::onError)
            }
        })
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
        viewModel.getTopAdsProductList(userSession.shopId.toIntOrZero(), "", topAdsCategoryDataModel.id, "", "", ROW, START,
                this::onSuccessGetProductList, this::onError)
    }

    override fun onProductItemClick() {

    }

    private fun onSuccessGetProductList(data: List<ResponseProductList.Result.TopadsGetListProduct.Data>, eof: Boolean) {
        productsListAdapter?.setProductList(data as ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>)
    }

    private fun onError(t: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, t.localizedMessage) {

        }
    }

}