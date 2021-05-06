package com.tokopedia.profilecompletion.changepin.view.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changepin.view.fragment.ChangePinFragment
import com.tokopedia.profilecompletion.changepin.view.fragment.ResetPinFragment
import com.tokopedia.profilecompletion.changepin.view.fragment.ForgotPinFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule

//
// Created by Yoris Prayogo on 2019-10-22.
//

class ChangePinActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {
    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return if(intent.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, false) == true) {
            ResetPinFragment.createInstance(bundle)
        } else {
            ChangePinFragment.createInstance(bundle)
        }
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeyboardHandler.hideSoftKeyboard(this)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById(toolbarResourceID)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_back_toolbar_profile_completion)
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(MethodChecker.getColor(this@ChangePinActivity, com.tokopedia.unifyprinciples.R.color.Unify_N0)))
        }
    }

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
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

    private fun doFragmentTransaction(fragment: Fragment, tag: String, isBackAnimation: Boolean) {
        supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragmentTransactionManager = supportFragmentManager.beginTransaction()

        fragmentTransactionManager.add(parentViewResourceID, fragment, tag)
        if (isBackAnimation)
            fragmentTransactionManager.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
        else fragmentTransactionManager.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_left)
        fragmentTransactionManager.addToBackStack(BACK_STACK_ROOT_TAG)
        fragmentTransactionManager.commit()
    }

    fun goToForgotPin(bundle: Bundle) {
        val fragment = ForgotPinFragment.createInstance(bundle)
        doFragmentTransaction(fragment, TAG_FORGOT_PIN, false)
    }

    companion object {
        private const val BACK_STACK_ROOT_TAG = "root_fragment"

        private const val TAG_FORGOT_PIN = "forgotPin"
    }
}