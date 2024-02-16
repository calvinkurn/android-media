package com.tokopedia.loginregister.forbidden

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * Created by meyta on 2/22/18.
 */
class ForbiddenActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return ForbiddenFragment.createInstance()
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, ForbiddenActivity::class.java)
            context.startActivity(intent)
        }
    }
}