package com.tokopedia.scp_rewards_widgets.medal

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.loadLottieFromUrl
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.ItemMedalLayoutBinding

class MedalViewHolder(itemView: View) : AbstractViewHolder<MedalItem>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_medal_layout
    }

    private var binding: ItemMedalLayoutBinding = ItemMedalLayoutBinding.bind(itemView)

    override fun bind(item: MedalItem) {
        with(binding) {
            if (item.isPlaceHolder.not()) {
                showMedal(item)
            } else {
                showPlaceHolder()
            }
        }
    }

    private fun ItemMedalLayoutBinding.showPlaceHolder() {
        lottieView.gone()
        tvMedalTitle.gone()
        tvMedalSubTitle.gone()
        tvMedalCaption.gone()
        progressMedal.gone()
    }

    private fun ItemMedalLayoutBinding.showMedal(item: MedalItem) {
        handleLottieCelebration(item)
        tvMedalTitle.visible()
        tvMedalSubTitle.visible()
        tvMedalCaption.visible()
        tvMedalTitle.text = item.name
        tvMedalSubTitle.text = item.provider
        tvMedalCaption.text = item.extraInfo
        ivMedal.setImageUrl(item.imageUrl.orEmpty())
        handleProgressBar(item)
    }

    private fun ItemMedalLayoutBinding.handleProgressBar(item: MedalItem) {
        if (item.isEarned) {
            progressMedal.gone()
        } else {
            progressMedal.visible()
            progressMedal.setValue(item.progression ?: 0)
        }
    }

    private fun ItemMedalLayoutBinding.handleLottieCelebration(item: MedalItem) {
        if (item.isNewMedal == true && item.isEarned) {
            lottieView.visible()
            binding.lottieView.loadLottieFromUrl(
                    url = item.celebrationUrl,
                    autoPlay = true,
            )
        } else {
            lottieView.gone()
        }
    }
}
