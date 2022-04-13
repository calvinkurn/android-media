package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroBenefitImageItem
import kotlinx.android.synthetic.main.tm_dash_intro_benefit_image_item.view.*
import kotlinx.android.synthetic.main.tm_dash_video_view_item.view.*


class TokomemberIntroBenefitImageVh(val view: View)
    : AbstractViewHolder<TokomemberIntroBenefitImageItem>(view) {

    var lastPosition  = -1
    private val tmIntroVideoView = itemView.ivSection

    override fun bind(element: TokomemberIntroBenefitImageItem?) {
        element?.apply {
            tmIntroVideoView?.loadImage(element.imgUrl?:"")
            setAnimation()
        }
    }

 /*   if position div by 2 and 4 L->R text  isLeft isRight variable use it to change mode
    if position div by only 2 L<-R text
    if postion not div by 2 L<-R  image
    if*/


    //0 position text L->R
    //1 position image L<-R

    //2 position text L<-R
    //3 position image L->R

    //4 position text L->R
    //5 position image L<-R

    //6 position text L<-R
    //7 position image L->R

    private fun setAnimation() {
        if (adapterPosition > lastPosition) {
            val animType = getAnimationLeftOrRight()
            val animation: Animation =
                AnimationUtils.loadAnimation(itemView.context, animType)
            animation.duration = 600L
            this@TokomemberIntroBenefitImageVh.itemView.startAnimation(animation)
            lastPosition = adapterPosition
        }
    }

    private fun getAnimationLeftOrRight(): Int {
        return if (adapterPosition % 2 == 0) {
            R.anim.tm_dash_intro_benefit_left
        } else {
            R.anim.tm_dash_intro_benefit_right
        }
    }
    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_benefit_image_item
    }
}
