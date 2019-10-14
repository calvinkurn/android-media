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
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    enum class TargetPromotionsCouponType {
        SHOW_COUPON,
        COUPON_ACTION_TAKEN,
        COUPON_ERROR
    }

    fun show(activityContext: Context, couponUiType: TargetPromotionsCouponType, data: GetPopGratificationResponse): Dialog {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activityContext)
        val view = LayoutInflater.from(activityContext).inflate(getLayout(couponUiType), null, false)
        bottomSheet.setCustomContentView(view, "", true)
        bottomSheet.show()
        return bottomSheet
    }

    private fun initViews(root: View, activityContext: Context) {
        imageView = root.findViewById(R.id.imageView)
        tvTitle = root.findViewById(R.id.tvTitle)
        tvSubTitle = root.findViewById(R.id.tvSubTitle)
        btnAction = root.findViewById(R.id.btnAction)

        initInjections(activityContext)

        viewModel.liveData.observe(activityContext as AppCompatActivity, Observer { it ->


        })
        btnAction.setOnClickListener {

        }
    }

    private fun initInjections(activityContext: Context) {
        val activity = activityContext as AppCompatActivity
        activity.run {
            val viewModelProvider = ViewModelProviders.of(activityContext, viewModelFactory)
            viewModel = viewModelProvider[TargetPromotionsDialogVM::class.java]
        }

    }


    fun getLayout(couponUiType: TargetPromotionsCouponType): Int {
        return when (couponUiType) {
            TargetPromotionsCouponType.SHOW_COUPON -> R.layout.dialog_target_promotions
            else -> R.layout.dialog_target_promotions
        }
    }
}