package com.tokopedia.logisticorder.view.pod.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticCommon.data.constant.PodConstant
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.DEFAULT_OS_TYPE
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.IMAGE_LARGE_SIZE
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.getDeliveryImage
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.loadImagePod
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.FragmentProofOfDeliveryBinding
import com.tokopedia.logisticorder.view.pod.data.ProofOfDeliveryModel
import com.tokopedia.logisticorder.view.pod.di.DaggerProofOfDeliveryComponent
import com.tokopedia.logisticorder.view.pod.di.ProofOfDeliveryComponent
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * Created by irpan on 28/04/22.
 */
class ProofOfDeliveryFragment : BaseDaggerFragment() {

    companion object {

        private const val ARGUMENTS_IMAGE_ID = "ARGUMENTS_IMAGE_ID"
        private const val ARGUMENTS_ORDER_ID = "ARGUMENTS_ORDER_ID"
        private const val ARGUMENTS_DESCRIPTION = "ARGUMENTS_DESCRIPTION"

        fun createFragment(orderId: Long?, imageId: String?, description: String?): ProofOfDeliveryFragment {
            return ProofOfDeliveryFragment().apply {
                if (orderId != null) {
                    arguments = Bundle().apply {
                        putString(ARGUMENTS_IMAGE_ID, imageId)
                        putLong(ARGUMENTS_ORDER_ID, orderId)
                        putString(ARGUMENTS_DESCRIPTION, description)
                    }
                } else {
                    finishWithToastError()
                }
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentProofOfDeliveryBinding>()

    private var podData: ProofOfDeliveryModel? = null

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {
        val component: ProofOfDeliveryComponent = DaggerProofOfDeliveryComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataInstance()
    }

    private fun initDataInstance() {
        if (arguments != null) {
            val imageId = arguments?.getString(ARGUMENTS_IMAGE_ID)
            val orderId = arguments?.getLong(ARGUMENTS_ORDER_ID)
            val description = arguments?.getString(ARGUMENTS_DESCRIPTION)
            if (imageId != null && orderId != null && description != null) {
                initDataArgs(imageId, orderId, description)
            } else return finishWithToastError()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackButtonListener()
        initImage()
        initTextDescription()
    }

    private fun onBackButtonListener() {
        binding?.buttonBack?.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initDataArgs(imageId: String, orderId: Long, description: String) {
        podData = ProofOfDeliveryModel(imageId, orderId, description)
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): ConstraintLayout? {
        binding = FragmentProofOfDeliveryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun initImage() {
        val pod = podData ?: return finishWithToastError()

        val url = getDeliveryImage(
            pod.imageId,
            pod.orderId,
            IMAGE_LARGE_SIZE,
            userSession.userId,
            DEFAULT_OS_TYPE,
            userSession.deviceId
        )

        binding?.imgProof?.loadImagePod(
            requireContext(),
            userSession.accessToken,
            url,
            requireContext().getDrawable(R.drawable.ic_image_error),
            requireContext().getDrawable(R.drawable.ic_image_error)
        )
    }

    private fun initTextDescription() {
        binding?.run {
            proofDescription.text = podData?.description
        }
    }

    private fun finishWithToastError() {
        requireActivity().setResult(PodConstant.RESULT_FAIL_LOAD_IMAGE)
        requireActivity().finish()
    }

}

