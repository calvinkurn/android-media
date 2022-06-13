package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.power_merchant.subscribe.view.fragment.FallbackFragment

/**
 * Created by @ilhamsuaib on 09/06/22.
 */

class FallbackActivity : BaseSimpleActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, FallbackActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getNewFragment(): Fragment {
        return FallbackFragment.newInstance()
    }
}