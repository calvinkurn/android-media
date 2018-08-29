package com.tokopedia.talk.inboxtalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.talk.R
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.talk.common.TalkAnalytics

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = InboxTalkFragment()
    }


    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_INBOX_TALK
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_talk, container, false)
    }
}