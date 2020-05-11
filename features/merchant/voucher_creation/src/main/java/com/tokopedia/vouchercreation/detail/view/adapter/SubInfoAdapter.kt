package com.tokopedia.vouchercreation.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
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
            tvMvcInfoValue.text = model.infoValue.parseAsHtml()
            imgMvcVoucherCopy.isVisible = model.canCopy
            imgMvcVoucherCopy.setOnClickListener { 
                Toast.makeText(context, context.getString(R.string.mvc_voucher_code_copied), Toast.LENGTH_SHORT).show()
            }
        }
    }
}