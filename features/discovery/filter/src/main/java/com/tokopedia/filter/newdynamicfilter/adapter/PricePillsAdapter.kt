package com.tokopedia.filter.newdynamicfilter.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.NumberParseHelper

import java.util.ArrayList

class PricePillsAdapter(private val callback: Callback?) : RecyclerView.Adapter<PricePillsAdapter.ViewHolder>() {

    private var pricePills: List<Option> = ArrayList()

    override fun getItemCount(): Int {
        return pricePills.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent.context).inflate(R.layout.filter_price_pill_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pricePills[position], position)
    }

    fun setPricePills(pricePills: List<Option>) {
        this.pricePills = pricePills
        notifyDataSetChanged()
    }

    fun refreshData() {
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var pricePillItem: TextView? = itemView.findViewById(R.id.price_pill_item)
        var context: Context = itemView.context

        fun bind(pricePillOption: Option, position: Int) {
            val isPricePillSelected = isValueRangeMatch(pricePillOption)
            if (isPricePillSelected) {
                pricePillItem?.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
                pricePillItem?.background = context.resources.getDrawable(R.drawable.filter_price_pill_item_background_selected)
            } else {
                pricePillItem?.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                pricePillItem?.background = context.resources.getDrawable(R.drawable.filter_price_pill_item_background_neutral)
            }
            pricePillItem?.text = pricePillOption.name
            pricePillItem?.setOnClickListener {
                if (!isPricePillSelected) {
                    callback?.onPriceRangeSelected(
                            NumberParseHelper.safeParseInt(pricePillOption.valMin),
                            NumberParseHelper.safeParseInt(pricePillOption.valMax),
                            adapterPosition
                    )
                } else {
                    callback?.onPriceRangeRemoved(
                            NumberParseHelper.safeParseInt(pricePillOption.valMin),
                            NumberParseHelper.safeParseInt(pricePillOption.valMax),
                            adapterPosition
                    )
                }
            }
        }

        private fun isValueRangeMatch(pricePillOption: Option): Boolean {
            if(callback == null) return false
            val valMin = pricePillOption.valMin
            val valMax = pricePillOption.valMax

            return if (TextUtils.isEmpty(valMin) || TextUtils.isEmpty(valMax)) {
                false
            } else NumberParseHelper.safeParseInt(valMin) == callback.currentPriceMin && NumberParseHelper.safeParseInt(valMax) == callback.currentPriceMax

        }
    }

    interface Callback {
        val currentPriceMin: Int
        val currentPriceMax: Int
        fun onPriceRangeSelected(minValue: Int, maxValue: Int, position: Int)
        fun onPriceRangeRemoved(minValue: Int, maxValue: Int, position: Int)
    }
}
