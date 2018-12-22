package com.tokopedia.digital.widget.view.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.database.manager.GlobalCacheManager
import com.tokopedia.digital.R
import com.tokopedia.digital.common.constant.DigitalEventTracking
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService
import com.tokopedia.digital.common.data.source.CategoryListDataSource
import com.tokopedia.digital.common.data.source.StatusDataSource
import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository
import com.tokopedia.digital.widget.data.source.RecommendationListDataSource
import com.tokopedia.digital.widget.domain.interactor.DigitalRecommendationUseCase
import com.tokopedia.digital.widget.view.adapter.RecommendationAdapter
import com.tokopedia.digital.widget.view.model.Recommendation
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper
import com.tokopedia.digital.widget.view.presenter.DigitalChannelContract
import com.tokopedia.digital.widget.view.presenter.DigitalChannelPresenter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSession

/**
 * Created by Rizky on 15/11/18.
 */
class DigitalChannelFragment: BaseDaggerFragment(), DigitalChannelContract.View, RecommendationAdapter.OnClickListener {

    private lateinit var abstractionRouter: AbstractionRouter

    private lateinit var userSession: UserSession

    private lateinit var digitalChannelPresenter: DigitalChannelPresenter

    private val recommendations = mutableListOf<Recommendation>()
    private lateinit var recommendationAdapter: RecommendationAdapter

    private lateinit var pulsa_place_holders: RelativeLayout
    private lateinit var digital_widget_fragment: FrameLayout
    private lateinit var recyclerview_recommendation: RecyclerView
    private lateinit var error_view: LinearLayout
    private lateinit var text_error_message: TextView
    private lateinit var text_see_more: TextView
    private lateinit var text_title: TextView
    private lateinit var button_try_again: Button

    private val APPLINK_DIGITAL_BROWSE_PAGE = "tokopedia://category-explore?type=2"

    private val DIGITAL_CHANNEL_RECOMMENDATION = "mainapp_digital_channel_recommendation"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.fragment_digital_channel, container, false)

        pulsa_place_holders = rootview.findViewById(R.id.pulsa_place_holders)
        digital_widget_fragment = rootview.findViewById(R.id.digital_widget_fragment)
        recyclerview_recommendation = rootview.findViewById(R.id.recyclerview_recommendation)
        error_view = rootview.findViewById(R.id.error_view)
        text_error_message = rootview.findViewById(R.id.text_error_message)
        text_see_more = rootview.findViewById(R.id.see_more)
        text_title = rootview.findViewById(R.id.title)
        button_try_again = rootview.findViewById(R.id.button_try_again)

        button_try_again.setOnClickListener {
            error_view.visibility = View.GONE
            digital_widget_fragment.visibility = View.GONE
            pulsa_place_holders.visibility = View.VISIBLE

            digitalChannelPresenter.getRecommendationList(5)
        }

        text_see_more.setOnClickListener {
            RouteManager.route(activity, APPLINK_DIGITAL_BROWSE_PAGE)

            abstractionRouter
                    .analyticTracker
                    .sendEventTracking(
                            DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                            DigitalEventTracking.Category.DIGITAL_HOMEPAGE,
                            DigitalEventTracking.Action.CLICK_SEE_ALL_PRODUCTS,
                            ""
                    )
        }

        digitalChannelPresenter.attachView(this)

        recommendationAdapter = RecommendationAdapter(recommendations, this)

        recyclerview_recommendation.adapter = recommendationAdapter
        recyclerview_recommendation.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val remoteConfig = FirebaseRemoteConfigImpl(activity)

        if (userSession.isLoggedIn() && isDigitalChannelRecommendationEnabled(remoteConfig)) {
            digitalChannelPresenter.getRecommendationList(5)
        } else {
            fetchCategoryList()
        }
    }

    private fun isDigitalChannelRecommendationEnabled(remoteConfig: FirebaseRemoteConfigImpl): Boolean {
        return remoteConfig.getBoolean(DIGITAL_CHANNEL_RECOMMENDATION, false)
    }

    override fun initInjector() {
        abstractionRouter = activity?.application as AbstractionRouter
        userSession = UserSession(activity)

        val digitalEndpointService = DigitalEndpointService()
        val digitalGqlApiService = DigitalGqlApiService()
        val globalCacheManager = GlobalCacheManager()

        val statusDataSource = StatusDataSource(
                digitalEndpointService, globalCacheManager, StatusMapper()
        )

        val categoryListDataSource = CategoryListDataSource(
                digitalEndpointService, globalCacheManager, CategoryMapper()
        )

        val recommendationListDataSource = RecommendationListDataSource(
                digitalGqlApiService, context
        )

        val digitalWidgetRepository = DigitalWidgetRepository(
                statusDataSource, categoryListDataSource, recommendationListDataSource
        )

        val digitalRecommendationUseCase = DigitalRecommendationUseCase(digitalWidgetRepository)

        digitalChannelPresenter = DigitalChannelPresenter(
                digitalRecommendationUseCase
        )
    }

    override fun getScreenName(): String {
        return DigitalChannelFragment::class.java.simpleName
    }

    override fun renderDigitalTitle(stringRes: Int) {
        text_title.text = getString(stringRes)
    }

    override fun renderRecommendationList(recommendations: List<Recommendation>) {
        pulsa_place_holders.visibility = View.GONE
        digital_widget_fragment.visibility = View.GONE
        recyclerview_recommendation.visibility = View.VISIBLE
        this.recommendations.addAll(recommendations)
        recommendationAdapter.notifyDataSetChanged()
    }

    override fun fetchCategoryList() {
        pulsa_place_holders.visibility = View.GONE
        error_view.visibility = View.GONE
        digital_widget_fragment.visibility = View.VISIBLE
        val digitalWidgetFragment = DigitalWidgetFragment()
        childFragmentManager.beginTransaction()?.add(R.id.digital_widget_fragment, digitalWidgetFragment)
                ?.commit()
    }

    override fun showError(resId: Int) {
        pulsa_place_holders.visibility = View.GONE
        digital_widget_fragment.visibility = View.GONE
        error_view.visibility = View.VISIBLE
        text_error_message.text = getString(resId)
    }

    override fun onClickRecommendation(recommendation: Recommendation, position: Int) {
        if (RouteManager.isSupportApplink(activity, recommendation.applink)) {
            RouteManager.route(activity, recommendation.applink)

            val eventLabel = if (!recommendation.clientNumber.isNullOrEmpty()) {
                "history - ${recommendation.position} - ${recommendation.categoryName.toLowerCase()} - ${recommendation.productName.toLowerCase()}"
            } else {
                "recommendation - ${recommendation.position} - ${recommendation.categoryName.toLowerCase()}"
            }

            abstractionRouter
                    .analyticTracker
                    .sendEventTracking(
                            DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                            DigitalEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                            DigitalEventTracking.Action.CLICK_RECOMMENDATION_WIDGET,
                            eventLabel
                    )
        }
    }

}