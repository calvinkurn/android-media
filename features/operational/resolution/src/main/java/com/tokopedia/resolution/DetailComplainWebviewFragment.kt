package com.tokopedia.resolution

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.webview.BaseWebViewFragment

class DetailComplainWebviewFragment : BaseWebViewFragment(){

    companion object {
        fun instance(bundle: Bundle?): Fragment {
            return DetailComplainWebviewFragment().apply {
                arguments = bundle
            }
        }
    }
}