package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.model.SomListOrderParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*
import kotlinx.android.synthetic.main.filter_radio_item.view.*

class SomSubFilterAdapter(private val somSubFilterClickListener: SomSubFilterClickListener) : RecyclerView.Adapter<SomSubFilterAdapter.BaseViewHolder<*>>() {

    private var listSubFilter = mutableListOf<SomFilterChipsUiModel>()
    private var currentFilterParam = SomListOrderParam()
    private var idFilter: String = ""
    private var allCleared = false
    private var idList: ArrayList<Int> = ArrayList()

    companion object {
        private const val TYPE_RADIO = 0
        private const val TYPE_CHECKBOX = 1
    }

    fun setSubFilterList(subFilterList: List<SomFilterChipsUiModel>, idFilter: String) {
        this.listSubFilter = subFilterList.toMutableList()
        this.idFilter = idFilter
        notifyDataSetChanged()
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
        return when (listSubFilter[position].idFilter) {
            SomConsts.FILTER_STATUS_ORDER -> TYPE_RADIO
            SomConsts.FILTER_COURIER, SomConsts.FILTER_TYPE_ORDER -> TYPE_CHECKBOX
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return listSubFilter.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is RadioViewHolder -> {
                val element = listSubFilter.filter { it.idFilter == SomConsts.FILTER_STATUS_ORDER }.getOrNull(position)
                        ?: SomFilterChipsUiModel()
                holder.bind(element, position)
            }
            is CheckboxViewHolder -> {
                val element = listSubFilter.filter {
                    it.idFilter == SomConsts.FILTER_COURIER ||
                            it.idFilter == SomConsts.FILTER_TYPE_ORDER
                }.getOrNull(position) ?: SomFilterChipsUiModel()
                holder.bind(element, position)
            }
        }
    }

    inner class RadioViewHolder(itemView: View) : BaseViewHolder<SomFilterChipsUiModel>(itemView) {
        private val titleRadio = itemView.label_radio
        private val radioBtn = itemView.rb_filter

        override fun bind(item: SomFilterChipsUiModel, position: Int) {
            titleRadio.text = item.name
            radioBtn.isChecked = item.isSelected

            if (item.isSelected) {
                idList = ArrayList(item.idList)
            }

            itemView.rb_filter.setOnClickListener(clickHandlerRadio(item))
            itemView.label_radio.setOnClickListener(clickHandlerRadio(item))
        }

        private fun clickHandlerRadio(item: SomFilterChipsUiModel): (View) -> Unit = {
            listSubFilter.forEach {
                if (it.idFilter == SomConsts.FILTER_STATUS_ORDER) it.isSelected = false
            }

            item.isSelected = true
            allCleared = false

            idList = ArrayList(listSubFilter[adapterPosition].idList)
            notifyDataSetChanged()
        }

    }

    inner class CheckboxViewHolder(itemView: View) : BaseViewHolder<SomFilterChipsUiModel>(itemView) {
        private val titleCheckbox = itemView.label_checkbox
        private val checkbox = itemView.cb_filter

        override fun bind(item: SomFilterChipsUiModel, position: Int) {
            titleCheckbox.text = item.name
            checkbox.isChecked = item.isSelected

            itemView.setOnClickListener(clickHandler)
        }

        private val clickHandler: (View) -> Unit = {
            val id = listSubFilter[adapterPosition].id
            if (itemView.cb_filter.isChecked) idList.add(id)
            else idList.remove(id)

            val checkBoxChecked = checkbox.isChecked
            somSubFilterClickListener.onCheckboxItemClicked(checkBoxChecked, idList, adapterPosition)
        }
    }

    interface SomSubFilterClickListener {
        fun onCheckboxItemClicked(checkboxChecked: Boolean, idList: List<Int>, position: Int)
    }

}