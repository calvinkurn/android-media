package com.tokopedia.gamification.giftbox.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.utils.image.ImageUtils
import javax.inject.Inject

class CouponListResultVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = com.tokopedia.gamification.R.layout.gami_result_list_item_coupons
    }

    @Inject
    lateinit var presenter: CouponListResultPresenter

    private val button: UnifyButton = itemView.findViewById(R.id.btn)
    private val imageView: AppCompatImageView = itemView.findViewById(R.id.appCompatImageView)
    private val tvTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvTitle)
    private val tvSubTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvSubTitle)

    init {
        val component = DaggerGiftBoxComponent.builder()
                .activityContextModule(ActivityContextModule(itemView.context))
                .build()
        component.inject(this)
    }

    fun setData(data: GetCouponDetail, crackBenefitEntity: CrackBenefitEntity) {
        setGetCouponDetail(data)

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

    private fun setGetCouponDetail(data: GetCouponDetail) {
        tvTitle.text = data.minimumUsageLabel
        tvSubTitle.text = data.minimumUsage
        if (tvSubTitle.text.isNullOrEmpty()) {
            tvSubTitle.visibility = View.GONE
        } else {
            tvSubTitle.visibility = View.VISIBLE
        }

        data.imageUrl?.let {
            ImageUtils.loadImage(imageView, it)
        }
    }
}