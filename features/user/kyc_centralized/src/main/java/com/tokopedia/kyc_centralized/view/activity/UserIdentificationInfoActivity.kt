package com.tokopedia.kyc_centralized.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationInfoFragment
import com.tokopedia.user_identification_common.KYCConstant

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationInfoActivity : BaseSimpleActivity() {
    var isSourceSeller = false
    private var projectId = -1
    private var callbackUrl: String? = null

    interface Listener {
        fun onTrackBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragment != null &&
                fragment is Listener) {
            (fragment as Listener?)?.onTrackBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null && intent.extras != null) {
            isSourceSeller = intent.extras?.getBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER)?: false
        }
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
    }

    override fun getNewFragment(): Fragment? {
        try {
            projectId = intent.data?.getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID).toIntOrZero()
            callbackUrl = intent.data?.getQueryParameter(ApplinkConstInternalGlobal.PARAM_CALL_BACK)
        } catch (ex: NullPointerException) {
            projectId = KYCConstant.STATUS_DEFAULT
        } catch (ex: NumberFormatException) {
            projectId = KYCConstant.STATUS_DEFAULT
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return UserIdentificationInfoFragment.createInstance(isSourceSeller, projectId, callbackUrl)
    }
}