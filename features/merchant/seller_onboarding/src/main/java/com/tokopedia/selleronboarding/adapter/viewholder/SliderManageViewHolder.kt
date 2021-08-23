package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderManageUiModel
import com.tokopedia.selleronboarding.utils.IMG_DEVICE_SCREEN_PERCENT
import com.tokopedia.selleronboarding.utils.SobImageSliderUrl
import com.tokopedia.selleronboarding.utils.setupMarginTitleSob
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_home_view_holder.view.*
import kotlinx.android.synthetic.main.sob_slider_manage_view_holder.view.*
import kotlinx.android.synthetic.main.sob_slider_promo_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderManageViewHolder(itemView: View) : AbstractViewHolder<SobSliderManageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_manage_view_holder

        const val NO_SPACE_MARGIN = 0
    }
    
    private val animationObserver by lazy { 
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderManageUiModel) {
        with(itemView) {
            setupAnimation()
            
            imgSobManageBg?.loadImage(R.drawable.bg_sob_circle)
            setManageImageUrl()
            
            setupMarginTitleSob { setMarginManageTitle() }
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

    private fun setManageImageUrl() {
        with(itemView) {
            imgSobManage1?.loadImage(SobImageSliderUrl.IMG_MANAGE_STOCK) {
                setPlaceHolder(R.drawable.img_sob_manage_stock)
            }
            imgSobManage2?.loadImage(SobImageSliderUrl.IMG_SOM_CARD) {
                setPlaceHolder(R.drawable.img_sob_som_card)
            }
        }
    }

    private fun setMarginManageTitle() {
        with(itemView) {
            val tvSobCurrentView = tvSobSliderManageTitle?.layoutParams as? ConstraintLayout.LayoutParams
            tvSobCurrentView?.topToTop = ConstraintSet.PARENT_ID
            tvSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            tvSobSliderManageTitle?.layoutParams = tvSobCurrentView

            val imgSobCurrentView1 = imgSobManage1?.layoutParams as? ConstraintLayout.LayoutParams
            imgSobCurrentView1?.topMargin = NO_SPACE_MARGIN
            imgSobManage1?.layoutParams = imgSobCurrentView1

            val imgSobCurrentView2 = imgSobManage2?.layoutParams as? ConstraintLayout.LayoutParams
            imgSobCurrentView2?.bottomMargin = NO_SPACE_MARGIN
            imgSobManage2?.layoutParams = imgSobCurrentView2
        }
    }
}