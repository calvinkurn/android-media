package com.tokopedia.logisticorder.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticorder.databinding.TippingValueItemBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class RescheduleDayAdapter(private var rescheduleDayListener: RescheduleDayListener) : RecyclerView.Adapter<RescheduleDayAdapter.TippingValueViewHolder>() {

    var tippingValueList = mutableListOf<Int>()
    private var lastIndex = -1

    interface RescheduleDayListener {
        fun onTippingValueClicked(tippingValue: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TippingValueViewHolder {
        val binding = TippingValueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TippingValueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TippingValueViewHolder, position: Int) {
        holder.bind(tippingValueList[position]) {
            notifyItemChanged(lastIndex)
            lastIndex = position
            rescheduleDayListener.onTippingValueClicked(tippingValueList[position])
        }
    }

    override fun getItemCount(): Int {
        return tippingValueList.size
    }

    inner class TippingValueViewHolder(private val binding: TippingValueItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tippingValue: Int, onClick: () -> Unit) {
            binding.chipsItem.apply {
                chipText = CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(tippingValue)
                chipType = ChipsUnify.TYPE_NORMAL
                chipSize = ChipsUnify.SIZE_SMALL
                setOnClickListener {
                    onClick()
                    chipType = ChipsUnify.TYPE_SELECTED
                }
            }
        }
    }
}