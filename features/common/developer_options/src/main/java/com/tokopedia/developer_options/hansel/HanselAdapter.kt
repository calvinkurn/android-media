package com.tokopedia.developer_options.hansel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.hansel.uimodel.HanselUiModel
import com.tokopedia.unifyprinciples.Typography

class HanselAdapter(private val data: List<HanselUiModel>) : RecyclerView.Adapter<HanselAdapter.HanselViewHolder>(), Filterable {

    private val listHanselData = mutableListOf<HanselUiModel>().apply {
        addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HanselViewHolder {
       val inflater = LayoutInflater.from(parent.context).inflate(com.tokopedia.developer_options.R.layout.item_hansel, parent, false)
        return HanselViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: HanselViewHolder, position: Int) {
        val hansel = listHanselData[position]
        holder.functionName.text = hansel.functionName
        holder.counter.text = hansel.counter.toString()
        holder.functionId.text = hansel.functionId.toString()
        holder.patchId.text = hansel.patchId.toString()
        holder.patchName.text = hansel.patchName
    }

    override fun getItemCount(): Int {
        return listHanselData.size
    }

    inner class HanselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val functionName = view.findViewById<Typography>(com.tokopedia.developer_options.R.id.tv_function_name)
        val counter = view.findViewById<Typography>(com.tokopedia.developer_options.R.id.tv_counter)
        val functionId = view.findViewById<Typography>(com.tokopedia.developer_options.R.id.tv_function_id)
        val patchId = view.findViewById<Typography>(com.tokopedia.developer_options.R.id.tv_patch_id)
        val patchName = view.findViewById<Typography>(com.tokopedia.developer_options.R.id.tv_patch_name)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(text: CharSequence): FilterResults {
                val listResults = mutableListOf<HanselUiModel>()
                if (text.isEmpty()) {
                    listResults.clear()
                    listResults.addAll(data)
                } else {
                    listResults.clear()
                    val filteredData = data.filter { hanselUiModel ->
                        if (text.toString().toLongOrNull() != null) {
                            hanselUiModel.patchId.toString().contains(text.toString())
                        } else {
                            hanselUiModel.functionName.contains(text.toString())
                                || hanselUiModel.patchName.contains(text.toString())
                        }
                    }.toList()
                    listResults.addAll(filteredData)
                }
                val filterResult = FilterResults()
                filterResult.values = listResults.toList()
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, result: FilterResults) {
                try {
                    val data = result.values as List<HanselUiModel>
                    listHanselData.apply {
                        clear()
                        addAll(data.distinct())
                    }
                    notifyDataSetChanged()
                } catch (e: Exception) {

                }
            }
        }
    }
}
