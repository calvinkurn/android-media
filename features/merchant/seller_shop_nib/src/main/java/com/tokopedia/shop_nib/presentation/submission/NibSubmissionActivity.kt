package com.tokopedia.shop_nib.presentation.submission

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop_nib.R

class NibSubmissionActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, NibSubmissionActivity::class.java)
            context.startActivity(starter)
        }
    }


    override fun getLayoutRes() = R.layout.ssn_activity_nib_submission
    override fun getParentViewResourceID() = R.id.container
    override fun getNewFragment(): Fragment {
        return NibSubmissionFragment.newInstance()
    }
}

