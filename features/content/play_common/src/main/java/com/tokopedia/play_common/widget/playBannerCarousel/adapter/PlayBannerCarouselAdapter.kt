package com.tokopedia.play_common.widget.playBannerCarousel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.BasePlayBannerCarouselViewHolder

class PlayBannerCarouselAdapter(
        private val adapterTypeFactory: PlayBannerCarouselTypeFactory,
        private val listener: PlayBannerCarouselViewEventListener?
): RecyclerView.Adapter<BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel>>(){

    private var list: List<BasePlayBannerCarouselModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel> {
        return adapterTypeFactory.createViewHolder(onCreateViewItem(parent, viewType), viewType)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type(adapterTypeFactory)
    }

    override fun onBindViewHolder(holder: BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel>, position: Int) {
        holder.bind(list[position], listener)
    }

    fun setItems(list: List<BasePlayBannerCarouselModel>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(PlayDiffUtil(this.list, list))
        diffResult.dispatchUpdatesTo(this)
        this.list = list
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }
}