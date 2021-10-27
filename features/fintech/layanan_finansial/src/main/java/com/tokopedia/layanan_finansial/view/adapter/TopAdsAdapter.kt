package com.tokopedia.layanan_finansial.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.widget.TopAdsImageView

/**
 * @param recyclerView is passed as argument so that we can calculate the width of recycler view in onCreate
 */

class TopAdsAdapter(
    private val topAdsModelList: List<TopAdsImageModel>,
    private val onclick: (appLink: String) -> Unit,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<TopAdsViewHolder>() {

    /**
     * Additional recycler view width is calculated as we have to show a small part of the ads which is on the right side of the view
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_topads_sdk, parent, false)

        val width: Int = recyclerView.width
        val params = view.layoutParams
        params.width = (width * 0.96).toInt()
        view.layoutParams = params

        return TopAdsViewHolder(view, onclick)
    }

    override fun onBindViewHolder(holder: TopAdsViewHolder, position: Int) {
        holder.bind(topAdsModelList[position] as TopAdsImageViewModel)

    }

    override fun getItemCount(): Int {
        return topAdsModelList.size
    }


}


class TopAdsViewHolder(itemView: View, val onclick: (appLink: String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {


    fun bind(topAdsImageViewModel: TopAdsImageViewModel) {

        topAdsImageView.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                ImpresionTask(this@TopAdsViewHolder.javaClass.canonicalName.javaClass.canonicalName).execute(
                    viewUrl
                )
            }

        })


        topAdsImageView.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                applink?.let {
                    onclick(it)
                }

            }

        })
        topAdsImageView.loadImage(topAdsImageViewModel, 8)
    }

    private val topAdsImageView = itemView.findViewById<TopAdsImageView>(R.id.topAdsBanner)


}