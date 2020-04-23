package com.tokopedia.vouchercreation.create.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherDisplayUiModel
import kotlinx.android.synthetic.main.mvc_voucher_display_view.view.*

class VoucherDisplayAdapter(private val itemList: List<VoucherDisplayUiModel>) : RecyclerView.Adapter<VoucherDisplayAdapter.VoucherDisplayViewHolder>() {

    class VoucherDisplayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherDisplayViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mvc_voucher_display_view, parent, false)
        return VoucherDisplayViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VoucherDisplayViewHolder, position: Int) {
        holder.itemView.run {
            itemList[position].let { uiModel ->
                voucherDisplayImage?.setImageResource(uiModel.displayImageRes)
                voucherDisplayText?.text = resources?.getString(uiModel.displayTextRes).toBlankOrString()
            }
        }
    }
}