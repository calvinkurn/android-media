package com.tokopedia.filter.bottomsheet.pricerangecheckbox.item

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.PriceRangeFilterCheckboxListener
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.PriceRangeFilterCheckboxViewHolder
import com.tokopedia.filter.databinding.FilterPriceRangeItemBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import java.lang.StringBuilder

class PriceRangeCbItemViewHolder(
    private val binding: FilterPriceRangeItemBinding,
    private val priceRangeFilterCheckboxListener: PriceRangeFilterCheckboxListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PriceRangeFilterCheckboxItemUiModel, priceRangeSize: Int) {
        with(binding) {
            tvPriceRangeDollar.text =
                MethodChecker.fromHtml(getPriceLevelString(item, priceRangeSize))
            tvPriceRangeDesc.text = item.option.description
            bindCheckboxPriceRange(item)
        }
    }

    private fun bindCheckboxPriceRange(item: PriceRangeFilterCheckboxItemUiModel) {
        with(binding.cbPriceRange) {
            setOnCheckedChangeListener(null)
            isChecked = item.isSelected

            binding.root.setOnClickListener {
                isChecked = !isChecked
            }

            setOnCheckedChangeListener { _, isChecked ->
                priceRangeFilterCheckboxListener.onPriceRangeFilterCheckboxItemClicked(item, isChecked)
            }
        }
    }

    private fun getPriceLevelString(
        item: PriceRangeFilterCheckboxItemUiModel,
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
            ).substring(PriceRangeFilterCheckboxViewHolder.STRING_COLOR_INDEX)
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