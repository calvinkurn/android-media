package com.tokopedia.broadcaster.log.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class NetworkLogActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    companion object {
        fun route(context: Context) {}
    }

}