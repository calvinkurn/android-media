package com.tokopedia.promotionstarget

import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.components.AppModule
import com.tokopedia.promotionstarget.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.viewmodel.TargetPromotionsDialogVM
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

//todo use weak reference of activity context
class TargetPromotionsDialog {

    private lateinit var tvTitle: Typography
    private lateinit var tvSubTitle: Typography
    private lateinit var imageView: AppCompatImageView
    private lateinit var btnAction: AppCompatButton

    lateinit var viewModel: TargetPromotionsDialogVM
    private var liveData: MutableLiveData<Result<ClaimPopGratificationResponse>>? = null
    private var data: GetPopGratificationResponse?=null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    enum class TargetPromotionsCouponType {
        SHOW_COUPON,
        COUPON_ACTION_TAKEN,
        COUPON_ERROR
    }

    fun getLayout(couponUiType: TargetPromotionsCouponType): Int {
        return when (couponUiType) {
            TargetPromotionsCouponType.SHOW_COUPON -> R.layout.dialog_target_promotions
            else -> R.layout.dialog_target_promotions
        }
    }

    fun show(activityContext: Context, couponUiType: TargetPromotionsCouponType, data: GetPopGratificationResponse): Dialog {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activityContext)
        val view = LayoutInflater.from(activityContext).inflate(getLayout(couponUiType), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()

        initViews(view, activityContext, data)
        return bottomSheet
    }

    private fun initViews(root: View, activityContext: Context, data: GetPopGratificationResponse) {
        imageView = root.findViewById(R.id.imageView)
        tvTitle = root.findViewById(R.id.tvTitle)
        tvSubTitle = root.findViewById(R.id.tvSubTitle)
        btnAction = root.findViewById(R.id.btnAction)

        this.data = data
        initInjections(activityContext)
        setUiData(data)
        setListeners(activityContext)
    }


    private fun setUiData(getGratResponse: GetPopGratificationResponse) {
        tvTitle.text = getGratResponse.popGratification?.title
        tvSubTitle.text = getGratResponse.popGratification?.text
        btnAction.text = getGratResponse.popGratification?.popGratificationActionButton?.text
    }

    private fun setListeners(activityContext: Context){
        viewModel.liveData.observe(activityContext as AppCompatActivity, Observer { it ->

        })

        btnAction.setOnClickListener {
            val applink = data?.popGratification?.popGratificationActionButton?.appLink
            if(!TextUtils.isEmpty(applink)){
                RouteManager.route(btnAction.context, applink)
            }

            val popGratificationBenefits = data?.popGratification?.popGratificationBenefits
            if(popGratificationBenefits!=null && popGratificationBenefits.isNotEmpty()){
                viewModel.claimCoupon()
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