package com.tokopedia.salam.umrah.common.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.presentation.adapter.viewholder.UmrahSimpleDetailViewHolder
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleDetailModel

/**
 * @author by furqan on 14/10/2019
 */
class UmrahSimpleDetailAdapter : RecyclerView.Adapter<UmrahSimpleDetailViewHolder>() {

    private val simpleDetailViewModels = arrayListOf<UmrahSimpleDetailModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UmrahSimpleDetailViewHolder =
            UmrahSimpleDetailViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_umrah_simple_view, parent, false))

    override fun getItemCount(): Int = simpleDetailViewModels.size

    override fun onBindViewHolder(viewHolder: UmrahSimpleDetailViewHolder, position: Int) {
        viewHolder.bind(simpleDetailViewModels[position])
    }

    fun setData(datas: List<UmrahSimpleDetailModel>) {
        this.simpleDetailViewModels.clear()
        this.simpleDetailViewModels.addAll(datas)
        notifyDataSetChanged()
    }

    fun addData(data: UmrahSimpleDetailModel) {
        this.simpleDetailViewModels.add(data)
        notifyDataSetChanged()
    }
}