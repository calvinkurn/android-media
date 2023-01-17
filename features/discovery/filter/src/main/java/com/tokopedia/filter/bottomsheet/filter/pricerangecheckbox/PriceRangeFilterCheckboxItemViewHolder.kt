package com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.databinding.FilterPriceRangeItemBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import java.lang.StringBuilder

internal class PriceRangeFilterCheckboxItemViewHolder(
    private val binding: FilterPriceRangeItemBinding,
    private val priceRangeFilterCheckboxListener: PriceRangeFilterCheckboxListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        priceRangeFilterCheckboxDataView: PriceRangeFilterCheckboxDataView,
        item: OptionViewModel,
        priceRangeSize: Int
    ) {
        with(binding) {
            tvPriceRangeDollar.text =
                MethodChecker.fromHtml(getPriceLevelString(item, priceRangeSize))
            tvPriceRangeDesc.text = item.option.description
            bindCheckboxPriceRange(priceRangeFilterCheckboxDataView, item)
        }
    }

    private fun bindCheckboxPriceRange(
        priceRangeFilterCheckboxDataView: PriceRangeFilterCheckboxDataView,
        item: OptionViewModel
    ) {
        with(binding.cbPriceRange) {
            setOnCheckedChangeListener(null)
            isChecked = item.isSelected

            binding.root.setOnClickListener {
                isChecked = !isChecked
            }

            setOnCheckedChangeListener { _, _ ->
                priceRangeFilterCheckboxListener.onPriceRangeFilterCheckboxItemClicked(
                    priceRangeFilterCheckboxDataView,
                    item
                )
            }
        }
    }

    private fun getPriceLevelString(
        item: OptionViewModel,
        priceRangeSize: Int
    ): String {
        val priceBuilder = StringBuilder()
        val option = item.option
        val priceRangeValue = option.value.toIntSafely()
        val color = getColorHexString(com.tokopedia.unifyprinciples.R.color.Unify_NN950)

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

    private fun getColorHexString(idColor: Int): String {
        return try {
            val colorHexInt = ContextCompat.getColor(itemView.context, idColor)
            val colorToHexString = Integer.toHexString(colorHexInt).uppercase()
                .substring(PriceRangeFilterCheckboxViewHolder.STRING_COLOR_INDEX)
            return "#$colorToHexString"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}