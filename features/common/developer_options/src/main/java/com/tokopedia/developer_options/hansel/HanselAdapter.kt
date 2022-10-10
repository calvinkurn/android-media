package com.tokopedia.developer_options.hansel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.hansel.uimodel.HanselUiModel
import com.tokopedia.unifyprinciples.Typography

class HanselAdapter(private val data: List<HanselUiModel>) : RecyclerView.Adapter<HanselAdapter.HanselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HanselViewHolder {
       val inflater = LayoutInflater.from(parent.context).inflate(com.tokopedia.developer_options.R.layout.item_hansel, parent, false)
        return HanselViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: HanselViewHolder, position: Int) {
        val hansel = data[position]
        holder.functionName.text = hansel.functionName
        holder.counter.text = hansel.counter.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class HanselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val functionName = view.findViewById<Typography>(com.tokopedia.developer_options.R.id.tv_function_name)
        val counter = view.findViewById<Typography>(com.tokopedia.developer_options.R.id.tv_counter)
    }
}
