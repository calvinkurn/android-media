package com.tokopedia.notifcenter.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.notifcenter.presentation.fragment.NotificationUpdateFragment

/**
 * Created by faisalramd on 05/02/19.
 */

class NotificationSellerActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return NotificationUpdateFragment()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, NotificationSellerActivity::class.java)
        }
    }
}