package com.tokopedia.liveness.view

import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.Detector.DetectionFailedType.FACEMISSING
import ai.advance.liveness.lib.Detector.DetectionFailedType.TIMEOUT
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.liveness.R
import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import com.tokopedia.liveness.databinding.FragmentRevampLivenessErrorBinding
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class LivenessErrorFragment: BaseDaggerFragment(), OnBackListener {

    @Inject
    lateinit var analytics: LivenessDetectionAnalytics

    private var viewBinding by autoClearedNullable<FragmentRevampLivenessErrorBinding>()

    private var projectId = ""
    private var detectionFailedType: Detector.DetectionFailedType? = null

    override fun getScreenName(): String = "Liveness Error"

    override fun initInjector() {
        getComponent(LivenessDetectionComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getString(PARAM_PROJECT_ID).orEmpty()
            detectionFailedType = it.getSerializable(LivenessConstants.ARG_FAILED_TYPE) as Detector.DetectionFailedType
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentRevampLivenessErrorBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.apply {
            errorLivenessToolbar.apply {
                setTitle(com.tokopedia.liveness.R.string.liveness_title)
                setNavigationOnClickListener {
                    activity?.onBackPressed()
                }
            }

            button.setOnClickListener {
                buttonListener()
            }
        }

        when(detectionFailedType) {
            TIMEOUT -> {
                setViews(getString(R.string.liveness_failed_reason_timeout_title),
                    getString(R.string.liveness_failed_reason_timeout),
                    LivenessConstants.SCAN_FACE_FAIL_TIME)
            }
            FACEMISSING,  -> {
                setViews(getString(R.string.liveness_failed_reason_face_missing_title),
                    getString(R.string.liveness_failed_reason_face_missing_description),
                    LivenessConstants.SCAN_FACE_FAIL_TIME)
            }
            else -> {
                setViews(getString(R.string.liveness_failed_reason_bad_network_title),
                    getString(R.string.liveness_failed_reason_bad_network),
                    LivenessConstants.SCAN_FACE_FAIL_NETWORK)
            }
        }
    }

    private fun buttonListener(){
        when(detectionFailedType){
            TIMEOUT -> {
                analytics.eventClickTimeout(projectId)
            }
            else -> {
                analytics.eventClickConnectionTimeout(projectId)
            }
        }

        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun trackOnBackPressed() {
        when(detectionFailedType) {
            TIMEOUT -> { analytics.eventClickBackTimeout(projectId) }
            else -> {
                analytics.eventClickBackConnectionTimeout(projectId)
            }
        }
    }

    private fun setViews(failedReasonTitle: String, failedReason: String, failedImage: String){
        viewBinding?.apply {
            livenessErrorTitle.text = failedReasonTitle
            livenessErrorSubtitle.text = failedReason
            livenessErrorImage.loadImage(failedImage)
        }
    }

    companion object {
        fun newInstance(bundle: Bundle): Fragment {
            return LivenessErrorFragment().apply {
                arguments = bundle
            }
        }
    }
}
