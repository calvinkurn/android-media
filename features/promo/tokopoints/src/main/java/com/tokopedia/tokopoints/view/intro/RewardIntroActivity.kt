package com.tokopedia.tokopoints.view.intro

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class RewardIntroActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("Rewards")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.design.R.dimen.dp_0)
        }
    }

    override fun getNewFragment(): Fragment {
        return RewardIntroFragment.newInstance(intent.extras)
    }

    companion object {

        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, RewardIntroActivity::class.java)
            intent.putExtras(extras)
            return intent
        }
    }
}