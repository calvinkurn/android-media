package com.tokopedia.csat_rating.adapter

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterView


class CustomQuickOptionView : QuickSingleFilterView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initialAdapter() {
        this.adapterFilter = CustomQuickOptionViewAdapter(quickSingleFilterListener())
    }

    override fun isMultipleSelectionAllowed(): Boolean {
        return true
    }
}
