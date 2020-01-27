package com.tokopedia.salam.umrah.common.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.presentation.adapter.viewholder.UmrahSimpleViewHolder
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleModel

/**
 * @author by furqan on 08/10/2019
 */
class UmrahSimpleAdapter : RecyclerView.Adapter<UmrahSimpleViewHolder>() {

    private val simpleViewModels = arrayListOf<UmrahSimpleModel>()

    var isTitleBold: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UmrahSimpleViewHolder =
            UmrahSimpleViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_umrah_simple_view, parent, false))

    override fun getItemCount(): Int = simpleViewModels.size

    override fun onBindViewHolder(viewHolder: UmrahSimpleViewHolder, position: Int) {
        viewHolder.bind(simpleViewModels[position], isTitleBold)
    }

    fun setData(datas: List<UmrahSimpleModel>) {
        this.simpleViewModels.clear()
        this.simpleViewModels.addAll(datas)
        notifyDataSetChanged()
    }

    fun addData(data: UmrahSimpleModel) {
        this.simpleViewModels.add(data)
        notifyDataSetChanged()
    }

}