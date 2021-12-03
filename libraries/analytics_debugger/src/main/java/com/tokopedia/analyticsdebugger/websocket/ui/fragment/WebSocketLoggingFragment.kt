package com.tokopedia.analyticsdebugger.websocket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLoggingFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_websocket_logging, container, false)
    }
}