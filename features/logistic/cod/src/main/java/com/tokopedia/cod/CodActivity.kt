package com.tokopedia.cod

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodActivity : BaseSimpleActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent: Intent = Intent(context, CodActivity::class.java)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        return CodFragment.newInstance()
    }

}