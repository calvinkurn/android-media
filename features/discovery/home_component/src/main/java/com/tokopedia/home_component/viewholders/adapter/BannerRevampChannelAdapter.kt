package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_component.util.recordCrashlytics
import com.tokopedia.home_component.widget.atf_banner.BannerDiffUtil
import com.tokopedia.home_component.widget.atf_banner.BannerTypeFactoryImpl
import com.tokopedia.home_component.widget.atf_banner.BannerVisitable
import com.tokopedia.kotlin.extensions.view.ONE

class BannerRevampChannelAdapter(
    private val bannerItemListener: BannerItemListener
) : ListAdapter<BannerVisitable, AbstractViewHolder<BannerVisitable>>(BannerDiffUtil()) {

    private val adapterTypeFactory by lazy { BannerTypeFactoryImpl(bannerItemListener) }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<BannerVisitable> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<BannerVisitable>, position: Int) {
        try {
            val index = position % currentList.size
            if (isIndexValid(index)) {
                holder.bind(getItem(index))
            }
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val index = position % currentList.size
        return if (isIndexValid(index)) {
            getItem(index).type(adapterTypeFactory)
        } else HideViewHolder.LAYOUT
    }

    override fun getItemCount(): Int {
        return if (currentList.size > Int.ONE) Integer.MAX_VALUE else currentList.size
    }

    private fun isIndexValid(itemPosition: Int): Boolean {
        return itemPosition != RecyclerView.NO_POSITION && currentList.size > itemPosition
    }
}

