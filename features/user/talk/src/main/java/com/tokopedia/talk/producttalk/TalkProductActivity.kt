package com.tokopedia.talk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.talk.fragment.TalkProductFragment

class TalkProductActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return TalkProductFragment()
    }

    companion object {
        open fun createIntent(context: Context): Intent {
            return Intent(context, TalkProductActivity::class.java)
        }
    }
}