package com.tokopedia.profilecompletion.newprofilecompletion.view.activity

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.newprofilecompletion.view.fragment.ProfileCompletionFinishedFragment
import com.tokopedia.profilecompletion.newprofilecompletion.view.fragment.ProfileCompletionFragment

/**
 * @author by nisie on 6/19/17.
 */
class ProfileCompletionActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ProfileCompletionFragment.createInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)))
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N500))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
        }
        toolbar.setPadding(toolbar.paddingLeft, toolbar.paddingTop, 30, toolbar.paddingBottom)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.profilecompletion_ic_action_back)
        if (upArrow != null) {
            upArrow.setColorFilter(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N500), PorterDuff.Mode.SRC_ATOP)
            supportActionBar?.setHomeAsUpIndicator(upArrow)
        }
    }

    fun onFinishedForm() {
        val fragment = ProfileCompletionFinishedFragment.createInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(parentViewResourceID, fragment, fragment.javaClass.simpleName)
        fragmentTransaction.commit()
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
                .build()
    }
}