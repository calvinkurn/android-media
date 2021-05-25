package com.tokopedia.logisticorder.view

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.adapter.EmptyTrackingNotesAdapter
import com.tokopedia.logisticorder.adapter.TrackingHistoryAdapter
import com.tokopedia.logisticorder.di.DaggerTrackingPageComponent
import com.tokopedia.logisticorder.di.TrackingPageComponent
import com.tokopedia.logisticorder.uimodel.PageModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.utils.DateUtil
import com.tokopedia.logisticorder.view.livetracking.LiveTrackingActivity.Companion.createIntent
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TrackingPageFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var dateUtil: DateUtil
    @Inject
    lateinit var mAnalytics: OrderAnalyticsOrderTracking

    private val viewModel: TrackingPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TrackingPageViewModel::class.java)
    }

    private var mOrderId: String? = null
    private var mTrackingUrl: String? = null
    private var mCaller: String? = null

    private var loadingScreen: ProgressBar? = null
    private var referenceNumber: TextView? = null
    private var deliveryDate: TextView? = null
    private var storeName: TextView? = null
    private var storeAddress: TextView? = null
    private var serviceCode: TextView? = null
    private var buyerName: TextView? = null
    private var buyerLocation: TextView? = null
    private var currentStatus: TextView? = null
    private var trackingHistory: RecyclerView? = null
    private var emptyUpdateNotification: LinearLayout? = null
    private var notificationText: TextView? = null
    private var notificationHelpStep: RecyclerView? = null
    private var liveTrackingButton: UnifyButton? = null
    private var rootView: ViewGroup? = null
    private var descriptionLayout: LinearLayout? = null
    private var retryButton: UnifyButton? = null
    private var retryStatus: TextView? = null
    private var mCountDownTimer: CountDownTimer? = null
    private var tickerInfoCourier: Ticker? = null
    private var tickerInfoLayout: LinearLayout? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val component: TrackingPageComponent = DaggerTrackingPageComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mOrderId = arguments?.getString(ARGUMENTS_ORDER_ID)
            mTrackingUrl = arguments?.getString(ARGUMENTS_TRACKING_URL)
            mCaller = arguments?.getString(ARGUMENTS_CALLER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tracking_page_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mCountDownTimer != null) {
            mCountDownTimer?.cancel()
        }
    }

    private fun initView() {
        loadingScreen = view?.findViewById(R.id.main_progress_bar)

        rootView = view?.findViewById(R.id.root_view)
        referenceNumber = view?.findViewById(R.id.reference_number)
        deliveryDate = view?.findViewById(R.id.delivery_date)
        storeName = view?.findViewById(R.id.store_name)
        storeAddress = view?.findViewById(R.id.store_address)
        serviceCode = view?.findViewById(R.id.service_code)
        buyerName = view?.findViewById(R.id.buyer_name)
        buyerLocation = view?.findViewById(R.id.buyer_location)
        currentStatus = view?.findViewById(R.id.currenct_status)
        trackingHistory = view?.findViewById(R.id.tracking_history)
        trackingHistory?.isNestedScrollingEnabled = false
        emptyUpdateNotification = view?.findViewById(R.id.empty_update_notification)
        notificationText = view?.findViewById(R.id.notification_text)
        notificationHelpStep = view?.findViewById(R.id.notification_help_step)
        retryButton = view?.findViewById(R.id.retry_pickup_button)
        descriptionLayout = view?.findViewById(R.id.description_layout)
        retryStatus = view?.findViewById(R.id.tv_retry_status)

        liveTrackingButton = view?.findViewById(R.id.live_tracking_button)

        tickerInfoCourier = view?.findViewById(R.id.ticker_info_courier)
        tickerInfoLayout = view?.findViewById(R.id.ticker_info_layout)
    }

    private fun initObserver() {
        viewModel.trackingData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    hideLoading()
                    populateView(it.data)
                }
                is Fail -> {
                    hideLoading()
                    showError(it.throwable)
                }
                else -> showLoading()
            }
        })

        viewModel.retryAvailability.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val avail =  it.data.retryAvailability
                    val deadline = avail.deadlineRetryUnixtime.toLong()
                    if (avail.showRetryButton && avail.availabilityRetry) {
                        setRetryButton(true, 0L)
                    } else if (!avail.availabilityRetry) {
                        setRetryButton(false, deadline)
                    } else {
                        setRetryButton(false, 0L)
                    }
                }
                is Fail -> {
                    if (view != null) {
                        showSoftError(it.throwable)
                    }
                }
            }
        })

        viewModel.retryBooking.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> startSuccessCountdown()
                is Fail -> showError(it.throwable)
            }
        })

        viewModel.getDeliveryImage.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> Log.d("IMAGENYA", it.data.image.toString())
            }
        })
    }

    private fun fetchData() {
        mOrderId?.let { viewModel.getTrackingData(it) }
        if (mTrackingUrl != null && mCaller != null && mCaller.equals("seller", ignoreCase = true)) {
            mOrderId?.let { viewModel.retryAvailability(it) }
        }
    }

    private fun populateView(trackingDataModel: TrackingDataModel) {
        val model = trackingDataModel.trackOrder
        referenceNumber?.text = model.shippingRefNum
        if (model.detail.serviceCode.isEmpty()) descriptionLayout?.visibility = View.GONE
        if (model.detail.sendDate.isNotEmpty()) deliveryDate?.text = dateUtil.getFormattedDate(model.detail.sendDate)
        storeName?.text = model.detail.shipperName
        storeAddress?.text = model.detail.shipperCity
        serviceCode?.text = model.detail.serviceCode
        buyerName?.text = model.detail.receiverName
        buyerLocation?.text = model.detail.receiverCity
        currentStatus?.text = model.status
        initialHistoryView()
        setHistoryView(model)
        setEmptyHistoryView(model)
        setLiveTrackingButton(model)
        setTicketInfoCourier(trackingDataModel.page)
        mAnalytics.eventViewOrderTrackingImpressionButtonLiveTracking()

        viewModel.getDeliveryImage("1e906057-9ed0-422a-8f15-c3133b1e9763", 166826912, "small")
    }

    private fun showLoading() {
        loadingScreen?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingScreen?.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        NetworkErrorHelper.showEmptyState(activity, rootView, this::fetchData)
    }

    private fun showSoftError(error: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, error)
        view?.let { Toaster.build(it, message, Toaster.LENGTH_SHORT).show() }
    }

    private fun setRetryButton(active: Boolean, deadline: Long) {
        if (active) {
            retryButton?.visibility = View.VISIBLE
            retryButton?.text = getString(R.string.find_new_driver)
            retryButton?.isEnabled = true
            retryButton?.setOnClickListener {
                retryButton?.isEnabled = false
                mOrderId?.let { it -> viewModel.retryBooking(it) }
                mAnalytics.eventClickButtonCariDriver(mOrderId)
            }
            retryStatus?.visibility = View.GONE
            mAnalytics.eventViewButtonCariDriver(mOrderId)
        } else {
            retryButton?.visibility = View.GONE
            if (deadline > 0) {
                retryStatus?.visibility = View.VISIBLE
                val now = System.currentTimeMillis()/1000L
                val remainingTIme = deadline - now
                initTimer(remainingTIme)
            } else {
                retryStatus?.visibility = View.GONE
            }
        }
    }

    private fun initTimer(remainingSeconds: Long) {
        if (remainingSeconds <= 0) return
        val timeInMillis = remainingSeconds * 1000
        val strFormat = if (context != null) context?.getString(R.string.retry_dateline_info) else ""
        mAnalytics.eventViewLabelTungguRetry(
                DateUtils.formatElapsedTime(timeInMillis / 1000), mOrderId)
        mCountDownTimer = object : CountDownTimer(timeInMillis, PER_SECOND.toLong()) {
            override fun onTick(millsUntilFinished: Long) {
                if (context != null) {
                    val info = strFormat?.let {
                        String.format(it,
                                DateUtils.formatElapsedTime(millsUntilFinished / 1000))
                    }
                    retryStatus?.text = MethodChecker.fromHtml(info)
                }
            }

            override fun onFinish() {
                fetchData()
            }
        }
        mCountDownTimer?.start()
    }

    private fun startSuccessCountdown() {
        retryButton?.text = getText(R.string.find_new_driver)
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<Long>() {
                    override fun onNext(t: Long?) {
                        fetchData()
                    }

                    override fun onCompleted() {
                       //no-op
                    }

                    override fun onError(e: Throwable) {
                        showError(e)
                    }

                })
    }

    private fun initialHistoryView() {
        trackingHistory?.visibility = View.GONE
        emptyUpdateNotification?.visibility = View.GONE
        liveTrackingButton?.visibility = View.GONE
    }

    private fun setHistoryView(model: TrackOrderModel) {
        if (model.invalid || model.orderStatus == 501 || model.change == 0 || model.trackHistory.isEmpty()) {
            trackingHistory?.visibility = View.GONE
        } else {
            trackingHistory?.visibility = View.VISIBLE
            trackingHistory?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            trackingHistory?.adapter = TrackingHistoryAdapter(model.trackHistory, dateUtil, mOrderId?.toLong())
        }
    }

    private fun setTicketInfoCourier(page: PageModel) {
       if (page.additionalInfo.isEmpty()) {
           tickerInfoLayout?.visibility = View.GONE
       } else {
           tickerInfoLayout?.visibility = View.VISIBLE
           if (page.additionalInfo.size > 1) {
               val message = ArrayList<TickerData>()
               for (item in page.additionalInfo) {
                   val formattedDes = formatTitleHtml(item.notes, item.urlDetail, item.urlText)
                   message.add(TickerData(item.title, formattedDes, Ticker.TYPE_ANNOUNCEMENT, true))
               }
               val tickerPageAdapter = TickerPagerAdapter(context, message)
               tickerPageAdapter?.setPagerDescriptionClickEvent(object: TickerPagerCallback {
                   override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                       RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                   }

               })
               tickerInfoCourier?.addPagerView(tickerPageAdapter, message)
           } else {
               val formattedDesc = formatTitleHtml(page.additionalInfo[0].notes, page.additionalInfo[0].urlDetail, page.additionalInfo[0].urlText)
               tickerInfoCourier?.setHtmlDescription(formattedDesc)
               tickerInfoCourier?.tickerTitle = page.additionalInfo[0].title
               tickerInfoCourier?.tickerType = Ticker.TYPE_ANNOUNCEMENT
               tickerInfoCourier?.tickerShape = Ticker.SHAPE_LOOSE
               tickerInfoCourier?.setDescriptionClickEvent(object: TickerCallback {
                   override fun onDescriptionViewClick(linkUrl: CharSequence) {
                       RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                   }

                   override fun onDismiss() {
                       //no-op
                   }

               })
           }
       }
    }

    private fun setEmptyHistoryView(model: TrackOrderModel) {
        if (model.invalid) {
            emptyUpdateNotification?.visibility = View.VISIBLE
            notificationText?.text = getString(R.string.warning_courier_invalid)
            notificationHelpStep?.visibility = View.VISIBLE
            notificationHelpStep?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            notificationHelpStep?.adapter = EmptyTrackingNotesAdapter()
        } else if (model.orderStatus == 501 || model.change == 0 || model.trackHistory.isEmpty()) {
            emptyUpdateNotification?.visibility = View.VISIBLE
            notificationText?.text = getString(R.string.warning_no_courier_change)
            notificationHelpStep?.visibility = View.GONE
        } else {
            emptyUpdateNotification?.visibility = View.GONE
            notificationHelpStep?.visibility = View.GONE
        }
    }

    private fun setLiveTrackingButton(model: TrackOrderModel) {
        if (mTrackingUrl.isNullOrEmpty() && model.detail.trackingUrl.isEmpty()) {
            liveTrackingButton?.visibility = View.GONE
        } else {
            liveTrackingButton?.visibility = View.VISIBLE
            liveTrackingButton?.setOnClickListener {
                goToLiveTrackingPage(model)
            }
        }
    }

    private fun goToLiveTrackingPage(model: TrackOrderModel) {
        mAnalytics.eventClickOrderTrackingClickButtonLiveTracking()
        var trackingUrl = mTrackingUrl
        if (trackingUrl?.isEmpty() == true) {
            trackingUrl = model.detail.trackingUrl
        }
        val intent = context?.let { context -> trackingUrl?.let { createIntent(context, it) } }
        startActivityForResult(intent, LIVE_TRACKING_VIEW_REQ)
    }

    private fun formatTitleHtml(desc: String, urlText: String, url: String): String {
        return String.format("%s <a href=\"%s\">%s</a>", desc, urlText, url)
    }

    companion object {
        private const val PER_SECOND = 1000
        private const val LIVE_TRACKING_VIEW_REQ = 1
        private const val ARGUMENTS_ORDER_ID = "ARGUMENTS_ORDER_ID"
        private const val ARGUMENTS_TRACKING_URL = "ARGUMENTS_TRACKING_URL"
        private const val ARGUMENTS_CALLER = "ARGUMENTS_CALLER"

        fun createFragment(orderId: String?, liveTrackingUrl: String?, caller: String?): TrackingPageFragment {
            return TrackingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENTS_ORDER_ID, orderId)
                    putString(ARGUMENTS_TRACKING_URL, liveTrackingUrl)
                    putString(ARGUMENTS_CALLER, caller)
                }
            }
        }
    }

}