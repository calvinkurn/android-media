package com.tokopedia.contactus.inboxticket2.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.contactus.inboxticket2.view.customview.adapter.ContactUsCustomQuickOptionViewAdapter
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterView

class CustomQuickOptionView : QuickSingleFilterView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initialAdapter() {
        adapterFilter = ContactUsCustomQuickOptionViewAdapter(quickSingleFilterListener())
    }

    override fun isMultipleSelectionAllowed(): Boolean {
        return true
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
    }
}