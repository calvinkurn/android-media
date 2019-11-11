package com.tokopedia.profilecompletion.addpin.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.addpin.view.fragment.AddPinFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent

/**
 * Created by Ade Fulki on 2019-08-30.
 * ade.hadian@tokopedia.com
 */

class AddPinActivity: BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddPinFragment.createInstance(bundle)
    }

    override fun onBackPressed() {
        if(fragment != null && fragment is AddPinFragment){
            if(!(fragment as AddPinFragment).onBackPressedFromConfirm()) super.onBackPressed()
        }else{
            super.onBackPressed()
        }
    }
}