package com.tokopedia.accountprofile.settingprofile.addpin.view.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.accountprofile.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingModule

/**
 * Created by Ade Fulki on 2019-08-30.
 * ade.hadian@tokopedia.com
 */

class PinCompleteActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeyboardHandler.hideSoftKeyboard(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return PinCompleteFragment.createInstance(bundle)
    }

    override fun onBackPressed() {
        if (fragment != null && fragment is PinCompleteFragment) {
            (fragment as PinCompleteFragment).onBackPressed()
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }


    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById(toolbarResourceID)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_close_toolbar_profile_completion)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
            setBackgroundDrawable(
                ColorDrawable(
                    MethodChecker.getColor(
                        this@PinCompleteActivity,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                )
            )
        }
    }

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setWindowFlag(false)
        window.statusBarColor =
            MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setWindowFlag(on: Boolean) {
        val winParams = window.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags =
                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        window.attributes = winParams
    }
}
