package com.tokopedia.logisticorder.view

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.adapter.EmptyTrackingNotesAdapter
import com.tokopedia.logisticorder.adapter.TrackingHistoryAdapter
import com.tokopedia.logisticorder.databinding.FragmentTrackingPageBinding
import com.tokopedia.logisticorder.di.DaggerTrackingPageComponent
import com.tokopedia.logisticorder.di.TrackingPageComponent
import com.tokopedia.logisticorder.uimodel.PageModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.utils.DateUtil
import com.tokopedia.logisticorder.utils.TrackingPageUtil.getDeliveryImage
import com.tokopedia.logisticorder.view.imagepreview.ImagePreviewLogisticActivity
import com.tokopedia.logisticorder.view.livetracking.LiveTrackingActivity
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TrackingPageFragment: BaseDaggerFragment(), TrackingHistoryAdapter.OnImageClicked {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var dateUtil: DateUtil
    @Inject
    lateinit var mAnalytics: OrderAnalyticsOrderTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: TrackingPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TrackingPageViewModel::class.java)
    }

    private var binding by autoClearedNullable<FragmentTrackingPageBinding>()

    private var mOrderId: String? = null
    private var mTrackingUrl: String? = null
    private var mCaller: String? = null
    private var mCountDownTimer: CountDownTimer? = null

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
        binding = FragmentTrackingPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mCountDownTimer != null) {
            mCountDownTimer?.cancel()
        }
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
                    showSoftError(it.throwable)
                }
            }
        })

        viewModel.retryBooking.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> startSuccessCountdown()
                is Fail -> showError(it.throwable)
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
        binding?.referenceNumber?.text = model.shippingRefNum
        if (model.detail.serviceCode.isEmpty()) binding?.descriptionLayout?.visibility = View.GONE
        if (model.detail.sendDate.isNotEmpty()) binding?.deliveryDate?.text = dateUtil.getFormattedDate(model.detail.sendDate)
        binding?.storeName?.text = model.detail.shipperName
        binding?.storeAddress?.text = model.detail.shipperCity
        binding?.serviceCode?.text = model.detail.serviceCode
        binding?.buyerName?.text = model.detail.receiverName
        binding?.buyerLocation?.text = model.detail.receiverCity
        binding?.currentStatus?.text = model.status
        initialHistoryView()
        setHistoryView(model)
        setEmptyHistoryView(model)
        setLiveTrackingButton(model)
        setTicketInfoCourier(trackingDataModel.page)
        mAnalytics.eventViewOrderTrackingImpressionButtonLiveTracking()

    }

    private fun showLoading() {
        binding?.mainProgressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding?.mainProgressBar?.visibility = View.GONE
    }

    private fun showError(error: Throwable) {
        NetworkErrorHelper.showEmptyState(activity, binding?.rootView, this::fetchData)
    }

    private fun showSoftError(error: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, error)
        view?.let { Toaster.build(it, message, Toaster.LENGTH_SHORT).show() }
    }

    private fun setRetryButton(active: Boolean, deadline: Long) {
        if (active) {
            binding?.retryPickupButton?.visibility = View.VISIBLE
            binding?.retryPickupButton?.text = getString(R.string.find_new_driver)
            binding?.retryPickupButton?.isEnabled = true
            binding?.retryPickupButton?.setOnClickListener {
                binding?.retryPickupButton?.isEnabled = false
                mOrderId?.let { it -> viewModel.retryBooking(it) }
                mAnalytics.eventClickButtonCariDriver(mOrderId)
            }
            binding?.tvRetryStatus?.visibility = View.GONE
            mAnalytics.eventViewButtonCariDriver(mOrderId)
        } else {
            binding?.retryPickupButton?.visibility = View.GONE
            if (deadline > 0) {
                binding?.tvRetryStatus?.visibility = View.VISIBLE
                val now = System.currentTimeMillis()/1000L
                val remainingTIme = deadline - now
                initTimer(remainingTIme)
            } else {
                binding?.tvRetryStatus?.visibility = View.GONE
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
                    binding?.tvRetryStatus?.text = MethodChecker.fromHtml(info)
                }
            }

            override fun onFinish() {
                fetchData()
            }
        }
        mCountDownTimer?.start()
    }

    private fun startSuccessCountdown() {
        binding?.retryPickupButton?.text = getText(R.string.find_new_driver)
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
        binding?.trackingHistory?.visibility = View.GONE
        binding?.emptyUpdateNotification?.visibility = View.GONE
        binding?.liveTrackingButton?.visibility = View.GONE
    }

    private fun setHistoryView(model: TrackOrderModel) {
        if (model.invalid || model.orderStatus == 501 || model.change == 0 || model.trackHistory.isEmpty()) {
            binding?.trackingHistory?.visibility = View.GONE
        } else {
            binding?.trackingHistory?.visibility = View.VISIBLE
            binding?.trackingHistory?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding?.trackingHistory?.adapter = TrackingHistoryAdapter(model.trackHistory, dateUtil, mOrderId?.toLong(), this)
        }
    }

    private fun setTicketInfoCourier(page: PageModel) {
       if (page.additionalInfo.isEmpty()) {
           binding?.tickerInfoLayout?.visibility = View.GONE
       } else {
           binding?.tickerInfoLayout?.visibility = View.VISIBLE
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
               binding?.tickerInfoCourier?.addPagerView(tickerPageAdapter, message)
           } else {
               val formattedDesc = formatTitleHtml(page.additionalInfo[0].notes, page.additionalInfo[0].urlDetail, page.additionalInfo[0].urlText)
               binding?.tickerInfoCourier?.setHtmlDescription(formattedDesc)
               binding?.tickerInfoCourier?.tickerTitle = page.additionalInfo[0].title
               binding?.tickerInfoCourier?.tickerType = Ticker.TYPE_ANNOUNCEMENT
               binding?.tickerInfoCourier?.tickerShape = Ticker.SHAPE_LOOSE
               binding?.tickerInfoCourier?.setDescriptionClickEvent(object: TickerCallback {
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
            binding?.emptyUpdateNotification?.visibility = View.VISIBLE
            binding?.notificationText?.text = getString(R.string.warning_courier_invalid)
            binding?.notificationHelpStep?.visibility = View.VISIBLE
            binding?.notificationHelpStep?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding?.notificationHelpStep?.adapter = EmptyTrackingNotesAdapter()
        } else if (model.orderStatus == 501 || model.change == 0 || model.trackHistory.isEmpty()) {
            binding?.emptyUpdateNotification?.visibility = View.VISIBLE
            binding?.notificationText?.text = getString(R.string.warning_no_courier_change)
            binding?.notificationHelpStep?.visibility = View.GONE
        } else {
            binding?.emptyUpdateNotification?.visibility = View.GONE
            binding?.notificationHelpStep?.visibility = View.GONE
        }
    }

    private fun setLiveTrackingButton(model: TrackOrderModel) {
        if (mTrackingUrl.isNullOrEmpty() && model.detail.trackingUrl.isEmpty()) {
            binding?.liveTrackingButton?.visibility = View.GONE
        } else {
            binding?.liveTrackingButton?.visibility = View.VISIBLE
            binding?.liveTrackingButton?.setOnClickListener {
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
        val intent = context?.let { LiveTrackingActivity.createIntent(it, trackingUrl) }
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

    override fun onImageItemClicked(imageId: String, orderId: Long) {
        val url = getDeliveryImage(imageId, orderId, "large",
                userSession.userId, 1, userSession.deviceId)

        startActivity(activity?.let {
            url?.let { url -> ImagePreviewLogisticActivity.createIntent(it, arrayListOf(url)) }
        })
    }

}