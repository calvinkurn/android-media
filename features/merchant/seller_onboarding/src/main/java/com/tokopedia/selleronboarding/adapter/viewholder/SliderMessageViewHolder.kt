package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.databinding.SobSliderMessageViewHolderBinding
import com.tokopedia.selleronboarding.model.SobSliderMessageUiModel
import com.tokopedia.selleronboarding.utils.OnboardingConst
import com.tokopedia.selleronboarding.utils.OnboardingUtils
import com.tokopedia.selleronboarding.utils.adjustImageGravity

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderMessageViewHolder(
    itemView: View
) : AbstractViewHolder<SobSliderMessageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_message_view_holder
    }

    private val binding by lazy {
        SobSliderMessageViewHolderBinding.bind(itemView)
    }
    private val animObserver by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderMessageUiModel) {
        with(binding) {
            setupAnimation()

            imgSobMessageBg.loadImage(R.drawable.bg_sob_circle)
            showIllustrations()
        }
    }

    private fun setupAnimation() {
        with(binding) {
            root.viewTreeObserver.addOnPreDrawListener {
                tvSobSliderMessageTitle.alpha = animObserver.alpha
                tvSobSliderMessageTitle.translationY = animObserver.translationY

                imgSobMessage1.scaleX = animObserver.scaleX
                imgSobMessage1.scaleY = animObserver.scaleY
                imgSobMessage1.alpha = animObserver.alpha

                imgSobMessage2.scaleX = animObserver.scaleX
                imgSobMessage2.scaleY = animObserver.scaleY
                imgSobMessage2.alpha = animObserver.alpha

                imgSobMessage3.scaleX = animObserver.scaleX
                imgSobMessage3.scaleY = animObserver.scaleY
                imgSobMessage3.alpha = animObserver.alpha

                imgSobMessageBg.alpha = animObserver.alpha
                return@addOnPreDrawListener true
            }
        }
    }

    private fun showIllustrations() = with(binding) {
        showMessageIllustration()
        showSmartReplyIllustration()

        imgSobMessage3.loadImage(OnboardingConst.ImageUrl.IMG_BROADCAST_CHAT) {
            setPlaceHolder(R.drawable.img_sob_broadcast_chat)
        }
    }

    private fun showMessageIllustration() {
        binding.imgSobMessage1.let { imgView ->
            val imgGravity = OnboardingConst.Gravity.START_BOTTOM
            val drawableRes = R.drawable.img_sob_widget_android
            imgView.loadImage(drawableRes)
            imgView.adjustImageGravity(drawableRes, imgGravity)

            OnboardingUtils.loadImageAsBitmap(
                imgView.context,
                OnboardingConst.ImageUrl.IMG_SOB_WIDGET_ANDROID
            ) {
                imgView.loadImage(it)
                imgView.adjustImageGravity(it, imgGravity)
            }
        }
    }

    private fun showSmartReplyIllustration() {
        binding.imgSobMessage2.let { imgView ->
            val imgGravity = OnboardingConst.Gravity.END_CENTER_VERTICAL
            val drawableRes = R.drawable.img_sob_smart_reply
            imgView.loadImage(drawableRes)
            imgView.adjustImageGravity(drawableRes, imgGravity)

            OnboardingUtils.loadImageAsBitmap(
                imgView.context,
                OnboardingConst.ImageUrl.IMG_SMART_REPLY
            ) {
                imgView.loadImage(it)
                imgView.adjustImageGravity(it, imgGravity)
            }
        }
    }
}