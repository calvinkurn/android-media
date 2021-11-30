package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.banner.dynamic.util.ViewHelper
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeBannerEmptyBinding
import com.tokopedia.digital.home.model.RechargeHomepageBannerEmptyModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageBannerEmptyViewHolder(itemView: View, val listener: RechargeHomepageItemListener)
    : AbstractViewHolder<RechargeHomepageBannerEmptyModel>(itemView) {

    override fun bind(element: RechargeHomepageBannerEmptyModel) {
        val bind = ViewRechargeHomeBannerEmptyBinding.bind(itemView)
        val section = element.section
        if (section.title.isEmpty() && section.subtitle.isEmpty()) {
            listener.loadRechargeSectionData(section.id)
        } else {
            with(bind) {
                if (section.title.isNotEmpty()) {
                    tvRechargeHomeBannerEmptyTitle.show()
                    tvRechargeHomeBannerEmptyTitle.text = MethodChecker.fromHtml(section.title)
                } else tvRechargeHomeBannerEmptyTitle.hide()
                if (section.subtitle.isNotEmpty()) {
                    tvRechargeHomeBannerEmptyDesc.show()
                    tvRechargeHomeBannerEmptyDesc.text = MethodChecker.fromHtml(section.subtitle)
                } else tvRechargeHomeBannerEmptyDesc.hide()

                val layoutParams = rechargeHomeBannerEmptyTextContainer.layoutParams as? ConstraintLayout.LayoutParams
                layoutParams?.apply {
                    // Add status bar height to designated margin value
                    val marginTop = CONTENT_MARGIN_TOP_DP.dpToPx(root.resources.displayMetrics) + ViewHelper.getStatusBarHeight(root.context)
                    setMargins(leftMargin, marginTop, rightMargin, bottomMargin)
                    rechargeHomeBannerEmptyTextContainer.layoutParams = this
                }

                val vto = rechargeHomeBannerEmptyTextContainer.viewTreeObserver
                vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        rechargeHomeBannerEmptyTextContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        val location = intArrayOf(0,0)
                        rechargeHomeBannerEmptyTextContainer.getLocationOnScreen(location)

                        ivRechargeHomeBannerEmpty.layoutParams.height = rechargeHomeBannerEmptyTextContainer.measuredHeight +
                                root.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5) +
                                location[Y_COORDINATE_INDEX]
                        ivRechargeHomeBannerEmpty.requestLayout()
                    }
                })
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_banner_empty
        const val CONTENT_MARGIN_TOP_DP = 56

        private const val Y_COORDINATE_INDEX = 1
    }
}
