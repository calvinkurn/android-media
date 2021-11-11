package com.tokopedia.analyticsdebugger.sse.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
class SSELogViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private val tvSSELogTitle = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_title)
    private val tvSSELogDateTime = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_date_time)
    private val tvSSELogChannelId = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_channel_id)
    private val tvSSELogPageSource = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_page_source)
    private val tvSSELogGcToken = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_gc_token)
    private val tvSSELogData = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_data)

    fun bind(data: SSELogUiModel) {
        tvSSELogTitle.text = data.event
        tvSSELogChannelId.text = data.generalInfo.channelId
        tvSSELogPageSource.text = data.generalInfo.pageSource
        tvSSELogGcToken.text = data.generalInfo.gcToken
        tvSSELogData.text = data.message
        tvSSELogDateTime.text = data.dateTime

        if(data.message.isEmpty()) tvSSELogData.hide()
        else tvSSELogData.visible()
    }
}