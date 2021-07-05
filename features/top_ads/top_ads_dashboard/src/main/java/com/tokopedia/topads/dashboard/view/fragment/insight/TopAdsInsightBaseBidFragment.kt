package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_DAILY_BUDGET
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_GROUP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_PRICE_BID
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.DailyBudgetRecommendationModel
import com.tokopedia.topads.dashboard.data.model.DataBudget
import com.tokopedia.topads.dashboard.data.model.TopadsGetDailyBudgetRecommendation
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.insight.TopadsDailyBudgetRecomAdapter
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsRecommendationFragment.Companion.BUDGET_RECOM
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*
import kotlinx.android.synthetic.main.topads_dash_recon_daily_budget_list.*
import javax.inject.Inject


/**
 * Created by Pika on 20/7/20.
 */

const val CLICK_TERAPKAN = "click - terapkan"
class TopAdsInsightBaseBidFragment : BaseDaggerFragment() {

    private var dailyBudgetRecommendData: TopadsGetDailyBudgetRecommendation? = null
    private lateinit var adapter: TopadsDailyBudgetRecomAdapter
    private var currentPosition = 0
    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        fun createInstance(bundle: Bundle): TopAdsInsightBaseBidFragment {
            val fragment = TopAdsInsightBaseBidFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return TopAdsInsightBaseBidFragment::class.java.name
    }


    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TopadsDailyBudgetRecomAdapter(userSession, ::onButtonClick)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_dash_recon_daily_budget_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentData()
        swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        if (dailyBudgetRecommendData?.data?.isEmpty() == true)
            setEmptyState()
        else {
            dailyBudgetRecommendData?.let { setAdapterData(it) }
        }
        rvDailyBudget?.adapter = adapter
        rvDailyBudget?.layoutManager = LinearLayoutManager(context)
        rvDailyBudget?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun getArgumentData() {
        dailyBudgetRecommendData = arguments?.getParcelable(BUDGET_RECOM)
    }

    private fun setEmptyState() {
        emptyViewDailyBudgetRecommendation?.image_empty?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        emptyViewDailyBudgetRecommendation?.visible()
        emptyViewDailyBudgetRecommendation?.text_title?.text = getString(R.string.topads_dash_empty_daily_budget_title)
        emptyViewDailyBudgetRecommendation?.text_desc?.text = getString(R.string.topads_dash_empty_daily_budget_desc)
        rvDailyBudget?.gone()
        daily_budget_title?.gone()
        daily_budget_desc?.gone()
    }

    private fun loadData() {
        topAdsDashboardPresenter.getDailyBudgetRecommendation(::onSuccessDailyBudgetRecom)
    }

    private fun onSuccessDailyBudgetRecom(dailyBudgetRecommendationModel: DailyBudgetRecommendationModel) {
        swipeRefreshLayout.isRefreshing = false
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
        daily_budget_desc?.text = Html.fromHtml(String.format(getString(R.string.topads_dash_recom_daily_budget_desc), adapter.itemCount, potentialClick))
    }

    private fun calculatePotentialClick(data: List<DataBudget>): Int {
        var totalPotentialClick = 0
        data.forEach {
            totalPotentialClick += if (it.setCurrentBid >= it.priceDaily) {
                ((it.setCurrentBid - it.priceDaily) / it.avgBid.toDouble()).toInt()
            } else {
                ((it.suggestedPriceDaily - it.priceDaily) / it.avgBid.toDouble()).toInt()
            }
        }
        return totalPotentialClick
    }

    private fun onError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.topads.common.R.string.topads_common_text_ok), View.OnClickListener {}).show()
        }
        adapter.notifyItemChanged(currentPosition)
    }

    private fun onButtonClick(pos: Int) {
        currentPosition = pos
        topAdsDashboardPresenter.getGroupInfo(resources, adapter.items[pos].groupId, ::onSuccessGroupInfo)
        val eventLabel = "${adapter.items[pos].groupId} - ${adapter.items[pos].suggestedPriceDaily} - ${adapter.items[pos].setPotensiKlik} - ${adapter.items[pos].setCurrentBid}"
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightShopEvent(CLICK_TERAPKAN, eventLabel, userSession.userId)
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        val map = HashMap<String, Any?>()
        map[PARAM_PRICE_BID] = data.priceBid
        map[PARAM_DAILY_BUDGET] = adapter.items[currentPosition].setCurrentBid
        map[PARAM_GROUP_Id] = adapter.items[currentPosition].groupId
        topAdsDashboardPresenter.editBudgetThroughInsight(null, map, ::onResultEdit, ::onError)
    }

    private fun onResultEdit(topadsManageGroupAds: FinalAdResponse.TopadsManageGroupAds) {
        if (topadsManageGroupAds.groupResponse.errors?.isEmpty() == false) {
            onError(topadsManageGroupAds.groupResponse.errors?.firstOrNull()?.detail ?: "")
        } else {
            loadData()
            showSuccessToast()
        }
    }

    private fun showSuccessToast() {
        view?.let {
            Toaster.build(it, getString(R.string.topads_dash_success_daily_budget_toast), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_text_ok), View.OnClickListener {}).show()
        }
    }
}