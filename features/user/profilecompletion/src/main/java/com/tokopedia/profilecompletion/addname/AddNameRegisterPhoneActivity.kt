package com.tokopedia.profilecompletion.addname

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.profilecompletion.addname.fragment.AddNameRegisterPhoneCleanViewFragment
import com.tokopedia.profilecompletion.addname.fragment.AddNameRegisterPhoneFragment
import com.tokopedia.remoteconfig.RemoteConfigInstance

/**
 * @author by nisie on 22/04/19.
 * For navigate
 * default      : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_NAME_REGISTER]
 * clean view   : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_NAME_REGISTER_CLEAN_VIEW]
 * Please add Param
 * {@link ApplinkConstInternalGlobal.PARAM_PHONE}
 * {@link ApplinkConstInternalGlobal.PARAM_UUID}
 */
class AddNameRegisterPhoneActivity : BaseSimpleActivity() {

    private var rollanceType = ""

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return checkUri(bundle)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        rollanceType = RemoteConfigInstance.getInstance().abTestPlatform.getString(ROLLANCE_KEY, ROLLANCE_DEFAULT_VALUE)
        super.onCreate(savedInstanceState)
    }

    private fun checkUri(bundle: Bundle): Fragment {
        val uri = intent?.data
        return if (uri?.lastPathSegment?.contains(PATH_CLEAN_VIEW) == true && !rollanceType.contentEquals(ROLLANCE_DEFAULT_VALUE)) {
            AddNameRegisterPhoneCleanViewFragment.createInstance(bundle)
        } else {
            AddNameRegisterPhoneFragment.createInstance(bundle)
        }
    }

    companion object {
        private const val ROLLANCE_KEY = "android_defername"
        private const val ROLLANCE_DEFAULT_VALUE = ""
        private const val PATH_CLEAN_VIEW = "clean-view"
    }
}