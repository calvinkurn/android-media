package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeSwipeBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageSwipeBannerModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.unifycomponents.toPx

/**
 * @author: astidhiyaa on 01/11/21.
 */
class RechargeHomepageSwipeBannerViewHolder(
    itemView: View,
    val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageSwipeBannerModel>(itemView) {

    private lateinit var slidesList: List<RechargeHomepageSections.Item>

    override fun bind(element: RechargeHomepageSwipeBannerModel) {
        val bind = ViewRechargeHomeSwipeBannerBinding.bind(itemView)
        slidesList = element.section.items
        try {
            if (slidesList.isNotEmpty()) {
                initBanner(bind)
                bind.root.addOnImpressionListener(element.section) {
                    listener.onRechargeSectionItemImpression(element.section)
                }
            } else {
                listener.loadRechargeSectionData(element.visitableId())
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun initBanner(bind: ViewRechargeHomeSwipeBannerBinding) {
        bind.rechargeHomeSwipeBanner.apply {
            val urlArr = slidesList.map {
                it.mediaUrl
            }
            setPromoList(urlArr)
            setOnPromoClickListener { position ->
                if (::slidesList.isInitialized)
                    listener.onRechargeSectionItemClicked(slidesList[position])
            }
            setOnPromoAllClickListener {  }
            setOnPromoScrolledListener {  }
            setOnPromoLoadedListener {
                bannerIndicator.gone()
                bannerSeeAll.gone()
            }

            val bannerWidth = getScreenWidth(context as Activity) - BANNER_WIDTH_SPACE.toPx()
            customWidth = bannerWidth
            customHeight = bannerWidth / BANNER_HEIGHT_RATIO

            buildView()
        }
    }

    @SuppressLint("DeprecatedMethod")
    fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = activity.windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_swipe_banner

        private const val BANNER_WIDTH_SPACE = 32
        private const val BANNER_HEIGHT_RATIO = 3
    }
}
