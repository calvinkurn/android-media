package com.tokopedia.broadcaster.log.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.broadcaster.log.ui.fragment.main.NetworkLogFragment

class NetworkLogActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return NetworkLogFragment()
    }

    companion object {
        fun route(context: Context) {
            context.startActivity(Intent(context, NetworkLogActivity::class.java))
        }
    }

}