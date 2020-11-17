package com.tokopedia.chatbot.view.customview.csat

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.view.customview.csat.adapter.ChatBotQuickOptionViewAdapter
import com.tokopedia.design.quickfilter.QuickSingleFilterView

class ChatBotQuickOptionViewCsat : QuickSingleFilterView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initialAdapter() {
        adapterFilter = ChatBotQuickOptionViewAdapter(quickSingleFilterListener)
    }

    override fun isMultipleSelectionAllowed(): Boolean {
        return true
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
    }
}
