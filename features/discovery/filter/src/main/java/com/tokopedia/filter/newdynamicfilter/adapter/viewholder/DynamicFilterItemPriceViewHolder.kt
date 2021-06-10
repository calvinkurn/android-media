package com.tokopedia.filter.newdynamicfilter.adapter.viewholder

import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView

import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.widget.DecimalRangeInputView
import com.tokopedia.design.text.RangeInputView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.NumberParseHelper
import com.tokopedia.filter.newdynamicfilter.adapter.PricePillsAdapter
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.decoration.LinearHorizontalSpacingDecoration
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView

import java.util.ArrayList

class DynamicFilterItemPriceViewHolder(itemView: View, private val dynamicFilterView: DynamicFilterView) : DynamicFilterViewHolder(itemView) {

    private val wholesaleTitle: TextView? = itemView.findViewById(R.id.wholesale_title)
    private val wholesaleToggle: SwitchCompat? = itemView.findViewById(R.id.wholesale_toggle)
    private val wholesaleContainer: View? = itemView.findViewById(R.id.wholesale_container)
    private val priceRangeInputView: DecimalRangeInputView? = itemView.findViewById(R.id.price_range_input_view)
    private var priceMinOption: Option? = null
    private var priceMaxOption: Option? = null
    private var priceMinMaxOption: Option? = null
    private val pricePillsRecyclerView: RecyclerView = itemView.findViewById(R.id.price_pill_recycler_view)
    private var pricePillsAdapter: PricePillsAdapter? = null
    private var minBound = 0
    private var maxBound = 0

    override fun bind(filter: Filter) {
        var maxLabel = ""
        var minLabel = ""

        wholesaleContainer?.visibility = View.GONE

        var lastMinValue = 0
        var lastMaxValue = 0
        val priceRangeList = ArrayList<Option>()

        for (option in filter.options) {
            val optionValue = dynamicFilterView.getFilterValue(option.key)

            if (Option.KEY_PRICE_MIN_MAX_RANGE == option.key) {
                minBound = if (TextUtils.isEmpty(option.valMin)) 0 else Integer.parseInt(option.valMin)
                maxBound = if (TextUtils.isEmpty(option.valMax)) 0 else Integer.parseInt(option.valMax)
                priceMinMaxOption = option
            }

            if (Option.KEY_PRICE_MIN == option.key) {
                minLabel = option.name
                lastMinValue = if (TextUtils.isEmpty(optionValue)) 0 else Integer.parseInt(optionValue)
                priceMinOption = option
            }

            if (Option.KEY_PRICE_MAX == option.key) {
                maxLabel = option.name
                lastMaxValue = if (TextUtils.isEmpty(optionValue)) 0 else Integer.parseInt(optionValue)
                priceMaxOption = option
            }

            if (Option.KEY_PRICE_WHOLESALE == option.key) {
                bindWholesaleOptionItem(option)
            }

            if (option.isPriceRange) {
                priceRangeList.add(option)
            }
        }

        populatePricePills(priceRangeList)

        if (lastMinValue != 0 && lastMinValue > maxBound) {
            maxBound = lastMinValue
        }

        if (lastMaxValue != 0 && lastMaxValue > maxBound) {
            maxBound = lastMaxValue
        }

        if (lastMinValue != 0 && lastMinValue < minBound) {
            minBound = lastMinValue
        }

        if (lastMaxValue != 0 && lastMaxValue < minBound) {
            minBound = lastMaxValue
        }

        val defaultMinValue: Int
        defaultMinValue = if (lastMinValue == 0) {
            minBound
        } else {
            lastMinValue
        }

        val defaultMaxValue: Int
        defaultMaxValue = if (lastMaxValue == 0) {
            maxBound
        } else {
            lastMaxValue
        }

        priceRangeInputView?.setGestureListener(getPriceRangeInputViewGestureListener())

        priceRangeInputView?.setOnValueChangedListener(getPriceRangeInputViewOnValueChangeListener())

        priceRangeInputView?.setData(minLabel, maxLabel, minBound, maxBound,
                defaultMinValue, defaultMaxValue)
    }

    private fun populatePricePills(priceRangeList: List<Option>) {
        if (priceRangeList.isEmpty()) {
            pricePillsRecyclerView.visibility = View.GONE
            return
        }

        pricePillsRecyclerView.visibility = View.VISIBLE

        if (pricePillsRecyclerView.layoutManager == null) {
            pricePillsRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }

        val spacingBetween = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
        val edgeMargin = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)

        if (pricePillsRecyclerView.itemDecorationCount == 0) {
            pricePillsRecyclerView.addItemDecoration(LinearHorizontalSpacingDecoration(spacingBetween, edgeMargin))
        }

        pricePillsAdapter = PricePillsAdapter(object : PricePillsAdapter.Callback {

            override val currentPriceMin: Int
                get() = NumberParseHelper.safeParseInt(dynamicFilterView.getFilterValue(Option.KEY_PRICE_MIN))

            override val currentPriceMax: Int
                get() = NumberParseHelper.safeParseInt(dynamicFilterView.getFilterValue(Option.KEY_PRICE_MAX))

            override fun onPriceRangeSelected(minValue: Int, maxValue: Int, position: Int) {
                FilterTracking.eventClickPricePills(minValue, maxValue, position, true)
                priceRangeInputView?.setData(minBound, maxBound, minValue, maxValue)
                refreshPricePills()
                dynamicFilterView.onPriceRangeClicked()
            }

            override fun onPriceRangeRemoved(minValue: Int, maxValue: Int, position: Int) {
                FilterTracking.eventClickPricePills(minValue, maxValue, position, false)
                priceRangeInputView?.setData(minBound, maxBound, minBound, maxBound)
                refreshPricePills()
                dynamicFilterView.onPriceRangeClicked()
            }
        })
        pricePillsAdapter?.setPricePills(priceRangeList)
        pricePillsRecyclerView.adapter = pricePillsAdapter
    }

    private fun refreshPricePills() {
        pricePillsAdapter?.refreshData()
    }

    private fun getPriceRangeInputViewGestureListener() = object : RangeInputView.GestureListener {
        override fun onButtonRelease(minValue: Int, maxValue: Int) {
            refreshPricePills()
            dynamicFilterView.onPriceSliderRelease(minValue, maxValue)
        }

        override fun onButtonPressed(minValue: Int, maxValue: Int) {
            dynamicFilterView.onPriceSliderPressed(minValue, maxValue)
        }

        override fun onMinValueEditedFromTextInput(minValue: Int) {
            refreshPricePills()
            dynamicFilterView.onMinPriceEditedFromTextInput(minValue)
        }

        override fun onMaxValueEditedFromTextInput(maxValue: Int) {
            refreshPricePills()
            dynamicFilterView.onMaxPriceEditedFromTextInput(maxValue)
        }
    }

    private fun getPriceRangeInputViewOnValueChangeListener()= RangeInputView.OnValueChangedListener { minValue, maxValue, minBound, maxBound ->
        applyMinValueFilter(minValue, minBound)

        applyMaxValueFilter(maxValue, maxBound)
    }

    private fun applyMinValueFilter(minValue: Int, minBound: Int) {
        if (minValue == minBound) {
            dynamicFilterView.removeSavedTextInput(priceMinOption?.uniqueId?: "")
        } else {
            dynamicFilterView.saveTextInput(priceMinOption?.uniqueId?: "", minValue.toString())
        }
    }

    private fun applyMaxValueFilter(maxValue: Int, maxBound: Int) {
        if (maxValue == maxBound) {
            dynamicFilterView.removeSavedTextInput(priceMaxOption!!.uniqueId)
        } else {
            dynamicFilterView.saveTextInput(priceMaxOption!!.uniqueId, maxValue.toString())
        }
    }

    private fun bindWholesaleOptionItem(option: Option) {
        wholesaleContainer?.visibility = View.VISIBLE
        wholesaleTitle?.text = option.name

        wholesaleTitle?.setOnClickListener { wholesaleToggle?.isChecked = wholesaleToggle?.isChecked != true }

        val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked -> dynamicFilterView.saveCheckedState(option, isChecked) }
        wholesaleToggle?.let {
            bindSwitch(it,
                dynamicFilterView.loadLastCheckedState(option),
                onCheckedChangeListener)
        }
    }
}
