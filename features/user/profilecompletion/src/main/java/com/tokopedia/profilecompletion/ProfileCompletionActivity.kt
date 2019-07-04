package com.tokopedia.profilecompletion

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.profilecompletion.addname.fragment.AddNameRegisterPhoneFragment
import com.tokopedia.profilecompletion.changegender.ChangeGenderFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionComponent

/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalGlobal.PROFILE_COMPLETION}
 * !! Please add Param {@link ApplinkConstInternalGlobal.PARAM_MODE} with values from this class'
 * companion object with prefix MODE
 */
class ProfileCompletionActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionComponent> {

    var mode = MODE_PROFILE_COMPLETION
    var titleString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.extras?.run{
            mode = getString(ApplinkConstInternalGlobal.PARAM_MODE, MODE_PROFILE_COMPLETION)
        }

        setupTitle()
    }

//    private fun setupActionMenu() {
//        var mode = MODE_PROFILE_COMPLETION
//        intent?.extras?.run{
//            mode = getString(ApplinkConstInternalGlobal.PARAM_MODE, MODE_PROFILE_COMPLETION)
//        }
//
//        if(mode != MODE_PROFILE_COMPLETION){
//            //TODO DISABLE ACTION MENU
//        }
//    }

    private fun setupTitle() {

        titleString = when(mode){
            MODE_GENDER_ONLY -> getString(R.string.title_change_gender)
            else -> getString(R.string.title_profile_completion)
        }

        toolbar?.run{
            title = titleString
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        val mode = bundle.getString(ApplinkConstInternalGlobal.PARAM_MODE,MODE_PROFILE_COMPLETION)

        return when(mode){
            MODE_GENDER_ONLY -> ChangeGenderFragment.createInstance(bundle)
            else -> AddNameRegisterPhoneFragment.createInstance(bundle)
        }
    }

    override fun getComponent(): ProfileCompletionComponent {
        return DaggerProfileCompletionComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    companion object{
        val MODE_GENDER_ONLY = "start_gender"
        val MODE_PROFILE_COMPLETION = "start_profile_completion"
    }
}