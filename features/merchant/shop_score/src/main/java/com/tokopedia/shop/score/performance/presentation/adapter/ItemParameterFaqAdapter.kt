package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemParameterPerformanceShopScoreBinding
import com.tokopedia.shop.score.performance.presentation.model.ItemParameterFaqUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemParameterFaqAdapter :
    RecyclerView.Adapter<ItemParameterFaqAdapter.ItemParameterFaqViewHolder>() {

    private val itemParameterFaqList = mutableListOf<ItemParameterFaqUiModel>()

    fun setParameterFaqList(parameterFaqList: List<ItemParameterFaqUiModel>) {
        if (parameterFaqList.isNullOrEmpty()) return
        itemParameterFaqList.clear()
        itemParameterFaqList.addAll(parameterFaqList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemParameterFaqViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parameter_performance_shop_score, parent, false)
        return ItemParameterFaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemParameterFaqViewHolder, position: Int) {
        val data = itemParameterFaqList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemParameterFaqList.size

    inner class ItemParameterFaqViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemParameterPerformanceShopScoreBinding? by viewBinding()
        fun bind(data: ItemParameterFaqUiModel) {
            binding?.run {
                tvTitleIndicatorParameterPerformance.text =
                    data.title?.let { root.context.getString(it) }.orEmpty()
                tvDescIndicatorParameterPerformance.text = data.desc?.let {
                    MethodChecker.fromHtml(
                        root.context.getString(it)
                    )
                } ?: ""
                tvScoreParameterValue.text = data.score?.let {
                    root.context.getString(it)
                }.orEmpty()
            }
            showDividerVerticalTabletMode()
        }

        private fun showDividerVerticalTabletMode() {
            binding?.run {
                if (DeviceScreenInfo.isTablet(root.context)) {
                    dividerParameterVertical?.isVisible = adapterPosition.isMoreThanZero()
                }
            }
        }
    }
}