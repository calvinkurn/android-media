package com.tokopedia.sellerorder.list.presentation.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_CHECKBOX
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_RADIO
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomSubFilter
import com.tokopedia.sellerorder.list.presentation.activity.SomSubFilterActivity
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*
import kotlinx.android.synthetic.main.filter_radio_item.view.*

/**
 * Created by fwidjaja on 2019-09-17.
 */
class SomSubFilterAdapter(val data: List<SomSubFilter>, val currentFilterParam: SomListOrderParam) : RecyclerView.Adapter<SomSubFilterAdapter.BaseViewHolder<*>>(), SomSubFilterActivity.ActionListener {
    private var allCleared = false
    private var listId: ArrayList<Int> = ArrayList()

    var selectedRadio = -1

    companion object {
        private const val TYPE_RADIO = 0
        private const val TYPE_CHECKBOX = 1
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val context = parent.context
        return when (viewType) {
            TYPE_RADIO -> {
                val view = LayoutInflater.from(context).inflate(R.layout.filter_radio_item, parent, false)
                RadioViewHolder(view)
            }
            TYPE_CHECKBOX -> {
                val view = LayoutInflater.from(context).inflate(R.layout.filter_checkbox_item, parent, false)
                CheckboxViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].typeFilter) {
            FILTER_TYPE_RADIO -> TYPE_RADIO
            FILTER_TYPE_CHECKBOX -> TYPE_CHECKBOX
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        when (holder) {
            is RadioViewHolder -> {
                holder.bind(element, position)
            }
            is CheckboxViewHolder -> {
                holder.bind(element, position)
            }
        }
    }

    inner class RadioViewHolder(itemView: View) : BaseViewHolder<SomSubFilter>(itemView) {
        private val titleRadio = itemView.label_radio
        private val radioBtn = itemView.rb_filter

        override fun bind(item: SomSubFilter, position: Int) {
            titleRadio.text = item.name
            radioBtn.isChecked = position == selectedRadio
        }

        private val clickHandlerRadio: (View) -> Unit = {
            selectedRadio = adapterPosition
            notifyDataSetChanged()
        }

        init {
            itemView.rb_filter.setOnClickListener(clickHandlerRadio)
            itemView.label_radio.setOnClickListener(clickHandlerRadio)
        }
    }

    inner class CheckboxViewHolder(itemView: View) : BaseViewHolder<SomSubFilter>(itemView) {
        private val titleCheckbox = itemView.label_checkbox
        private val checkbox = itemView.cb_filter

        override fun bind(item: SomSubFilter, position: Int) {
            titleCheckbox.text = item.name
            if (allCleared) {
                listId.clear()
                checkbox.isChecked = false
            } else {
                if (currentFilterParam.shippingList.isNotEmpty()) {
                    currentFilterParam.shippingList.filter { it == item.id }.map {
                        listId.add(item.id)
                        checkbox.isChecked = it == item.id
                    }
                }
            }
        }

        private val clickHandler: (View) -> Unit = {
            // selectedPosition = adapterPosition
            // notifyDataSetChanged()
            println("++ index = $position")
            val id = data[position].id
            if (itemView.cb_filter.isChecked) listId.add(id)
            else listId.remove(id)
        }

        init {
            itemView.cb_filter.setOnClickListener(clickHandler)
            itemView.label_checkbox.setOnClickListener(clickHandler)
        }
    }

    override fun onResetClicked() {
        allCleared = true
        notifyDataSetChanged()
    }

    override fun saveSubFilter() : SomListOrderParam {
        currentFilterParam.shippingList = listId
        return currentFilterParam
    }

}