package com.tokopedia.promotionstarget.presentation.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TaskStackBuilder
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.coupon.CouponStatusType
import com.tokopedia.promotionstarget.data.coupon.CouponUiData
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationComponent
import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.notification.GratifNotification
import com.tokopedia.promotionstarget.data.notification.HachikoButtonType
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.presentation.GratificationAnalyticsHelper
import com.tokopedia.promotionstarget.presentation.ui.CustomToast
import com.tokopedia.promotionstarget.presentation.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.presentation.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.CmGratificationViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import javax.inject.Inject

class CmGratificationDialog {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CmGratificationViewModel

    private lateinit var tvTitle: Typography
    private lateinit var tvSubTitle: Typography
    private lateinit var btnAction: Typography
    private lateinit var btnAction2: UnifyButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var couponListAdapter: CouponListAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var space: Space
    private var bottomSheetFmContainer: ViewGroup? = null
    private var bottomSheetCoordinatorLayout: ViewGroup? = null

    private val couponUiDataList = arrayListOf<CouponUiData>()
    private var gratifNotification: GratifNotification? = null
    private var couponDetailResponse: TokopointsCouponDetailResponse? = null
    private var buttonText = ""

    @NotificationEntryType
    private var notificationEntryType = NotificationEntryType.ORGANIC
    private var screenName = ""
    private var closeCurrentActivity = false
    private var inAppId: Long? = null

    protected fun getLayout(): Int {
        return R.layout.dialog_gratification
    }

    fun show(activityContext: Context,
             gratifNotification: GratifNotification,
             couponDetailResponse: TokopointsCouponDetailResponse,
             @NotificationEntryType notificationEntryType: Int,
             onShowListener: DialogInterface.OnShowListener,
             screenName: String,
             closeCurrentActivity: Boolean,
             inAppId: Long?
    ): BottomSheetDialog? {
        this.gratifNotification = gratifNotification
        this.couponDetailResponse = couponDetailResponse
        this.notificationEntryType = notificationEntryType
        this.screenName = screenName
        this.closeCurrentActivity = closeCurrentActivity
        this.inAppId = inAppId

        val pair = prepareBottomSheet(activityContext, onShowListener)
        bottomSheetDialog = pair.second
        initViews(pair.first, activityContext, gratifNotification, couponDetailResponse)
        setUiData(gratifNotification, couponDetailResponse)
        updateGratifNotification(gratifNotification, pair.first, notificationEntryType, inAppId)
        return pair.second
    }

    private fun prepareBottomSheet(activityContext: Context, onShowListener: DialogInterface.OnShowListener): Pair<View, BottomSheetDialog> {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceCloseableRounded(activityContext, {})
        val view = LayoutInflater.from(activityContext).inflate(getLayout(), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.setOnShowListener(onShowListener)
        bottomSheet.show()
        return Pair(view, bottomSheet)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(root: View, activityContext: Context,
                          gratifNotification: GratifNotification,
                          couponDetailResponse: TokopointsCouponDetailResponse) {
        tvTitle = root.findViewById(R.id.tvTitle)
        tvSubTitle = root.findViewById(R.id.tvSubTitle)
        btnAction = root.findViewById(R.id.btnAction)
        btnAction2 = root.findViewById(R.id.btnAction2)
        space = root.findViewById(R.id.space)
        recyclerView = root.findViewById(R.id.recyclerView)
        nestedScrollView = root.findViewById(R.id.nestedScrollView)
        progressBar = root.findViewById(R.id.targetProgressBar)

        try {
            val imageClose = (root.parent.parent as ConstraintLayout).findViewById<ImageView>(R.id.close_button_rounded)
            imageClose.setImageResource(R.drawable.t_promo_close)

            val couponStatus = couponDetailResponse.coupon?.couponStatus ?: 0
            val dialogTitleText = when (couponStatus) {
                CouponStatusType.USED -> gratifNotification.wordingUsed?.title
                CouponStatusType.EXPIRED -> gratifNotification.wordingExpired?.title
                CouponStatusType.ACTIVE -> gratifNotification.wordingActive?.title
                else -> ""
            }


            val tvDialogTitle = (root.parent.parent as ConstraintLayout).findViewById<TextView>(R.id.title_closeable_rounded)
            if (tvDialogTitle.parent is RelativeLayout && !dialogTitleText.isNullOrEmpty()) {
                val rlParent = tvDialogTitle.parent as RelativeLayout
                val typographyTitle = Typography(tvTitle.context)
                val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.addRule(RelativeLayout.RIGHT_OF, tvDialogTitle.id)
                lp.addRule(RelativeLayout.END_OF, tvDialogTitle.id)
                lp.addRule(RelativeLayout.CENTER_VERTICAL)
                typographyTitle.setTextColor(ContextCompat.getColor(tvDialogTitle.context, R.color.t_promo_title_color))
                typographyTitle.layoutParams = lp
                typographyTitle.setType(Typography.HEADING_3)
                rlParent.addView(typographyTitle)

                typographyTitle.text = dialogTitleText
            }
        } catch (th: Throwable) {
            Timber.d(th)
        }

        recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)

        val pageSnaper = PagerSnapHelper()
        pageSnaper.attachToRecyclerView(recyclerView)

        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(CouponItemDecoration())
        couponListAdapter = CouponListAdapter(couponUiDataList)
        recyclerView.adapter = couponListAdapter

        initInjections(activityContext)
    }

    private fun initInjections(activityContext: Context) {
        val activity = activityContext as AppCompatActivity

        val component = DaggerCmGratificationComponent.builder()
                .appModule(AppModule(activity.application))
                .build()
        component.inject(this)
        activity.run {
            val viewModelProvider = ViewModelProviders.of(activityContext, viewModelFactory)
            viewModel = viewModelProvider[CmGratificationViewModel::class.java]
        }
    }


    private fun setUiData(gratifNotification: GratifNotification, couponDetailResponse: TokopointsCouponDetailResponse) {
        val couponStatus = couponDetailResponse.coupon?.couponStatus

        setTitles(couponStatus, gratifNotification)
        setCoupon(couponDetailResponse)
        handleActionButton(couponStatus)
        handleWhiteButton(gratifNotification)
    }

    private fun setTitles(couponStatus: Int?, gratifNotification: GratifNotification) {
        val pair = couponStatus?.let {
            when (it) {
                CouponStatusType.USED -> gratifNotification.wordingUsed?.let { word -> Pair(word.subtitle1, word.subtitle2) }
                CouponStatusType.EXPIRED -> gratifNotification.wordingExpired?.let { word -> Pair(word.subtitle1, word.subtitle2) }
                CouponStatusType.ACTIVE -> gratifNotification.wordingActive?.let { word -> Pair(word.subtitle1, word.subtitle2) }
                else -> Pair("", "")
            }
        }
        tvTitle.text = pair?.first
        tvSubTitle.text = pair?.second
    }

    private fun setCoupon(couponDetailResponse: TokopointsCouponDetailResponse) {
        if (couponDetailResponse.coupon != null) {
            couponUiDataList.add(couponDetailResponse.coupon)
            couponListAdapter.notifyDataSetChanged()
        }
    }

    private fun handleWhiteButton(gratifNotification: GratifNotification) {
        val showSecondButton = gratifNotification.secondButton?.isShown != null && gratifNotification.secondButton.isShown
        toggleSecondButton(showSecondButton)
        if (showSecondButton) {
            btnAction2.text = gratifNotification.secondButton?.text
            btnAction2.setOnClickListener {
                val type = gratifNotification.secondButton?.type
                if (!type.isNullOrEmpty()) {
                    when (type) {
                        HachikoButtonType.REDIRECT -> {
                            if (!gratifNotification.secondButton?.applink.isNullOrEmpty()) {
                                launchApplink(it.context, gratifNotification.secondButton.applink!!)
                            }
                            bottomSheetDialog.dismiss()
                        }
                        HachikoButtonType.DISMISS -> {
                            bottomSheetDialog.dismiss()
                        }
                    }
                }

                val userId = UserSession(btnAction.context).userId
                GratificationAnalyticsHelper.handleClickSecondaryCta(userId, notificationEntryType, gratifNotification, couponDetailResponse, screenName)
                isInteracted()
            }
            btnAction2.post { expandBottomSheet() }
        }
    }

    private fun handleActionButton(couponStatus: Int?) {
        setActionButtonText(couponStatus)
        observerViewModel()
        setClickEvent(couponStatus)
    }

    private fun setActionButtonText(couponStatus: Int?) {
        buttonText = btnAction.context.getString(R.string.t_promo_lanjut_berbelanja)
        if (couponStatus == CouponStatusType.ACTIVE) {
            val btnUsageText = couponDetailResponse?.coupon?.usage?.btnUsage?.text
            if (!btnUsageText.isNullOrEmpty()) {
                buttonText = btnUsageText
            }
        }
        btnAction.text = buttonText
    }

    private fun observerViewModel() {
        val activity = btnAction.context
        if (activity is AppCompatActivity) {
            viewModel.autoApplyLiveData.observe(activity, Observer {
                when (it.status) {
                    LiveDataResult.STATUS.SUCCESS -> {
                        handleAutoApplySuccess(it.data)
                    }
                    LiveDataResult.STATUS.ERROR -> {
                        btnAction.text = buttonText
                        toggleProgressBar(false)

                        val userId = UserSession(btnAction.context).userId
                        GratificationAnalyticsHelper.handleMainCtaClick(userId, notificationEntryType, gratifNotification, couponDetailResponse, screenName, "fail")
                    }
                    LiveDataResult.STATUS.LOADING -> {
                        btnAction.text = ""
                        toggleProgressBar(true)
                    }
                }
            })
        }
    }

    private fun handleAutoApplySuccess(autoApplyResponse: AutoApplyResponse?) {
        performShowToast(autoApplyResponse)
        val code = autoApplyResponse?.tokopointsSetAutoApply?.resultStatus?.code
        if (code == "200") {
            handleGreenBtnRedirection()
        }

        val userId = UserSession(btnAction.context).userId
        val autoApplyStatusCode = if (code == "200") "success" else "fail"
        GratificationAnalyticsHelper.handleMainCtaClick(userId, notificationEntryType, gratifNotification, couponDetailResponse, screenName, autoApplyStatusCode)

        toggleProgressBar(false)
        btnAction.text = buttonText
    }

    private fun setClickEvent(couponStatus: Int?) {
        btnAction.setOnClickListener {
            handleButtonAction(couponStatus)
            isInteracted()
        }
    }

    private fun handleButtonAction(couponStatus: Int?) {
        if (couponStatus != null && couponStatus == CouponStatusType.ACTIVE) {
            val code = gratifNotification?.promoCode
            if (!code.isNullOrEmpty() && couponDetailResponse?.coupon?.isApplicable == true) {
                viewModel.autoApply(code)
            } else {
                handleGreenBtnRedirection()
                sendGreenButtonClickEventForNoAutoApply()
            }
        } else {
            handleGreenBtnRedirection()
            sendGreenButtonClickEventForNoAutoApply()
        }
    }

    private fun sendGreenButtonClickEventForNoAutoApply() {
        val userId = UserSession(btnAction.context).userId
        GratificationAnalyticsHelper.handleMainCtaClick(userId, notificationEntryType, gratifNotification, couponDetailResponse, screenName, null)
    }

    private fun toggleSecondButton(toggle: Boolean) {
        if (toggle) {
            btnAction2.visibility = View.VISIBLE
            space.visibility = View.VISIBLE
        } else {
            btnAction2.visibility = View.GONE
            space.visibility = View.GONE
        }
    }

    private fun updateGratifNotification(gratifNotification: GratifNotification, view: View, @NotificationEntryType notificationEntryType: Int, inAppId: Long?) {
        view.post {
            if (view.context is AppCompatActivity && !(view.context as AppCompatActivity).isFinishing) {
                viewModel.updateGratification(gratifNotification.notificationID,
                        notificationEntryType,
                        GratificationAnalyticsHelper.getPopupType(gratifNotification, couponDetailResponse),
                        screenName, inAppId)
            }
        }
    }

    private fun expandBottomSheet() {
        if (bottomSheetFmContainer == null) {
            var child: ViewGroup = nestedScrollView
            var parent: ViewParent = nestedScrollView.parent

            while (!(parent is CoordinatorLayout)) {
                if (parent is ViewGroup) {
                    child = parent
                }
                parent = parent.parent
            }
            bottomSheetFmContainer = child
            bottomSheetCoordinatorLayout = parent
        }
        if (bottomSheetCoordinatorLayout != null && bottomSheetFmContainer is FrameLayout) {
            BottomSheetBehavior.from(bottomSheetFmContainer as FrameLayout).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun performShowToast(data: AutoApplyResponse?) {
        val messageList = data?.tokopointsSetAutoApply?.resultStatus?.message
        val code = data?.tokopointsSetAutoApply?.resultStatus?.code
        if (messageList != null && messageList.isNotEmpty()) {
            if (code == "200") {
                CustomToast.show(btnAction.context.applicationContext, messageList[0].toString())
            } else {
                CustomToast.show(activityContext = btnAction.context.applicationContext,
                        text = messageList[0].toString(),
                        bg = R.drawable.t_promo_custom_toast_bg_red)
            }
        }
    }

    private fun handleGreenBtnRedirection() {
        when (gratifNotification?.hachikoButton?.type) {
            HachikoButtonType.REDIRECT -> {
                val applink = couponDetailResponse?.coupon?.usage?.btnUsage?.applink
                if (!applink.isNullOrEmpty()) {
                    launchApplink(btnAction.context, applink)
                }
                bottomSheetDialog.dismiss()
            }
            HachikoButtonType.DISMISS -> {
                bottomSheetDialog.dismiss()
            }
        }
    }

    private fun toggleProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun launchApplink(context: Context, applink: String) {
        if (closeCurrentActivity && context is Activity) {
            val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME, "")
            val intent = RouteManager.getIntent(context, applink, "")
            intent?.let {
                TaskStackBuilder.create(context)
                        .addNextIntent(homeIntent)
                        .addNextIntent(intent)
                        .startActivities()
            }
        } else {
            RouteManager.route(context, applink)
        }
    }

    private fun isInteracted() {
        if (inAppId != null) {
            CMInAppManager.getInstance().dataConsumer.interactedWithView(inAppId!!)
        }
    }
}