package com.tokopedia.groupchat.room.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.groupchat.room.view.fragment.PlayWebviewFragment

/**
 * @author by nisie on 12/02/19.
 */
class PlayWebviewActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return PlayWebviewFragment.createInstance(bundle)
    }
}