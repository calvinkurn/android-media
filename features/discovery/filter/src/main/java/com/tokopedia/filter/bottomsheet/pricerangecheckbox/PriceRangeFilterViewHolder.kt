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
import com.tokopedia.kotlin.extensions.view.toIntSafely
import java.lang.StringBuilder

internal class PriceRangeFilterViewHolder(
    itemView: View,
    private val priceRangeFilterListener: PriceRangeFilterListener
) : AbstractViewHolder<PriceRangeFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.filter_price_range_layout

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
            holder.bind(priceRangeFilterList[position], priceRangeFilterList.size)
        }

        override fun getItemCount(): Int = priceRangeFilterList.size
    }

    private class PriceRangeCbItemViewHolder(
        private val binding: FilterPriceRangeItemBinding,
        private val priceRangeFilterListener: PriceRangeFilterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PriceRangeFilterItemUiModel, priceRangeSize: Int) {
            with(binding) {
                tvPriceRangeDollar.text =
                    MethodChecker.fromHtml(getPriceLevelString(item, priceRangeSize))
                tvPriceRangeDesc.text = item.option.description
                bindCheckboxPriceRange(item)
            }
        }

        private fun bindCheckboxPriceRange(item: PriceRangeFilterItemUiModel) {
            with(binding.cbPriceRange) {
                setOnCheckedChangeListener(null)
                isChecked = item.isSelected

                binding.root.setOnClickListener {
                    isChecked = !isChecked
                }

                setOnCheckedChangeListener { _, isChecked ->
                    priceRangeFilterListener.onPriceRangeItemClicked(item, isChecked)
                }
            }
        }

        private fun getPriceLevelString(
            item: PriceRangeFilterItemUiModel,
            priceRangeSize: Int
        ): String {
            val priceBuilder = StringBuilder()
            val option = item.option
            val priceRangeValue = option.value.toIntSafely()
            val color = "#${
                Integer.toHexString(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    )
                ).substring(STRING_COLOR_INDEX)
            }"

            priceBuilder.append("<font color=$color>")

            for (i in Int.ZERO..priceRangeValue) {
                priceBuilder.append(option.name)
                if (i == (priceRangeValue - Int.ONE)) {
                    priceBuilder.append("</font>")
                    break
                }
            }

            val priceRangeLevelRegular = priceRangeSize - priceRangeValue
            var i = 0
            while (i < priceRangeLevelRegular) {
                priceBuilder.append(option.name)
                if (i == (priceRangeLevelRegular - Int.ONE)) {
                    break
                }
                i++
            }

            return priceBuilder.toString()
        }
    }

}