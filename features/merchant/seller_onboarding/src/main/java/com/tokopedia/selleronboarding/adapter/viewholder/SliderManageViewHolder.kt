package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderManageUiModel
import com.tokopedia.selleronboarding.utils.setupMarginTitleSob
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_manage_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderManageViewHolder(itemView: View) : AbstractViewHolder<SobSliderManageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_manage_view_holder
    }

    override fun bind(element: SobSliderManageUiModel) {
        with(itemView) {
            tvSobSliderManageTitle.viewTreeObserver
                    .addOnDrawListener {
                        tvSobSliderManageTitle.alpha = itemView.viewObserver.alpha
                        tvSobSliderManageTitle.translationY = itemView.viewObserver.translationY
                    }
            imgSobManage1.run {
                viewTreeObserver.addOnDrawListener {
                    scaleX = itemView.viewObserver.scaleX
                    scaleY = itemView.viewObserver.scaleY
                    alpha = itemView.viewObserver.alpha
                }
            }
            imgSobManage2.run {
                viewTreeObserver.addOnDrawListener {
                    scaleX = itemView.viewObserver.scaleX
                    scaleY = itemView.viewObserver.scaleY
                    alpha = itemView.viewObserver.alpha
                }
            }
            imgSobManageBg.run {
                loadImage(R.drawable.bg_sob_circle)
                viewTreeObserver.addOnDrawListener {
                    alpha = itemView.viewObserver.alpha
                }
            }

            setupMarginTitleSob { setMarginManageTitle() }
        }
    }

    private fun setMarginManageTitle() {
        with(itemView) {
            val tvSobCurrentView = tvSobSliderManageTitle?.layoutParams as? ConstraintLayout.LayoutParams
            tvSobCurrentView?.topToTop = ConstraintSet.PARENT_ID
            tvSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl7)
            tvSobSliderManageTitle?.layoutParams = tvSobCurrentView
        }
    }
}