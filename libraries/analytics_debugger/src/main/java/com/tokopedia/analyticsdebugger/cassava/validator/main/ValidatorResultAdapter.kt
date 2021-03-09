package com.tokopedia.analyticsdebugger.cassava.validator.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.cassava.validator.core.Status
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator

class ValidatorResultAdapter(private val listener: (Validator) -> Unit)
    : RecyclerView.Adapter<ValidatorResultAdapter.ResultItemViewHolder>() {

    private val mData: MutableList<Validator> by lazy { mutableListOf<Validator>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test_case, parent, false)
        return ResultItemViewHolder(view).apply {
            itemView.setOnClickListener { listener.invoke(mData[adapterPosition]) }
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mData[position], position)
    }

    fun setData(list: List<Validator>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNum: TextView = itemView.findViewById(R.id.tv_number)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val ivStatus: ImageView = itemView.findViewById(R.id.iv_status)

        fun bind(dataItem: Validator, position: Int) {
            tvNum.text = position.toString()
            tvTitle.text = dataItem.name
            val resId = when (dataItem.status) {
                Status.SUCCESS -> R.drawable.ic_done_green_24dp
                Status.FAILURE -> R.drawable.ic_close_red_24dp
                Status.PENDING -> R.drawable.ic_block_yellow_24dp
            }
            ivStatus.setImageResource(resId)
        }
    }
}