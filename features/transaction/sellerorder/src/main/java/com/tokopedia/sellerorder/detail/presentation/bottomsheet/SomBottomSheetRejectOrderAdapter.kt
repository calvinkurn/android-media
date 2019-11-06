package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import kotlinx.android.synthetic.main.bottomsheet_reject_item.view.*

/**
 * Created by fwidjaja on 2019-10-08.
 */
class SomBottomSheetRejectOrderAdapter(private var listener: ActionListener, private var hasRadioBtn: Boolean, private var hasReasonEditText: Boolean): RecyclerView.Adapter<SomBottomSheetRejectOrderAdapter.ViewHolder>() {
    var mapKey = HashMap<String, String>()

    interface ActionListener {
        fun onBottomSheetItemClick(key: String)
    }

    /*companion object {
        private const val LAYOUT_TEXT_ONLY = 0
        private const val LAYOUT_TEXT_RADIO = 1
        private const val LAYOUT_TEXT_RADIO_WITH_REASON = 2
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_reject_item, parent, false)
        return when (viewType) {
            LAYOUT_TEXT_ONLY -> SomRejectOrderViewHolder(view, hasRadioBtn = false, hasReasonEditText = false)
            LAYOUT_TEXT_RADIO -> SomRejectOrderViewHolder(view, hasRadioBtn = true, hasReasonEditText = false)
            LAYOUT_TEXT_RADIO_WITH_REASON -> SomRejectOrderViewHolder(view, hasRadioBtn = true, hasReasonEditText = true)
            else -> throw IllegalArgumentException("Invalid view type")
        }*/

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_reject_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mapKey.size
    }

    /*override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = listRejectData[position]
        when (holder) {
            is SomRejectOrderViewHolder -> {
                holder.bind(element, position, listener)
            }
        }
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrayValues = mapKey.values.toMutableList()
        val arrayKeys = mapKey.keys.toMutableList()
        holder.itemView.label_reject.text = arrayValues[position]

        if (!hasRadioBtn) {
            holder.itemView.rb_reject.visibility = View.GONE
        } else {
            holder.itemView.rb_reject.visibility = View.VISIBLE
        }

        if (!hasReasonEditText) {
            holder.itemView.reject_reason.visibility = View.GONE
        } else {
            holder.itemView.reject_reason.visibility = View.VISIBLE
            holder.itemView.reject_reason.setPlaceholder(holder.itemView.context.getString(R.string.placeholder_reject_reason))
        }

        holder.itemView.setOnClickListener {
            listener.onBottomSheetItemClick(arrayKeys[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    /*abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int, listener: ActionListener)
    }*/
}