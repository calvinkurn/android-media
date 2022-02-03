package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.TopAdsCarouselModel
import com.tokopedia.topads.sdk.listener.TopAdsCarouselListener
import com.tokopedia.topads.sdk.view.adapter.TopAdsCarouselAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.topads_carousel_layout.view.*

class ToadsCarousel : BaseCustomView {

    private var topAdsCarouselAdapter: TopAdsCarouselAdapter? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.topads_carousel_layout, this)
    }

    fun setTopAdsCarouselModel(topAdsCarouselModel: TopAdsCarouselModel, topAdsCarouselListener: TopAdsCarouselListener) {

        initTopAdsCarouselTitle(topAdsCarouselModel)
        initTopAdsCarouselItem(topAdsCarouselModel, topAdsCarouselListener)

    }

    private fun initTopAdsCarouselItem(topAdsCarouselModel: TopAdsCarouselModel, topAdsCarouselListener: TopAdsCarouselListener) {
        topAdsCarouselAdapter = TopAdsCarouselAdapter(topAdsCarouselListener)
        val list = findViewById<RecyclerView>(R.id.topAdsCarouselRv)
        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = topAdsCarouselAdapter
        topAdsCarouselAdapter?.setList(topAdsCarouselModel.items)

    }

    private fun initTopAdsCarouselTitle(topAdsCarouselModel: TopAdsCarouselModel) {
        topAdsCarouselTitle.text = topAdsCarouselModel.title
    }
}
