package ai.advance.liveness.fragment

import ai.advance.liveness.LivenessConstants
import ai.advance.liveness.OnBackListener
import ai.advance.liveness.R
import ai.advance.liveness.activity.LivenessFailedActivity
import ai.advance.liveness.analytics.LivenessDetectionAnalytics
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import kotlinx.android.synthetic.main.fragment_liveness_error.*

class LivenessErrorFragment : Fragment(), OnBackListener {

    private var failedType = -1
    private var projectId = -1
    private lateinit var analytics: LivenessDetectionAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_liveness_error, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        analytics = LivenessDetectionAnalytics().createInstance(projectId)
        failedType = arguments?.getInt("failed_type")?: -1
        initViews()
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

        button.setOnClickListener {
            buttonListener()
        }
    }

    private fun setViews(failedReasonTitle: String, failedReason: String, failedImage: String){
        title?.text = failedReasonTitle
        subtitle?.text = failedReason
        main_image?.let {
            ImageHandler.LoadImage(main_image, failedImage)
        }
    }

    private fun buttonListener(){
        when(failedType){
            LivenessConstants.FAILED_BADNETWORK -> {
                analytics.eventClickConnectionTimeout()
            }
            LivenessConstants.FAILED_TIMEOUT -> {
                analytics.eventClickTimeout()
            }
        }
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun trackOnBackPressed() {
        when(failedType){
            LivenessConstants.FAILED_BADNETWORK -> {
                analytics.eventClickBackConnectionTimeout()
            }
            LivenessConstants.FAILED_TIMEOUT -> {
                analytics.eventClickBackTimeout()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return LivenessErrorFragment()
        }
    }
}
