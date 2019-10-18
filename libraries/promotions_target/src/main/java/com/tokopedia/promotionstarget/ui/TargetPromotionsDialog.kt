package com.tokopedia.promotionstarget.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.ViewFlipper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.GratificationDataContract
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.components.AppModule
import com.tokopedia.promotionstarget.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.loadImageGlide
import com.tokopedia.promotionstarget.subscriber.GratificationData
import com.tokopedia.promotionstarget.subscriber.GratificationSubscriber
import com.tokopedia.promotionstarget.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.ui.viewmodel.TargetPromotionsDialogVM
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class TargetPromotionsDialog(val subscriber: GratificationSubscriber) {

    private val CONTAINER_COUPON = 0
    private val CONTAINER_IMAGE = 1

    private lateinit var tvTitle: Typography
    private lateinit var tvSubTitle: Typography
    private lateinit var imageView: AppCompatImageView
    private lateinit var btnAction: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var couponListAdapter: CouponListAdapter
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var bottomSheetDialog: Dialog
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar

    lateinit var viewModel: TargetPromotionsDialogVM
    private var data: GratificationDataContract? = null
    private lateinit var gratificationData: GratificationData
    var originalBtnText: String? = null
    var couponCodeAfterClaim: String? = null
    var shouldCallAutoApply = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var originallyLoggedIn = false
    private val REQUEST_CODE = 29
    private val MAX_RETRY_COUNT = 4
    private var retryCount = 0
    private var skipBtnAction = false

    var uiType: TargetPromotionsCouponType = TargetPromotionsCouponType.SINGLE_COUPON

    enum class TargetPromotionsCouponType {
        SINGLE_COUPON,
        MULTIPLE_COUPON,
        COUPON_ACTION_TAKEN,
        COUPON_ERROR
    }

    fun getLayout(couponUiType: TargetPromotionsCouponType): Int {
        uiType = couponUiType
        return when (couponUiType) {
            TargetPromotionsCouponType.SINGLE_COUPON -> R.layout.dialog_target_promotions
            else -> R.layout.dialog_target_promotions
        }
    }

    fun show(activityContext: Context,
             couponUiType: TargetPromotionsCouponType,
             data: GratificationDataContract,
             couponDetailResponse: GetCouponDetailResponse,
             gratificationData: GratificationData): Dialog {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activityContext)
        val view = LayoutInflater.from(activityContext).inflate(getLayout(couponUiType), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()
        bottomSheet.setOnDismissListener {
            //            GratificationSubscriber.waitingForLogin.set(false)
            if (TextUtils.isEmpty(couponCodeAfterClaim) && shouldCallAutoApply) {
                viewModel.autoApply(couponCodeAfterClaim!!)
            }
        }

        initViews(view, activityContext, data, couponDetailResponse, gratificationData)
        return bottomSheet
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(root: View, activityContext: Context, data: GratificationDataContract, couponDetailResponse: GetCouponDetailResponse, gratificationData: GratificationData) {
        imageView = root.findViewById(R.id.imageView)
        tvTitle = root.findViewById(R.id.tvTitle)
        tvSubTitle = root.findViewById(R.id.tvSubTitle)
        btnAction = root.findViewById(R.id.btnAction)
        recyclerView = root.findViewById(R.id.recyclerView)
        viewFlipper = root.findViewById(R.id.viewFlipper)
        nestedScrollView = root.findViewById(R.id.nestedScrollView)
        progressBar = root.findViewById(R.id.targetProgressBar)

        recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)

        val pageSnaper = PagerSnapHelper()
        pageSnaper.attachToRecyclerView(recyclerView)

        recyclerView.isNestedScrollingEnabled = false

        val session = UserSession(activityContext)
        originallyLoggedIn = session.isLoggedIn

        this.data = data
        this.gratificationData = gratificationData
        initInjections(activityContext)
        setUiData(data, couponDetailResponse)
        setListeners(data, activityContext)
    }


    private fun setUiData(data: GratificationDataContract, couponDetailResponse: GetCouponDetailResponse) {

        if (data is GetPopGratificationResponse) {
            tvTitle.text = data.popGratification?.title
            tvSubTitle.text = data.popGratification?.text
            originalBtnText = data.popGratification?.popGratificationActionButton?.text
            btnAction.text = originalBtnText


            //show single/multiple coupon
            val popGratificationBenefits = data.popGratification?.popGratificationBenefits
            val multipleCoupon = popGratificationBenefits != null && popGratificationBenefits.size > 1 && popGratificationBenefits[0]?.referenceID != null && popGratificationBenefits[0]?.referenceID != 0
            if (multipleCoupon) {
                uiType = TargetPromotionsCouponType.MULTIPLE_COUPON
            }

            if (uiType == TargetPromotionsCouponType.MULTIPLE_COUPON) {
                val couponDetailList = ArrayList<GetCouponDetail>()
                if (couponDetailResponse.couponList != null) {
                    couponDetailList.addAll(couponDetailResponse.couponList)
                }
                couponListAdapter = CouponListAdapter(couponDetailList)
                recyclerView.adapter = couponListAdapter
                recyclerView.addItemDecoration(CouponItemDecoration())
                viewFlipper.displayedChild = CONTAINER_COUPON

            } else {

                val imageUrl = data.popGratification?.imageUrl
                val imageUrlMobile = data.popGratification?.imageUrlMobile
                val showError = TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(imageUrlMobile)
                if (showError) {
                    val urlToDisplay = if (TextUtils.isEmpty(imageUrl)) imageUrlMobile else imageUrl
                    uiType = TargetPromotionsCouponType.COUPON_ERROR
                }
                viewFlipper.displayedChild = CONTAINER_IMAGE
            }

        }

    }

    fun onActivityResumeIfWaitingForLogin() {
//        if (GratificationSubscriber.waitingForLogin.get()) {
//            GratificationSubscriber.waitingForLogin.set(false)
//            btnAction.performClick()
//        } else {
//            //Do nothing
//        }
//        subscriber.waitingForLoginActivity?.clear()
    }

    private fun setErrorUiForPopGratification() {
        viewFlipper.displayedChild = CONTAINER_IMAGE
        retryCount += 1

        val lessThanThreeTimes = retryCount < MAX_RETRY_COUNT
        val context = tvTitle.context
        tvTitle.text = context.getString(R.string.t_promo_disturbance_at_toko_house)
        tvSubTitle.text = context.getString(R.string.t_promo_we_will_fix_it_as_soon_as_poss)

        if (lessThanThreeTimes) {
            originalBtnText = context.getString(R.string.t_promo_coba_lagi)
        } else {
            originalBtnText = context.getString(R.string.t_promo_ke_homepage)
        }
        btnAction.text = originalBtnText
        imageView.loadImageGlide(R.drawable.t_promo_server_error)
    }

    private fun setUiForSuccessClaimGratification(data: ClaimPopGratificationResponse) {
        viewFlipper.displayedChild = CONTAINER_IMAGE
        tvTitle.text = data.popGratificationClaim?.title
        tvSubTitle.text = data.popGratificationClaim?.text
        originalBtnText = data.popGratificationClaim?.popGratificationActionButton?.text
        btnAction.text = originalBtnText
        imageView.loadImageGlide(data.popGratificationClaim?.imageUrlMobile)
        performAnimationToGotoClaimUI()
        couponCodeAfterClaim = data.popGratificationClaim?.dummyCouponCode
    }

    private fun setListeners(data: GratificationDataContract, activityContext: Context) {
        viewModel.couponClaimLiveData.observe(activityContext as AppCompatActivity, Observer { it ->
            when (it) {
                is Success -> {
                    setUiForSuccessClaimGratification(it.data)
                }
                is Error -> {
                    setErrorUiForPopGratification()
                }
            }
            toggleProgressBar(false)
            toggleBtnText(true)

        })

        viewModel.autoApplyLiveData.observe(activityContext as AppCompatActivity, Observer { it ->
            when (it) {
                is Success -> {
                    val messageList = it.data.tokopointsSetAutoApply?.resultStatus?.message
                    if (messageList != null && messageList.isNotEmpty())
                        Toaster.showNormal(nestedScrollView, messageList[0].toString(), Toast.LENGTH_SHORT)
                }
            }
        })

        btnAction.setOnClickListener {
            if (!skipBtnAction) {

                val retryAvailable = retryCount < MAX_RETRY_COUNT
                if (retryAvailable) {

                    toggleProgressBar(true)
                    toggleBtnText(false)
                    if (data is GetPopGratificationResponse) {
                        performActionToClaimCoupon(data, activityContext)
                    } else if (data is ClaimPopGratificationResponse) {
                        performActionAfterCouponIsClaimed(data)
                    } else {
                        shouldCallAutoApply = false
                        bottomSheetDialog.dismiss()
                    }
                } else {
                    RouteManager.route(btnAction.context, ApplinkConst.HOME)
                    shouldCallAutoApply = false
                    bottomSheetDialog.dismiss()
                }
                skipBtnAction = true
            }
        }
    }

    private fun toggleBtnText(show: Boolean) {
        if (show) {
            btnAction.text = originalBtnText
            skipBtnAction = false
        } else {
            btnAction.text = ""
            skipBtnAction = true
        }
    }

    private fun toggleProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun performAnimationToGotoClaimUI() {

//        val vg = viewFlipper.parent.parent.parent as ViewGroup
//        val touchOutside: View = vg.findViewById(R.id.touch_outside)
//        touchOutside.setOnClickListener {
//            viewModel.autoApply(couponCodeAfterClaim)
//        }
    }

    private fun performActionAfterCouponIsClaimed(data: ClaimPopGratificationResponse) {

        val applink = data.popGratificationClaim?.popGratificationActionButton?.appLink
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(btnAction.context, applink)
            shouldCallAutoApply = false
            bottomSheetDialog.dismiss()
        }
    }

    private fun performActionToClaimCoupon(data: GetPopGratificationResponse, activityContext: Activity) {

        val userSession = UserSession(activityContext)
        if (userSession.isLoggedIn) {

            val applink = data.popGratification?.popGratificationActionButton?.appLink
            if (!TextUtils.isEmpty(applink)) {
                RouteManager.route(btnAction.context, applink)
                shouldCallAutoApply = false
                bottomSheetDialog.dismiss()
            } else {

                val popGratificationBenefits = data.popGratification?.popGratificationBenefits
                if (popGratificationBenefits != null && popGratificationBenefits.isNotEmpty()) {
                    viewModel.claimCoupon(gratificationData.campaignSlug, gratificationData.page)
                }
            }
        } else {
//            subscriber.waitingForLoginActivity = WeakReference(activityContext)
//            GratificationSubscriber.waitingForLogin.set(true)
            val loginIntent = RouteManager.getIntent(activityContext, ApplinkConst.LOGIN)
            activityContext.startActivityForResult(loginIntent, REQUEST_CODE)
//            RouteManager.route(activityContext, ApplinkConst.LOGIN)
            shouldCallAutoApply = false
            bottomSheetDialog.dismiss()
        }
    }

    private fun initInjections(activityContext: Context) {
        val activity = activityContext as AppCompatActivity

        val component = DaggerPromoTargetComponent.builder()
                .appModule(AppModule(activity))
                .build()
        component.inject(this)
        activity.run {
            val viewModelProvider = ViewModelProviders.of(activityContext, viewModelFactory)
            viewModel = viewModelProvider[TargetPromotionsDialogVM::class.java]
        }
    }
}