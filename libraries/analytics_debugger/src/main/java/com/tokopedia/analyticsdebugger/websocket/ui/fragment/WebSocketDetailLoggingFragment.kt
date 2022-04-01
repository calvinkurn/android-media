package com.tokopedia.analyticsdebugger.websocket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
class WebSocketDetailLoggingFragment: Fragment() {

    private lateinit var tvTitle: Typography
    private lateinit var tvDateTime: Typography
    private lateinit var tvChannelID: Typography
    private lateinit var tvGcToken: Typography
    private lateinit var tvMessage: Typography

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_websocket_detail_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setData()
    }

    private fun initView(view: View) {
        tvTitle = view.findViewById(R.id.tv_websocket_detail_log_title)
        tvDateTime = view.findViewById(R.id.tv_websocket_detail_log_date_time)
        tvChannelID = view.findViewById(R.id.tv_websocket_detail_log_channel_id)
        tvGcToken = view.findViewById(R.id.tv_websocket_detail_log_gc_token)
        tvMessage = view.findViewById(R.id.tv_websocket_detail_log_message)
    }

    private fun setData() {
        requireArguments().let {
            tvTitle.text = it.getString(EXTRA_TITLE)
            tvDateTime.text = it.getString(EXTRA_DATE_TIME)
            tvChannelID.text = it.getString(EXTRA_CHANNEL_ID)
            tvGcToken.text = it.getString(EXTRA_GC_TOKEN)
            tvMessage.text = it.getString(EXTRA_MESSAGE)
        }
    }

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DATE_TIME = "EXTRA_DATE_TIME"
        const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        const val EXTRA_GC_TOKEN = "EXTRA_GC_TOKEN"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    }
}