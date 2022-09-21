package com.tokopedia.tokochat.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.tokochat.view.activity.base.BaseTokoChatActivity
import com.tokopedia.tokochat.view.fragment.TokoChatFragment

open class TokoChatActivity : BaseTokoChatActivity() {

    override fun getNewFragment(): Fragment {
        return TokoChatFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle()
        )
    }
}
