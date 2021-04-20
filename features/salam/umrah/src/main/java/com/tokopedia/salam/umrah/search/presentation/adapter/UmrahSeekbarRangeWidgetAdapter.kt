package com.tokopedia.salam.umrah.search.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.item_umrah_seekbar_range_number.view.*
import kotlin.math.log10
import kotlin.math.roundToInt
/**
 * @author by M on 18/10/2019
 */
class UmrahSeekbarRangeWidgetAdapter : RecyclerView.Adapter<UmrahSeekbarRangeWidgetAdapter.UmrahSeekbarRangeWidgetViewHolder>() {
    var items: List<Int> = listOf()
    private val selectedItems: MutableSet<Int> = mutableSetOf()
    var halfSpanWidth = 0f
    var eighthSpanWidth = 0f
    var itemWidth = 0
    var listener:SeekbarListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahSeekbarRangeWidgetViewHolder {
        return UmrahSeekbarRangeWidgetViewHolder(parent.inflateLayout(R.layout.item_umrah_seekbar_range_number))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UmrahSeekbarRangeWidgetViewHolder, position: Int) {
        val item = items[position]
        with(holder){
            if(itemWidth == 0) {
                setItemWidth()
            }
            bindItem(item, selectedItems.contains(item))
        }
    }

    fun setSpanWidth(spanWidth: Float){
        halfSpanWidth = spanWidth/2
        eighthSpanWidth = spanWidth/8
    }

    fun updateSelectedItems(selected: Set<Int>? = null){
        selected?.let { selectedItems.clear(); selectedItems.addAll(it) }
        notifyDataSetChanged()
    }

    inner class UmrahSeekbarRangeWidgetViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItem(item: Int, isSelected: Boolean) {
            with(itemView){
                val decimalPoint = (log10(item.toFloat())+1).toInt()
                var marginLeft = halfSpanWidth-(itemWidth.toFloat()/2)-eighthSpanWidth
                if(decimalPoint==1){
                    marginLeft = halfSpanWidth-eighthSpanWidth
                }
                number.apply {
                    setMargin(marginLeft.roundToInt(),0,0,0)
                    text = item.toString()
                }
                if (isSelected){
                    number.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G400))
                } else {
                    number.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
                }
            }
        }
        fun setItemWidth(){
            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            itemWidth = itemView.measuredWidth
            listener?.onItemWidthMeasured(itemWidth)
        }
    }

    interface SeekbarListener{
        fun onItemWidthMeasured(itemWidth: Int)
    }
}