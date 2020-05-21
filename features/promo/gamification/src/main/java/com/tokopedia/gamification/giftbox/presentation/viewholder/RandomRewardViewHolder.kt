package com.tokopedia.gamification.giftbox.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.entities.SimpleReward
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils


class RandomRewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        val LAYOUT = com.tokopedia.gamification.R.layout.list_item_random_reward
    }

    private val image: AppCompatImageView = itemView.findViewById(R.id.image)
    private val tvTitle: Typography = itemView.findViewById(R.id.tvTitle)

    fun bind(simpleReward: SimpleReward) {
        ImageUtils.loadImage(image, simpleReward.imageUrl)
        tvTitle.text = simpleReward.text
    }
}