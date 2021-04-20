package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import kotlinx.android.synthetic.main.filter_radio_item.view.*


class SomSubFilterRadioButtonAdapter(private val somSubFilterRadioButtonFilterListener: SomSubRadioButtonFilterListener) :
        RecyclerView.Adapter<SomSubFilterRadioButtonAdapter.RadioButtonViewHolder>() {

    private var listSubFilter = mutableListOf<SomFilterChipsUiModel>()
    private var idFilter: String = ""
    private var keyChildFilter: String = ""
    private var idList = mutableListOf<Int>()

    private var somSubFilterChildCheckBoxAdapter: SomSubFilterChildCheckBoxAdapter? = null
    private var rvChildStatus: RecyclerView? = null

    fun getSubFilterList() = listSubFilter

    fun setSubFilterList(newSubFilterList: List<SomFilterChipsUiModel>, idFilter: String) {
        this.idFilter = idFilter
        val callback = SomSubFilterDiffUtil(listSubFilter, newSubFilterList)
        val diffResult = DiffUtil.calculateDiff(callback)
        this.listSubFilter.clear()
        this.listSubFilter.addAll(newSubFilterList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun clickHandlerRadio(item: SomFilterChipsUiModel, position: Int) {
        listSubFilter.filter {
            it.idFilter == SomConsts.FILTER_STATUS_ORDER
        }.mapIndexed { index, somFilterChipsUiModel ->
            if(somFilterChipsUiModel.isSelected) {
                somFilterChipsUiModel.isSelected = false
                notifyItemChanged(index)
            }
        }
        item.isSelected = true
        notifyItemChanged(position)
        if (item.isSelected) {
            this.idList = ArrayList(item.idList)
        }
    }

    fun resetRadioButtonFilter() {
        listSubFilter.mapIndexed { index, filterItemUiModel ->
            if (filterItemUiModel.isSelected) {
                filterItemUiModel.isSelected = false
                notifyItemChanged(index)
                rvChildStatus?.hide()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioButtonViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.filter_radio_item, parent, false)
        return RadioButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioButtonViewHolder, position: Int) {
        holder.bind(listSubFilter[position])
    }

    override fun getItemCount(): Int = listSubFilter.size

    inner class RadioButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            SomSubFilterChildCheckBoxAdapter.SomSubChildFilterListener {

        fun bind(item: SomFilterChipsUiModel) {
            with(itemView) {
                setChildStatusList()
                label_radio.text = item.name
                rb_filter.setOnCheckedChangeListener(null)
                rb_filter.isChecked = item.isSelected
                rb_filter.skipAnimation()
                toggleChildStatus(item, rb_filter.isChecked)

                rb_filter.setOnCheckedChangeListener { _, _ ->
                    clickHandlerRadio(item, adapterPosition)
                    somSubFilterRadioButtonFilterListener.onRadioButtonItemClicked(idList, adapterPosition)
                }
                setOnClickListener {
                    rb_filter.isChecked = !rb_filter.isChecked
                }
            }
        }

        private fun setChildStatusList() {
            with(itemView) {
                rvChildStatus = rvChildFilterStatus
                somSubFilterChildCheckBoxAdapter = SomSubFilterChildCheckBoxAdapter(this@RadioButtonViewHolder)
                rvChildFilterStatus?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = somSubFilterChildCheckBoxAdapter
                }
            }
        }

        private fun toggleChildStatus(item: SomFilterChipsUiModel, isChecked: Boolean) {
            with(itemView) {
                if (item.childStatus.isEmpty()) {
                    rvChildFilterStatus.hide()
                } else if (item.childStatus.isNotEmpty() && isChecked) {
                    val childItem = item.childStatus
                    somSubFilterChildCheckBoxAdapter?.setSubFilterList(childItem, item.key)
                    rvChildFilterStatus.show()
                }
            }
        }

        override fun onCheckboxChildItemClicked(childIdList: List<Int>, childPosition: Int, keyFilter: String, subChildFilterList: List<SomFilterChipsUiModel.ChildStatusUiModel>) {
            idList = childIdList.toMutableList()
            keyChildFilter = keyFilter
            listSubFilter.find { it.key == keyFilter }?.childStatus = subChildFilterList
            somSubFilterRadioButtonFilterListener.onRadioButtonItemClicked(idList, adapterPosition)
        }
    }

    interface SomSubRadioButtonFilterListener {
        fun onRadioButtonItemClicked(idList: List<Int>, position: Int)
    }
}