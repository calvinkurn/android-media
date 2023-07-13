package com.tokopedia.scp_rewards_widgets.medal

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.grayscale
import com.tokopedia.scp_rewards_common.loadImageOrFallback
import com.tokopedia.scp_rewards_common.loadLottieFromUrl
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.ItemMedalLayoutBinding

class MedalViewHolder(
    itemView: View,
    private val listener: MedalCallbackListener? = null
) : AbstractViewHolder<MedalItem>(itemView) {

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
        ivMedal.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_empty_medal))
    }

    private fun ItemMedalLayoutBinding.showMedal(item: MedalItem) {
        handleLottieCelebration(item)
        handleTextSection(item)
        tvMedalCaption.visible()
        tvMedalCaption.text = item.extraInfo
        listener?.onMedalLoad(item)
        if (item.isDisabled == true) {
            ivMedal.grayscale()
            ivMedal.loadImageOrFallback(item.imageUrl, R.drawable.ic_empty_medal) {
                listener?.onMedalFailed(item)
            }
        } else {
            ivMedal.loadImageOrFallback(item.imageUrl, R.drawable.ic_empty_medal)
        }
        listener?.let {
            this.root.setOnClickListener { listener.onMedalClick(item) }
        }
        handleProgressBar(item)
    }

    private fun ItemMedalLayoutBinding.handleTextSection(item: MedalItem) {
        if (item.provider.isNullOrEmpty()) {
            tvMedalTitle.setLines(2)
            tvMedalSubTitle.gone()
        } else {
            tvMedalTitle.setLines(1)
            tvMedalSubTitle.text = item.provider
            tvMedalSubTitle.visible()
        }
        tvMedalTitle.text = item.name
        tvMedalTitle.visible()
    }

    private fun ItemMedalLayoutBinding.handleProgressBar(item: MedalItem) {
        if (item.isEarned()) {
            progressMedal.gone()
        } else {
            progressMedal.visible()
            progressMedal.setValue(item.progression ?: 0)
        }
    }

    private fun ItemMedalLayoutBinding.handleLottieCelebration(item: MedalItem) {
        if (item.isNewlyEarned()) {
            lottieView.visible()
            binding.lottieView.loadLottieFromUrl(
                url = item.celebrationUrl,
                autoPlay = true
            )
        } else {
            lottieView.gone()
        }
    }
}
