package com.tokopedia.promotionstarget.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.promotionstarget.subscriber.GratificationSubscriber
import com.tokopedia.promotionstarget.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.ui.viewmodel.TargetPromotionsDialogVM
import com.tokopedia.promotionstarget.ui.views.TargetPromoProgressBar
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import java.lang.ref.WeakReference
import javax.inject.Inject

//todo use weak reference of activity context
class TargetPromotionsDialog(val subscriber: GratificationSubscriber) {

    val CONTAINER_COUPON = 0
    val CONTAINER_IMAGE = 1

    private lateinit var tvTitle: Typography
    private lateinit var tvSubTitle: Typography
    private lateinit var imageView: AppCompatImageView
    private lateinit var btnAction: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var couponListAdapter: CouponListAdapter
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var bottomSheetDialog: Dialog
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: TargetPromoProgressBar

    lateinit var viewModel: TargetPromotionsDialogVM
    private var liveData: MutableLiveData<Result<ClaimPopGratificationResponse>>? = null
    private var fakeLiveData: MutableLiveData<Result<String>>? = null
    private var data: GratificationDataContract? = null


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var originallyLoggedIn = false


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
             couponDetailResponse: GetCouponDetailResponse): Dialog {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activityContext)
        val view = LayoutInflater.from(activityContext).inflate(getLayout(couponUiType), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()
        bottomSheet.setOnDismissListener {
            GratificationSubscriber.waitingForLogin.set(false)
        }

        initViews(view, activityContext, data, couponDetailResponse)
        return bottomSheet
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(root: View, activityContext: Context, data: GratificationDataContract, couponDetailResponse: GetCouponDetailResponse) {
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
        initInjections(activityContext)
        setUiData(data, couponDetailResponse)
        setListeners(data, activityContext)
    }


    private fun setUiData(data: GratificationDataContract, couponDetailResponse: GetCouponDetailResponse) {

        if (data is GetPopGratificationResponse) {
            tvTitle.text = data.popGratification?.title
            tvSubTitle.text = data.popGratification?.text
            btnAction.text = data.popGratification?.popGratificationActionButton?.text

            val couponDetailList = ArrayList<GetCouponDetail>()
            if (couponDetailResponse.couponList != null) {
                couponDetailList.addAll(couponDetailResponse.couponList)
            }
            couponListAdapter = CouponListAdapter(couponDetailList)
            recyclerView.adapter = couponListAdapter
            recyclerView.addItemDecoration(CouponItemDecoration())


            //show single/multiple coupon
            val popGratificationBenefits = data.popGratification?.popGratificationBenefits
            val multipleCoupon = popGratificationBenefits != null && popGratificationBenefits.size > 1
            if (multipleCoupon) {
                uiType = TargetPromotionsCouponType.MULTIPLE_COUPON
            }

            //show error
            val imageUrl = data.popGratification?.imageUrl
            val imageUrlMobile = data.popGratification?.imageUrlMobile
            val showError = TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(imageUrlMobile)
            if (showError) {
                val urlToDisplay = if (TextUtils.isEmpty(imageUrl)) imageUrlMobile else imageUrl
                uiType = TargetPromotionsCouponType.COUPON_ERROR
            }

            //todo remove test code
            viewFlipper.displayedChild = CONTAINER_COUPON
        } else if (data is ClaimPopGratificationResponse) {


        }

    }

    fun onActivityResumeIfWaitingForLogin() {
        if (GratificationSubscriber.waitingForLogin.get()) {
            GratificationSubscriber.waitingForLogin.set(false)
            btnAction.performClick()
        } else {
            //Do nothing
        }
    }

    private fun setErrorUiForPopGratification() {
        val lessThanThreeTimes = false
        val context = tvTitle.context
        tvTitle.text = context.getString(R.string.t_promo_disturbance_at_toko_house)
        tvSubTitle.text = context.getString(R.string.t_promo_we_will_fix_it_as_soon_as_poss)

        if (lessThanThreeTimes) {
            btnAction.text = context.getString(R.string.t_promo_coba_lagi)
        } else {
            btnAction.text = context.getString(R.string.t_promo_ke_homepage)
        }

        imageView.loadImageGlide(R.drawable.t_promo_server_error)
    }

    private fun setUiForSuccessClaimGratification(data: ClaimPopGratificationResponse) {
        viewFlipper.displayedChild = CONTAINER_IMAGE
        tvTitle.text = data.popGratificationClaim?.title
        tvSubTitle.text = data.popGratificationClaim?.text
        btnAction.text = data.popGratificationClaim?.popGratificationActionButton?.text
        imageView.loadImageGlide(data.popGratificationClaim?.imageUrlMobile)
        performAnimationToGotoClaimUI()
    }

    private fun setListeners(data: GratificationDataContract, activityContext: Context) {
        viewModel.liveData.observe(activityContext as AppCompatActivity, Observer { it ->
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

        viewModel.fakeLiveData.observe(activityContext as AppCompatActivity, Observer { it ->

        })

        viewModel.autoApplyLiveData.observe(activityContext as AppCompatActivity, Observer { it ->
            when (it) {
                is Success -> {
                    val messageList = it.data.tokopointsSetAutoApply?.resultStatus?.message
                    if (messageList != null && messageList.isNotEmpty())
                        Toaster.showNormal(nestedScrollView, messageList[0].toString(), Toast.LENGTH_SHORT)
                }
            }
            bottomSheetDialog.dismiss()
        })

        btnAction.setOnClickListener {

            toggleProgressBar(true)
            toggleBtnText(false)

            if (data is GetPopGratificationResponse) {
                performActionClaimCoupon(data, activityContext)
            } else if (data is ClaimPopGratificationResponse) {
                performActionAfterCouponIsClaimed(data, activityContext)
            }
//            viewModel.claimFakeCoupon()
        }
    }

    private fun toggleBtnText(show: Boolean) {
        if (show) {
            btnAction.visibility = View.VISIBLE
        } else {
            btnAction.visibility = View.GONE
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

        val vg = viewFlipper.parent.parent.parent as ViewGroup
        val touchOutside: View = vg.findViewById(R.id.touch_outside)
        val couponCode = ""
        touchOutside.setOnClickListener {
            viewModel.autoApply(couponCode)
        }
    }

    private fun performActionAfterCouponIsClaimed(data: ClaimPopGratificationResponse, activityContext: Context) {

        val couponCode = data.popGratificationClaim?.dummyCouponCode
        val retryAttemptAvailable = false
        if (retryAttemptAvailable) {
            if (!TextUtils.isEmpty(couponCode)) {
                viewModel.autoApply(couponCode!!)
            }
        } else {
            //Route to home page
//            RouteManager.route to home page
            bottomSheetDialog.dismiss()
        }


    }

    private fun performActionClaimCoupon(data: GetPopGratificationResponse, activityContext: Activity) {

        val userSession = UserSession(activityContext)
        if (userSession.isLoggedIn) {

            val applink = data.popGratification?.popGratificationActionButton?.appLink
            if (!TextUtils.isEmpty(applink)) {
                RouteManager.route(btnAction.context, applink)
            }

            val popGratificationBenefits = data.popGratification?.popGratificationBenefits
            if (popGratificationBenefits != null && popGratificationBenefits.isNotEmpty()) {
                viewModel.claimCoupon()
            }
        } else {
            subscriber.waitingForLoginActivity = WeakReference(activityContext)
            GratificationSubscriber.waitingForLogin.set(true)
            RouteManager.route(activityContext, ApplinkConst.LOGIN)
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