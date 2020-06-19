package com.tokopedia.gamification.giftbox.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.unifyprinciples.Typography


class RandomRewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        val LAYOUT = com.tokopedia.gamification.R.layout.list_item_random_reward
    }

    private val image: AppCompatImageView = itemView.findViewById(R.id.image)
    private val tvTitle: Typography = itemView.findViewById(R.id.tvTitle)

    fun bind(benfit: CrackBenefitEntity) {
        when (benfit.benefitType) {
            BenefitType.COUPON -> image.setImageResource(R.drawable.gami_coupon)
            BenefitType.OVO -> image.setImageResource(R.drawable.gami_ovo)
        }
        tvTitle.text = benfit.text
    }
}