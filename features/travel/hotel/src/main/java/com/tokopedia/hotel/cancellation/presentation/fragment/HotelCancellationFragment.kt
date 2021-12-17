package com.tokopedia.hotel.cancellation.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationButtonEnum
import com.tokopedia.hotel.cancellation.data.HotelCancellationError
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationActivity
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.hotel.cancellation.presentation.widget.HotelCancellationRefundDetailWidget
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.HotelTextHyperlinkUtil
import com.tokopedia.hotel.databinding.FragmentHotelCancellationBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationFragment : HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var cancellationViewModel: HotelCancellationViewModel
    private var binding by autoClearedNullable<FragmentHotelCancellationBinding>()

    private val cancelInfoBottomSheet = BottomSheetUnify()

    private var invoiceId: String = ""

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            invoiceId = it.getString(EXTRA_INVOICE_ID) ?: ""
        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelCancellationBinding.inflate(inflater,container, false)
        return binding?.root
    }


    override fun onErrorRetryClicked() {
        hideErrorContainer()

        showLoadingState()
        getCancellationData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (cancellationViewModel.cancellationData.value == null) showLoadingState()
        getCancellationData()
    }

    private fun getCancellationData() {
        cancellationViewModel.getCancellationData(invoiceId)
    }

    override fun onResume() {
        super.onResume()
        (activity as HotelCancellationActivity).updateSubtitle(getString(R.string.hotel_cancellation_page_1_subtitle))
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationViewModel.cancellationData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    initView(it.data)
                    hideLoadingState()
                }
                is Fail -> {
                    convertDynamicError(it.throwable)
                }
            }
        })
    }

    fun showErrorView(error: Throwable?){
        context?.run {
            binding?.containerError?.globalError?.let {
                ErrorHandlerHotel.getErrorUnify(this, error,
                    { onErrorRetryClicked() }, it
                )
            }
        }
    }

    private fun convertDynamicError(error: Throwable){
        hideLoadingState()
        showErrorContainer()
        try {
            val gson = Gson()
            val itemType = object : TypeToken<HotelCancellationError>() {}.type
            val errorData = gson.fromJson<HotelCancellationError>(error.message, itemType)
            context?.run {
                binding?.containerError?.globalError?.let {
                    it.setType(GlobalError.SERVER_ERROR)
                    it.errorTitle.text = errorData.content.title
                    it.errorDescription.text = errorData.content.desc
                    it.errorAction.text = errorData.content.actionButton.firstOrNull()?.label ?: ""
                    (it.errorAction as UnifyButton).buttonType = HotelCancellationButtonEnum
                        .getEnumFromValue(errorData.content.actionButton.firstOrNull()?.buttonType ?: "").buttonType
                    (it.errorAction as UnifyButton).buttonVariant = HotelCancellationButtonEnum
                        .getEnumFromValue(errorData.content.actionButton.firstOrNull()?.buttonType ?: "").buttonVariant
                    it.setActionClickListener {
                        RouteManager.route(this,errorData.content.actionButton.firstOrNull()?.uri)
                    }
                    if(errorData.content.actionButton.lastIndex == 1){
                        it.errorSecondaryAction.show()
                        it.errorSecondaryAction.text = errorData.content.actionButton.lastOrNull()?.label ?: ""
                        (it.errorSecondaryAction as UnifyButton).buttonType = HotelCancellationButtonEnum
                            .getEnumFromValue(errorData.content.actionButton.lastOrNull()?.buttonType ?: "").buttonType
                        (it.errorSecondaryAction as UnifyButton).buttonVariant = HotelCancellationButtonEnum
                            .getEnumFromValue(errorData.content.actionButton.lastOrNull()?.buttonType ?: "").buttonVariant
                        it.setSecondaryActionClickListener {
                            RouteManager.route(this,errorData.content.actionButton.lastOrNull()?.uri)
                        }
                    }else{
                        it.errorSecondaryAction.gone()
                    }
                }
            }
        }catch (throwable: Throwable){
            showErrorView(throwable)
        }
    }

    private fun initView(hotelCancellationModel: HotelCancellationModel) {
        hideErrorContainer()

        hotelCancellationModel.property.let {
            binding?.layoutHotelCancellationSummary?.hotelCancellationPropertyName?.text = it.name
            binding?.layoutHotelCancellationSummary?.hotelCancellationRoomName?.text = it.room.firstOrNull()?.roomName ?: ""
            binding?.layoutHotelCancellationSummary?.hotelCancellationRoomGuestInfo?.text = it.room.firstOrNull()?.roomContent ?: ""

            val checkIn = it.checkInOut.firstOrNull()
                    ?: HotelCancellationModel.PropertyData.CheckInOut()
            val checkOut = if (it.checkInOut.size > 1) it.checkInOut[1] else HotelCancellationModel.PropertyData.CheckInOut()
            binding?.hotelCancellationRoomDurationView?.setViewLabel(checkIn.title, checkOut.title)
            binding?.hotelCancellationRoomDurationView?.setRoomDatesFormatted(checkIn.checkInOut.date, checkOut.checkInOut.date, it.stayLength)
            binding?.hotelCancellationRoomDurationView?.setRoomCheckTimes("${checkIn.checkInOut.day}, ${checkIn.checkInOut.time}",
                    "${checkIn.checkInOut.day}, ${checkOut.checkInOut.time}")

            if (it.isDirectPayment) binding?.layoutHotelCancellationSummary?.contentCancellationPayAtHotel?.hide() else binding?.layoutHotelCancellationSummary?.contentCancellationPayAtHotel?.show()
        }

        hotelCancellationModel.cancelPolicy.let {
            binding?.hotelCancellationPolicyWidget?.initView(it.title, it.policy)
        }

        hotelCancellationModel.cancelInfo.let {
            if (it.desc.isEmpty()) {
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTickerRefundInfo?.hide()
            } else {
                val description = it.desc.replace(getString(R.string.hotel_cancellation_hyperlink_open_tag), getString(R.string.hotel_cancellation_a_hyperlink_open_tag))
                        .replace(getString(R.string.hotel_cancellation_hyperlink_close_tag), getString(R.string.hotel_cancellation_a_hyperlink_close_tag))
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTickerRefundInfo?.setHtmlDescription(description)
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTickerRefundInfo?.isClickable = it.isClickable
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTickerRefundInfo?.tickerShape = Ticker.SHAPE_LOOSE
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTickerRefundInfo?.tickerType = Ticker.TYPE_ANNOUNCEMENT

                if (it.isClickable) {
                    cancelInfoBottomSheet.setTitle(it.longDesc.title)
                    val typography = AppCompatTextView(requireContext())
                    typography.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.body_3))
                    typography.text = TextHtmlUtils.getTextFromHtml(it.longDesc.desc)
                    typography.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
                    typography.setMargin(0, 0, 0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2))
                    typography.setLineSpacing(ADD_LINE_SPACING, MUL_LINE_SPACING)
                    typography.setTextColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    cancelInfoBottomSheet.setChild(typography)

                    binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTickerRefundInfo?.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            fragmentManager?.let { fm -> cancelInfoBottomSheet.show(fm, "") }
                        }

                        override fun onDismiss() {
                            //do nothing
                        }
                    })

                    binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTickerRefundInfo?.setOnClickListener {
                        fragmentManager?.let { fm -> cancelInfoBottomSheet.show(fm, "") }
                    }
                }else{
                    //do nothing
                }
            }
        }

        hotelCancellationModel.payment.let {
            binding?.layoutHotelCancellationRefundDetail?.hotelCancellationPaymentTitle?.text = it.title
            if (it.title.isEmpty()) binding?.layoutHotelCancellationRefundDetail?.hotelCancellationPaymentTitle?.hide() else binding?.layoutHotelCancellationRefundDetail?.hotelCancellationPaymentTitle?.show()

            binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundPriceDetail?.removeAllViews()
            for (paymentDetail in it.detail) {
                val widgetDetail = context?.let { HotelCancellationRefundDetailWidget(it) }
                widgetDetail?.let { widget ->
                    widget.initView(paymentDetail.title, paymentDetail.amount)
                    binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundPriceDetail?.addView(widget)
                }
            }
            if (it.detail.isEmpty()) {
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundPriceDetail?.hide()
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundDetailSeperator?.hide()
            }

            binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTotalPriceRefund?.removeAllViews()
            for (paymentSummary in it.summary) {
                val widgetDetail = context?.let { HotelCancellationRefundDetailWidget(it) }
                widgetDetail?.let { widget ->
                    widget.initView(paymentSummary.title, paymentSummary.amount, true)
                    binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTotalPriceRefund?.addView(widget)
                }
            }
            if (it.summary.isEmpty()) binding?.layoutHotelCancellationRefundDetail?.hotelCancellationTotalPriceRefund?.hide()

            if (it.footer.desc.isNotEmpty()) {
                val spannable = HotelTextHyperlinkUtil.getSpannedFromHtmlString(requireContext(),
                        it.footer.desc, it.footer.links)
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundAdditionalText?.highlightColor = Color.TRANSPARENT
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundAdditionalText?.movementMethod = LinkMovementMethod.getInstance()
                binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundAdditionalText?.setText(spannable, TextView.BufferType.SPANNABLE)
            } else binding?.layoutHotelCancellationRefundDetail?.hotelCancellationRefundAdditionalText?.hide()
        }

        if (hotelCancellationModel.footer.desc.isNotEmpty()) {
            binding?.hotelCancellationPageFooter?.highlightColor = Color.TRANSPARENT
            binding?.hotelCancellationPageFooter?.movementMethod = LinkMovementMethod.getInstance()
            binding?.hotelCancellationPageFooter?.setText(HotelTextHyperlinkUtil.getSpannedFromHtmlString(requireContext(),
                    hotelCancellationModel.footer.desc, hotelCancellationModel.footer.links), TextView.BufferType.SPANNABLE)
        } else binding?.hotelCancellationPageFooter?.hide()

        binding?.hotelCancellationButtonNext?.setOnClickListener {
            trackingHotelUtil.clickNextOnCancellationPage(requireContext(), invoiceId, hotelCancellationModel, HOTEL_CANCELLATION_SCREEN_NAME)
            (activity as HotelCancellationActivity).showCancellationReasonFragment()
        }

        trackingHotelUtil.viewHotelCancellationPage(requireContext(), invoiceId, hotelCancellationModel, HOTEL_CANCELLATION_SCREEN_NAME)
    }

    private fun showErrorContainer(){
        binding?.hotelCancellationContainer?.gone()
        binding?.containerError?.root?.visible()
    }

    private fun hideErrorContainer(){
        binding?.hotelCancellationContainer?.visible()
        binding?.containerError?.root?.gone()
    }
    companion object {
        const val HOTEL_CANCELLATION_SCREEN_NAME = "/hotel/ordercancel"
        const val ADD_LINE_SPACING = 6f
        const val MUL_LINE_SPACING = 1f

        private const val EXTRA_INVOICE_ID = "extra_invoice_id"
        fun getInstance(invoiceId: String): HotelCancellationFragment =
                HotelCancellationFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                    }
                }
    }
}