package com.tokopedia.promotionstarget.presentation.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.CouponGratificationParams
import com.tokopedia.promotionstarget.data.CouponGratificationParams.CAMPAIGN_SLUG
import com.tokopedia.promotionstarget.data.CouponGratificationParams.POP_SLUG
import com.tokopedia.promotionstarget.data.GratificationDataContract
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.components.AppModule
import com.tokopedia.promotionstarget.data.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.domain.usecase.ClaimCouponApi
import com.tokopedia.promotionstarget.domain.usecase.ClaimCouponApiResponseCallback
import com.tokopedia.promotionstarget.presentation.loadImageGlide
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber
import com.tokopedia.promotionstarget.presentation.ui.CustomToast
import com.tokopedia.promotionstarget.presentation.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.presentation.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.TargetPromotionsDialogVM
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
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
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var imageViewRight: AppCompatImageView
    private lateinit var tvTitleRight: AppCompatTextView
    private lateinit var tvSubTitleRight: AppCompatTextView

    lateinit var viewModel: TargetPromotionsDialogVM
    private lateinit var gratificationData: GratificationData
    private lateinit var claimCouponApi: ClaimCouponApi

    private var data: GratificationDataContract? = null

    private var originalBtnText: String? = null
    private var couponCodeAfterClaim: String? = null
    private var shouldCallAutoApply = false
    private val rightViewList = ArrayList<View>()
    private val leftViewList = ArrayList<View>()
    private var bottomSheetFmContainer: ViewGroup? = null
    private var bottomSheetCoordinatorLayout: ViewGroup? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var autoApplyObserver: Observer<LiveDataResult<AutoApplyResponse>>? = null

    private var originallyLoggedIn = false
    private val REQUEST_CODE = 29
    private val MAX_RETRY_COUNT = 4
    private var retryCount = 0
    private var skipBtnAction = false
    private var screenWidth = 0f
    private var IS_DISMISSED = false

    companion object {
        const val PARAM_WAITING_FOR_LOGIN = "PARAM_WAITING_FOR_LOGIN"
    }

    private val couponClaimResponseCallback = object : ClaimCouponApiResponseCallback {
        override fun onNext(it: Result<ClaimPopGratificationResponse>) {
            when (it) {
                is Success -> {
                    setUiForSuccessClaimGratification(it.data)
                }
                is Error,
                is Fail -> {
                    setErrorUiForPopGratification()
                    expandBottomSheet()
                }
            }
            toggleProgressBar(false)
            toggleBtnText(true)
        }

        override fun onError(th: Throwable) {
            toggleProgressBar(false)
            toggleBtnText(true)
            setErrorUiForPopGratification()
            expandBottomSheet()
        }
    }

    var uiType: TargetPromotionsCouponType = TargetPromotionsCouponType.SINGLE_COUPON

    enum class TargetPromotionsCouponType {
        SINGLE_COUPON,
        MULTIPLE_COUPON,
        COUPON_ERROR
    }

    fun getLayout(couponUiType: TargetPromotionsCouponType): Int {
        uiType = couponUiType
        return R.layout.dialog_target_promotions
    }

    fun show(activityContext: Context,
             couponUiType: TargetPromotionsCouponType,
             data: GratificationDataContract,
             couponDetailResponse: GetCouponDetailResponse,
             gratificationData: GratificationData,
             claimCouponApi: ClaimCouponApi,
             autoHitActionButton: Boolean): Dialog? {
        if (activityContext is Activity && activityContext.isFinishing) {
            return null
        }
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activityContext)
        val view = LayoutInflater.from(activityContext).inflate(getLayout(couponUiType), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()
        bottomSheet.setOnDismissListener {
            val canHitAutoApply = !TextUtils.isEmpty(couponCodeAfterClaim) && shouldCallAutoApply
            if (canHitAutoApply) {
                viewModel.autoApply(couponCodeAfterClaim!!)
            }
            IS_DISMISSED = true
            if (activityContext is Activity) {
                subscriber.clearMaps(activityContext, !canHitAutoApply)
            }
        }
        bottomSheetDialog = bottomSheet
        this.claimCouponApi = claimCouponApi
        initViews(view, activityContext, data, couponDetailResponse, gratificationData)

        if (autoHitActionButton) {
            btnAction.performClick()
        }
        return bottomSheet
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(root: View, activityContext: Context,
                          data: GratificationDataContract,
                          couponDetailResponse: GetCouponDetailResponse,
                          gratificationData: GratificationData) {
        imageView = root.findViewById(R.id.imageView)
        tvTitle = root.findViewById(R.id.tvTitle)
        tvSubTitle = root.findViewById(R.id.tvSubTitle)
        btnAction = root.findViewById(R.id.btnAction)
        recyclerView = root.findViewById(R.id.recyclerView)
        viewFlipper = root.findViewById(R.id.viewFlipper)
        nestedScrollView = root.findViewById(R.id.nestedScrollView)
        progressBar = root.findViewById(R.id.targetProgressBar)
        imageViewRight = root.findViewById(R.id.imageViewRight)
        tvTitleRight = root.findViewById(R.id.tvTitleRight)
        tvSubTitleRight = root.findViewById(R.id.tvSubTitleRight)

        screenWidth = activityContext.resources.displayMetrics.widthPixels.toFloat()
        rightViewList.add(imageViewRight)
        rightViewList.add(tvSubTitleRight)
        rightViewList.add(tvTitleRight)

        leftViewList.add(viewFlipper)
        leftViewList.add(tvSubTitle)
        leftViewList.add(tvTitle)

        initialAnimation()

        recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)

        val pageSnaper = PagerSnapHelper()
        pageSnaper.attachToRecyclerView(recyclerView)

        recyclerView.isNestedScrollingEnabled = false

        val session = UserSession(activityContext)
        originallyLoggedIn = session.isLoggedIn

        this.data = data
        this.gratificationData = gratificationData

        initInjections(activityContext)
        setUiData(couponDetailResponse)
        setListeners(activityContext)

    }

    private fun initialAnimation() {
        rightViewList.forEach {
            it.translationX = screenWidth
            it.alpha = 0f
        }
    }

    private fun setUiData(couponDetailResponse: GetCouponDetailResponse) {

        if (data is GetPopGratificationResponse) {
            val couponDetail = data as GetPopGratificationResponse
            tvTitle.text = couponDetail.popGratification?.title
            tvSubTitle.text = couponDetail.popGratification?.text
            originalBtnText = couponDetail.popGratification?.popGratificationActionButton?.text
            btnAction.text = originalBtnText


            //show single/multiple coupon
            val popGratificationBenefits = couponDetail.popGratification?.popGratificationBenefits
            val multipleCoupon = popGratificationBenefits != null && popGratificationBenefits.isNotEmpty() && popGratificationBenefits[0]?.referenceID != null && popGratificationBenefits[0]?.referenceID != 0
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

                val imageUrl = couponDetail.popGratification?.imageUrl
                val imageUrlMobile = couponDetail.popGratification?.imageUrlMobile
                val showError = !TextUtils.isEmpty(imageUrl) || !TextUtils.isEmpty(imageUrlMobile)
                if (showError) {
                    val urlToDisplay = if (TextUtils.isEmpty(imageUrl)) imageUrlMobile else imageUrl
                    uiType = TargetPromotionsCouponType.COUPON_ERROR
                    imageView.loadImageGlide(urlToDisplay) { success ->
                        expandBottomSheet()
                    }
                }
                viewFlipper.displayedChild = CONTAINER_IMAGE
            }
        }
    }

    fun onActivityResumeIfWaitingForLogin(isLoggedIn: Boolean) {
        if (!originallyLoggedIn && !IS_DISMISSED && isLoggedIn) {
            skipBtnAction = false
            btnAction.performClick()
        }
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
        tvTitleRight.text = data.popGratificationClaim?.title
        tvSubTitleRight.text = data.popGratificationClaim?.text

        originalBtnText = data.popGratificationClaim?.popGratificationActionButton?.text
        btnAction.text = originalBtnText
        imageViewRight.loadImageGlide(data.popGratificationClaim?.imageUrl) { success ->
            expandBottomSheet()
        }

        performAnimationToGotoClaimUI()
        couponCodeAfterClaim = data.popGratificationClaim?.dummyCouponCode
        this.data = data
        shouldCallAutoApply = true
    }

    private fun setListeners(activityContext: Context) {

        removeAutoApplyLiveDataObserver()

        autoApplyObserver = Observer { it ->
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    val messageList = it.data?.tokopointsSetAutoApply?.resultStatus?.message
                    if (messageList != null && messageList.isNotEmpty())
                        CustomToast.show(activityContext, messageList[0].toString())
                }
            }
            removeAutoApplyLiveDataObserver()
        }

        viewModel.autoApplyLiveData.observe((activityContext as AppCompatActivity), autoApplyObserver!!)

        btnAction.setOnClickListener {
            shouldCallAutoApply = false
            if (!skipBtnAction) {

                val retryAvailable = retryCount < MAX_RETRY_COUNT
                if (retryAvailable) {

                    toggleProgressBar(true)
                    toggleBtnText(false)
                    if (data is GetPopGratificationResponse) {
                        performActionToClaimCoupon(data as GetPopGratificationResponse, activityContext)
                    } else if (data is ClaimPopGratificationResponse) {
                        performActionAfterCouponIsClaimed(activityContext, data as ClaimPopGratificationResponse)
                    } else {
                        bottomSheetDialog.dismiss()
                    }
                } else {
                    dropKeysFromBundle(ApplinkConst.HOME, activityContext.intent)
                    RouteManager.route(btnAction.context, ApplinkConst.HOME)
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

        val alphaDuration = 300L
        leftViewList.forEach {
            it.animate().setDuration(alphaDuration).alpha(0f)
        }

        val translateAnimationDuration = 600L

        imageView.animate().setDuration(translateAnimationDuration).translationX(-screenWidth)
        tvTitle.animate().setStartDelay(100L).setDuration(translateAnimationDuration).translationX(-screenWidth)
        tvSubTitle.animate().setStartDelay(150L).setDuration(translateAnimationDuration).translationX(-screenWidth)

        val startDelay = alphaDuration
        rightViewList.forEach {
            it.animate().setStartDelay(startDelay).setDuration(translateAnimationDuration).alpha(1f)
        }

        imageViewRight.animate().setStartDelay(startDelay).setDuration(translateAnimationDuration).translationX(0f)
        tvTitleRight.animate().setStartDelay(startDelay + 100L).setDuration(translateAnimationDuration).translationX(0f)
        tvSubTitleRight.animate().setStartDelay(startDelay + 150L).setDuration(translateAnimationDuration).translationX(0f)

    }

    private fun performActionAfterCouponIsClaimed(activityContext: Activity, data: ClaimPopGratificationResponse) {

        val applink = data.popGratificationClaim?.popGratificationActionButton?.appLink
        if (!TextUtils.isEmpty(applink)) {
            dropKeysFromBundle(applink, activityContext.intent)
            RouteManager.route(btnAction.context, applink)
            bottomSheetDialog.dismiss()
        }
    }

    private fun performActionToClaimCoupon(data: GetPopGratificationResponse, activityContext: Activity) {

        val userSession = UserSession(activityContext)
        if (userSession.isLoggedIn) {

            val applink = data.popGratification?.popGratificationActionButton?.appLink
            if (TextUtils.isEmpty(applink)) {
                val popGratificationBenefits = data.popGratification?.popGratificationBenefits
                if (popGratificationBenefits != null && popGratificationBenefits.isNotEmpty()) {
                    val claimPayload = ClaimPayload(gratificationData.popSlug, gratificationData.page)
                    claimCouponApi.claimGratification(claimPayload, couponClaimResponseCallback)
                }
            } else {
                dropKeysFromBundle(applink, activityContext.intent)
                RouteManager.route(btnAction.context, applink)
                bottomSheetDialog.dismiss()
            }
        } else {
            val loginIntent = RouteManager.getIntent(activityContext, ApplinkConst.LOGIN)
            activityContext.startActivityForResult(loginIntent, REQUEST_CODE)
            val bundle = addGratificationDataInBundleIfNotLoggedIn(activityContext, gratificationData)
            activityContext.intent.putExtras(bundle)
            activityContext.intent?.putExtra(PARAM_WAITING_FOR_LOGIN, true)

            val handler = Handler()
            handler.postDelayed({
                toggleProgressBar(false)
                toggleBtnText(true)
            }, 300L)
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

    private fun addGratificationDataInBundleIfNotLoggedIn(activityContext: Context, gratificationData: GratificationData): Bundle {
        val outerBundle = Bundle()
        if (activityContext is Activity && !UserSession(activityContext).isLoggedIn) {
            val innerBundle = Bundle()
            innerBundle.putString(CouponGratificationParams.POP_SLUG, gratificationData.popSlug)
            innerBundle.putString(CouponGratificationParams.PAGE, gratificationData.page)
            outerBundle.putBundle(RouteManager.QUERY_PARAM, innerBundle)
        }
        return outerBundle
    }


    private fun dropKeysFromBundle(applink: String?, intent: Intent): Intent {
        try {
            if (!applink.isNullOrEmpty()) {
                val uri = Uri.parse(applink)
                val queryParametersNames = uri.queryParameterNames
                val keyList = arrayListOf<String>(CAMPAIGN_SLUG, POP_SLUG)
                keyList.removeAll(queryParametersNames)
                val parentKey = RouteManager.QUERY_PARAM
                keyList.forEach {
                    intent.removeExtra(it)
                }
                val bundle = intent.getBundleExtra(parentKey)
                if (bundle != null) {
                    keyList.forEach {
                        bundle.remove(it)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return intent
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

    fun removeAutoApplyLiveDataObserver() {
        if (autoApplyObserver != null) {
            viewModel.autoApplyLiveData.removeObserver((autoApplyObserver!!))
        }
    }
}