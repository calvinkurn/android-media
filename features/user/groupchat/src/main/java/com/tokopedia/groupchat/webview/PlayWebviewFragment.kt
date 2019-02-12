package com.tokopedia.groupchat.webview

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString

/**
 * @author by nisie on 12/02/19.
 */
class PlayWebviewFragment : Fragment(){

    var url : String = ""
    var hasTitlebar : Boolean = false
    var gcToken : String = ""

    fun createInstance(bundle : Bundle): PlayWebviewFragment {
        val fragment = PlayWebviewFragment()
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel(savedInstanceState)

    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run{
            hasTitlebar = getParamBoolean(ApplinkConst.Play.PARAM_HAS_TITLEBAR, arguments,
                    savedInstanceState, true)

            url = getParamString(ApplinkConst.Play.PARAM_URL, arguments,
                    savedInstanceState, "")

            if(url.isBlank())
                finish()

            gcToken = "123"
        }
    }
}