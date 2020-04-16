package com.tokopedia.analyticsdebugger.validator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_autocomplete_text.view.*

class ValidatorResultAdapter : RecyclerView.Adapter<ValidatorResultAdapter.ResultItemViewHolder>() {

    private val mData: MutableList<Map<String, Any>> by lazy { mutableListOf<Map<String, Any>>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(android.R.id.text1).text = position.toString()
    }

    fun setData(list: List<Map<String, Any>>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    inner class ResultItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}