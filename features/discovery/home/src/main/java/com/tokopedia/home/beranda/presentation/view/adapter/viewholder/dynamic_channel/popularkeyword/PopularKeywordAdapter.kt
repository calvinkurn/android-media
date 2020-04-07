package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.popularkeyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView.ShimmeringImageView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by yoasfs on 2020-02-18
 */

class PopularKeywordAdapter(val popularKeywordListener: PopularKeywordViewHolder.PopularKeywordListener,
                            val channel: DynamicHomeChannel.Channels)
    : RecyclerView.Adapter<PopularKeywordAdapter.Holder>() {

    private val popularKeywordList: MutableList<PopularKeywordDataModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.layout_popular_keyword_item, parent, false))
    }

    override fun getItemCount(): Int {
        return popularKeywordList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(popularKeywordList[position], popularKeywordListener, channel, position)
    }

    fun submitList(list: List<PopularKeywordDataModel>){
        popularKeywordList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        popularKeywordList.clear()
        notifyDataSetChanged()
    }

    class Holder(view: View): RecyclerView.ViewHolder(view) {
        val cardProduct = view.findViewById<CardView>(R.id.card_product)
        val ivImage = view.findViewById<ShimmeringImageView>(R.id.iv_product)
        val tvProduct = view.findViewById<Typography>(R.id.tv_product)
        val tvCount = view.findViewById<Typography>(R.id.tv_count)

        fun bind(data : PopularKeywordDataModel, popularKeywordListener: PopularKeywordViewHolder.PopularKeywordListener, channel: DynamicHomeChannel.Channels, position: Int) {
            ivImage.loadImage(data.imageUrl)
            tvProduct.text = data.title.capitalize()
            if (data.productCount.isNotEmpty()) {
                tvCount.show()
                tvCount.text = data.productCount
            } else tvCount.hide()
            itemView.addOnImpressionListener(data.impressHolder) {
                popularKeywordListener.onPopularKeywordItemImpressed(channel, position, data.title)
            }
            cardProduct.setOnClickListener{
                popularKeywordListener.onPopularKeywordItemClicked(data.applink, channel, position, data.title)
            }
        }
    }
}