package com.tokopedia.topads.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.productimagepreview.TopAdsProductImagePreviewAdapter

class TopAdsProductImagePreviewWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var recyclerView: RecyclerView
    private var topAdsProductImagePreviewAdapter: TopAdsProductImagePreviewAdapter
    private var topAdsImagePreviewClick: TopAdsImagePreviewClick? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.topads_widget_product_image_preview,
                this, true)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        topAdsProductImagePreviewAdapter = TopAdsProductImagePreviewAdapter()
        recyclerView.adapter = topAdsProductImagePreviewAdapter
    }

    fun setSelectedProductList(imageList: ArrayList<String>) {
        topAdsProductImagePreviewAdapter.setSelectedProductList(imageList)
    }

    private fun setAdapterTopAdsPreviewClick() {
        topAdsProductImagePreviewAdapter.setAdapterTopAdsPreviewClick(topAdsImagePreviewClick)
    }

    fun setTopAdsImagePreviewClick(topAdsImagePreviewClick: TopAdsImagePreviewClick){
        this.topAdsImagePreviewClick = topAdsImagePreviewClick
        setAdapterTopAdsPreviewClick()
    }

    interface TopAdsImagePreviewClick {
        fun onDeletePreview(position: Int)
        fun onClickPreview(position: Int)
    }
}