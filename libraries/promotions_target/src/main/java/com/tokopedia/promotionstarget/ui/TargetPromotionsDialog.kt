package com.tokopedia.promotionstarget.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ViewFlipper
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.GratificationDataContract
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.components.AppModule
import com.tokopedia.promotionstarget.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.ui.adapter.CouponData
import com.tokopedia.promotionstarget.ui.adapter.CouponListAdapter
import com.tokopedia.promotionstarget.ui.recycleViewHelper.CouponItemDecoration
import com.tokopedia.promotionstarget.ui.viewmodel.TargetPromotionsDialogVM
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

//todo use weak reference of activity context
class TargetPromotionsDialog {

    val CONTAINER_COUPON = 0
    val CONTAINER_ERROR = 1

    private lateinit var tvTitle: Typography
    private lateinit var tvSubTitle: Typography
    private lateinit var imageView: AppCompatImageView
    private lateinit var btnAction: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var couponListAdapter: CouponListAdapter
    private lateinit var viewFlipper: ViewFlipper

    lateinit var viewModel: TargetPromotionsDialogVM
    private var liveData: MutableLiveData<Result<ClaimPopGratificationResponse>>? = null
    private var data: GratificationDataContract? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


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

    fun show(activityContext: Context, couponUiType: TargetPromotionsCouponType, data: GratificationDataContract): Dialog {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activityContext)
        val view = LayoutInflater.from(activityContext).inflate(getLayout(couponUiType), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()

        initViews(view, activityContext, data)
        return bottomSheet
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(root: View, activityContext: Context, data: GratificationDataContract) {
        imageView = root.findViewById(R.id.imageView)
        tvTitle = root.findViewById(R.id.tvTitle)
        tvSubTitle = root.findViewById(R.id.tvSubTitle)
        btnAction = root.findViewById(R.id.btnAction)
        recyclerView = root.findViewById(R.id.recyclerView)
        viewFlipper = root.findViewById(R.id.viewFlipper)

        recyclerView.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)

        val pageSnaper = PagerSnapHelper()
        pageSnaper.attachToRecyclerView(recyclerView)

        recyclerView.isNestedScrollingEnabled = false

        this.data = data
        initInjections(activityContext)
        setUiData(data)
        setListeners(data, activityContext)
    }


    private fun setUiData(data: GratificationDataContract) {

        if (data is GetPopGratificationResponse) {
            tvTitle.text = data.popGratification?.title
            tvSubTitle.text = data.popGratification?.text
            btnAction.text = data.popGratification?.popGratificationActionButton?.text


            val dummyCoupons = arrayListOf(CouponData(1), CouponData(2), CouponData(1))
            couponListAdapter = CouponListAdapter(dummyCoupons)
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


    private fun setListeners(data: GratificationDataContract, activityContext: Context) {
        viewModel.liveData.observe(activityContext as AppCompatActivity, Observer { it ->

        })

        btnAction.setOnClickListener {

            if (data is GetPopGratificationResponse) {
                val applink = data.popGratification?.popGratificationActionButton?.appLink
                if (!TextUtils.isEmpty(applink)) {
                    RouteManager.route(btnAction.context, applink)
                }

                val popGratificationBenefits = data.popGratification?.popGratificationBenefits
                if (popGratificationBenefits != null && popGratificationBenefits.isNotEmpty()) {
                    viewModel.claimCoupon()
                }
            }
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