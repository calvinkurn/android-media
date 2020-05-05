package com.tokopedia.vouchercreation.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.SubInfoItemUiModel
import kotlinx.android.synthetic.main.item_mvc_sub_info.view.*

/**
 * Created By @ilhamsuaib on 05/05/20
 */

class SubInfoAdapter : RecyclerView.Adapter<SubInfoAdapter.SubInfoViewHolder>() {

    private val items = mutableListOf<SubInfoItemUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SubInfoViewHolder(inflater.inflate(R.layout.item_mvc_sub_info, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SubInfoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun setSubInfoItems(items: List<SubInfoItemUiModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class SubInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: SubInfoItemUiModel) = with(itemView) {
            tvMvcInfoKey.text = model.infoKey
            tvMvcInfoValue.text = model.infoValue
        }
    }
}