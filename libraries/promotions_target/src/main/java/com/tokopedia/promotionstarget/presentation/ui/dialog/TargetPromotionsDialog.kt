package com.tokopedia.promotionstarget.presentation.ui.dialog

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.annotation.IntDef
import androidx.annotation.StringDef
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
import com.tokopedia.promotionstarget.data.claim.PopGratificationActionButton
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.components.AppModule
import com.tokopedia.promotionstarget.data.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.domain.usecase.ClaimCouponApi
import com.tokopedia.promotionstarget.domain.usecase.ClaimCouponApiResponseCallback
import com.tokopedia.promotionstarget.presentation.TargetedPromotionAnalytics
import com.tokopedia.promotionstarget.presentation.dpToPx
import com.tokopedia.promotionstarget.presentation.loadImageGlide
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber
import com.tokopedia.promotionstarget.presentation.ui.AnimationListener
import com.tokopedia.promotionstarget.presentation.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.presentation.ui.dialog.BtnType.Companion.DEFAULT
import com.tokopedia.promotionstarget.presentation.ui.dialog.BtnType.Companion.DISMISS
import com.tokopedia.promotionstarget.presentation.ui.dialog.PopUpVersion.Companion.AUTO_CLAIM
import com.tokopedia.promotionstarget.presentation.ui.dialog.PopUpVersion.Companion.NORMAL
import com.tokopedia.promotionstarget.presentation.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.TargetPromotionsDialogVM
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

//todo Rahul need to changes assets for non-login state
/**
 * Notes -
 * ImageViewRight is used to display image for non-logged in state
 * */

//todo Rahul List
// ALREADY LOGGED IN FLOW (DONE)
// RETRY FLOW (DONE)
// ALL ERROR STATES FOR BOTH UI (DONE)
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
    private var catalogId: Int = 0
    private lateinit var claimCouponApi: ClaimCouponApi

    private var data: GratificationDataContract? = null
    var popGratificationActionBtn: PopGratificationActionButton? = null

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

    @PopUpVersion
    private var popUpVersion = NORMAL
    private val referenceIds = arrayListOf<Int>()

    companion object {
        const val PARAM_WAITING_FOR_LOGIN = "PARAM_WAITING_FOR_LOGIN"
        const val PARAM_REFERENCE_ID = "PARAM_REFERENCE_ID"
    }

    private val couponClaimResponseCallback = object : ClaimCouponApiResponseCallback {
        override fun onNext(it: Result<ClaimPopGratificationResponse>) {
            when (it) {
                is Success -> {
                    setUiForSuccessClaimGratification(it.data)
                    TargetedPromotionAnalytics.viewClaimSuccess(it.data.popGratificationClaim?.title)
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

    fun showNonLoggedInUi(activityContext: Context,
                          data: GetPopGratificationResponse,
                          couponDetailResponse: GetCouponDetailResponse,
                          gratificationData: GratificationData): Dialog? {
        return nonLoggedInUiUi(activityContext, data, couponDetailResponse, gratificationData)
    }

    private fun nonLoggedInUiUi(activityContext: Context, getPopGratificationResponse: GetPopGratificationResponse, couponDetailResponse: GetCouponDetailResponse, gratificationData: GratificationData): Dialog? {
        popUpVersion = AUTO_CLAIM

        val pair = prepareBottomSheet(activityContext, TargetPromotionsCouponType.SINGLE_COUPON)
        val view = pair.first

        initViews(view, activityContext, getPopGratificationResponse, couponDetailResponse, gratificationData)
//        setNonLoginUiData(getPopGratificationResponse)
        setUiData(couponDetailResponse)
        setNonLoggedInListeners()
        return pair.second
    }

    fun showNonLoggedInDestroyedActivity(activityContext: Context,
                                         referenceId: IntArray,
                                         gratificationData: GratificationData): Dialog? {
        val bottomSheet = commonNonLoggedInUi(activityContext, null, gratificationData)

        referenceId.forEach {
            referenceIds.add(it)
        }
        viewModel.claimGratification(gratificationData.popSlug, gratificationData.page, referenceIds)
        return bottomSheet
    }

    private fun commonNonLoggedInUi(activityContext: Context, data: GratificationDataContract?, gratificationData: GratificationData): Dialog? {
        popUpVersion = AUTO_CLAIM

        val pair = prepareBottomSheet(activityContext, TargetPromotionsCouponType.SINGLE_COUPON)
        val view = pair.first

        initViews(view, activityContext, data, null, gratificationData)
//        setNonLoginUiData()
        setNonLoggedInListeners()
        return pair.second
    }

    //todo Rahul (DONE)
    fun showAutoClaimLoggedIn(activityContext: Context,
                              couponUiType: TargetPromotionsCouponType,
                              popGratificationResponse: GetPopGratificationResponse,
                              claimPopGratificationResponse: ClaimPopGratificationResponse?,
                              couponDetailResponse: GetCouponDetailResponse?,
                              gratificationData: GratificationData): Dialog? {
        popUpVersion = AUTO_CLAIM

        val pair = prepareBottomSheet(activityContext, couponUiType)
        val bottomSheet = pair.second
        val view = pair.first

        initViews(view, activityContext, claimPopGratificationResponse, couponDetailResponse, gratificationData)

        popGratificationResponse.popGratification?.popGratificationBenefits?.forEach { benefit ->
            benefit?.referenceID?.let { rId ->
                referenceIds.add(rId)
            }
        }
        if (claimPopGratificationResponse == null) {

            rightViewList.forEach {
                it.translationX = 0f
                it.alpha = 1f
            }

            showErrorUIForClaimGratificationLoggedIn()
        } else {
            setLoggedInUiForSuccessClaimGratificationVersionAutoClaim(claimPopGratificationResponse, couponDetailResponse)
        }
        observeLiveData(activityContext as AppCompatActivity, errorUi = { showErrorUIForClaimGratificationLoggedIn() })
        setListenersLoggedIn(activityContext as AppCompatActivity)

        return bottomSheet
    }


    private fun showErrorUIForClaimGratificationLoggedIn() {
        retryCount += 1
        val lessThanThreeTimes = retryCount < MAX_RETRY_COUNT
        val context = tvTitle.context
        tvTitleRight.text = context.getString(R.string.t_promo_disturbance_at_toko_house)
        tvSubTitleRight.text = context.getString(R.string.t_promo_we_will_fix_it_as_soon_as_poss)

        if (lessThanThreeTimes) {
            originalBtnText = context.getString(R.string.t_promo_coba_lagi)
        } else {
            originalBtnText = context.getString(R.string.t_promo_ke_homepage)
        }
        btnAction.text = originalBtnText
        imageViewRight.loadImageGlide(R.drawable.t_promo_server_error)
    }


    private fun setListenersLoggedIn(activity: AppCompatActivity) {

        btnAction.setOnClickListener {
            val btnActionText = btnAction.text.toString()

            if (!skipBtnAction) {
                val retryAvailable = retryCount < MAX_RETRY_COUNT
                if (retryAvailable) {
                    if (data == null) {
                        viewModel.claimGratification(gratificationData.popSlug, gratificationData.page, referenceIds)
                    } else {
                        performActionBasedOnClaim(popGratificationActionBtn)
                    }

                } else {
                    dismissAfterRetryIsOver(activity, btnActionText)
                }
            }
            skipBtnAction = true
        }
    }

    fun show(activityContext: Context,
             couponUiType: TargetPromotionsCouponType,
             data: GratificationDataContract,
             couponDetailResponse: GetCouponDetailResponse?,
             gratificationData: GratificationData,
             claimCouponApi: ClaimCouponApi,
             autoHitActionButton: Boolean): Dialog? {
        if (activityContext is Activity && activityContext.isFinishing) {
            return null
        }
        val pair = prepareBottomSheet(activityContext, couponUiType)
        val view = pair.first
        val bottomSheet = pair.second

        bottomSheet.setOnDismissListener {
            val canHitAutoApply = !TextUtils.isEmpty(couponCodeAfterClaim) && shouldCallAutoApply
            if (canHitAutoApply) {
                viewModel.autoApply(couponCodeAfterClaim!!)
            } else {
                removeAutoApplyLiveDataObserver()
            }
            IS_DISMISSED = true
        }

        this.claimCouponApi = claimCouponApi
        initViews(view, activityContext, data, couponDetailResponse, gratificationData)
        setListeners(activityContext)

        if (autoHitActionButton) {
            btnAction.performClick()
        }


        if (data is GetPopGratificationResponse) {
            val benefits = data.popGratification?.popGratificationBenefits
            if (benefits != null && benefits.isNotEmpty()) {
                val referenceId = benefits[0]?.referenceID
                if (referenceId != null) {
                    catalogId = referenceId
                }
            }
            TargetedPromotionAnalytics.viewCoupon(catalogId.toString(), UserSession(activityContext).isLoggedIn)
        }

        return bottomSheet
    }

    private fun prepareBottomSheet(activityContext: Context, couponUiType: TargetPromotionsCouponType): Pair<View, BottomSheetDialog> {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activityContext)
        val view = LayoutInflater.from(activityContext).inflate(getLayout(couponUiType), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()

        bottomSheetDialog = bottomSheet

        return Pair(view, bottomSheet)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(root: View, activityContext: Context,
                          data: GratificationDataContract?,
                          couponDetailResponse: GetCouponDetailResponse?,
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

        //todo Rahul think later
//        initialAnimation(popUpVersion)

        recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)

        val pageSnaper = PagerSnapHelper()
        pageSnaper.attachToRecyclerView(recyclerView)

        recyclerView.isNestedScrollingEnabled = false

        val session = UserSession(activityContext)
        originallyLoggedIn = session.isLoggedIn

        this.data = data
        this.gratificationData = gratificationData

        initInjections(activityContext)
        couponDetailResponse?.let {
            setUiData(it)
        }
    }

    private fun initialAnimation(@PopUpVersion popUpVersion: Int) {
        if (popUpVersion == AUTO_CLAIM) {
            leftViewList.forEach {
                it.translationX = screenWidth
                it.alpha = 0f
            }
        } else {
            rightViewList.forEach {
                it.translationX = screenWidth
                it.alpha = 0f
            }
        }

    }

    private fun setNonLoginUiData(popGratificationResponse: GetPopGratificationResponse) {
        tvTitleRight.text = tvTitle.context.getString(R.string.t_promo_non_login_title)
        tvSubTitleRight.text = tvTitle.context.getString(R.string.t_promo_non_login_sub_title)
        originalBtnText = tvTitle.context.getString(R.string.t_promo_login_sekarang)
        btnAction.text = originalBtnText
        imageViewRight.layoutParams.apply {
            height = imageViewRight.dpToPx(280).toInt()
        }
        imageViewRight.setImageResource(R.drawable.t_promo_non_login)
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

                tvSubTitle.text = tvSubTitle.context.getString(R.string.t_promo_apakah_kamu_mau_klaim_kupon_ini)

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
                    val label = couponDetail.popGratification?.title
                    if (!TextUtils.isEmpty(label)) {
                        TargetedPromotionAnalytics.couponClaimedLastOccasion(label!!)
                    }
                }
                viewFlipper.displayedChild = CONTAINER_IMAGE
            }
        }
    }

    fun onActivityResumeIfWaitingForLogin(isLoggedIn: Boolean) {
        if (!originallyLoggedIn && !IS_DISMISSED && isLoggedIn) {
            if (popUpVersion == AUTO_CLAIM) {
                if (data is GetPopGratificationResponse) {
                    viewModel.claimGratification(gratificationData.popSlug, gratificationData.page, (data as GetPopGratificationResponse).popGratification?.popGratificationBenefits?.map { it -> it?.referenceID })
                }
            } else {
                skipBtnAction = false
                btnAction.performClick()
            }
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

    //todo Rahul
    private fun setUiForSuccessClaimGratificationVersionAutoClaim(data: ClaimPopGratificationResponse, couponDetailResponse: GetCouponDetailResponse? = null) {
        tvTitle.text = data.popGratificationClaim?.title
        tvSubTitle.text = data.popGratificationClaim?.text
        originalBtnText = data.popGratificationClaim?.popGratificationActionButton?.text
        btnAction.text = originalBtnText
        popGratificationActionBtn = data.popGratificationClaim?.popGratificationActionButton
        this.data = data


        val couponDetailList = ArrayList<GetCouponDetail>()
        couponDetailResponse?.couponList?.let {
            couponDetailList.addAll(it)
            couponListAdapter = CouponListAdapter(couponDetailList)
            recyclerView.adapter = couponListAdapter
            recyclerView.addItemDecoration(CouponItemDecoration())
            viewFlipper.displayedChild = CONTAINER_COUPON
        }

        //todo Rahul-> we have to use rightImage & right textviews
        if (couponDetailList.isEmpty()) {
            imageView.loadImageGlide(data.popGratificationClaim?.imageUrl) { success ->
                expandBottomSheet()
                viewFlipper.displayedChild = CONTAINER_IMAGE
            }
        }

        //todo Rahul - need to add new animation of just fadein-fade out -> no translatio
        // or do transation from coupon to non coupon, in case of coupon to coupon -> do fade effect
//        performAnimationClaimUiVersionAutoClaim()

    }

    //todo Rahul
    private fun setLoggedInUiForSuccessClaimGratificationVersionAutoClaim(data: ClaimPopGratificationResponse, couponDetailResponse: GetCouponDetailResponse? = null) {

        tvTitle.text = data.popGratificationClaim?.title
        tvSubTitle.text = data.popGratificationClaim?.text
        originalBtnText = data.popGratificationClaim?.popGratificationActionButton?.text
        btnAction.text = originalBtnText
        popGratificationActionBtn = data.popGratificationClaim?.popGratificationActionButton
        this.data = data

        //load data in rv
        // check for error as well

        //todo check 1
        val couponDetailList = ArrayList<GetCouponDetail>()
        couponDetailResponse?.couponList?.let {
            couponDetailList.addAll(it)
            couponListAdapter = CouponListAdapter(couponDetailList)
            recyclerView.adapter = couponListAdapter
            recyclerView.addItemDecoration(CouponItemDecoration())
            viewFlipper.displayedChild = CONTAINER_COUPON
        }

        //todo check 2
        if (couponDetailList.isEmpty()) {
            imageView.loadImageGlide(data.popGratificationClaim?.imageUrl) { success ->
                expandBottomSheet()
            }
        }
    }

    //todo Rahul (NOW - coupod detail is empty because benefits are empty in claim response)
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

        if (!couponCodeAfterClaim.isNullOrEmpty()) {
            tvSubTitleRight.text = tvSubTitleRight.context.getString(R.string.t_promo_kupon_sudah_ada_dihalaman_keranjangmu_ya)
            originalBtnText = tvSubTitleRight.context.getString(R.string.t_promo_belanja_sekarang)
            btnAction.text = originalBtnText
        }
    }

    //todo Rahul not ready
    private fun setNonLoggedInListeners() {
        if (btnAction.context is AppCompatActivity) {
            val activity = btnAction.context as AppCompatActivity

            btnAction.setOnClickListener {
                val btnActionText = btnAction.text.toString()

                if (!skipBtnAction) {
                    val retryAvailable = retryCount < MAX_RETRY_COUNT
                    if (retryAvailable) {

                        if (UserSession(activity).isLoggedIn) {

                            //coupon is yet to be claimed
                            if (data == null && referenceIds.isNotEmpty()) {
                                //activity was destroyed - case
                                viewModel.claimGratification(gratificationData.popSlug, gratificationData.page, referenceIds)
                            } else if (data is GetPopGratificationResponse) {
                                viewModel.claimGratification(gratificationData.popSlug,
                                        gratificationData.page,
                                        (data as GetPopGratificationResponse).popGratification?.popGratificationBenefits?.map { benefit -> benefit?.referenceID })
                            } else {
                                //coupon is claimed
                                performActionBasedOnClaim(popGratificationActionBtn)
                            }
                        } else {
                            //todo Rahul uncomment
//                            routeToLogin(activity)
                            fake1()
                        }
                    } else {
                        dismissAfterRetryIsOver(activity, btnActionText)
                    }
                }
                skipBtnAction = true
            }

            //todo Rahul - set error ui (DONE)
            observeLiveData(activity, errorUi = { showErrorUIForClaimGratification() })
        }
    }

    fun fake1(){
        viewModel.claimGratification(gratificationData.popSlug,gratificationData.page, arrayListOf(2446))
    }

    fun observeLiveData(activity: AppCompatActivity, errorUi: (() -> Unit)? = null) {
        viewModel.claimApiLiveData.observe(activity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    toggleProgressBar(false)
                    toggleBtnText(true)
                    if (it.data != null && it.data.first.popGratificationClaim?.resultStatus?.code == "200") {
                        setUiForSuccessClaimGratificationVersionAutoClaim(it.data.first, it.data.second)
                    } else {
                        errorUi?.invoke()
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    toggleProgressBar(false)
                    toggleBtnText(true)
                    errorUi?.invoke()
                }

                LiveDataResult.STATUS.LOADING -> {
                    toggleProgressBar(true)
                    toggleBtnText(false)
                }
            }
        })
    }

    fun performActionBasedOnClaim(popGratificationActionButton: PopGratificationActionButton?) {
        if (popGratificationActionButton != null) {
            if (!popGratificationActionButton.type.isNullOrEmpty() && popGratificationActionButton.type == DISMISS) {
                bottomSheetDialog.dismiss()
            } else if (!popGratificationActionButton.appLink.isNullOrEmpty()) {
                RouteManager.route(btnAction.context, popGratificationActionButton.appLink)
            } else {
                bottomSheetDialog.dismiss()
            }
        } else {
            bottomSheetDialog.dismiss()
        }
    }

    fun dismissAfterRetryIsOver(activityContext: AppCompatActivity, btnActionText: String) {
        dropKeysFromBundle(ApplinkConst.HOME, activityContext.intent)
        RouteManager.route(btnAction.context, ApplinkConst.HOME)
        bottomSheetDialog.dismiss()

        TargetedPromotionAnalytics.performButtonAction(btnActionText)
    }

    private fun showErrorUIForClaimGratification(useLeftView: Boolean = true) {
        retryCount += 1
        val lessThanThreeTimes = retryCount < MAX_RETRY_COUNT
        val context = tvTitleRight.context

        if (useLeftView) {
            tvTitle.text = context.getString(R.string.t_promo_disturbance_at_toko_house)
            tvSubTitle.text = context.getString(R.string.t_promo_we_will_fix_it_as_soon_as_poss)
            imageViewRight.loadImageGlide(R.drawable.t_promo_server_error)
        } else {
            tvTitleRight.text = context.getString(R.string.t_promo_disturbance_at_toko_house)
            tvSubTitleRight.text = context.getString(R.string.t_promo_we_will_fix_it_as_soon_as_poss)
            imageViewRight.loadImageGlide(R.drawable.t_promo_server_error)
        }

        if (lessThanThreeTimes) {
            originalBtnText = context.getString(R.string.t_promo_coba_lagi)
        } else {
            originalBtnText = context.getString(R.string.t_promo_ke_homepage)
        }
        btnAction.text = originalBtnText

    }

    private fun setListeners(activityContext: Context) {

        removeAutoApplyLiveDataObserver()

        autoApplyObserver = Observer {
            removeAutoApplyLiveDataObserver()
        }

        viewModel.autoApplyLiveData.observe((activityContext as AppCompatActivity), autoApplyObserver!!)

        btnAction.setOnClickListener {
            val btnActionText = btnAction.text.toString()
            shouldCallAutoApply = false
            if (!skipBtnAction) {

                val retryAvailable = retryCount < MAX_RETRY_COUNT
                if (retryAvailable) {

                    toggleProgressBar(true)
                    toggleBtnText(false)
                    if (data is GetPopGratificationResponse) {
                        performActionToClaimCoupon(data as GetPopGratificationResponse, activityContext, btnActionText)
                    } else if (data is ClaimPopGratificationResponse) {
                        performActionAfterCouponIsClaimed(activityContext, data as ClaimPopGratificationResponse, btnActionText)
                    } else {
                        bottomSheetDialog.dismiss()
                    }
                } else {
                    dismissAfterRetryIsOver(activityContext, btnActionText)
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

    //todo Rahul same code as performAnimationToGotoClaimUI - refactor this
    // todo Rahul imageView is switched with imageViewRight
    private fun performAnimationClaimUiVersionAutoClaim() {
        val alphaDuration = 300L
        val translateAnimationDuration = 600L
        val startDelay = alphaDuration

        //fade out
        rightViewList.forEach {
            it.animate().setDuration(alphaDuration).alpha(0f)
        }

        imageViewRight.animate().setDuration(translateAnimationDuration).translationX(-screenWidth).setListener(object : AnimationListener() {
            override fun onAnimationEnd(animation: Animator?) {
                imageViewRight.visibility = View.GONE
                expandBottomSheet()
            }
        })
        tvTitleRight.animate().setStartDelay(100L).setDuration(translateAnimationDuration).translationX(-screenWidth)
        tvSubTitleRight.animate().setStartDelay(150L).setDuration(translateAnimationDuration).translationX(-screenWidth)


        //fade in

        leftViewList.forEach {
            it.animate().setStartDelay(startDelay).setDuration(translateAnimationDuration).alpha(1f)
        }

        viewFlipper.animate().setStartDelay(startDelay).setDuration(translateAnimationDuration).translationX(0f)
        tvTitle.animate().setStartDelay(startDelay + 100L).setDuration(translateAnimationDuration).translationX(0f)
        tvSubTitle.animate().setStartDelay(startDelay + 150L).setDuration(translateAnimationDuration).translationX(0f)
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

    //todo Rahul needs to refactor
    private fun performActionAfterCouponIsClaimed(activityContext: Activity, data: ClaimPopGratificationResponse, buttonText: String) {

        val applink = data.popGratificationClaim?.popGratificationActionButton?.appLink
        if (!TextUtils.isEmpty(applink)) {
            dropKeysFromBundle(applink, activityContext.intent)
        }
        shouldCallAutoApply = true
        bottomSheetDialog.dismiss()
        TargetedPromotionAnalytics.userClickCheckMyCoupon(buttonText)
    }

    private fun performActionToClaimCoupon(data: GetPopGratificationResponse, activityContext: Activity, btnActionText: String) {

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

            routeToLogin(activityContext)
            val applink = data.popGratification?.popGratificationActionButton?.appLink
            if (retryCount > 0) {
                TargetedPromotionAnalytics.tryAgain()
            } else if (TextUtils.isEmpty(applink)) {
                TargetedPromotionAnalytics.clickClaimCoupon(catalogId.toString(), userSession.isLoggedIn)
            } else {
                TargetedPromotionAnalytics.performButtonAction(btnActionText)
            }
        }
    }

    private fun routeToLogin(activityContext: Activity) {
        val bundle = addGratificationDataInBundleIfNotLoggedIn(activityContext, gratificationData)
        activityContext.intent.putExtras(bundle)
        activityContext.intent?.putExtra(PARAM_WAITING_FOR_LOGIN, true)
        if (popUpVersion == AUTO_CLAIM) {
            activityContext.intent?.putExtra(PARAM_WAITING_FOR_LOGIN, true)

            if (data is GetPopGratificationResponse) {
                val benefits = (data as GetPopGratificationResponse).popGratification?.popGratificationBenefits
                if (!benefits.isNullOrEmpty()) {
                    val intArray = IntArray(benefits.size)
                    benefits.forEachIndexed { index, item ->
                        item?.referenceID?.let { referenceID ->
                            intArray[index] = referenceID
                        }
                    }

                    if (intArray.isNotEmpty() && intArray[0] != 0) {
                        activityContext.intent?.putExtra(PARAM_REFERENCE_ID, intArray)
                    }

                }
            }
        }

        val loginIntent = RouteManager.getIntent(activityContext, ApplinkConst.LOGIN)
        activityContext.startActivityForResult(loginIntent, REQUEST_CODE)

        val handler = Handler()
        handler.postDelayed({
            toggleProgressBar(false)
            toggleBtnText(true)
        }, 300L)
    }


    private fun initInjections(activityContext: Context) {
        val activity = activityContext as AppCompatActivity

        val component = DaggerPromoTargetComponent.builder()
                .appModule(AppModule(activity.application))
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

@Retention(AnnotationRetention.SOURCE)
@StringDef(DISMISS, DEFAULT)
annotation class BtnType {
    companion object {
        const val DISMISS = "dismiss"
        const val DEFAULT = "default"
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(NORMAL, AUTO_CLAIM)
annotation class PopUpVersion {
    companion object {
        const val NORMAL = 0
        const val AUTO_CLAIM = 1
    }
}