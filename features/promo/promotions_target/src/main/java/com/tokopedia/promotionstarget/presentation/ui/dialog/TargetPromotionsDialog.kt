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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
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
import com.tokopedia.promotionstarget.data.coupon.CouponUiData
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.domain.usecase.ClaimCouponApi
import com.tokopedia.promotionstarget.domain.usecase.ClaimCouponApiResponseCallback
import com.tokopedia.promotionstarget.presentation.TargetedPromotionAnalytics
import com.tokopedia.promotionstarget.presentation.loadImageGlide
import com.tokopedia.promotionstarget.presentation.loadImageWithNoPlaceholder
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber
import com.tokopedia.promotionstarget.presentation.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.presentation.ui.dialog.BtnType.Companion.DISMISS
import com.tokopedia.promotionstarget.presentation.ui.dialog.BtnType.Companion.EMPTY
import com.tokopedia.promotionstarget.presentation.ui.dialog.BtnType.Companion.REDIRECT
import com.tokopedia.promotionstarget.presentation.ui.dialog.PopUpVersion.Companion.AUTO_CLAIM
import com.tokopedia.promotionstarget.presentation.ui.dialog.PopUpVersion.Companion.NORMAL
import com.tokopedia.promotionstarget.presentation.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.TargetPromotionsDialogViewModel
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

    lateinit var viewModel: TargetPromotionsDialogViewModel
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

                    val userSession = UserSession(btnAction.context)
                    TargetedPromotionAnalytics.viewClaimSuccess(it.data.popGratificationClaim?.title, userSession.userId)
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
                          couponDetailResponse: GetCouponDetailResponse?,
                          gratificationData: GratificationData): Dialog? {

        DestroyedActivity.couponDetailResponse = couponDetailResponse
        DestroyedActivity.popGratificationResponse = data

        return nonLoggedInUiUi(activityContext, data, couponDetailResponse, gratificationData)
    }

    private fun nonLoggedInUiUi(activityContext: Context, getPopGratificationResponse: GetPopGratificationResponse, couponDetailResponse: GetCouponDetailResponse?, gratificationData: GratificationData): Dialog? {
        popUpVersion = AUTO_CLAIM

        val pair = prepareBottomSheet(activityContext, TargetPromotionsCouponType.SINGLE_COUPON)
        val view = pair.first

        initViews(view, activityContext, getPopGratificationResponse, couponDetailResponse, gratificationData)

        setNonLoggedInListeners()

        prePareCatalogId(getPopGratificationResponse)
        TargetedPromotionAnalytics.viewCoupon(catalogId.toString(), false, "")

        pair.second.setOnDismissListener {
            shouldCallAutoApply = true
            autoApplyApi()
            IS_DISMISSED = true
        }

        return pair.second
    }

    fun showNonLoggedInDestroyedActivity(activityContext: Context,
                                         gratificationData: GratificationData): Dialog? {
        if (DestroyedActivity.popGratificationResponse != null && DestroyedActivity.couponDetailResponse != null) {

            val dialog = nonLoggedInUiUi(activityContext, DestroyedActivity.popGratificationResponse!!, DestroyedActivity.couponDetailResponse!!, gratificationData)
            viewModel.claimGratification(gratificationData.popSlug, gratificationData.page, referenceIds)
            return dialog
        }
        return null
    }

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

        bottomSheet.setOnDismissListener {
            shouldCallAutoApply = true
            autoApplyApi()
            IS_DISMISSED = true
        }

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
            expandBottomSheet()
        } else {
            setLoggedInUiForSuccessClaimGratificationVersionAutoClaim(claimPopGratificationResponse, couponDetailResponse)
        }
        observeLiveData(activityContext as AppCompatActivity, errorUi = { showErrorUIForClaimGratificationLoggedIn() })
        setListenersLoggedIn(activityContext as AppCompatActivity)
        prePareCatalogId(popGratificationResponse)
        TargetedPromotionAnalytics.viewCoupon(catalogId.toString(), true, UserSession(btnAction.context).userId)
        return bottomSheet
    }


    private fun showErrorUIForClaimGratificationLoggedIn() {
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
        imageViewRight.loadImageGlide(R.drawable.t_promo_server_error)
        imageViewRight.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }


    private fun setListenersLoggedIn(activity: AppCompatActivity) {
        val userSession = UserSession(btnAction.context)

        btnAction.setOnClickListener {
            val btnActionText = btnAction.text.toString()
            if (!skipBtnAction) {
                val retryAvailable = retryCount < MAX_RETRY_COUNT
                if (retryAvailable) {
                    if (data == null) {
                        viewModel.claimGratification(gratificationData.popSlug, gratificationData.page, referenceIds)
                        TargetedPromotionAnalytics.tryAgain(userSession.userId)
                    } else {
                        performActionBasedOnClaim(popGratificationActionBtn)

                        TargetedPromotionAnalytics.clickClaimCoupon(catalogId.toString(), userSession.isLoggedIn, btnActionText, userSession.userId)
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
            autoApplyApi()
            IS_DISMISSED = true
        }

        this.claimCouponApi = claimCouponApi
        initViews(view, activityContext, data, couponDetailResponse, gratificationData)
        setListeners(activityContext)

        if (autoHitActionButton) {
            btnAction.performClick()
        }


        if (data is GetPopGratificationResponse) {
            prePareCatalogId(data)
            val userSession = UserSession(activityContext)
            TargetedPromotionAnalytics.viewCoupon(catalogId.toString(), userSession.isLoggedIn, userSession.userId)
        }

        return bottomSheet
    }

    private fun autoApplyApi() {
        val canHitAutoApply = !TextUtils.isEmpty(couponCodeAfterClaim) && shouldCallAutoApply
        if (canHitAutoApply) {
            viewModel.autoApply(couponCodeAfterClaim!!)
        } else {
            removeAutoApplyLiveDataObserver()
        }
    }

    private fun prepareBottomSheet(activityContext: Context, couponUiType: TargetPromotionsCouponType): Pair<View, BottomSheetDialog> {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceCloseableRounded(activityContext, {})
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

        try {
            val imageClose = (root.parent.parent as ConstraintLayout).findViewById<ImageView>(R.id.close_button_rounded)
            imageClose.setImageResource(R.drawable.t_promo_close)
        } catch (th: Throwable) {
        }

        screenWidth = activityContext.resources.displayMetrics.widthPixels.toFloat()
        rightViewList.add(imageViewRight)
        rightViewList.add(tvSubTitleRight)
        rightViewList.add(tvTitleRight)

        leftViewList.add(viewFlipper)
        leftViewList.add(tvSubTitle)
        leftViewList.add(tvTitle)

        initialAnimation(popUpVersion)

        recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)

        val pageSnaper = PagerSnapHelper()
        pageSnaper.attachToRecyclerView(recyclerView)

        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(CouponItemDecoration())

        val session = UserSession(activityContext)
        originallyLoggedIn = session.isLoggedIn

        this.data = data
        this.gratificationData = gratificationData

        initInjections(activityContext)
        viewModel.gratificationData = gratificationData

        couponDetailResponse?.let {
            setUiData(it)
        }
    }

    private fun initialAnimation(@PopUpVersion popUpVersion: Int) {
        if (popUpVersion == NORMAL) {
            rightViewList.forEach {
                it.translationX = screenWidth
                it.alpha = 0f
            }
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
                val couponDetailList = ArrayList<CouponUiData>()
                if (couponDetailResponse.couponList != null) {
                    couponDetailList.addAll(couponDetailResponse.couponList)
                }
                imageViewRight.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                couponListAdapter = CouponListAdapter(couponDetailList)
                recyclerView.adapter = couponListAdapter

                viewFlipper.displayedChild = CONTAINER_COUPON

            } else {

                val imageUrl = couponDetail.popGratification?.imageUrl
                val imageUrlMobile = couponDetail.popGratification?.imageUrlMobile
                val showError = !TextUtils.isEmpty(imageUrl) || !TextUtils.isEmpty(imageUrlMobile)
                if (showError) {
                    val urlToDisplay = if (TextUtils.isEmpty(imageUrl)) imageUrlMobile else imageUrl
                    uiType = TargetPromotionsCouponType.COUPON_ERROR
                    imageView.loadImageWithNoPlaceholder(urlToDisplay) { success ->
                        expandBottomSheet()
                    }
                    val label = couponDetail.popGratification?.title
                    if (!TextUtils.isEmpty(label)) {
                        val userSession = UserSession(imageView.context)
                        TargetedPromotionAnalytics.couponClaimedLastOccasion(label!!, userSession.userId)
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

    private fun setUiForSuccessClaimGratificationVersionAutoClaim(data: ClaimPopGratificationResponse, couponDetailResponse: GetCouponDetailResponse? = null) {
        tvTitle.text = data.popGratificationClaim?.title
        tvSubTitle.text = data.popGratificationClaim?.text
        originalBtnText = data.popGratificationClaim?.popGratificationActionButton?.text
        btnAction.text = originalBtnText
        popGratificationActionBtn = data.popGratificationClaim?.popGratificationActionButton
        this.data = data


        val couponDetailList = ArrayList<CouponUiData>()
        couponDetailResponse?.couponList?.let {
            imageViewRight.visibility = View.GONE
            couponDetailList.addAll(it)
            couponListAdapter = CouponListAdapter(couponDetailList)
            recyclerView.adapter = couponListAdapter
            recyclerView.visibility = View.VISIBLE
            viewFlipper.displayedChild = CONTAINER_COUPON
        }

        if (couponDetailList.isEmpty()) {
            imageView.loadImageWithNoPlaceholder(data.popGratificationClaim?.imageUrl) { success ->
                expandBottomSheet()
                viewFlipper.displayedChild = CONTAINER_IMAGE
            }
        }
    }

    private fun setLoggedInUiForSuccessClaimGratificationVersionAutoClaim(data: ClaimPopGratificationResponse, couponDetailResponse: GetCouponDetailResponse? = null) {

        tvTitle.text = data.popGratificationClaim?.title
        tvSubTitle.text = data.popGratificationClaim?.text
        originalBtnText = data.popGratificationClaim?.popGratificationActionButton?.text
        btnAction.text = originalBtnText
        popGratificationActionBtn = data.popGratificationClaim?.popGratificationActionButton
        this.data = data

        val couponDetailList = ArrayList<CouponUiData>()
        couponDetailResponse?.couponList?.let {
            if (!it.isNullOrEmpty()) {
                if (it[0].id != null && it[0].id != 0) {
                    imageViewRight.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    couponDetailList.addAll(it)
                    couponListAdapter = CouponListAdapter(couponDetailList)
                    recyclerView.adapter = couponListAdapter
                    viewFlipper.displayedChild = CONTAINER_COUPON
                }
            }
        }

        if (couponDetailList.isEmpty()) {
            viewFlipper.displayedChild = CONTAINER_IMAGE
            imageView.loadImageWithNoPlaceholder(data.popGratificationClaim?.imageUrl) { success ->
                expandBottomSheet()
            }
        }

        couponCodeAfterClaim = data.popGratificationClaim?.dummyCouponCode
    }

    private fun setUiForSuccessClaimGratification(data: ClaimPopGratificationResponse) {
        tvTitleRight.text = data.popGratificationClaim?.title
        tvSubTitleRight.text = data.popGratificationClaim?.text

        originalBtnText = data.popGratificationClaim?.popGratificationActionButton?.text
        btnAction.text = originalBtnText

        imageViewRight.visibility = View.VISIBLE

        imageViewRight.loadImageWithNoPlaceholder(data.popGratificationClaim?.imageUrl) { success ->
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

    private fun setNonLoggedInListeners() {
        if (btnAction.context is AppCompatActivity) {
            val activity = btnAction.context as AppCompatActivity

            btnAction.setOnClickListener {
                val btnActionText = btnAction.text.toString()

                if (!skipBtnAction) {
                    val retryAvailable = retryCount < MAX_RETRY_COUNT
                    if (retryAvailable) {

                        val userSession = UserSession(activity)
                        if (userSession.isLoggedIn) {

                            //coupon is yet to be claimed
                            if (data == null && referenceIds.isNotEmpty()) {
                                //activity was destroyed - case
                                viewModel.claimGratification(gratificationData.popSlug, gratificationData.page, referenceIds)
                                TargetedPromotionAnalytics.tryAgain(userSession.userId)
                            } else if (data is GetPopGratificationResponse) {
                                viewModel.claimGratification(gratificationData.popSlug,
                                        gratificationData.page,
                                        (data as GetPopGratificationResponse).popGratification?.popGratificationBenefits?.map { benefit -> benefit?.referenceID })
                                TargetedPromotionAnalytics.tryAgain(userSession.userId)
                            } else {
                                //coupon is claimed
                                performActionBasedOnClaim(popGratificationActionBtn)
                                TargetedPromotionAnalytics.clickClaimCoupon(catalogId.toString(), userSession.isLoggedIn, btnActionText, userSession.userId)
                            }
                        } else {
                            routeToLogin(activity)
                            TargetedPromotionAnalytics.clickClaimCoupon(catalogId.toString(), userSession.isLoggedIn, btnActionText, userSession.userId)
                        }

                    } else {
                        dismissAfterRetryIsOver(activity, btnActionText)
                    }
                }
                skipBtnAction = true
            }

            observeLiveData(activity, errorUi = { showErrorUIForClaimGratification() })
        }
    }

    fun observeLiveData(activity: AppCompatActivity, errorUi: (() -> Unit)? = null) {
        viewModel.claimApiLiveData.observe(activity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    toggleProgressBar(false)
                    toggleBtnText(true)
                    if (it.data != null && it.data.first.popGratificationClaim?.resultStatus?.code == "200") {
                        setUiForSuccessClaimGratificationVersionAutoClaim(it.data.first, it.data.second)
                        couponCodeAfterClaim = it.data.first.popGratificationClaim?.dummyCouponCode
                        shouldCallAutoApply = true
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
            popGratificationActionButton.type?.let { type ->
                when (type) {
                    REDIRECT -> {
                        if (!popGratificationActionButton.appLink.isNullOrEmpty()) {
                            RouteManager.route(btnAction.context, popGratificationActionButton.appLink)
                        }
                    }
                }
            }
        }
        bottomSheetDialog.dismiss()
    }

    fun dismissAfterRetryIsOver(activityContext: AppCompatActivity, btnActionText: String) {
        dropKeysFromBundle(ApplinkConst.HOME, activityContext.intent)
        RouteManager.route(btnAction.context, ApplinkConst.HOME)
        bottomSheetDialog.dismiss()

        val userSession = UserSession(btnAction.context)
        TargetedPromotionAnalytics.performButtonAction(btnActionText, userSession.userId)
    }

    private fun showErrorUIForClaimGratification(useLeftView: Boolean = true) {
        retryCount += 1
        val lessThanThreeTimes = retryCount < MAX_RETRY_COUNT
        val context = tvTitleRight.context

        if (useLeftView) {
            tvTitle.text = context.getString(R.string.t_promo_disturbance_at_toko_house)
            tvSubTitle.text = context.getString(R.string.t_promo_we_will_fix_it_as_soon_as_poss)
        } else {
            tvTitleRight.text = context.getString(R.string.t_promo_disturbance_at_toko_house)
            tvSubTitleRight.text = context.getString(R.string.t_promo_we_will_fix_it_as_soon_as_poss)
        }

        imageViewRight.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        imageViewRight.loadImageGlide(R.drawable.t_promo_server_error)

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

    private fun performActionAfterCouponIsClaimed(activityContext: Activity, data: ClaimPopGratificationResponse, buttonText: String) {

        val applink = data.popGratificationClaim?.popGratificationActionButton?.appLink
        if (!TextUtils.isEmpty(applink)) {
            dropKeysFromBundle(applink, activityContext.intent)
        }
        shouldCallAutoApply = true
        bottomSheetDialog.dismiss()

        val userSession = UserSession(btnAction.context)
        TargetedPromotionAnalytics.userClickCheckMyCoupon(buttonText, userSession.userId)
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
                TargetedPromotionAnalytics.tryAgain(userSession.userId)
            } else if (TextUtils.isEmpty(applink)) {
                TargetedPromotionAnalytics.clickClaimCoupon(catalogId.toString(), userSession.isLoggedIn, btnActionText, userSession.userId)
            } else {
                TargetedPromotionAnalytics.performButtonAction(btnActionText, userSession.userId)
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
            viewModel = viewModelProvider[TargetPromotionsDialogViewModel::class.java]
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
            BottomSheetBehavior.from(bottomSheetFmContainer as FrameLayout).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun removeAutoApplyLiveDataObserver() {
        if (autoApplyObserver != null) {
            viewModel.autoApplyLiveData.removeObserver((autoApplyObserver!!))
        }
    }

    fun prePareCatalogId(data: GetPopGratificationResponse) {
        val benefits = data.popGratification?.popGratificationBenefits
        if (benefits != null && benefits.isNotEmpty()) {
            val referenceId = benefits[0]?.referenceID
            if (referenceId != null) {
                catalogId = referenceId
            }
        }
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(DISMISS, EMPTY, REDIRECT)
annotation class BtnType {
    companion object {
        const val DISMISS = "dismiss"
        const val EMPTY = ""
        const val REDIRECT = "redirect"
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