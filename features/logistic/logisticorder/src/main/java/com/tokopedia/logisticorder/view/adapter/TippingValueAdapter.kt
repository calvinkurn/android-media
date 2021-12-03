package com.tokopedia.logisticorder.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.ChipsUnifyItemBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TippingValueAdapter(private var actionListener: ActionListener) : RecyclerView.Adapter<TippingValueAdapter.TippingValueViewHolder>() {

    var tippingValueList = mutableListOf<Int>()
    private var lastIndex = -1

    interface ActionListener {
        fun onTippingValueClicked(tippingValue: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TippingValueViewHolder {
        val binding = ChipsUnifyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TippingValueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TippingValueViewHolder, position: Int) {
        holder.bind(tippingValueList[position]) {
            notifyItemChanged(lastIndex)
            lastIndex = position
            actionListener.onTippingValueClicked(tippingValueList[position])
        }
    }

    override fun getItemCount(): Int {
        return tippingValueList.size
    }

    inner class TippingValueViewHolder(private val binding: ChipsUnifyItemBinding) : RecyclerView.ViewHolder(binding.root) {
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