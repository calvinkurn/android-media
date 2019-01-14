package com.tokopedia.digital.widget.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_digital.common.data.api.DigitalResponseConverter
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital.R
import com.tokopedia.digital.common.constant.DigitalEventTracking
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService
import com.tokopedia.digital.common.data.apiservice.DigitalHmacAuthInterceptor
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi
import com.tokopedia.digital.common.data.source.CategoryListDataSource
import com.tokopedia.digital.common.data.source.StatusDataSource
import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository
import com.tokopedia.digital.widget.data.source.RecommendationListDataSource
import com.tokopedia.digital.widget.domain.interactor.DigitalRecommendationUseCase
import com.tokopedia.digital.widget.view.adapter.RecommendationAdapter
import com.tokopedia.digital.widget.view.listener.DigitalChannelFragmentInteraction
import com.tokopedia.digital.widget.view.model.Recommendation
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper
import com.tokopedia.digital.widget.view.presenter.DigitalChannelContract
import com.tokopedia.digital.widget.view.presenter.DigitalChannelPresenter
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSession
import java.util.ArrayList
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Rizky on 15/11/18.
 */
class DigitalChannelFragment : BaseDaggerFragment(), DigitalChannelContract.View, RecommendationAdapter.OnClickListener {

    private lateinit var abstractionRouter: AbstractionRouter

    private lateinit var userSession: UserSession

    private lateinit var digitalChannelPresenter: DigitalChannelPresenter

    private val recommendations = mutableListOf<Recommendation>()
    private lateinit var recommendationAdapter: RecommendationAdapter

    private lateinit var pulsa_place_holders: RelativeLayout
    private lateinit var recyclerview_recommendation: RecyclerView
    private lateinit var error_view: LinearLayout
    private lateinit var text_error_message: TextView
    private lateinit var button_try_again: Button

    companion object {

        fun newInstance(interaction: DigitalChannelFragmentInteraction): DigitalChannelFragment {
            val fragment = DigitalChannelFragment()
            fragment.setInteraction(interaction)
            return fragment
        }
    }

    private var interaction: DigitalChannelFragmentInteraction? = null

    private fun setInteraction(interaction: DigitalChannelFragmentInteraction) {
        this.interaction = interaction
    }

    private val DIGITAL_CHANNEL_RECOMMENDATION = "mainapp_digital_channel_recommendation"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.fragment_digital_channel, container, false)

        pulsa_place_holders = rootview.findViewById(R.id.pulsa_place_holders)
        recyclerview_recommendation = rootview.findViewById(R.id.recyclerview_recommendation)
        error_view = rootview.findViewById(R.id.error_view)
        text_error_message = rootview.findViewById(R.id.text_error_message)
        button_try_again = rootview.findViewById(R.id.button_try_again)

        button_try_again.setOnClickListener {
            error_view.visibility = View.GONE
            pulsa_place_holders.visibility = View.VISIBLE

            digitalChannelPresenter.getRecommendationList(5)
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

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        val  retrofitBuilder = Retrofit.Builder()
                .baseUrl(TkpdBaseURL.DIGITAL_API_DOMAIN + TkpdBaseURL.DigitalApi.VERSION)
                .addConverterFactory(DigitalResponseConverter())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        val okHttpbuilder = OkHttpClient.Builder();
        var loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(loggingLevel);

        val networkRouter =  context!!.applicationContext as NetworkRouter;
        val userSessionInterceptor = userSession as UserSessionInterface;
        val  okHttpRetryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        okHttpbuilder.addInterceptor(httpLoggingInterceptor);
        val okHttpClient = okHttpbuilder
        .addInterceptor(FingerprintInterceptor(networkRouter, userSessionInterceptor))
                .addInterceptor(DigitalHmacAuthInterceptor(
                  context,
                        networkRouter,
                        userSession,
                        TkpdBaseURL.DigitalApi.HMAC_KEY
                ))
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .build();

        val retrofit = retrofitBuilder.client(okHttpClient).build();

        val digitalEndpointService = retrofit.create(DigitalRestApi::class.java);
        val digitalGqlApiService = DigitalGqlApiService()
        val globalCacheManager = context as CacheManager

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
        interaction?.updateHeaderText(stringRes)
    }

    override fun renderRecommendationList(recommendations: List<Recommendation>) {
        pulsa_place_holders.visibility = View.GONE
        recyclerview_recommendation.visibility = View.VISIBLE
        this.recommendations.addAll(recommendations)
        recommendationAdapter.notifyDataSetChanged()
    }

    override fun fetchCategoryList() {
        interaction?.changeToDigitalWidget()
    }

    override fun showError(resId: Int) {
        pulsa_place_holders.visibility = View.GONE
        error_view.visibility = View.VISIBLE
        if (context != null) {
            text_error_message.text = getString(resId)
        }
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

    override fun onDestroy() {
        digitalChannelPresenter.detachView()
        super.onDestroy()
    }

}