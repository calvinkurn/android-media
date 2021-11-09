package com.tokopedia.analyticsdebugger.sse.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELoggingFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sse_logging, container, false)
    }

    companion object {

        @JvmStatic
        val TAG = SSELoggingFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return SSELoggingFragment()
        }
    }
}