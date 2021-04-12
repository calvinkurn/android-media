package com.tokopedia.kyc_centralized.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationCameraFragment

/**
 * @author by alvinatin on 07/11/18.
 */
class UserIdentificationCameraActivity : BaseSimpleActivity() {
    private var viewMode = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.extras != null) {
            viewMode = intent.getIntExtra(EXTRA_VIEW_MODE, 1)
        }
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
    }

    override fun getNewFragment(): Fragment? {
        return UserIdentificationCameraFragment.createInstance(viewMode)
    }

    companion object {
        private const val EXTRA_VIEW_MODE = "view_mode"
        @JvmStatic
        fun createIntent(context: Context?, viewMode: Int): Intent {
            val intent = Intent(context, UserIdentificationCameraActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(EXTRA_VIEW_MODE, viewMode)
            intent.putExtras(bundle)
            return intent
        }
    }
}