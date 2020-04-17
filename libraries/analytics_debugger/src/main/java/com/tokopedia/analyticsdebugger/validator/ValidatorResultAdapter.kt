package com.tokopedia.analyticsdebugger.validator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R

class ValidatorResultAdapter : RecyclerView.Adapter<ValidatorResultAdapter.ResultItemViewHolder>() {

    private val mData: MutableList<Map<String, Any>> by lazy { mutableListOf<Map<String, Any>>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test_case, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mData[position], position)
    }

    fun setData(list: List<Map<String, Any>>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNum: TextView = itemView.findViewById(R.id.tv_number)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        fun bind(map: Map<String, Any>, position: Int) {
            tvNum.text = position.toString()
        }
    }
}