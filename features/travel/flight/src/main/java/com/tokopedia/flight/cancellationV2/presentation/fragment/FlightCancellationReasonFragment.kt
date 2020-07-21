package com.tokopedia.flight.cancellationV2.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationV2.di.FlightCancellationComponent
import com.tokopedia.flight.cancellationV2.presentation.activity.FlightCancellationChooseReasonActivity
import com.tokopedia.flight.cancellationV2.presentation.activity.FlightCancellationReasonActivity
import com.tokopedia.flight.cancellationV2.presentation.adapter.FlightCancellationAttachmentAdapter
import com.tokopedia.flight.cancellationV2.presentation.adapter.FlightCancellationAttachmentAdapterTypeFactory
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationAttachmentModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.cancellationV2.presentation.viewmodel.FlightCancellationReasonViewModel
import com.tokopedia.flight.common.util.FlightAnalytics
import kotlinx.android.synthetic.main.fragment_flight_cancellation_refundable_step_two.*
import javax.inject.Inject

/**
 * @author by furqan on 17/07/2020
 */
class FlightCancellationReasonFragment : BaseDaggerFragment(),
        FlightCancellationAttachmentAdapterTypeFactory.AdapterInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cancellationReasonViewModel: FlightCancellationReasonViewModel

    private lateinit var adapter: FlightCancellationAttachmentAdapter

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

        buildView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationReasonViewModel.viewAttachmentModelList.observe(viewLifecycleOwner, Observer {
            if (it.size > 0) {
                renderAttachments(it)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_CHOOSE_REASON -> {
                data?.let {
                    cancellationReasonViewModel.selectedReason = it.getParcelableExtra(FlightCancellationChooseReasonFragment.EXTRA_SELECTED_REASON)
                    renderSelectedReason()
//                    setupNextButton()
                }
            }
        }
    }

    override fun onUploadAttachmentButtonClicked(position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewImage(filePath: String) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun buildView() {
        et_saved_passenger.setOnClickListener {
            startActivityForResult(FlightCancellationChooseReasonActivity.getCallingIntent(requireContext(), cancellationReasonViewModel.selectedReason),
                    REQUEST_CODE_CHOOSE_REASON)
            requireActivity().overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_slide_up_in, com.tokopedia.common.travel.R.anim.travel_anim_stay)
        }

        val adapterTypeFactory = FlightCancellationAttachmentAdapterTypeFactory(this, true)
        adapter = FlightCancellationAttachmentAdapter(adapterTypeFactory)
        rv_attachments.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_attachments.setHasFixedSize(true)
        rv_attachments.isNestedScrollingEnabled = false
        rv_attachments.adapter = adapter

        btn_next.setOnClickListener {
            //            onNextButtonClicked()
        }

        buildAttachmentReasonView()
        hideProgressBar()
    }

    private fun showProgressBar() {
        container.visibility = View.GONE
        btn_next.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        container.visibility = View.VISIBLE
        btn_next.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    private fun renderSelectedReason() {
        cancellationReasonViewModel.selectedReason?.let {
            et_saved_passenger.setText(it.title)
            deleteAllAttachments()
            if (it.formattedRequiredDocs.size > 0) {
                cancellationReasonViewModel.buildViewAttachmentList(it.formattedRequiredDocs[0].id.toInt())
            } else {
                cancellationReasonViewModel.buildViewAttachmentList(0)
            }
            buildAttachmentReasonView()
        }
    }

    private fun deleteAllAttachments() {
        adapter.clearAllElements()
    }

    private fun buildAttachmentReasonView() {
        val selectedReason = cancellationReasonViewModel.selectedReason
        val attachments = cancellationReasonViewModel.getAttachments()
        val viewAttachments = cancellationReasonViewModel.viewAttachmentModelList.value
        if (selectedReason != null && selectedReason.formattedRequiredDocs.size > 0 &&
                attachments.isNotEmpty() && viewAttachments != null && viewAttachments.size > 0) {
            showAttachmentContainer()
        } else {
            hideAttachmentContainer()
        }
    }

    private fun showAttachmentContainer() {
        attachment_container.visibility = View.VISIBLE
    }

    private fun hideAttachmentContainer() {
        attachment_container.visibility = View.GONE
    }

    private fun renderAttachments(attachmentList: MutableList<FlightCancellationAttachmentModel>) {
        adapter.addElement(attachmentList)
        adapter.notifyDataSetChanged()
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