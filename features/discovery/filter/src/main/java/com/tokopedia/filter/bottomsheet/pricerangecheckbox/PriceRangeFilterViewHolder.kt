package com.tokopedia.filter.bottomsheet.pricerangecheckbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.filter.R
import com.tokopedia.filter.databinding.FilterPriceRangeItemBinding
import com.tokopedia.filter.databinding.FilterPriceRangeLayoutBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import java.lang.StringBuilder

internal class PriceRangeFilterViewHolder(
    itemView: View,
    private val priceRangeFilterListener: PriceRangeFilterListener
) : AbstractViewHolder<PriceRangeFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.filter_price_range_layout

        const val PRICE_RANGE_LEVEL_1 = 1
        const val PRICE_RANGE_LEVEL_2 = 2
        const val PRICE_RANGE_LEVEL_3 = 3
        const val PRICE_RANGE_LEVEL_4 = 4

        const val STRING_COLOR_INDEX = 2
    }

    private val binding = FilterPriceRangeLayoutBinding.bind(itemView)

    init {
        binding.rvPriceRange.run {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }

    override fun bind(element: PriceRangeFilterUiModel) {
        bindRvFilterPriceRange(element)
        binding.tvPriceRangeLabel.text = element.priceRangeLabel
    }

    private fun bindRvFilterPriceRange(priceRangeFilterUiModel: PriceRangeFilterUiModel) {
        val priceRangeOptionAdapter =
            PriceRangeCbItemAdapter(
                priceRangeFilterUiModel.priceRangeList,
                priceRangeFilterListener
            )
        val removeAndRecycleExistingViews = false
        binding.rvPriceRange.swapAdapter(priceRangeOptionAdapter, removeAndRecycleExistingViews)
    }

    private class PriceRangeCbItemAdapter(
        private val priceRangeFilterList: List<PriceRangeFilterItemUiModel>,
        private val priceRangeFilterListener: PriceRangeFilterListener
    ) : RecyclerView.Adapter<PriceRangeCbItemViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PriceRangeCbItemViewHolder {
            val binding = FilterPriceRangeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PriceRangeCbItemViewHolder(binding, priceRangeFilterListener)
        }

        override fun onBindViewHolder(holder: PriceRangeCbItemViewHolder, position: Int) {
            holder.bind(priceRangeFilterList[position])
        }

        override fun getItemCount(): Int = priceRangeFilterList.size
    }

    private class PriceRangeCbItemViewHolder(
        private val binding: FilterPriceRangeItemBinding,
        private val priceRangeFilterListener: PriceRangeFilterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PriceRangeFilterItemUiModel) {
            with(binding) {
                tvPriceRangeDollar.text = MethodChecker.fromHtml(getPriceLevelString(item))
                tvPriceRangeDesc.text = item.priceRangeDesc
                bindCheckboxPriceRange(item)
            }
        }

        private fun bindCheckboxPriceRange(item: PriceRangeFilterItemUiModel) {
            with(binding.cbPriceRange) {
                setOnCheckedChangeListener(null)
                isChecked = item.isSelected
                skipAnimation()

                setOnCheckedChangeListener { _, isChecked ->
                    priceRangeFilterListener.onPriceRangeItemClicked(item, isChecked)
                }
            }
        }

        private fun getPriceLevelString(item: PriceRangeFilterItemUiModel): String {
            val priceBuilder = StringBuilder()
            val color = "#${Integer.toHexString(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950
                )
            ).substring(STRING_COLOR_INDEX)}"

            priceBuilder.append("<font color=$color>")

            for (i in Int.ZERO..item.priceRangeLevel) {
                priceBuilder.append(item.priceText)
                if (i == (item.priceRangeLevel - Int.ONE)) {
                    priceBuilder.append("</font>")
                }
            }

            return priceBuilder.toString()
        }
    }

}