package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderMessageUiModel
import com.tokopedia.selleronboarding.utils.OnboardingConst
import com.tokopedia.selleronboarding.utils.OnboardingUtils
import com.tokopedia.selleronboarding.utils.adjustImageGravity
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_message_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderMessageViewHolder(itemView: View) :
    AbstractViewHolder<SobSliderMessageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_message_view_holder
    }

    private val observer by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderMessageUiModel) {
        with(itemView) {
            setupAnimation()

            imgSobMessageBg?.loadImage(R.drawable.bg_sob_circle)
            showIllustrations()
        }
    }

    private fun setupAnimation() {
        with(itemView) {
            viewTreeObserver.addOnPreDrawListener {
                tvSobSliderMessageTitle?.alpha = observer.alpha
                tvSobSliderMessageTitle?.translationY = itemView.viewObserver.translationY

                imgSobMessage1?.scaleX = observer.scaleX
                imgSobMessage1?.scaleY = observer.scaleY
                imgSobMessage1?.alpha = observer.alpha

                imgSobMessage2?.scaleX = observer.scaleX
                imgSobMessage2?.scaleY = observer.scaleY
                imgSobMessage2?.alpha = observer.alpha

                imgSobMessage3?.scaleX = observer.scaleX
                imgSobMessage3?.scaleY = observer.scaleY
                imgSobMessage3?.alpha = observer.alpha

                imgSobMessageBg?.alpha = observer.alpha
                return@addOnPreDrawListener true
            }
        }
    }

    private fun showIllustrations() = with(itemView) {
        showMessageIllustration()
        showSmartReplyIllustration()

        imgSobMessage3?.loadImage(OnboardingConst.ImageUrl.IMG_BROADCAST_CHAT) {
            setPlaceHolder(R.drawable.img_sob_broadcast_chat)
        }
    }

    private fun showMessageIllustration() {
        itemView.imgSobMessage1?.let { imgView ->
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
        itemView.imgSobMessage2?.let { imgView ->
            val imgGravity = OnboardingConst.Gravity.END_CENTER_VERTICAL
            val drawableRes = R.drawable.img_sob_smart_reply
            imgView.loadImage(drawableRes)
            imgView.adjustImageGravity(drawableRes, imgGravity)

            OnboardingUtils.loadImageAsBitmap(imgView.context, OnboardingConst.ImageUrl.IMG_SMART_REPLY) {
                imgView.loadImage(it)
                imgView.adjustImageGravity(it, imgGravity)
            }
        }
    }
}