package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.ItemParameterFaqUiModel
import kotlinx.android.synthetic.main.item_parameter_performance_shop_score.view.*

class ItemParameterFaqAdapter: RecyclerView.Adapter<ItemParameterFaqAdapter.ItemParameterFaqViewHolder>() {

    private val itemParameterFaqList = mutableListOf<ItemParameterFaqUiModel>()

    fun setParameterFaqList(parameterFaqList: List<ItemParameterFaqUiModel>) {
        if (parameterFaqList.isNullOrEmpty()) return
        itemParameterFaqList.clear()
        itemParameterFaqList.addAll(parameterFaqList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemParameterFaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parameter_performance_shop_score, parent, false)
        return ItemParameterFaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemParameterFaqViewHolder, position: Int) {
        val data = itemParameterFaqList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemParameterFaqList.size

    inner class ItemParameterFaqViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(data: ItemParameterFaqUiModel) {
            with(itemView) {
                tv_title_indicator_parameter_performance?.text = data.title
                tv_desc_indicator_parameter_performance?.text = MethodChecker.fromHtml(data.desc)
                tv_score_parameter_value?.text = data.score
            }
        }
    }
}