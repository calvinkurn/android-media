package com.tokopedia.talk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return InboxTalkFragment()
    }

    companion object {
        open fun createIntent(context: Context): Intent {
            return Intent(context, InboxTalkActivity::class.java)
        }
    }
}
