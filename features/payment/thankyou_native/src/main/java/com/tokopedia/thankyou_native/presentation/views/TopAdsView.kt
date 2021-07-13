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
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.TopAdsViewAdapter
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class TopAdsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val layout = R.layout.thanks_topads_view
    private lateinit var recyclerView: RecyclerView
    private lateinit var thanksPageData: ThanksPageData

    val adapter: TopAdsViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TopAdsViewAdapter(arrayListOf(), ::onClick)
    }

    init {
        LayoutInflater.from(context).inflate(layout, this, true)
    }

    fun addData(
        topAdsParams: TopAdsRequestParams,
        thanksPageData: ThanksPageData
    ) {
        this.thanksPageData = thanksPageData
        if (!topAdsParams.topAdsImageViewModel.isNullOrEmpty()) {
            visible()
            if (topAdsParams.title.isNotBlank())
                findViewById<TextView>(R.id.tvFeatureTitle).text = topAdsParams.title
            else
                findViewById<TextView>(R.id.tvFeatureTitle).gone()

            if (topAdsParams.description.isNotBlank())
                findViewById<TextView>(R.id.tvFeatureDescription).text = topAdsParams.description
            else
                findViewById<TextView>(R.id.tvFeatureDescription).gone()

            addTORecyclerView(topAdsParams.topAdsImageViewModel)
        } else {
            gone()
        }
    }

    private fun addTORecyclerView(topAdsImageViewModelList: ArrayList<TopAdsImageViewModel>?) {
        topAdsImageViewModelList?.let {
            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            recyclerView.post {
                adapter.addItems(topAdsImageViewModelList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onClick(topAdsImageViewModel: TopAdsImageViewModel) {
        context?.let {
            RouteManager.route(context, topAdsImageViewModel.applink)
        }
    }

}