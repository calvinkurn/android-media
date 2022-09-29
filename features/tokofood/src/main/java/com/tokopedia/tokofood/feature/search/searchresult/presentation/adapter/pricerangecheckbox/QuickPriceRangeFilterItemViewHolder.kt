package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.pricerangecheckbox

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokofood.databinding.ItemTokofoodSearchPriceRangeBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeFilterCheckboxItemUiModel
import java.lang.StringBuilder

class QuickPriceRangeFilterItemViewHolder(
    private val binding: ItemTokofoodSearchPriceRangeBinding,
    private val quickPriceRangeFilterListener: QuickPriceRangeFilterListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PriceRangeFilterCheckboxItemUiModel, priceRangeSize: Int) {
        with(binding) {
            tvTokofoodPriceRangeDollar.text =
                MethodChecker.fromHtml(getPriceLevelString(item, priceRangeSize))
            tvTokofoodPriceRangeDesc.text = item.option.description
            bindCheckboxPriceRange(item)
        }
    }

    private fun bindCheckboxPriceRange(item: PriceRangeFilterCheckboxItemUiModel) {
        with(binding.checkboxTokofoodPriceRange) {
            setOnCheckedChangeListener(null)
            isChecked = item.isSelected

            binding.root.setOnClickListener {
                isChecked = !isChecked
            }

            setOnCheckedChangeListener { _, isChecked ->
                quickPriceRangeFilterListener.onPriceRangeFilterCheckboxItemClicked(
                    item,
                    isChecked
                )
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
                .substring(STRING_COLOR_INDEX)
            return "#$colorToHexString"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        const val STRING_COLOR_INDEX = 2
    }

}