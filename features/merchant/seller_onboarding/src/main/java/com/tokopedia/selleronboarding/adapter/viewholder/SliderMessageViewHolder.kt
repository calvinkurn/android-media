package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderMessageUiModel
import com.tokopedia.selleronboarding.utils.SobImageSliderUrl
import com.tokopedia.selleronboarding.utils.setupMarginTitleSob
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_message_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderMessageViewHolder(itemView: View) : AbstractViewHolder<SobSliderMessageUiModel>(itemView) {

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
            setMessageImageUrl()
            setupMarginTitleSob { setMarginMessageTitle() }
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

    private fun setMessageImageUrl() {
        with(itemView) {
            imgSobMessage1?.loadImage(SobImageSliderUrl.IMG_SOB_WIDGET_ANDROID) {
                setPlaceHolder(R.drawable.img_sob_widget_android)
            }
            imgSobMessage2?.loadImage(SobImageSliderUrl.IMG_SMART_REPLY) {
                setPlaceHolder(R.drawable.img_sob_smart_reply)
            }
            imgSobMessage3?.loadImage(SobImageSliderUrl.IMG_BROADCAST_CHAT) {
                setPlaceHolder(R.drawable.img_sob_broadcast_chat)
            }
        }
    }

    private fun setMarginMessageTitle() {
        with(itemView) {
            val tvSobCurrentView = tvSobSliderMessageTitle?.layoutParams as? ConstraintLayout.LayoutParams
            tvSobCurrentView?.topToTop = ConstraintSet.PARENT_ID
            tvSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            tvSobSliderMessageTitle?.layoutParams = tvSobCurrentView
        }
    }
}