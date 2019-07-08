package com.tokopedia.profilecompletion.addname

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.profilecompletion.addname.fragment.AddNameRegisterPhoneFragment

/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalGlobal.ADD_NAME_REGISTER}
 * Please add Param  {@link ApplinkConstInternalGlobal.PARAM_PHONE} and
 *  {@link ApplinkConstInternalGlobal.PARAM_UUID}
 */
class AddNameRegisterPhoneActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddNameRegisterPhoneFragment.createInstance(bundle)
    }

}