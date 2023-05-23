package com.tokopedia.sellerorder.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemWidgetFilterDateBinding

class FilterSelectDate: RelativeLayout {

    private var _binding: ItemWidgetFilterDateBinding? = null
    private val binding get() = _binding!!

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        _binding = ItemWidgetFilterDateBinding.inflate(LayoutInflater.from(context), this, true)
        binding.tvSelectDateText.setBackgroundResource(R.drawable.bg_som_filter_date_border)
    }

    fun setDateLabel(date: String) {
        binding.tvSelectDateText.text = date
        binding.tvSelectDateText.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
    }

    fun setDateLabelEmpty(date: String) {
        binding.tvSelectDateText.text = date
        binding.tvSelectDateText.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }
}