package com.tokopedia.thankyou_native.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.TopAdsUIModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.thanks_item_top_ads_view.view.*

class TopAdsViewAdapter(
        private val topAdsUIModelList: ArrayList<TopAdsUIModel>,
        private val onclick: (appLink: String) -> Unit
) :
        RecyclerView.Adapter<TopAdsViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsViewViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.thanks_item_top_ads_view, parent, false)
        val widthFraction = if (topAdsUIModelList.size > Int.ONE) WIDTH_FRACTION_MULTIPLE else WIDTH_FRACTION_SINGLE
        val params = view.layoutParams
        params.width = (widthFraction * getScreenWidth()).toInt()
        view.layoutParams = params
        return  TopAdsViewViewHolder(view, onclick)
    }

    override fun onBindViewHolder(holder: TopAdsViewViewHolder, position: Int) {
        holder.bind(topAdsUIModelList[position])
    }

    override fun getItemCount(): Int = topAdsUIModelList.size

    fun addItems(topAdsUIModels: List<TopAdsUIModel>) {
        this.topAdsUIModelList.addAll(topAdsUIModels)
    }

    companion object {
        const val WIDTH_FRACTION_MULTIPLE = 0.90f
        const val WIDTH_FRACTION_SINGLE = 1.0f
    }

}

class TopAdsViewViewHolder(
        itemView: View,
        val onclick: (appLink: String) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val topAdsImageView: TopAdsImageView = itemView.adsTopAdsImageView
    fun bind(topAdsUIModel: TopAdsUIModel) {
        topAdsImageView.visible()
        topAdsImageView.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                applink?.let {
                    onclick(it)
                }
            }
        })
        addImpression(topAdsUIModel)

        topAdsImageView.loadImage(topAdsUIModel.topAdsImageViewModel, 8.toPx(), onLoadFailed = {
            topAdsImageView.gone()
        })
    }

    /*
    * Problem : Since impression is sent on onResourcesReady from glide which incase of
    * horizontal RV is called for items that are not visible(2, 3rd item)
    * Hence we added a scroll change listener (see addOnImpressionListener) to hit impression
    * if after scroll change view is visible.
    * But for first item there is no scroll hence its impression needs to be explicitely fired
    * via itemView.scrollTo(itemView.scrollX + Int.ONE, itemView.scrollY)
    * */
    private fun addImpression(topAdsUIModel: TopAdsUIModel) {
        topAdsImageView.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                itemView.addOnImpressionListener(topAdsUIModel.impressHolder) {
                    hitTopAdsImpression(viewUrl)
                    topAdsUIModel.impressHolder.invoke()
                }
                // for first item impression scroll callback invoke
                itemView.scrollTo(itemView.scrollX + Int.ONE, itemView.scrollY)
            }

        })
    }

    private fun hitTopAdsImpression(viewUrl: String) {
        itemView.context.let {
            TopAdsUrlHitter(it).hitImpressionUrl(
                    this::class.java.simpleName,
                    viewUrl,
                    "",
                    "",
                    ""
            )
        }
    }
}

