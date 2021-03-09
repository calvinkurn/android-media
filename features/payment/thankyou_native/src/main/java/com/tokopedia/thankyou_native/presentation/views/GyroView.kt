package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.GyroRecommendationAnalytics
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.GyroAdapter
import com.tokopedia.thankyou_native.presentation.adapter.GyroAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.factory.GyroRecommendationFactory
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationListItem


const val KEY_NEED_LOGIN = "need_login"

class GyroView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), GyroAdapterListener {


    private val APP_LINK_URL_FORMAT = "%s?url=%s"

    private val layout = R.layout.thank_feature_listing
    private lateinit var recyclerView: RecyclerView
    private lateinit var analytics: GyroRecommendationAnalytics
    private lateinit var thanksPageData: ThanksPageData


    val adapter: GyroAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GyroAdapter(arrayListOf(), GyroRecommendationFactory(this))
    }

    init {
        LayoutInflater.from(context).inflate(layout, this, true)
    }

    fun addData(gyroRecommendation: GyroRecommendation, thanksPageData: ThanksPageData,
                analytics: GyroRecommendationAnalytics) {
        this.analytics = analytics
        this.thanksPageData = thanksPageData
        if (!gyroRecommendation.gyroVisitable.isNullOrEmpty()) {
            findViewById<TextView>(R.id.tvFeatureTitle).text = if (gyroRecommendation.title.isNotEmpty())
                gyroRecommendation.title else context.getString(R.string.thank_special_feature_for_you)
            findViewById<TextView>(R.id.tvFeatureDescription).text = if (gyroRecommendation.description.isNotEmpty())
                gyroRecommendation.description else context.getString(R.string.thank_lets_try_feature)
            addTORecyclerView(gyroRecommendation.gyroVisitable)
        } else {
            gone()
        }
    }

    private fun addTORecyclerView(featureEngineItem: ArrayList<Visitable<*>>) {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.post {
            adapter.addItems(featureEngineItem)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemDisplayed(gyroRecommendationListItem: GyroRecommendationListItem,
                                 position: Int) {
        if (::analytics.isInitialized || ::thanksPageData.isInitialized)
            analytics.onGyroRecommendationListView(gyroRecommendationListItem,
                    thanksPageData, position + 1)
    }

    override fun onItemClicked(gyroRecommendationListItem: GyroRecommendationListItem,
                               position: Int) {
        if (::analytics.isInitialized || ::thanksPageData.isInitialized)
            analytics.onGyroRecommendationListClick(gyroRecommendationListItem,
                    thanksPageData, position + 1)
    }

    override fun openAppLink(appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun openWebUrl(url: String) {
        val intent = RouteManager.getIntent(context, String.format(APP_LINK_URL_FORMAT,
                ApplinkConst.WEBVIEW, url)).apply {
            putExtra(KEY_NEED_LOGIN, true)
        }
        context.startActivity(intent)
    }
}