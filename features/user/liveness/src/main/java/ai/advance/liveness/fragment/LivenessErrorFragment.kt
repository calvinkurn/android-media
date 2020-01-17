package ai.advance.liveness.fragment

import ai.advance.liveness.R
import ai.advance.liveness.activity.LivenessFailedActivity
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import kotlinx.android.synthetic.main.fragment_liveness_error.*

class LivenessErrorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_liveness_error, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews(arguments?.getString("failed_reason_title")?: "",
                arguments?.getString("failed_reason")?: "",
                arguments?.getString("failed_image")?: "")
    }

    private fun initViews(failedReasonTitle: String, failedReason: String, failedImage: String){
        if (activity is LivenessFailedActivity) {
            (activity as LivenessFailedActivity)
                    .updateToolbarTitle(R.string.liveness_title)
        }

        title?.text = failedReasonTitle
        subtitle?.text = failedReason
        main_image?.let {
            ImageHandler.LoadImage(main_image, failedImage)
        }
        button.setOnClickListener {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return LivenessErrorFragment()
        }
    }
}
