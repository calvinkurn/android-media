package com.tokopedia.kyc_centralized.ui.tokoKyc.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_CALL_BACK
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_REDIRECT_URL
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycStatus

/**
 * @author by alvinatin on 02/11/18.
 */
class UserIdentificationInfoActivity : BaseSimpleActivity() {
    var isSourceSeller = false
    private var projectId = -1
    private var redirectUrl: String? = null
    private var callback: String? = null
    private var kycType = ""

    interface Listener {
        fun onTrackBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragment != null &&
                fragment is Listener
        ) {
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
        intent?.data?.let {
            projectId = it.getQueryParameter(
                PARAM_PROJECT_ID
            )?.toIntOrZero() ?: KycStatus.DEFAULT.code
            kycType = it.getQueryParameter(PARAM_KYC_TYPE).orEmpty()
            callback = it.getQueryParameter(PARAM_CALL_BACK).orEmpty()
            redirectUrl = it.getQueryParameter(PARAM_REDIRECT_URL).orEmpty()
        }

        if (kycType.isEmpty()) {
            kycType = intent?.extras?.getString(PARAM_KYC_TYPE).orEmpty()
        }

        return UserIdentificationInfoFragment.createInstance(
                isSourceSeller,
                projectId,
                kycType,
                if (callback.isNullOrEmpty()) redirectUrl else callback
        )
    }
}
