package com.tokopedia.managepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.managepassword.common.ManagePasswordConstant

class ManagePasswordWebViewActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ManagePasswordWebViewFragment.instance(bundle)
    }

    companion object {
        fun createIntent(context: Context, url: String): Intent {
            val intent = Intent(context, ManagePasswordWebViewActivity::class.java)
            intent.putExtra(ManagePasswordConstant.KEY_URL, url)
            return intent
        }
    }
}