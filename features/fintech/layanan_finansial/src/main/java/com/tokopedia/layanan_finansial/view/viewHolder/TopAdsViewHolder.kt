package com.tokopedia.layanan_finansial.view.viewHolder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.adapter.TopAdsAdapter
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class TopAdsViewHolder(val view: View): AbstractViewHolder<Visitable<*>>(view) {

  val topAdsImageView = TopAdsImageView(view.context)

    private val displayRecycler: RecyclerView by lazy {
        itemView.findViewById<RecyclerView>(
            R.id.topAdsImagesRecycler
        )
    }

    companion object{
        val LAYOUT:Int = R.layout.layout_topads_view
    }


    override fun bind(element: Visitable<*>?) {
        var element  = element as TopAdsImageModel
        if (element.imageUrl?.isEmpty() == true) {
            topAdsImageView.getImageData(source = "17", adsCount = 3, dimenId = 3)
        }

        topAdsImageView.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {
            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                val imageList= imageDataList as ArrayList<TopAdsImageModel>
                if (imageDataList.isNotEmpty())
                {
                    generateAdsCarousal(imageList)
                }
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }

        })

    }

    private fun generateAdsCarousal(imageList: ArrayList<TopAdsImageModel>) {
        displayRecycler.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val topAdsAdapter = TopAdsAdapter(imageList, ::onClick)
        displayRecycler.adapter = topAdsAdapter
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(displayRecycler)
    }

    private fun onClick(appLink: String) {
        view.context?.let {
            RouteManager.route(it, appLink)
        }
    }
}