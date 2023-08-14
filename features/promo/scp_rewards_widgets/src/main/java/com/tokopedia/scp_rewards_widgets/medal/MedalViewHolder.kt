package com.tokopedia.scp_rewards_widgets.medal

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.LINEAR
import com.tokopedia.scp_rewards_common.OVER_SHOOT
import com.tokopedia.scp_rewards_common.animateView
import com.tokopedia.scp_rewards_common.grayscale
import com.tokopedia.scp_rewards_common.loadImageOrFallback
import com.tokopedia.scp_rewards_common.loadLottieFromUrl
import com.tokopedia.scp_rewards_common.propertyValueHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.ItemMedalLayoutBinding

class MedalViewHolder(
    itemView: View,
    private val listener: MedalCallbackListener? = null
) : AbstractViewHolder<MedalItem>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_medal_layout
        const val ALPHA_0 = 0f
        const val ALPHA_100 = 1f
        const val SCALE_0 = 0f
        const val SCALE_100 = 1f
        const val POSITION_96 = -96f
        const val POSITION_95 = -95f
        const val POSITION_0 = 0f
        const val DURATION_1_SEC = 1000L
        const val DURATION_800_MILLIS = 800L
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
            tvMedalTitle.alpha = ALPHA_0
            tvMedalSubTitle.alpha = ALPHA_0
            tvMedalCaption.alpha = ALPHA_0
            ivCelebrationPlaceholder.visible()
            ivMedal.alpha = ALPHA_0
            binding.lottieView.loadLottieFromUrl(
                url = item.celebrationUrl,
                autoPlay = false,
                onLottieLoaded = {
                    lottieView.postDelayed({
                        ivMedal.runAnimation(SCALE_0, SCALE_100, ALPHA_0, ALPHA_100)
                        ivCelebrationPlaceholder.fadeIn(ALPHA_100, ALPHA_0)
                        ivCelebrationPlaceholder.gone()
                        tvMedalTitle.translateAndAlphaText(POSITION_96, POSITION_0, ALPHA_0, ALPHA_100)
                        tvMedalSubTitle.translateAndAlphaText(POSITION_96, POSITION_0, ALPHA_0, ALPHA_100)
                        tvMedalCaption.translateAndAlphaText(POSITION_95, POSITION_0, ALPHA_0, ALPHA_100)
                        lottieView.playAnimation()
                    }, DURATION_1_SEC)
                }
            )
        } else {
            tvMedalTitle.alpha = ALPHA_100
            tvMedalSubTitle.alpha = ALPHA_100
            tvMedalCaption.alpha = ALPHA_100
            ivMedal.alpha = ALPHA_100
            lottieView.gone()
            ivCelebrationPlaceholder.gone()
        }
    }

    private fun View.translateAndAlphaText(fromPosition: Float, toPosition: Float, fromAlpha: Float, toAlpha: Float) {
        val animations = arrayOf(
            propertyValueHolder(property = View.TRANSLATION_Y, from = fromPosition, to = toPosition),
            propertyValueHolder(property = View.ALPHA, from = fromAlpha, to = toAlpha)
        )
        animateView(animations, DURATION_800_MILLIS, OVER_SHOOT)
    }

    private fun View.fadeIn(from: Float, to: Float) {
        animateView(arrayOf(propertyValueHolder(View.ALPHA, from, to)), DURATION_800_MILLIS, LINEAR)
    }

    private fun View.runAnimation(fromScale: Float, toScale: Float, fromAlpha: Float, toAlpha: Float) {
        val animations = arrayOf(
            propertyValueHolder(property = View.SCALE_X, from = fromScale, to = toScale),
            propertyValueHolder(property = View.SCALE_Y, from = fromScale, to = toScale),
            propertyValueHolder(property = View.ALPHA, from = fromAlpha, to = toAlpha)
        )

        animateView(animations, DURATION_800_MILLIS, OVER_SHOOT)
    }
}
