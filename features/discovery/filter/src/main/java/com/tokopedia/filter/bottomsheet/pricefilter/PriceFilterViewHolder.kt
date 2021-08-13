package com.tokopedia.filter.bottomsheet.pricefilter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.R
import com.tokopedia.filter.common.helper.ChipSpacingItemDecoration
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.utils.text.currency.AfterTextWatcher
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.sort_filter_bottom_sheet_chips_layout.view.*
import kotlinx.android.synthetic.main.sort_filter_bottom_sheet_price_filter_view_holder.view.*

internal class PriceFilterViewHolder(itemView: View, private val priceFilterViewListener: PriceFilterViewListener) : AbstractViewHolder<PriceFilterViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.sort_filter_bottom_sheet_price_filter_view_holder
    }

    private val layoutManager = ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

    private val spacingItemDecoration = ChipSpacingItemDecoration(
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    )

    init {
        itemView.priceFilterMinValue?.addNumberTextChangedListener()
        itemView.priceFilterMaxValue?.addNumberTextChangedListener()
        itemView.priceRangeFilterRecyclerView?.let {
            it.layoutManager = layoutManager
            it.isNestedScrollingEnabled = false
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    private fun TextFieldUnify.addNumberTextChangedListener() {
        textFieldInput.run {
            addTextChangedListener(object: AfterTextWatcher() {
                override fun afterTextChanged(s: Editable) {
                    CurrencyFormatHelper.setToRupiahCheckPrefix(textFieldInput)
                }
            })
        }
    }

    override fun bind(element: PriceFilterViewModel) {
        bindTitle(element)
        bindMinMaxPriceValue(element)
        bindPriceRangeRecyclerView(element)
    }

    private fun bindTitle(data: PriceFilterViewModel) {
        itemView.priceFilterTitle?.text = data.priceFilter.title
    }

    private fun bindMinMaxPriceValue(priceFilterViewModel: PriceFilterViewModel) {
        itemView.priceFilterMinValue?.bindPriceFilterValue(priceFilterViewModel.minPriceFilterTitle, priceFilterViewModel.minPriceFilterValue) {
            priceFilterViewListener.onMinPriceEditedFromTextInput(priceFilterViewModel, it.currencyToInt())
        }

        itemView.priceFilterMaxValue?.bindPriceFilterValue(priceFilterViewModel.maxPriceFilterTitle, priceFilterViewModel.maxPriceFilterValue) {
            priceFilterViewListener.onMaxPriceEditedFromTextInput(priceFilterViewModel, it.currencyToInt())
        }
    }

    private fun String.currencyToInt(): Int {
        return replace(".", "")
                .replace(",", "")
                .toIntOrZero()
    }

    private fun TextFieldUnify.bindPriceFilterValue(title: String, value: String, onFocusChange: (String) -> Unit) {
        textFiedlLabelText.text = title

        textFieldInput.run {
            setText(value)
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) onFocusChange(text.toString())
            }
        }
    }

    private fun bindPriceRangeRecyclerView(element: PriceFilterViewModel) {
        itemView.priceRangeFilterRecyclerView?.swapAdapter(PriceRangeOptionAdapter(element, priceFilterViewListener), false)
    }

    private class PriceRangeOptionAdapter(
            val priceFilterViewModel: PriceFilterViewModel,
            val priceFilterViewListener: PriceFilterViewListener
    ): RecyclerView.Adapter<PriceRangeOptionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceRangeOptionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sort_filter_bottom_sheet_chips_layout, parent, false)
            return PriceRangeOptionViewHolder(view, priceFilterViewModel, priceFilterViewListener)
        }

        override fun getItemCount() = priceFilterViewModel.priceRangeOptionViewModelList.size

        override fun onBindViewHolder(holder: PriceRangeOptionViewHolder, position: Int) {
            holder.bind(priceFilterViewModel.priceRangeOptionViewModelList[position])
        }
    }

    private class PriceRangeOptionViewHolder(
            itemView: View,
            private val priceFilterViewModel: PriceFilterViewModel,
            private val priceFilterViewListener: PriceFilterViewListener
    ): RecyclerView.ViewHolder(itemView) {

        fun bind(optionViewModel: PriceOptionViewModel) {
            itemView.sortFilterChipsUnify?.chipText = optionViewModel.option.name
            itemView.sortFilterChipsUnify?.chipType = ChipsUnify.TYPE_NORMAL
            itemView.sortFilterChipsUnify?.chipSize = ChipsUnify.SIZE_MEDIUM
            itemView.sortFilterChipsUnify?.chipType =
                    if (optionViewModel.isSelected) ChipsUnify.TYPE_SELECTED
                    else ChipsUnify.TYPE_NORMAL
            itemView.sortFilterChipsUnify?.setOnClickListener {
                priceFilterViewListener.onPriceRangeClicked(priceFilterViewModel, optionViewModel)
            }
        }
    }
}