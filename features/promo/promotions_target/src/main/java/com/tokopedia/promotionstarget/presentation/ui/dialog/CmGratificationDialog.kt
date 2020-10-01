package com.tokopedia.promotionstarget.presentation.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
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
import com.tokopedia.promotionstarget.presentation.ui.CustomToast
import com.tokopedia.promotionstarget.presentation.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.presentation.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.CmGratificationViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
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

    protected fun getLayout(): Int {
        return R.layout.dialog_gratification
    }

    fun show(activityContext: Context, gratifNotification: GratifNotification, couponDetailResponse: TokopointsCouponDetailResponse, @NotificationEntryType notificationEntryType: Int):BottomSheetDialog? {
        val pair = prepareBottomSheet(activityContext)
        initViews(pair.first, activityContext, gratifNotification, couponDetailResponse)
        setUiData(gratifNotification, couponDetailResponse)
        updateGratifNotification(gratifNotification, pair.first, notificationEntryType)
        return pair.second
    }

    private fun prepareBottomSheet(activityContext: Context): Pair<View, BottomSheetDialog> {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceCloseableRounded(activityContext, {})
        val view = LayoutInflater.from(activityContext).inflate(getLayout(), null, false)
        bottomSheet.setCustomContentView(view, "", true)
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

            val dialogTitleText = couponDetailResponse.coupon?.couponStatus?.let {
                when (it) {
                    CouponStatusType.USED -> gratifNotification.wordingUsed?.title
                    CouponStatusType.EXPIRED -> gratifNotification.wordingExpired?.title
                    CouponStatusType.ACTIVE -> gratifNotification.wordingActive?.title
                    else -> ""
                }
            }
            val tvDialogTitle = (root.parent.parent as ConstraintLayout).findViewById<TextView>(R.id.title_closeable_rounded)
            tvDialogTitle.text = dialogTitleText
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

        //set coupon data
//        if (couponDetailResponse.couponList != null && couponDetailResponse.couponList.isNotEmpty()) {
        if (couponDetailResponse.coupon != null) {
            couponUiDataList.add(couponDetailResponse.coupon)
            couponListAdapter.notifyDataSetChanged()
        }

        handleAutoApply(couponStatus)


        //second button
        val showSecondButton = gratifNotification.secondButton?.isShown != null && gratifNotification.secondButton.isShown
        toggleSecondButton(showSecondButton)
        if (showSecondButton) {
            btnAction2.text = gratifNotification.secondButton?.text
            btnAction2.setOnClickListener {
                if (!gratifNotification.secondButton?.applink.isNullOrEmpty()) {
                    RouteManager.route(it.context, gratifNotification.secondButton?.applink)
                }
            }
        }
    }

    private fun handleAutoApply(couponStatus: Int?) {
        if (couponStatus == CouponStatusType.ACTIVE) {
            val activity = btnAction.context
            if (activity is AppCompatActivity) {
                viewModel.autoApplyLiveData.observe(activity, Observer {
                    when (it.status) {
                        LiveDataResult.STATUS.SUCCESS -> {
                            performShowToast(it.data)
                            handleRedirection()
                            toggleProgressBar(false)
                            btnAction.text = btnAction.context.getString(R.string.t_promo_lanjut_berbelanja)
                        }
                        LiveDataResult.STATUS.ERROR -> {
                            btnAction.text = btnAction.context.getString(R.string.t_promo_lanjut_berbelanja)
                            toggleProgressBar(false)
                        }
                        LiveDataResult.STATUS.LOADING -> {
                            btnAction.text = ""
                            toggleProgressBar(true)
                        }
                    }
                })
            }
        }
        btnAction.setOnClickListener {
            couponStatus?.let { status ->
                when (status) {
                    CouponStatusType.ACTIVE -> viewModel.autoApply("")
                }
            }
        }
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

    private fun updateGratifNotification(gratifNotification: GratifNotification, view: View, @NotificationEntryType notificationEntryType: Int) {
        view.post {
            if (view.context is AppCompatActivity && !(view.context as AppCompatActivity).isFinishing) {
                viewModel.updateGratification(gratifNotification.notificationID ?: "", notificationEntryType)
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
            BottomSheetBehavior.from(bottomSheetFmContainer).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun performShowToast(data: AutoApplyResponse?) {
        val messageList = data?.tokopointsSetAutoApply?.resultStatus?.message
        val code = data?.tokopointsSetAutoApply?.resultStatus?.code
        if (messageList != null && messageList.isNotEmpty()) {
            if (code == "200") {
                CustomToast.show(btnAction.context, messageList[0].toString())
            } else {
                CustomToast.show(btnAction.context, messageList[0].toString(), R.drawable.t_promo_custom_toast_bg_red)
            }
        }
    }

    private fun handleRedirection() {
        when (gratifNotification?.hachikoButton?.type) {
            HachikoButtonType.REDIRECT -> {
                val applink = couponDetailResponse?.coupon?.usage?.btnUsage?.applink
                if (!applink.isNullOrEmpty()) {
                    RouteManager.route(btnAction.context, applink)
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
}