package com.tokopedia.analyticsdebugger.sse.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
class SSELogViewHolder(private val v: View): RecyclerView.ViewHolder(v) {

    private var clickListener: ((SSELogUiModel) -> Unit)? = null

    private val tvSSELogTitle = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_title)
    private val tvSSELogMessage = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_message)
    private val tvSSELogDateTime = v.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_sse_log_date_time)

    fun bind(data: SSELogUiModel) {
        tvSSELogTitle.text = data.event
        tvSSELogMessage.text = data.message
        tvSSELogDateTime.text = data.dateTime

        v.rootView.setOnClickListener {
            clickListener?.invoke(data)
        }
    }

    fun setOnClickListener(listener: (SSELogUiModel) -> Unit) {
        this.clickListener = listener
    }
}