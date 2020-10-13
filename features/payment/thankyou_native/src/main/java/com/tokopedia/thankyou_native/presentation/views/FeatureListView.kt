package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.presentation.adapter.FeatureListingAdapter
import com.tokopedia.thankyou_native.presentation.adapter.FeatureListingAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.factory.FeatureListingFactory
import com.tokopedia.webview.KEY_NEED_LOGIN

class FeatureListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), FeatureListingAdapterListener {


    private val APP_LINK_URL_FORMAT = "%s?url=%s"

    private val layout = R.layout.thank_feature_listing
    private lateinit var recyclerView: RecyclerView


    val adapter: FeatureListingAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FeatureListingAdapter(arrayListOf(), FeatureListingFactory(this))
    }

    init {
        LayoutInflater.from(context).inflate(layout, this, true)
    }

    fun addData(featureEngineData: FeatureEngineData) {
        findViewById<TextView>(R.id.tvFeatureTitle).text = if (featureEngineData.title.isNotEmpty())
            featureEngineData.title else context.getString(R.string.thank_special_feature_for_you)
        findViewById<TextView>(R.id.tvFeatureDescription).text = if (featureEngineData.description.isNotEmpty())
            featureEngineData.description else context.getString(R.string.thank_lets_try_feature)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.post {
            adapter.addItems(FeatureRecommendationMapper.getFeatureList(featureEngineData.featureEngineItem))
            adapter.notifyDataSetChanged()
        }
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