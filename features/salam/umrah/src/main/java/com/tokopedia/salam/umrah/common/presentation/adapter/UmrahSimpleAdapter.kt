package com.tokopedia.salam.umrah.common.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.presentation.adapter.viewholder.UmrahSimpleViewHolder
import com.tokopedia.salam.umrah.common.presentation.viewmodel.UmrahSimpleViewModel

/**
 * @author by furqan on 08/10/2019
 */
class UmrahSimpleAdapter : RecyclerView.Adapter<UmrahSimpleViewHolder>() {

    private val simpleViewModels = arrayListOf<UmrahSimpleViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UmrahSimpleViewHolder =
            UmrahSimpleViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_umrah_simple_view, parent, false))

    override fun getItemCount(): Int = simpleViewModels.size

    override fun onBindViewHolder(viewHolder: UmrahSimpleViewHolder, position: Int) {
        viewHolder.bind(simpleViewModels[position])
    }

    fun setDatas(datas: List<UmrahSimpleViewModel>) {
        this.simpleViewModels.clear()
        this.simpleViewModels.addAll(datas)
        notifyDataSetChanged()
    }

    fun addData(data: UmrahSimpleViewModel) {
        this.simpleViewModels.add(data)
        notifyDataSetChanged()
    }

}