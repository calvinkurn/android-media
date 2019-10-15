package com.tokopedia.promotionstarget

import android.app.Activity
import android.content.Context
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.components.AppModule
import com.tokopedia.promotionstarget.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.presenter.DialogManagerPresenter
import com.tokopedia.promotionstarget.subscriber.GratificationData
import com.tokopedia.promotionstarget.ui.TargetPromotionsDialog
import javax.inject.Inject

class DialogManager(val applicationContext: Context) {

    @Inject
    lateinit var presenter: DialogManagerPresenter

    init {
        val component = DaggerPromoTargetComponent.builder()
                .appModule(AppModule(applicationContext))
                .build()
        component.inject(this)
    }

    suspend fun getGratificationAndShowDialog(activity: Activity, gratificationData: GratificationData) {
        presenter.getGratificationAndShowDialog(gratificationData) { it ->
            show(activity, it)
        }
    }


    private fun show(activity: Activity, data: GetPopGratificationResponse) {
        val dialog = TargetPromotionsDialog()
        dialog.show(activity, TargetPromotionsDialog.TargetPromotionsCouponType.SINGLE_COUPON, data)
    }


    fun cancel() {
        presenter.onDestroy()
    }
}