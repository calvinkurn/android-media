package com.tokopedia.hotel.cancellation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationButtonEnum
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitModel
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitParam
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_CANCELLATION_FEE
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_CANCELLATION_INVOICE_ID
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_CANCELLATION_IS_ORDER_NOT_FOUND
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_CANCELLATION_ORDER_AMOUNT
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_CANCELLATION_SUBMIT_PARAM
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_REFUND_AMOUNT
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_cancellation_confirmation.*
import javax.inject.Inject

/**
 * @author by jessica on 08/05/20
 */

class HotelCancellationConfirmationFragment: HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cancellationViewModel: HotelCancellationViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    private lateinit var cancellationSubmitParam: HotelCancellationSubmitParam
    private var invoiceId = ""
    private var orderAmount = ""
    private var cancellationFee = ""
    private var refundAmount = ""

    private var cancellationSubmitData: HotelCancellationSubmitModel? = null
    private var isOrderNotFound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            cancellationSubmitData = it.getParcelable(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA)
            isOrderNotFound = it.getBoolean(EXTRA_HOTEL_CANCELLATION_IS_ORDER_NOT_FOUND, false)

            cancellationSubmitParam = it.getParcelable(EXTRA_HOTEL_CANCELLATION_SUBMIT_PARAM) ?: HotelCancellationSubmitParam()
            invoiceId = it.getString(EXTRA_HOTEL_CANCELLATION_INVOICE_ID) ?: "0"
            orderAmount = it.getString(EXTRA_HOTEL_CANCELLATION_ORDER_AMOUNT) ?: "0"
            cancellationFee = it.getString(EXTRA_HOTEL_CANCELLATION_FEE) ?: "0"
            refundAmount = it.getString(EXTRA_HOTEL_REFUND_AMOUNT) ?: "0"
        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
    }

    override fun onErrorRetryClicked() {
        submitCancellation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (cancellationSubmitData == null) submitCancellation()
        else {
            cancellationSubmitData?.let { initView(it) }
        }
    }

    private fun submitCancellation() {
        showLoadingState()
        (activity as HotelCancellationConfirmationActivity).setPageTitle("")
        cancellationViewModel.submitCancellationData(GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_submit_hotel_cancellation),
                cancellationSubmitParam)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationViewModel.cancellationSubmitData.observe(this, Observer {
            hideLoadingState()
            when (it) {
                is Success -> {
                    initView(it.data)
                }
                is Fail -> { showErrorState(it.throwable) }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_cancellation_confirmation, container, false)


    private fun initView(cancellationSubmitModel: HotelCancellationSubmitModel) {
        hotel_cancellation_confirmation_title.text = cancellationSubmitModel.title
        hotel_cancellation_confirmation_subtitle.text = cancellationSubmitModel.desc

        hotel_cancellation_confirmation_button_layout.removeAllViews()
        for (button in cancellationSubmitModel.actionButton) {
            hotel_cancellation_confirmation_button_layout.addView(getButtonFromType(button))
        }

        if (cancellationSubmitModel.success) {
            hotel_cancellation_confirmation_iv.setImageResource(R.drawable.ic_hotel_cancellation_success)
            (activity as HotelCancellationConfirmationActivity).setPageTitle(getString(R.string.hotel_cancellation_success))
        } else {
            if (isOrderNotFound) hotel_cancellation_confirmation_iv.setImageResource(R.drawable.ic_order_not_found)
            else hotel_cancellation_confirmation_iv.setImageResource(R.drawable.ic_hotel_cancellation_error)
            (activity as HotelCancellationConfirmationActivity).setPageTitle(getString(R.string.hotel_cancellation_failed))
        }

        val status = if (cancellationSubmitModel.success) getString(R.string.hotel_cancellation_success_status) else
            getString(R.string.hotel_cancellation_fail_status)
        trackingHotelUtil.viewCancellationStatus(requireContext(), invoiceId, orderAmount, cancellationFee, refundAmount, status, HOTEL_ORDER_STATUS_RESULT_SCREEN_NAME)
    }

    private fun getButtonFromType(actionButton: HotelCancellationSubmitModel.ActionButton): UnifyButton {
        val button = UnifyButton(requireContext())
        button.buttonType = HotelCancellationButtonEnum.getEnumFromValue(actionButton.buttonType).buttonType
        button.buttonVariant = HotelCancellationButtonEnum.getEnumFromValue(actionButton.buttonType).buttonVariant
        button.buttonSize = UnifyButton.Size.MEDIUM
        button.text = actionButton.label
        button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        button.setMargin(0, 0, 0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1))
        button.setOnClickListener {
            trackingHotelUtil.clickOnCancellationStatusActionButton(requireContext(), actionButton.label, HOTEL_ORDER_STATUS_RESULT_SCREEN_NAME)
            if (actionButton.uri == RETRY_SUBMISSION) {
                submitCancellation()
            } else {
                RouteManager.route(requireContext(), actionButton.uri)
                activity?.finish()
            }
        }
        return button
    }

    companion object {
        const val HOTEL_ORDER_STATUS_RESULT_SCREEN_NAME = "/hotel/ordercancelresult"
        const val RETRY_SUBMISSION = "RETRYSUBMISSION"
        fun getInstance(invoiceId: String, orderAmount: String, cancellationFee: String, refundAmount: String,
                        cancellationSubmitParam: HotelCancellationSubmitParam): HotelCancellationConfirmationFragment =
                HotelCancellationConfirmationFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_HOTEL_CANCELLATION_SUBMIT_PARAM, cancellationSubmitParam)
                        putString(EXTRA_HOTEL_CANCELLATION_INVOICE_ID, invoiceId)
                        putString(EXTRA_HOTEL_CANCELLATION_ORDER_AMOUNT, orderAmount)
                        putString(EXTRA_HOTEL_CANCELLATION_FEE, cancellationFee)
                        putString(EXTRA_HOTEL_REFUND_AMOUNT, refundAmount)
                    }
                }

        fun getInstance(submitModel: HotelCancellationSubmitModel, isOrderNotFound: Boolean) =
                HotelCancellationConfirmationFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA, submitModel)
                    }
                }
    }
}