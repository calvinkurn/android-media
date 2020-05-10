package com.tokopedia.changepassword.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.changepassword.view.fragment.ChangePasswordFragment
import com.tokopedia.user.session.UserSession

class ChangePasswordActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChangePasswordFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            ChangePasswordFragment.REQUEST_LOGIN -> {
                var userSession = UserSession(this)
                if (!userSession.isLoggedIn) {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}
