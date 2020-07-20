package com.tokopedia.flight.cancellationV2.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationChooseReasonActivity
import com.tokopedia.flight.cancellationV2.di.FlightCancellationComponent
import com.tokopedia.flight.cancellationV2.presentation.activity.FlightCancellationReasonActivity
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.cancellationV2.presentation.viewmodel.FlightCancellationReasonViewModel
import com.tokopedia.flight.common.util.FlightAnalytics
import kotlinx.android.synthetic.main.fragment_flight_cancellation_refundable_step_two.*
import javax.inject.Inject

/**
 * @author by furqan on 17/07/2020
 */
class FlightCancellationReasonFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cancellationReasonViewModel: FlightCancellationReasonViewModel

    override fun getScreenName(): String = FlightAnalytics.Screen.FLIGHT_CANCELLATION_STEP_TWO

    override fun initInjector() {
        getComponent(FlightCancellationComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationReasonViewModel = viewModelProvider.get(FlightCancellationReasonViewModel::class.java)

            arguments?.let {
                cancellationReasonViewModel.cancellationWrapperModel = it.getParcelable(FlightCancellationReasonActivity.EXTRA_CANCELLATION_MODEL)
            }
            if (cancellationReasonViewModel.getAttachments().isEmpty()) {
                cancellationReasonViewModel.buildAttachmentList()
                cancellationReasonViewModel.buildViewAttachmentList(0)
            } else {
                cancellationReasonViewModel.buildViewAttachmentList(0)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_cancellation_refundable_step_two, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_saved_passenger.setOnClickListener {
            startActivityForResult(FlightCancellationChooseReasonActivity.createIntent(requireContext(), cancellationReasonViewModel.selectedReason),
                    REQUEST_CODE_CHOOSE_REASON)
            requireActivity().overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_slide_up_in, com.tokopedia.common.travel.R.anim.travel_anim_stay)

        }
    }

    companion object {

        private const val TAG_DIALOG_FRAGMENT = "TAG_DIALOG_FRAGMENT"

        private const val REQUEST_CODE_IMAGE = 1001
        private const val REQUEST_CODE_CHOOSE_REASON = 1111

        fun newInstance(cancellationWrapperModel: FlightCancellationWrapperModel): FlightCancellationReasonFragment =
                FlightCancellationReasonFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(FlightCancellationReasonActivity.EXTRA_CANCELLATION_MODEL, cancellationWrapperModel)
                    }
                }
    }
}