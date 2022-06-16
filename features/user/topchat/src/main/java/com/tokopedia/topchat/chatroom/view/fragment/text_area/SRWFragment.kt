package com.tokopedia.topchat.chatroom.view.fragment.text_area

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class SRWFragment: BaseDaggerFragment() {

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.abstraction.R.color.green_400))
    }

    companion object {
        private const val SCREEN_NAME = "SRW_FRAGMENT"
    }
}