package com.tokopedia.profilecompletion.addpin.view.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.view.fragment.AddPinFragment
import com.tokopedia.profilecompletion.addpin.view.fragment.AddPinFrom2FAFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule

/**
 * Created by Ade Fulki on 2019-08-30.
 * ade.hadian@tokopedia.com
 */

class AddPinActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    var enableBackBtn = true
    var isFrom2FA = false

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        intent?.extras?.run {
            enableBackBtn = getBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, true)
            isFrom2FA = getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_2FA, false)
        }
        super.onCreate(savedInstanceState)
        KeyboardHandler.hideSoftKeyboard(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return if (isFrom2FA) {
            AddPinFrom2FAFragment.createInstance(bundle)
        } else {
            AddPinFragment.createInstance(bundle)
        }
    }

    override fun onBackPressed() {
        if (fragment != null && fragment is AddPinFragment) {
            if (!(fragment as AddPinFrom2FAFragment).onBackPressedFromConfirm()) {
                super.onBackPressed()
            } else if (!(fragment as AddPinFragment).onBackPressedFromConfirm()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById(toolbarResourceID)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            if(enableBackBtn) {
                setHomeAsUpIndicator(R.drawable.ic_back_toolbar_profile_completion)
            }
            setDisplayHomeAsUpEnabled(enableBackBtn)
            setDisplayShowTitleEnabled(false)
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(MethodChecker.getColor(this@AddPinActivity, com.tokopedia.unifyprinciples.R.color.Unify_N0)))
        }
    }

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }

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
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        window.attributes = winParams
    }
}