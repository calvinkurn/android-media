package com.tokopedia.kyc_centralized.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.kyc_centralized.KycConstant.EXTRA_USE_COMPRESSION
import com.tokopedia.kyc_centralized.KycConstant.EXTRA_USE_CROPPING
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationCameraFragment
import com.tokopedia.kyc_centralized.view.fragment.camera.CameraKtpFragment
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

/**
 * @author by alvinatin on 07/11/18.
 */
class UserIdentificationCameraActivity : BaseSimpleActivity() {

    private var viewMode = 0
    private var projectId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.extras != null) {
            viewMode = intent.getIntExtra(EXTRA_VIEW_MODE, 1)
            projectId = intent.getIntExtra(PARAM_PROJECT_ID, -1)
        }
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
    }

    override fun getNewFragment(): Fragment? {
        return if (viewMode == PARAM_VIEW_MODE_KTP && isUseCropAndCompression()) {
            intent.extras?.let { CameraKtpFragment.createInstance(it) }
        } else {
            UserIdentificationCameraFragment.createInstance(viewMode, projectId)
        }
    }

    private fun isUseCropAndCompression(): Boolean {
        return getAbTestPlatform()
                .getString(CROP_AND_COMPRESSION_ROLLOUT)
                .contains(CROP_AND_COMPRESSION_ROLLOUT)
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }

    companion object {
        private const val EXTRA_VIEW_MODE = "view_mode"
        private const val PARAM_VIEW_MODE_KTP = 1
        private const val CROP_AND_COMPRESSION_ROLLOUT = "new_rollout_kyccrop"

        @JvmStatic
        fun createIntent(
            context: Context,
            viewMode: Int,
            projectId: Int,
            useCropping: Boolean = false,
            useCompression: Boolean = false
        ): Intent {
            return Intent(context, UserIdentificationCameraActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putInt(EXTRA_VIEW_MODE, viewMode)
                    putInt(PARAM_PROJECT_ID, projectId)
                    putBoolean(EXTRA_USE_CROPPING, useCropping)
                    putBoolean(EXTRA_USE_COMPRESSION, useCompression)
                })
            }
        }
    }
}
