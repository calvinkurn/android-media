package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.TopAdsUIModel
import com.tokopedia.thankyou_native.presentation.adapter.TopAdsViewAdapter
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams

class TopAdsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val layout = R.layout.thanks_topads_view
    private lateinit var recyclerView: RecyclerView

    val adapter: TopAdsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TopAdsViewAdapter(arrayListOf(), ::onClick)
    }

    init {
        LayoutInflater.from(context).inflate(layout, this, true)
    }

    fun addData(
        topAdsParams: TopAdsRequestParams
    ) {
        if (!topAdsParams.topAdsUIModelList.isNullOrEmpty()) {
            visible()
            if (topAdsParams.sectionTitle.isNotBlank())
                findViewById<TextView>(R.id.tvFeatureTitle).text = topAdsParams.sectionTitle
            else
                findViewById<TextView>(R.id.tvFeatureTitle).gone()

            if (topAdsParams.sectionDescription.isNullOrBlank())
                findViewById<TextView>(R.id.tvFeatureDescription).gone()
            else
                findViewById<TextView>(R.id.tvFeatureDescription).text = topAdsParams.sectionDescription

            addTORecyclerView(topAdsParams.topAdsUIModelList)
        } else {
            gone()
        }
    }

    private fun addTORecyclerView(topAdsUIModels: List<TopAdsUIModel>?) {
        topAdsUIModels?.let {
            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            recyclerView.post {
                adapter.addItems(topAdsUIModels)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onClick(appLink : String) {
        context?.let {
            RouteManager.route(context, appLink)
        }
    }

}