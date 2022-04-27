package com.tokopedia.liveness.view.fragment

import com.tokopedia.liveness.R
import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants
import com.tokopedia.liveness.view.OnBackListener
import com.tokopedia.liveness.view.activity.LivenessFailedActivity
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.liveness.databinding.FragmentLivenessErrorBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class LivenessErrorFragment : BaseDaggerFragment(), OnBackListener {

    @Inject
    lateinit var analytics: LivenessDetectionAnalytics
    private var projectId = ""

    private var viewBinding by autoClearedNullable<FragmentLivenessErrorBinding>()

    private var failedType = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentLivenessErrorBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        failedType = arguments?.getInt(LivenessConstants.ARG_FAILED_TYPE)?: -1
        projectId = arguments?.getString(ApplinkConstInternalGlobal.PARAM_PROJECT_ID).orEmpty()
        initViews()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(LivenessDetectionComponent::class.java).inject(this)
    }

    private fun initViews(){
        if (activity is LivenessFailedActivity) {
            (activity as LivenessFailedActivity)
                    .updateToolbarTitle(R.string.liveness_title)
        }

        when(failedType) {
            LivenessConstants.FAILED_BADNETWORK -> {
                setViews(getString(R.string.liveness_failed_reason_bad_network_title),
                        getString(R.string.liveness_failed_reason_bad_network),
                        LivenessConstants.SCAN_FACE_FAIL_NETWORK)
            }
            LivenessConstants.FAILED_TIMEOUT -> {
                setViews(getString(R.string.liveness_failed_reason_timeout_title),
                        getString(R.string.liveness_failed_reason_timeout),
                        LivenessConstants.SCAN_FACE_FAIL_TIME)
            }
        }

        viewBinding?.button?.setOnClickListener {
            buttonListener()
        }
    }

    private fun setViews(failedReasonTitle: String, failedReason: String, failedImage: String){
        viewBinding?.apply {
            title.text = failedReasonTitle
            subtitle.text = failedReason
            mainImage.let {
                ImageHandler.LoadImage(mainImage, failedImage)
            }
        }
    }

    private fun buttonListener(){
        when(failedType){
            LivenessConstants.FAILED_BADNETWORK -> {
                analytics.eventClickConnectionTimeout(projectId)
            }
            LivenessConstants.FAILED_TIMEOUT -> {
                analytics.eventClickTimeout(projectId)
            }
        }
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun trackOnBackPressed() {
        when(failedType){
            LivenessConstants.FAILED_BADNETWORK -> {
                analytics.eventClickBackConnectionTimeout(projectId)
            }
            LivenessConstants.FAILED_TIMEOUT -> {
                analytics.eventClickBackTimeout(projectId)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): Fragment {
            return LivenessErrorFragment().apply {
                arguments = bundle
            }
        }
    }
}
