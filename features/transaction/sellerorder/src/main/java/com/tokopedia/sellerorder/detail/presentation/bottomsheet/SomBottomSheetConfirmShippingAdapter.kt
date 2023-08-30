package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetConfirmshippingItemBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by irpan on 30/08/23.
 */
class SomBottomSheetConfirmShippingAdapter(
) : RecyclerView.Adapter<SomBottomSheetConfirmShippingAdapter.ViewHolder>() {
    private var list: List<String> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateListInfo(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_confirmshipping_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.apply {
            tvInfo.text = list[position]
            tvInfoNumber.text = "${position.plus(1)}"
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding by viewBinding<BottomsheetConfirmshippingItemBinding>()
    }
}
