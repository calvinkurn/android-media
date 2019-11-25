package com.tokopedia.filter.newdynamicfilter.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView

import java.util.ArrayList

/**
 * Created by henrypriyono on 8/11/17.
 */

open class DynamicFilterDetailAdapter(protected var filterDetailView: DynamicFilterDetailView) : RecyclerView.Adapter<DynamicFilterDetailViewHolder>() {

    private var list: List<Option> = ArrayList()

    protected open val layout: Int = R.layout.filter_detail_item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicFilterDetailViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return getViewHolder(view)
    }

    protected open fun getViewHolder(view: View): DynamicFilterDetailViewHolder {
        return ViewHolder(view, filterDetailView)
    }

    override fun onBindViewHolder(holder: DynamicFilterDetailViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setOptionList(optionList: List<Option>) {
        list = optionList
        notifyDataSetChanged()
    }

    fun resetAllOptionsInputState() {
        for (option in list) {
            option.inputState = ""
        }
        notifyDataSetChanged()
    }

    private class ViewHolder(itemView: View, filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailViewHolder(itemView, filterDetailView) {

        override fun bind(option: Option) {
            super.bind(option)
            checkBox.text = option.name
        }
    }
}
