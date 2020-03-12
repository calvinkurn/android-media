package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.typeFactory.MixTopTypeFactoryImpl

class MixTopAdapter(var mixTypeFactoryImpl: MixTopTypeFactoryImpl,
                    val grids: Array<DynamicHomeChannel.Grid>,
                    val channel: DynamicHomeChannel.Channels,
                    val homeCategoryListener: HomeCategoryListener): RecyclerView.Adapter<AbstractViewHolder<MixTopVisitable>>(){

    private var visitables: List<MixTopVisitable> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<MixTopVisitable> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return mixTypeFactoryImpl.onCreateViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(mixTypeFactoryImpl)
    }

    fun setItems(visitables: List<MixTopVisitable>) {
        this.visitables = visitables
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<MixTopVisitable>, position: Int) {
        holder.bind(visitables[position])
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<MixTopVisitable>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(visitables[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}