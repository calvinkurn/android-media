package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_DAILY_BUDGET
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_GROUP_Id
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.util.SpaceItemDecoration
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.insight.TopadsDailyBudgetRecomAdapter
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.data.model.DailyBudgetRecommendationModel
import com.tokopedia.topads.dashboard.data.model.DataBudget
import com.tokopedia.topads.dashboard.data.model.TopadsGetDailyBudgetRecommendation
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*
import kotlinx.android.synthetic.main.topads_dash_recon_daily_budget_list.*
import javax.inject.Inject


/**
 * Created by Pika on 20/7/20.
 */

class TopAdsInsightBaseBidFragment(private val dailyBudgetRecommendData: TopadsGetDailyBudgetRecommendation?) : BaseDaggerFragment() {

    private lateinit var adapter: TopadsDailyBudgetRecomAdapter
    private var currentPosition = 0

    override fun getScreenName(): String {
        return TopAdsInsightBaseBidFragment::class.java.name
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TopadsDailyBudgetRecomAdapter(::onButtonClick)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_dash_recon_daily_budget_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        if (dailyBudgetRecommendData?.data?.isEmpty() == true)
            setEmptyState()
        else {
            if (dailyBudgetRecommendData != null) {
                setAdapterData(dailyBudgetRecommendData)
            }
        }
        rvDailyBudget?.adapter = adapter
        rvDailyBudget?.layoutManager = LinearLayoutManager(context)
    }

    private fun setEmptyState() {
        emptyViewDailyBudgetRecommendation?.image_empty?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        emptyViewDailyBudgetRecommendation?.visibility = View.VISIBLE
        emptyViewDailyBudgetRecommendation?.text_title?.text = getString(R.string.topads_dash_empty_daily_budget_title)
        emptyViewDailyBudgetRecommendation?.text_desc?.text = getString(R.string.topads_dash_empty_daily_budget_desc)
        daily_budget_title?.visibility = View.GONE
        daily_budget_desc?.visibility = View.GONE
    }

    private fun loadData() {
        topAdsDashboardPresenter.getDailyBudgetRecommendation(::onSuccessDailyBudgetRecom)
    }

    private fun onSuccessDailyBudgetRecom(dailyBudgetRecommendationModel: DailyBudgetRecommendationModel) {
        setAdapterData(dailyBudgetRecommendationModel.topadsGetDailyBudgetRecommendation)
    }

    private fun setAdapterData(topadsGetDailyBudgetRecommendation: TopadsGetDailyBudgetRecommendation) {
        adapter.items.clear()
        topadsGetDailyBudgetRecommendation.data.forEach {
            adapter.items.add(it)
        }
        adapter.notifyDataSetChanged()
        (parentFragment as TopAdsRecommendationFragment).setCount(adapter.items.size, 1)
        setTitle(topadsGetDailyBudgetRecommendation.data)
    }

    private fun setTitle(data: List<DataBudget>) {
        val potentialClick = calculatePotentialClick(data)
        daily_budget_desc?.text = String.format(getString(R.string.topads_dash_recom_daily_budget_desc), adapter.itemCount, potentialClick)
    }

    private fun calculatePotentialClick(data: List<DataBudget>): Int {
        var totalPotentialClick = 0
        data.forEach {
            totalPotentialClick += if (it.setCurrentBid >= it.priceDaily) {
                (it.setCurrentBid - it.priceDaily) / it.avgBid
            } else {
                (it.suggestedPriceDaily - it.priceDaily) / it.avgBid
            }
        }
        return totalPotentialClick
    }

    private fun onError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
        adapter.notifyItemChanged(currentPosition)
    }

    private fun onButtonClick(pos: Int) {
        val map = HashMap<String,Any?>()
       // map["price_bid"] = 1
        map[PARAM_DAILY_BUDGET] = adapter.items[pos].setCurrentBid
        map[PARAM_GROUP_Id] = adapter.items[pos].groupId
        currentPosition = pos
        topAdsDashboardPresenter.editBudgetThroughInsight(null,map,::onResultEdit)
    }

    private fun onResultEdit(topadsManageGroupAds: FinalAdResponse.TopadsManageGroupAds) {
        if(topadsManageGroupAds.groupResponse.errors?.isEmpty() == false)
        {
            onError(topadsManageGroupAds.groupResponse.errors?.firstOrNull()?.detail?:"")
        }else{
            loadData()
        }
    }
}