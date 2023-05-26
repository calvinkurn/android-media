package com.tokopedia.inbox.universalinbox.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxWidgetViewHolder
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel

class UniversalInboxWidgetAdapter(
    private val widgetList: List<UniversalInboxWidgetUiModel>,
    private val listener: UniversalInboxWidgetListener
): RecyclerView.Adapter<UniversalInboxWidgetViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UniversalInboxWidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.universal_inbox_widget_item, parent, false)
        return UniversalInboxWidgetViewHolder(view, listener)
    }

    override fun getItemCount(): Int = widgetList.size

    override fun onBindViewHolder(holder: UniversalInboxWidgetViewHolder, position: Int) {
        holder.bind(widgetList[position])
    }
}
