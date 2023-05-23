package com.tokopedia.hotel.orderdetail.presentation.fragment

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.QueryTravelCrossSelling
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.presentation.adapter.TravelCrossSellAdapter
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.common.travel.utils.TrackingCrossSellUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.fragment.HotelBookingFragment
import com.tokopedia.hotel.booking.presentation.widget.HotelBookingBottomSheets
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.QueryHotelOrderDetail
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_ORDER_DETAIL
import com.tokopedia.hotel.databinding.FragmentHotelOrderDetailBinding
import com.tokopedia.hotel.evoucher.presentation.activity.HotelEVoucherActivity
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.data.model.TitleContent
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_CATEGORY
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity.Companion.KEY_ORDER_ID
import com.tokopedia.hotel.orderdetail.presentation.activity.SeeInvoiceActivity
import com.tokopedia.hotel.orderdetail.presentation.adapter.ContactAdapter
import com.tokopedia.hotel.orderdetail.presentation.adapter.TitleTextAdapter
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelContactPhoneBottomSheet
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelRefundBottomSheet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailFragment : HotelBaseFragment(), ContactAdapter.OnClickCallListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var orderDetailViewModel: HotelOrderDetailViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    private var binding by autoClearedNullable<FragmentHotelOrderDetailBinding>()

    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false
    private var isCrossSellingLoaded = false
    private var isOrderDetailLoaded = false

    @Inject
    lateinit var trackingCrossSellUtil: TrackingCrossSellUtil

    lateinit var remoteConfig: RemoteConfig

    private var orderId: String = ""
    private var orderCategory: String = ""

    override fun getScreenName(): String = getString(R.string.hotel_order_detail_title)

    override fun initInjector() = getComponent(HotelOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_ORDER_DETAIL)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            orderDetailViewModel = viewModelProvider.get(HotelOrderDetailViewModel::class.java)
        }

        arguments?.let {
            orderId = it.getString(KEY_ORDER_ID, "")
            orderCategory = it.getString(KEY_ORDER_CATEGORY, "")
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        orderDetailViewModel.orderDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderTransactionDetail(it.data)
                    renderConditionalInfo(it.data)
                    renderCancellationInfo(it.data.hotelTransportDetails)
                    if (it.data.hotelTransportDetails.propertyDetail.isNotEmpty())
                        renderHotelDetail(it.data.hotelTransportDetails.propertyDetail.first())
                    renderGuestDetail(it.data.hotelTransportDetails.guestDetail)
                    renderPaymentDetail(it.data.payMethod, it.data.pricing, it.data.paymentsData)
                    renderFooter(it.data)
                    binding?.loadingState?.visibility = View.GONE
                }
                is Fail -> {
                    showErrorView(it.throwable)
                }
            }
            isOrderDetailLoaded = true
            stopTrace()
        })

        orderDetailViewModel.crossSellData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderCrossSelling(it.data)
                is Fail -> {
                }
            }
            isCrossSellingLoaded = true
            stopTrace()
        })
    }


    override fun onErrorRetryClicked() {
        binding?.containerError?.root?.hide()
        getOrderDetailData()
    }

    fun showErrorView(error: Throwable?){
        binding?.containerError?.root?.visible()
        context?.run {
            binding?.containerError?.globalError?.let {
                ErrorHandlerHotel.getErrorUnify(this, error,
                    { onErrorRetryClicked() }, it
                )
            }
        }
        binding?.loadingState?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_KEY_ORDER_ID) &&
                savedInstanceState.containsKey(SAVED_KEY_ORDER_CATEGORY)) {
            orderId = savedInstanceState.getString(SAVED_KEY_ORDER_ID) ?: ""
            orderCategory = savedInstanceState.getString(SAVED_KEY_ORDER_CATEGORY) ?: ""
        }

        binding?.loadingState?.visibility = View.VISIBLE

        getOrderDetailData()
    }

    private fun getOrderDetailData() {
        if (userSessionInterface.isLoggedIn) {
           orderDetailViewModel.getOrderDetail(
               QueryHotelOrderDetail(),
               QueryTravelCrossSelling(),
               orderId, orderCategory)

        } else RouteManager.route(context, ApplinkConst.LOGIN)
    }

    private fun renderConditionalInfo(hotelOrderDetail: HotelOrderDetail) {
        binding?.let {
            it.layoutOrderDetailTransaction.topConditionalText.visibility = if (hotelOrderDetail.conditionalInfo.title.isNotBlank()) View.VISIBLE else View.GONE
            it.layoutOrderDetailTransaction.topConditionalText.text = hotelOrderDetail.conditionalInfo.title

            it.layoutOrderDetailPayment.bottomConditionalText.visibility = if (hotelOrderDetail.conditionalInfoBottom.title.isNotBlank()) View.VISIBLE else View.GONE
            it.layoutOrderDetailPayment.bottomConditionalText.text = Html.fromHtml(hotelOrderDetail.conditionalInfoBottom.title)
        }
    }

    private fun renderCancellationInfo(hotelTransportDetail: HotelTransportDetail) {

        if (hotelTransportDetail.cancellation.title.isEmpty()) {
            binding?.orderHotelDetail?.refundTicker?.visibility = View.GONE
        } else {
            binding?.orderHotelDetail?.refundTicker?.visibility = View.VISIBLE
            binding?.orderHotelDetail?.refundTicker?.tickerTitle = hotelTransportDetail.cancellation.title
            if (hotelTransportDetail.cancellation.isClickable)
                binding?.orderHotelDetail?.refundTicker?.setHtmlDescription(getString(R.string.hotel_order_detail_refund_ticker, hotelTransportDetail.cancellation.content))
            else binding?.orderHotelDetail?.refundTicker?.setHtmlDescription(hotelTransportDetail.cancellation.content)
            binding?.orderHotelDetail?.refundTicker?.closeButtonVisibility = View.GONE

            binding?.orderHotelDetail?.refundTicker?.setOnClickListener {
                if (hotelTransportDetail.cancellation.isClickable)
                    showRefundInfo(hotelTransportDetail.cancellation.cancellationPolicies)
            }
        }

        if (hotelTransportDetail.contactInfo.isNotEmpty()) {
            binding?.orderHotelDetail?.callHotelLayout?.setOnClickListener { showCallButtonSheet(hotelTransportDetail.contactInfo) }
        } else binding?.orderHotelDetail?.callHotelLayout?.visibility = View.GONE

    }

    private fun renderTransactionDetail(orderDetail: HotelOrderDetail) {

        binding?.layoutOrderDetailTransaction?.transactionStatus?.text = orderDetail.status.statusText
        if (orderDetail.status.textColor.isNotEmpty()){
            binding?.layoutOrderDetailTransaction?.transactionStatus?.setTextColor(Color.parseColor(orderDetail.status.textColor))
        }else{
            binding?.layoutOrderDetailTransaction?.transactionStatus?.setTextColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_GN500_96))
        }

        var transactionDetailAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        binding?.layoutOrderDetailTransaction?.transactionDetailTitleRecyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.layoutOrderDetailTransaction?.transactionDetailTitleRecyclerView?.adapter = transactionDetailAdapter
        for (transactionDetails in orderDetail.title) {
            transactionDetailAdapter.addData(TitleContent(transactionDetails.label, transactionDetails.value))
        }
        transactionDetailAdapter.notifyDataSetChanged()

        if (orderDetail.invoice.invoiceRefNum.isBlank()) {
            binding?.layoutOrderDetailTransaction?.invoiceLayout?.visibility = View.GONE
        } else {
            binding?.layoutOrderDetailTransaction?.invoiceLayout?.visibility = View.VISIBLE
            binding?.layoutOrderDetailTransaction?.invoiceNumber?.text = orderDetail.invoice.invoiceRefNum
            binding?.layoutOrderDetailTransaction?.invoiceSeeButton?.visibility = if (orderDetail.invoice.invoiceUrl.isNotBlank()) View.VISIBLE else View.GONE
            if (orderDetail.invoice.invoiceUrl.isNotBlank()) {
                binding?.layoutOrderDetailTransaction?.invoiceSeeButton?.setOnClickListener {
                    startActivity(
                            SeeInvoiceActivity.newInstance(
                                    requireContext(),
                                    orderDetail.invoice.invoiceUrl,
                                    orderDetail.invoice.invoiceRefNum
                            )
                    )
                }
            }
        }

        if (orderDetail.hotelTransportDetails.isShowEVoucher) {
            binding?.layoutOrderDetailTransaction?.evoucherLayout?.visibility = View.VISIBLE
            binding?.layoutOrderDetailTransaction?.evoucherLayout?.setOnClickListener {
                goToEvoucherPage()
            }
        } else binding?.layoutOrderDetailTransaction?.evoucherLayout?.visibility = View.GONE

    }

    private fun renderCrossSelling(crossSelling: TravelCrossSelling) {
        if (crossSelling.items.isNotEmpty()) {
            trackingCrossSellUtil.crossSellImpression(crossSelling.items)
            binding?.crossSellWidget?.show()
            binding?.crossSellWidget?.buildView(crossSelling)
            binding?.crossSellWidget?.setListener(object : TravelCrossSellAdapter.OnItemClickListener {
                override fun onItemClickListener(item: TravelCrossSelling.Item, position: Int) {
                    trackingCrossSellUtil.crossSellClick(item, position)
                    RouteManager.route(context, item.uri)
                }

            })
        } else binding?.crossSellWidget?.hide()
    }

    private fun goToEvoucherPage() {
        context?.run { startActivity(HotelEVoucherActivity.getCallingIntent(this, orderId)) }
    }

    private fun renderHotelDetail(propertyDetail: HotelTransportDetail.PropertyDetail) {

        if (propertyDetail.bookingKey.content.isNotEmpty()) {
            hideBookingCode(false)
            binding?.orderHotelDetail?.bookingCode?.text = propertyDetail.bookingKey.content
        } else hideBookingCode(true)

        binding?.orderHotelDetail?.hotelName?.text = propertyDetail.propertyInfo.name
        binding?.orderHotelDetail?.hotelAddress?.text = propertyDetail.propertyInfo.address

        if (propertyDetail.room.isNotEmpty()) {
            binding?.orderHotelDetail?.roomName?.text = propertyDetail.room.first().title
            binding?.orderHotelDetail?.roomInfo?.text = propertyDetail.room.first().content

            for (amenity in propertyDetail.room.first().amenities) {
                if (context != null) {
                    val amenityTextView = Typography(requireContext())
                    amenityTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, AMENITY_TEXT_SIZE)
                    amenityTextView.text = amenity.content
                    binding?.orderHotelDetail?.roomAmenities?.addView(amenityTextView)
                }
            }
        }

        if (propertyDetail.specialRequest.content.isNotEmpty()) {
            binding?.orderHotelDetail?.specialRequestRecyclerView?.visibility = View.VISIBLE
            var specialRequestAdapter = TitleTextAdapter(TitleTextAdapter.VERTICAL_LAYOUT)
            binding?.orderHotelDetail?.specialRequestRecyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding?.orderHotelDetail?.specialRequestRecyclerView?.adapter = specialRequestAdapter
            specialRequestAdapter.addData(mutableListOf(propertyDetail.specialRequest))
        } else binding?.orderHotelDetail?.specialRequestRecyclerView?.visibility = View.GONE

        if (propertyDetail.extraInfo.content.isNotBlank()) {
            if (propertyDetail.extraInfo.isClickable) binding?.orderHotelDetail?.specialNotes?.setText(createHyperlinkText(propertyDetail.extraInfo.content,
                    propertyDetail.extraInfo.longDesc, R.string.hotel_order_detail_additional_info), TextView.BufferType.SPANNABLE)
            else binding?.orderHotelDetail?.specialNotes?.text = propertyDetail.extraInfo.content
            binding?.orderHotelDetail?.specialNotes?.visibility = View.VISIBLE
            binding?.orderHotelDetail?.specialNotes?.movementMethod = LinkMovementMethod.getInstance()
        } else binding?.orderHotelDetail?.specialNotes?.visibility = View.GONE

        if (propertyDetail.checkInOut.size >= 2) {
            binding?.orderHotelDetail?.checkinCheckoutDate?.setRoomDatesFormatted(
                    propertyDetail.checkInOut[0].checkInOut.date,
                    propertyDetail.checkInOut[1].checkInOut.date,
                    propertyDetail.stayLength.content)

            binding?.orderHotelDetail?.checkinCheckoutDate?.setRoomCheckTimes(
                    getString(R.string.hotel_order_detail_day_and_time,
                            propertyDetail.checkInOut[0].checkInOut.day, propertyDetail.checkInOut[0].checkInOut.time),
                    getString(R.string.hotel_order_detail_day_and_time,
                            propertyDetail.checkInOut[1].checkInOut.day, propertyDetail.checkInOut[1].checkInOut.time))
        }

        binding?.orderHotelDetail?.seeHotelDetailButton?.setOnClickListener { RouteManager.route(context, propertyDetail.applink) }
    }

    private fun showCallButtonSheet(contactList: List<HotelTransportDetail.ContactInfo>) {
        val bottomSheet = HotelContactPhoneBottomSheet()
        bottomSheet.contactList = contactList
        bottomSheet.listener = this
        activity?.let { bottomSheet.show(it.supportFragmentManager, TAG_CONTACT_INFO) }
    }

    private fun showRefundInfo(cancellationPolicies: List<HotelTransportDetail.Cancellation.CancellationPolicy>) {
        val bottomSheet = HotelRefundBottomSheet()
        bottomSheet.cancellationPolicies = cancellationPolicies
        activity?.let { bottomSheet.show(it.supportFragmentManager, TAG_CANCELLATION_INFO) }
    }

    private fun renderGuestDetail(guestDetail: TitleContent) {
        binding?.orderHotelDetail?.guestDetailNameHint?.text = guestDetail.title
        binding?.orderHotelDetail?.guestDetailName?.text = guestDetail.content
    }

    private fun renderPaymentDetail(payMethod: List<HotelOrderDetail.LabelValue>,
                                    pricing: List<HotelOrderDetail.PaymentData>,
                                    paymentData: List<HotelOrderDetail.PaymentData>) {

        if (payMethod.isEmpty()) {
            binding?.layoutOrderDetailPayment?.paymentInfoRecyclerView?.visibility = View.GONE
            binding?.layoutOrderDetailPayment?.paymentSeperator1?.visibility = View.GONE
        } else {
            var paymentAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LEFT_LAYOUT)
            binding?.layoutOrderDetailPayment?.paymentInfoRecyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding?.layoutOrderDetailPayment?.paymentInfoRecyclerView?.adapter = paymentAdapter
            for (item in payMethod) {
                paymentAdapter.addData(TitleContent(item.label, item.value))
            }
        }

        var faresAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT)
        binding?.layoutOrderDetailPayment?.paymentFaresRecyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.layoutOrderDetailPayment?.paymentFaresRecyclerView?.adapter = faresAdapter
        for (item in pricing) {
            faresAdapter.addData(TitleContent(item.label, item.value))
        }

        var summaryAdapter = TitleTextAdapter(TitleTextAdapter.HORIZONTAL_LAYOUT_ORANGE)
        binding?.layoutOrderDetailPayment?.paymentSummaryRecyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.layoutOrderDetailPayment?.paymentSummaryRecyclerView?.adapter = summaryAdapter
        for (item in paymentData) {
            summaryAdapter.addData(TitleContent(item.label, item.value))
        }
    }

    fun renderFooter(orderDetail: HotelOrderDetail) {

        binding?.orderDetailFooterLayout?.removeAllViews()
        if (orderDetail.contactUs.helpText.isNotBlank() && context != null) {
            val helpLabel = Typography(requireContext())
            helpLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, AMENITY_TEXT_SIZE)
            context?.resources?.let {
                helpLabel.setTextColor(ResourcesCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96, null))
            }

            val spannableString = createHyperlinkText(orderDetail.contactUs.helpText,
                    orderDetail.contactUs.helpUrl)

            helpLabel.highlightColor = Color.TRANSPARENT
            helpLabel.movementMethod = LinkMovementMethod.getInstance()
            helpLabel.setText(spannableString, TextView.BufferType.SPANNABLE)
            helpLabel.gravity = Gravity.CENTER
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            context?.resources?.let {
                params.bottomMargin = it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            }
            helpLabel.layoutParams = params

            binding?.orderDetailFooterLayout?.addView(helpLabel)
        }


        for (button in orderDetail.actionButtons) {
            context?.let {
                val buttonCompat = UnifyButton(it)
                buttonCompat.text = button.label
                buttonCompat.isAllCaps = false
                buttonCompat.buttonSize = UnifyButton.Size.MEDIUM

                if (button.weight == 1) {
                    buttonCompat.background = ContextCompat.getDrawable(it, R.drawable.bg_hotel_rect_rounded_stroke_gray)
                    buttonCompat.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN500))
                } else if (button.weight == 2) {
                    buttonCompat.buttonType = UnifyButton.Type.TRANSACTION
                }
                buttonCompat.setOnClickListener {
                    if (button.uri.isNotBlank()) RouteManager.route(context, button.uri)
                    else if (button.uriWeb.isNotBlank()) RouteManager.route(context, button.uriWeb)
                }
                binding?.orderDetailFooterLayout?.addView(buttonCompat)
            }
        }
    }

    private fun createHyperlinkText(htmlText: String = "", content: String = "", @StringRes resId: Int = 0): SpannableString {

        val text = if (resId == 0) TextHtmlUtils.getTextFromHtml(htmlText) else TextHtmlUtils.getTextFromHtml(getString(resId, htmlText))
        val spannableString = SpannableString(text)
        val hyperlinkIndex = htmlText.toLowerCase().indexOf("<hyperlink>")
        val endIndexOfLink = htmlText.toLowerCase().indexOf("</hyperlink>")
        if (hyperlinkIndex >= 0) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    try {
                        if (resId == 0) RouteManager.route(context, "tokopedia://webview?url=$content")
                        else onImportantNotesClicked(content)
                    } catch (e: Exception) {
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    context?.let {
                        ds.color = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500) // specific color for this link
                    }
                }
            }, hyperlinkIndex, endIndexOfLink - "<hyperlink>".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (resId != 0) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    onImportantNotesClicked(content)
                }
            }, text.indexOf("<font"), text.length - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    private fun onImportantNotesClicked(notes: String) {
        val importantNotesBottomSheets = HotelBookingBottomSheets()
        activity?.let {
            context?.let { ctx ->
                val textView = Typography(ctx)
                textView.text = notes
                importantNotesBottomSheets.setTitle(getString(R.string.hotel_important_info_title))
                importantNotesBottomSheets.addContentView(textView)
                importantNotesBottomSheets.show(it.supportFragmentManager, HotelBookingFragment.TAG_HOTEL_IMPORTANT_NOTES)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelOrderDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }


    private fun hideBookingCode(enableHide: Boolean) {
        binding?.let {
            it.orderHotelDetail.bookingCodeHint.visibility = if (enableHide) View.GONE else View.VISIBLE
            it.orderHotelDetail.bookingCode.visibility = if (enableHide) View.GONE else View.VISIBLE
            it.orderHotelDetail.seperator1.visibility = if (enableHide) View.GONE else View.VISIBLE
        }
    }

    override fun onClickCall(contactNumber: String) {
        val callIntent = Intent(ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$contactNumber")
        startActivity(callIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_KEY_ORDER_ID, orderId)
        outState.putString(SAVED_KEY_ORDER_CATEGORY, orderCategory)
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            if (isOrderDetailLoaded && isCrossSellingLoaded) {
                performanceMonitoring?.stopTrace()
                isTraceStop = true
            }
        }
    }

    companion object {
        fun getInstance(orderId: String, orderCategory: String): HotelOrderDetailFragment =
                HotelOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(KEY_ORDER_ID, orderId)
                        putString(KEY_ORDER_CATEGORY, orderCategory)
                    }
                }

        const val TAG_CONTACT_INFO = "guestContactInfo"
        const val TAG_CANCELLATION_INFO = "cancellationPolicyInfo"

        const val SAVED_KEY_ORDER_ID = "keyOrderId"
        const val SAVED_KEY_ORDER_CATEGORY = "keyOrderCategory"

        const val AMENITY_TEXT_SIZE = 12f
    }
}
