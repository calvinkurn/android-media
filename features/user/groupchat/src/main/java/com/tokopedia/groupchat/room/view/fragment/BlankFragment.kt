package com.tokopedia.groupchat.room.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.common.util.TransparentStatusBarHelper

/**
 * @author : Steven 11/02/19
 */

class BlankFragment : BaseDaggerFragment() {
    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

    companion object {
        private const val POST_ID = "{post_id}"
        fun createInstance(bundle: Bundle): BlankFragment {
            val fragment = BlankFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.blank_fragment, container, false)
        TransparentStatusBarHelper.assistActivity(activity)
        return view
    }


}