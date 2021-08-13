package com.tokopedia.tokopoints.view.sendgift

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogDetailsActivity
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.SendgiftPlt.Companion.SENDGIFT_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.SendgiftPlt.Companion.SENDGIFT_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.SendgiftPlt.Companion.SENDGIFT_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.SendgiftPlt.Companion.SENDGIFT_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.util.*
import javax.inject.Inject
import com.tokopedia.unifycomponents.TextFieldUnify

class SendGiftFragment : BottomSheetDialogFragment(), SendGiftContract.View, View.OnClickListener, TextWatcher, TokopointPerformanceMonitoringListener {
    private var mContainerMain: ViewFlipper? = null
    private var mBtnSendGift: TextView? = null
    private var mBtnSendNow: TextView? = null
    private var mWrapperEmail: TextFieldUnify? = null
    private var mWrapperNote: TextFieldUnify? = null
    var tokoPointComponent: TokopointBundleComponent? = null

    @Inject
    lateinit var factory: ViewModelFactory
    private val mViewModel: SendGiftViewModel by lazy { ViewModelProviders.of(this, factory)[SendGiftViewModel::class.java] }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.tokopedia.tokopoints.R.style.TokopointTransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.tokopoints.R.layout.tp_fragment_send_gift, container, false)
        initView()
        return view
    }

    fun initView() {
        tokoPointComponent = (activity as CouponCatalogDetailsActivity).component
        tokoPointComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContainerMain = view.findViewById(com.tokopedia.tokopoints.R.id.container_main)
        mContainerMain?.displayedChild = CONTAINER_SEND_FORM
        mWrapperEmail = view.findViewById(com.tokopedia.tokopoints.R.id.wrapper_email)
        mWrapperNote = view.findViewById(com.tokopedia.tokopoints.R.id.wrapper_text_count)
        mBtnSendGift = view.findViewById(com.tokopedia.tokopoints.R.id.button_send)
        mBtnSendNow = view.findViewById(com.tokopedia.tokopoints.R.id.button_send_now)
        val ivCancelInitial = getView()!!.findViewById<ImageView>(com.tokopedia.tokopoints.R.id.iv_cancel)
        val ivCancelPreConfirm = getView()!!.findViewById<ImageView>(com.tokopedia.tokopoints.R.id.iv_cancel_preconfirmation)
        ivCancelInitial.setOnClickListener { view1: View? -> dismiss() }
        ivCancelPreConfirm.setOnClickListener { view1: View? -> dismiss() }
        mWrapperEmail?.textFieldInput?.addTextChangedListener(this)
        mWrapperNote?.textFieldInput?.addTextChangedListener(this)
        mBtnSendGift?.setOnClickListener(View.OnClickListener { view: View -> onClick(view) })
        mBtnSendNow?.setOnClickListener(View.OnClickListener { view: View -> onClick(view) })
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        addObserver()
    }

    private fun addObserver() {
        addSendGiftObserver()
        addPreValidationObserver()
    }

    private fun addPreValidationObserver() = mViewModel.prevalidateLiveData.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> showLoading()
                is ErrorMessage -> {
                    hideLoading()
                    if (!it.data.isEmpty()) {
                        onErrorPreValidate(it.data)
                    }
                }
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    hideLoading()
                    openPreConfirmationWindow()
                    stopRenderPerformanceMonitoring()
                    stopPerformanceMonitoring()
                }
            }
        }
    })

    private fun addSendGiftObserver() = mViewModel.sendGiftLiveData.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> showLoadingSendNow()
                is Success -> {
                    hideLoadingSendNow()
                    val title = if (it.data.title is Int) getString(it.data.title) else it.data.title.toString()
                    val message = if (it.data.messsage is Int) getString(it.data.messsage) else it.data.messsage.toString()
                    showPopup(title, message, it.data.success)
                }
                is ErrorMessage -> {
                    hideLoadingSendNow()
                }
            }
        }
    })

    override fun onClick(view: View) {
        if (view.id == com.tokopedia.tokopoints.R.id.button_send) {
            if (arguments == null || activity == null) {
                return
            }

            KeyboardHandler.hideSoftKeyboard(activity)
            mViewModel!!.preValidateGift(arguments!!.getInt(CommonConstant.EXTRA_COUPON_ID), mWrapperEmail?.textFieldInput?.text.toString())
        } else if (view.id == com.tokopedia.tokopoints.R.id.button_send_now) {
            if (arguments == null || activity == null) {
                return
            }
            mViewModel.sendGift(arguments!!.getInt(CommonConstant.EXTRA_COUPON_ID),
                    mWrapperEmail?.textFieldInput?.text.toString(),
                    mWrapperNote?.textFieldInput?.text.toString())
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KIRIM_KUPON,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_KIRIM_SEKARANG,
                    couponTitle)
        }
    }

    override fun showLoading() {
        if (view == null) {
            return
        }
        view!!.findViewById<View>(com.tokopedia.tokopoints.R.id.progress_send).visibility = View.VISIBLE
        mBtnSendGift!!.text = ""
    }

    override fun hideLoading() {
        if (view == null) {
            return
        }
        view!!.findViewById<View>(com.tokopedia.tokopoints.R.id.progress_send).visibility = View.GONE
        mBtnSendGift!!.setText(com.tokopedia.tokopoints.R.string.tp_label_send)
    }

    override fun showLoadingSendNow() {
        if (view == null) {
            return
        }
        view!!.findViewById<View>(com.tokopedia.tokopoints.R.id.progress_send_now).visibility = View.VISIBLE
        mBtnSendGift!!.text = ""
    }

    override fun hideLoadingSendNow() {
        if (view == null) {
            return
        }
        view!!.findViewById<View>(com.tokopedia.tokopoints.R.id.progress_send_now).visibility = View.GONE
        mBtnSendGift!!.setText(com.tokopedia.tokopoints.R.string.tp_label_send_now)
    }

    private val couponTitle: String
        private get() {
            if (arguments != null) {
                val couponTitle = arguments!!.getString(CommonConstant.EXTRA_COUPON_TITLE)
                if (couponTitle != null) return couponTitle
            }
            return ""
        }

    private val couponPoint: String
        private get() {
            if (arguments != null) {
                val couponPoint = arguments!!.getString(CommonConstant.EXTRA_COUPON_POINT)
                if (couponPoint != null) return couponPoint
            }
            return ""
        }

    override fun openPreConfirmationWindow() {
        mContainerMain!!.displayedChild = CONTAINER_PRE_CONFIRMATION
        if (view == null || arguments == null) {
            return
        }
        val textTitle = view!!.findViewById<TextView>(com.tokopedia.tokopoints.R.id.tv_title_banner)
        val textPoint = view!!.findViewById<TextView>(com.tokopedia.tokopoints.R.id.point)
        val textEmail = view!!.findViewById<TextView>(com.tokopedia.tokopoints.R.id.email)
        val textNotes = view!!.findViewById<TextView>(com.tokopedia.tokopoints.R.id.message)
        val imgBanner = view!!.findViewById<ImageView>(com.tokopedia.tokopoints.R.id.iv_banner)
        arguments?.getString(CommonConstant.EXTRA_COUPON_BANNER)?.let { imgBanner.loadImage(it) }
        textTitle.text = couponTitle
        textPoint.text = couponPoint
        textEmail.text = mWrapperEmail?.textFieldInput?.text?.toString()
        if (!mWrapperNote?.textFieldInput.toString().trim { it <= ' ' }.isEmpty()) {
            textNotes.text = "\"" + mWrapperNote?.textFieldInput?.text.toString().trim { it <= ' ' } + "\""
        }
    }

    override fun onErrorPreValidate(error: String) {
        mWrapperEmail?.setError(true)
        mWrapperEmail?.setMessage(error)
    }

    override fun onSuccess() {}
    override fun onError(error: String) {}
    override fun getAppContext(): Context {
        return activity!!
    }

    override fun getActivityContext(): Context {
        return activity!!
    }

    override fun showPopup(title: String, message: String, success: Int) {
        val constraintLayout: ConstraintLayout = view!!.findViewById(com.tokopedia.tokopoints.R.id.pre_confirmation_parent_container)
        constraintLayout.visibility = View.GONE
        val viewSentFail = View.inflate(this.context, com.tokopedia.tokopoints.R.layout.tp_gift_coupon_sent_fail, null)
        val viewSentSuccess = View.inflate(this.context, com.tokopedia.tokopoints.R.layout.tp_gift_coupon_sent, null)
        val adb = AlertDialog.Builder(activityContext)
        if (success == 1) {
            val tvTitle = viewSentSuccess.findViewById<TextView>(com.tokopedia.tokopoints.R.id.tv_title)
            val tvContent = viewSentSuccess.findViewById<TextView>(com.tokopedia.tokopoints.R.id.content)
            val btnSuccess = viewSentSuccess.findViewById<TextView>(com.tokopedia.tokopoints.R.id.btn_sentSuccess)
            btnSuccess.setOnClickListener { view: View? -> activity!!.finish() }
            tvTitle.text = title
            tvContent.text = message
            adb.setView(viewSentSuccess)
        } else {
            val tvTitle = viewSentFail.findViewById<TextView>(com.tokopedia.tokopoints.R.id.tv_title)
            val tvContent = viewSentFail.findViewById<TextView>(com.tokopedia.tokopoints.R.id.content)
            val tvRoute = viewSentFail.findViewById<TextView>(com.tokopedia.tokopoints.R.id.tv_route)
            val btnFailed = viewSentFail.findViewById<TextView>(com.tokopedia.tokopoints.R.id.btn_sentFail)
            btnFailed.setOnClickListener { view: View? -> activity!!.finish() }
            tvRoute.setOnClickListener { view: View? -> RouteManager.route(this.context, ApplinkConst.TOKOPOINTS) }
            tvTitle.text = title
            tvContent.text = message
            adb.setView(viewSentFail)
        }
        val dialog = adb.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        if (charSequence.length == 0) {
            mBtnSendGift!!.isEnabled = false
            mBtnSendGift!!.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        } else {
            mBtnSendGift!!.isEnabled = true
            mBtnSendGift!!.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y500))
        }
    }

    override fun afterTextChanged(editable: Editable) {}

    override fun onDestroy() {
        super.onDestroy()
        mWrapperEmail?.textFieldInput?.removeTextChangedListener(this)
        mWrapperNote?.textFieldInput?.removeTextChangedListener(this)
    }

    companion object {
        private const val CONTAINER_SEND_FORM = 0
        private const val CONTAINER_PRE_CONFIRMATION = 1
        fun newInstance(extras: Bundle?): Fragment {
            val fragment: Fragment = SendGiftFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                SENDGIFT_TOKOPOINT_PLT_PREPARE_METRICS,
                SENDGIFT_TOKOPOINT_PLT_NETWORK_METRICS,
                SENDGIFT_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(SENDGIFT_TOKOPOINT_PLT)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }
}