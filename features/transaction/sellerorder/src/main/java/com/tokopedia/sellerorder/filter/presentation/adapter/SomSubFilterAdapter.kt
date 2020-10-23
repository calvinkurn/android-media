package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.model.SomListOrderParam
import com.tokopedia.sellerorder.common.presenter.model.SomSubFilter
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.oldlist.presentation.activity.SomSubFilterActivity
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*
import kotlinx.android.synthetic.main.filter_radio_item.view.*

class SomSubFilterAdapter : RecyclerView.Adapter<SomSubFilterAdapter.BaseViewHolder<*>>(), SomSubFilterActivity.ActionListener {

    var listSubFilter = mutableListOf<SomSubFilter>()
    var currentFilterParam = SomListOrderParam()
    var category: String = ""
    private var allCleared = false
    private var listId: ArrayList<Int> = ArrayList()

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
        return when (listSubFilter[position].typeFilter) {
            SomConsts.FILTER_TYPE_RADIO -> TYPE_RADIO
            SomConsts.FILTER_TYPE_CHECKBOX -> TYPE_CHECKBOX
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return listSubFilter.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = listSubFilter[position]
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
        private val divider = itemView.divider

        override fun bind(item: SomSubFilter, position: Int) {
            titleRadio.text = item.name
            radioBtn.isChecked = item.isChecked

            if (item.isChecked) {
                listId = item.listValue as ArrayList<Int>
            }

            itemView.rb_filter.setOnClickListener(clickHandlerRadio(item))
            itemView.label_radio.setOnClickListener(clickHandlerRadio(item))

            when {
                item.typeView.equals(SomConsts.FILTER_TYPE_SEPARATOR, true) -> {
                    titleRadio.visibility = View.GONE
                    radioBtn.visibility = View.GONE
                    divider.visibility = View.VISIBLE

                }
                item.typeView.equals(SomConsts.FILTER_TYPE_LABEL, true) -> {
                    divider.visibility = View.GONE
                    radioBtn.visibility = View.GONE
                    titleRadio.visibility = View.VISIBLE

                }
                else -> {
                    divider.visibility = View.GONE
                    titleRadio.visibility = View.VISIBLE
                    radioBtn.visibility = View.VISIBLE
                }
            }
        }

        private fun clickHandlerRadio(item: SomSubFilter): (View) -> Unit = {
            listSubFilter.forEach {
                if (it.typeFilter == SomConsts.FILTER_TYPE_RADIO) it.isChecked = false
            }

            item.isChecked = true
            allCleared = false

            listId = listSubFilter[adapterPosition].listValue as ArrayList<Int>
            notifyDataSetChanged()
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
                if (category.equals(SomConsts.CATEGORY_COURIER_TYPE, true)) {
                    if (currentFilterParam.shippingList.isNotEmpty()) {
                        currentFilterParam.shippingList.filter { it == item.id }.map {
                            listId.add(item.id)
                            checkbox.isChecked = it == item.id
                        }
                    }
                } else if (category.equals(SomConsts.CATEGORY_ORDER_TYPE, true)) {
                    if (currentFilterParam.orderTypeList.isNotEmpty()) {
                        currentFilterParam.orderTypeList.filter { it == item.id }.map {
                            listId.add(item.id)
                            checkbox.isChecked = it == item.id
                        }
                    }
                }
            }
        }

        private val clickHandler: (View) -> Unit = {
            val id = listSubFilter[adapterPosition].id
            if (itemView.cb_filter.isChecked) listId.add(id)
            else listId.remove(id)
        }

        init {
            itemView.cb_filter.setOnClickListener(clickHandler)
            itemView.label_checkbox.setOnClickListener(clickHandler)
        }
    }

    override fun onResetClicked() {
        listSubFilter.forEach {
            if (it.key == SomConsts.STATUS_ALL_ORDER) {
                it.isChecked = true
                listId = it.listValue as ArrayList<Int>
            } else {
                it.isChecked = false
            }
        }
        allCleared = true
        notifyDataSetChanged()
    }

    override fun saveSubFilter(): SomListOrderParam {
        when {
            category.equals(SomConsts.CATEGORY_COURIER_TYPE, true) -> currentFilterParam.shippingList = listId
            category.equals(SomConsts.CATEGORY_ORDER_TYPE, true) -> currentFilterParam.orderTypeList = listId
            category.equals(SomConsts.CATEGORY_ORDER_STATUS, true) -> currentFilterParam.statusList = listId
        }
        return currentFilterParam
    }

    override fun saveSubFilterKey(): String {
        return listSubFilter.find { it.isChecked }?.key.orEmpty()
    }

}