package com.tokopedia.gamification.giftbox.presentation.viewholder

import android.view.View
import com.tokopedia.gamification.R
import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.gamification.giftbox.presentation.adapter.CouponListVH
import com.tokopedia.gamification.giftbox.presentation.presenter.CouponListResultPresenter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class CouponListResultVH(itemView: View) : CouponListVH(itemView) {

    companion object {
        val LAYOUT = com.tokopedia.gamification.R.layout.gami_result_list_item_coupons
    }

    @Inject
    lateinit var presenter: CouponListResultPresenter
    private val button: UnifyButton = itemView.findViewById(R.id.btn)

    init {
        val component = DaggerGiftBoxComponent.builder()
                .activityContextModule(ActivityContextModule(itemView.context))
                .build()
        component.inject(this)
    }

    fun setData(data: GetCouponDetail, crackBenefitEntity: CrackBenefitEntity) {
        super.setData(data)

        if (crackBenefitEntity.isAutoApply) {
            button.visible()
            button.text = String.format(itemView.context.getString(R.string.gami_pakai_kupon), data.minimumUsage)
            button.setOnClickListener {
                if (!crackBenefitEntity.dummyCode.isNullOrEmpty())
                    presenter.autoApply(crackBenefitEntity.dummyCode, crackBenefitEntity.autoApplyMsg)
            }
        } else {
            button.gone()
        }
    }
}