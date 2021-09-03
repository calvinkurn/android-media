package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderManageUiModel
import com.tokopedia.selleronboarding.utils.OnboardingConst
import com.tokopedia.selleronboarding.utils.OnboardingUtils
import com.tokopedia.selleronboarding.utils.adjustImageGravity
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_home_view_holder.view.*
import kotlinx.android.synthetic.main.sob_slider_manage_view_holder.view.*
import kotlinx.android.synthetic.main.sob_slider_promo_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderManageViewHolder(
    itemView: View
) : AbstractViewHolder<SobSliderManageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_manage_view_holder
    }

    private val animationObserver by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderManageUiModel) {
        with(itemView) {
            setupAnimation()

            imgSobManageBg?.loadImage(R.drawable.bg_sob_circle)
            setManageImageUrl()
        }
    }

    private fun setupAnimation() {
        with(itemView) {
            viewTreeObserver.addOnPreDrawListener {
                tvSobSliderManageTitle?.alpha = animationObserver.alpha
                tvSobSliderManageTitle?.translationY = animationObserver.translationY

                imgSobManage1?.scaleX = animationObserver.scaleX
                imgSobManage1?.scaleY = animationObserver.scaleY
                imgSobManage1?.alpha = animationObserver.alpha

                imgSobManage2?.scaleX = animationObserver.scaleX
                imgSobManage2?.scaleY = animationObserver.scaleY
                imgSobManage2?.alpha = animationObserver.alpha

                imgSobManageBg?.alpha = animationObserver.alpha
                return@addOnPreDrawListener true
            }
        }
    }

    private fun setManageImageUrl() = with(itemView) {
        imgSobManage1?.let {
            it.loadImage(R.drawable.img_sob_manage_stock)
            it.adjustImageGravity(
                R.drawable.img_sob_manage_stock,
                OnboardingConst.Gravity.START_BOTTOM
            )
        }

        imgSobManage2?.let {
            it.loadImage(R.drawable.img_sob_som_card)
            it.adjustImageGravity(R.drawable.img_sob_som_card, OnboardingConst.Gravity.END_TOP)
        }

        loadRemoteImages()
    }

    private fun loadRemoteImages() = with(itemView) {
        OnboardingUtils.loadImageAsBitmap(context, OnboardingConst.ImageUrl.IMG_MANAGE_STOCK) {
            imgSobManage1?.let { imgView ->
                imgView.loadImage(it)
                imgView.adjustImageGravity(it, OnboardingConst.Gravity.START_BOTTOM)
            }
        }

        OnboardingUtils.loadImageAsBitmap(context, OnboardingConst.ImageUrl.IMG_SOM_CARD) {
            imgSobManage2?.let { img ->
                img.loadImage(it)
                img.adjustImageGravity(it, OnboardingConst.Gravity.END_TOP)
            }
        }
    }
}