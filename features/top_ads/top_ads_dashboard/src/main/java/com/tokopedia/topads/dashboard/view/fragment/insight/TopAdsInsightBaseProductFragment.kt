package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_ADD_OPTION
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_GROUP_Id
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationData
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationModel
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.insight.TopadsProductRecomAdapter
import com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet.TopAdsRecomGroupBottomSheet
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*
import kotlinx.android.synthetic.main.topads_dash_recom_product_list.*
import javax.inject.Inject

/**
 * Created by Pika on 20/7/20.
 */

class TopAdsInsightBaseProductFragment(private val productRecommendData: ProductRecommendationData?) : BaseDaggerFragment() {

    private lateinit var adapter: TopadsProductRecomAdapter

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter
    var rvRecomProduct: RecyclerView? = null

    private val sheet: TopAdsRecomGroupBottomSheet by lazy {
        TopAdsRecomGroupBottomSheet.getInstance()
    }

    override fun getScreenName(): String {
        return TopAdsInsightBaseProductFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        adapter = TopadsProductRecomAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.topads_dash_recom_product_list, container, false)
        rvRecomProduct = view.findViewById(R.id.rvProductRecom)
        setAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        if (productRecommendData?.products?.isEmpty() == true) {
            setEmptyState()
        } else {
            setAdapterData(productRecommendData?.products)
        }
        onBudgetClicked()
    }

    private fun calculateSetTitle(products: List<ProductRecommendation>?): Int {
        var totalClicks = 0
        products?.forEach {
            totalClicks += it.searchCount
        }
        return totalClicks
    }

    private fun setAdapterData(products: List<ProductRecommendation>?) {
        adapter.items.clear()
        products?.forEach {
            adapter.items.add(it)
        }
        adapter.notifyDataSetChanged()
        (parentFragment as TopAdsRecommendationFragment).setCount(adapter.items.size, 0)
        product_recom_desc.text = String.format(getString(R.string.topads_dash_recom_product_desc), products?.size, calculateSetTitle(products))
        selectedItems?.text = String.format(getString(R.string.topads_common_selected_product), adapter.itemCount)
    }

    fun onBudgetClicked() {
        (parentFragment as TopAdsRecommendationFragment).setClick()
    }

    private fun setEmptyState() {
        emptyViewProductRecommendation?.image_empty?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        emptyViewProductRecommendation?.text_title?.text = getString(R.string.topads_dash_empty_product_recom_title)
        emptyViewProductRecommendation?.text_desc?.text = getString(R.string.topads_dash_empty_product_recom_desc)
        emptyViewProductRecommendation?.visibility = View.VISIBLE
        product_recom_title?.visibility = View.GONE
        product_recom_desc?.visibility = View.GONE
        cb_product_recom?.visibility = View.GONE
        selectedItems?.visibility = View.GONE
        (parentFragment as TopAdsRecommendationFragment).setEmpty()
    }

    private fun loadData() {
        topAdsDashboardPresenter.getProductRecommendation(::onSuccessProductRecommendation)
    }

    private fun setAdapter() {
        rvRecomProduct?.adapter = adapter
        rvRecomProduct?.layoutManager = LinearLayoutManager(context)
    }

    private fun onSuccessProductRecommendation(productRecommendationModel: ProductRecommendationModel) {
        if (productRecommendationModel.topadsGetProductRecommendation.data.products.isEmpty())
            setEmptyState()
        else
            setAdapterData(productRecommendationModel.topadsGetProductRecommendation.data.products)
        swipeRefreshLayout.isRefreshing = false
    }

    fun test() {
        sheet.show(childFragmentManager)
        sheet.onItemClick = { groupId ->
            val productList: MutableList<GroupEditInput.Group.AdOperationsItem>? = getAds()
            val map = HashMap<String, Any?>()
            map[PARAM_GROUP_Id] = groupId
            topAdsDashboardPresenter.editBudgetThroughInsight(productList, map, ::onResultEdit)
        }
    }


    private fun getAds(): MutableList<GroupEditInput.Group.AdOperationsItem>? {
        val ids = adapter.getSelectedIds()
        val adOperation = GroupEditInput.Group.AdOperationsItem()
        val ad = adOperation.ad
        val list:MutableList<GroupEditInput.Group.AdOperationsItem>? = mutableListOf()
        ids.forEach {
            ad.productId = it
            adOperation.ad = ad
            adOperation.action = PARAM_ADD_OPTION
            list?.add(adOperation)
        }
        return list
    }

    private fun onError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onResultEdit(topadsManageGroupAds: FinalAdResponse.TopadsManageGroupAds) {
        if (topadsManageGroupAds.groupResponse.errors?.isEmpty() == false) {
            onError(topadsManageGroupAds.groupResponse.errors?.firstOrNull()?.detail ?: "")
        } else {
            loadData()
        }
    }
}