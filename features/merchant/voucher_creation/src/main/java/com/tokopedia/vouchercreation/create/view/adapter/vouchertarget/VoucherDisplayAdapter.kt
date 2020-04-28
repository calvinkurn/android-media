package com.tokopedia.vouchercreation.create.view.adapter.vouchertarget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.vouchertips.VoucherDisplayUiModel
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
            val cardWidth = getScreenWidth() - context.resources.getDimension(R.dimen.mvc_create_voucher_display_recycler_view_decoration).toInt() * 3
            voucherDisplayImage?.layoutParams?.width = cardWidth
            itemList[position].let { uiModel ->
                voucherDisplayImage?.run {
                    Glide.with(context)
                            .load(uiModel.imageUrl)
                            .into(this)
                }
                voucherDisplayText?.text = resources?.getString(uiModel.displayTextRes).toBlankOrString()
            }
        }
    }
}