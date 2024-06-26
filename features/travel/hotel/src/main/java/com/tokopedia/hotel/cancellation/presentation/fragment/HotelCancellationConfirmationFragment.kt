package com.tokopedia.hotel.cancellation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.hotel.databinding.FragmentHotelCancellationConfirmationBinding
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.globalerror.R as globalerrorR

/**
 * @author by jessica on 08/05/20
 */

class HotelCancellationConfirmationFragment: HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cancellationViewModel: HotelCancellationViewModel

    private var binding by autoClearedNullable<FragmentHotelCancellationConfirmationBinding>()

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
            invoiceId = it.getString(EXTRA_HOTEL_CANCELLATION_INVOICE_ID) ?: HOTEL_DEFAULT_AMOUNT_ZERO
            orderAmount = it.getString(EXTRA_HOTEL_CANCELLATION_ORDER_AMOUNT) ?: HOTEL_DEFAULT_AMOUNT_ZERO
            cancellationFee = it.getString(EXTRA_HOTEL_CANCELLATION_FEE) ?: HOTEL_DEFAULT_AMOUNT_ZERO
            refundAmount = it.getString(EXTRA_HOTEL_REFUND_AMOUNT) ?: HOTEL_DEFAULT_AMOUNT_ZERO
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
        binding?.containerError?.root?.hide()
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
        cancellationViewModel.submitCancellationData(cancellationSubmitParam)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationViewModel.cancellationSubmitData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    initView(it.data)
                }
                is Fail -> { showErrorView(it.throwable) }
            }
            hideLoadingState()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelCancellationConfirmationBinding.inflate(inflater,container,false)
        return binding?.root
    }


    private fun initView(cancellationSubmitModel: HotelCancellationSubmitModel) {
        context?.let { context ->
            binding?.hotelCancellationConfirmationTitle?.text = cancellationSubmitModel.title
            binding?.hotelCancellationConfirmationSubtitle?.text = cancellationSubmitModel.desc

            binding?.hotelCancellationConfirmationButtonLayout?.removeAllViews()
            for (button in cancellationSubmitModel.actionButton) {
                binding?.hotelCancellationConfirmationButtonLayout?.addView(getButtonFromType(button))
            }

            if (cancellationSubmitModel.success) {
                (activity as HotelCancellationConfirmationActivity).setPageTitle(context.resources.getString(R.string.hotel_cancellation_success))
            } else {
                if (isOrderNotFound) binding?.hotelCancellationConfirmationIv?.loadImageDrawable(globalerrorR.drawable.unify_globalerrors_404)
                else binding?.hotelCancellationConfirmationIv?.loadImageDrawable(globalerrorR.drawable.unify_globalerrors_500)
                (activity as HotelCancellationConfirmationActivity).setPageTitle(context.resources.getString(R.string.hotel_cancellation_failed))
            }

            val status = if (cancellationSubmitModel.success) context.resources.getString(R.string.hotel_cancellation_success_status) else
                context.resources.getString(R.string.hotel_cancellation_fail_status)
            trackingHotelUtil.viewCancellationStatus(requireContext(), invoiceId, orderAmount, cancellationFee, refundAmount, status, HOTEL_ORDER_STATUS_RESULT_SCREEN_NAME)
        }
    }

    private fun getButtonFromType(actionButton: HotelCancellationSubmitModel.ActionButton): UnifyButton {
        val button = UnifyButton(requireContext())
        button.buttonType = HotelCancellationButtonEnum.getEnumFromValue(actionButton.buttonType).buttonType
        button.buttonVariant = HotelCancellationButtonEnum.getEnumFromValue(actionButton.buttonType).buttonVariant
        button.buttonSize = UnifyButton.Size.MEDIUM
        button.text = actionButton.label
        button.tag = actionButton.label
        button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        context?.let {
            button.setMargin(0, 0, 0, it.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.layout_lvl1))
        }
        button.setOnClickListener {
            trackingHotelUtil.clickOnCancellationStatusActionButton(requireContext(), actionButton.label, HOTEL_ORDER_STATUS_RESULT_SCREEN_NAME)
            if (actionButton.uri == RETRY_SUBMISSION) {
                submitCancellation()
            } else {
                val intent = RouteManager.getIntent(requireContext(), actionButton.uri)
                startActivityForResult(intent, REDIRECT_REQUEST_CODE)
                activity?.finish()
            }
        }
        return button
    }

    fun showErrorView(error: Throwable?){
        hideLoadingState()
        binding?.containerError?.root?.visible()
        context?.run {
            binding?.containerError?.globalError?.let {
                ErrorHandlerHotel.getErrorUnify(this, error,
                    { onErrorRetryClicked() }, it
                )
            }
        }
    }

    private fun showLoadingState() {
        binding?.containerLoadingState?.mainLoading?.show()
    }

    private fun hideLoadingState() {
        binding?.containerLoadingState?.mainLoading?.hide()
    }

    companion object {
        const val HOTEL_ORDER_STATUS_RESULT_SCREEN_NAME = "/hotel/ordercancelresult"
        const val RETRY_SUBMISSION = "RETRYSUBMISSION"
        const val REDIRECT_REQUEST_CODE = 33

        const val HOTEL_DEFAULT_AMOUNT_ZERO = "0"

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
                        putBoolean(EXTRA_HOTEL_CANCELLATION_IS_ORDER_NOT_FOUND, isOrderNotFound)
                    }
                }
    }
}
