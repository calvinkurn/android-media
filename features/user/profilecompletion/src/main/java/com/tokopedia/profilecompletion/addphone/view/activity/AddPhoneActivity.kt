package com.tokopedia.profilecompletion.addphone.view.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule


/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalGlobal.ADD_PHONE}
 */
class AddPhoneActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        intent?.data?.getQueryParameter(ApplinkConstInternalGlobal.PARAM_PHONE)?.let {
            phone -> bundle.putString(PARAM_PHONE_NUMBER, phone)
        }
        return AddPhoneFragment.createInstance(bundle)
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
                    .build()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    companion object {
        const val PARAM_PHONE_NUMBER = "phone"
    }

}