package com.tokopedia.instantdebitbca

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * Created by nabillasabbaha on 21/03/19.
 */
class InstantDebitBcaActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return InstantDebitBca2Fragment.newInstance();
    }

    companion object {
        fun callingIntent(context: Context) : Intent {
            val intent = Intent(context, InstantDebitBcaActivity::class.java);
            return intent;
        }
    }
}
