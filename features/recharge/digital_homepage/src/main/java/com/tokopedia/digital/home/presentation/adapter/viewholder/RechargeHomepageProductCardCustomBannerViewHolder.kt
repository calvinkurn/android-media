package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeProductCardCustomBannerModel
import com.tokopedia.digital.home.presentation.adapter.RechargeCustomBannerProductCardAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.util.loadImage
import kotlinx.android.synthetic.main.view_recharge_home_product_card_custom_banner.view.*
import kotlin.math.abs

/**
 * @author by jessicasean on 27/10/20.
 */

class RechargeHomepageProductCardCustomBannerViewHolder(
        val view: View,
        val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeProductCardCustomBannerModel>(view) {

    private lateinit var layoutManager: LinearLayoutManager

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_product_card_custom_banner
    }

    override fun bind(element: RechargeProductCardCustomBannerModel) {
        val section = element.section
        if (section.items.isNotEmpty()) {
            showItemView()
            setUpBackground(section)
            setUpList(section)
        } else {
            hideItemView()
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun hideItemView() {
        itemView.parallax_view.visibility = View.INVISIBLE
        itemView.rv_recharge_product.visibility = View.INVISIBLE
        itemView.background_loader.visibility = View.VISIBLE
    }

    private fun showItemView() {
        itemView.parallax_view.visibility = View.VISIBLE
        itemView.rv_recharge_product.visibility = View.VISIBLE
        itemView.background_loader.visibility = View.INVISIBLE
    }

    private fun setUpBackground(section: RechargeHomepageSections.Section) {
        itemView.parallax_image.loadImage(section.items.firstOrNull()?.mediaUrl ?: "")
        itemView.parallax_background.setBackgroundColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Red_R200))
    }

    private fun setUpList(section: RechargeHomepageSections.Section) {
        with(itemView) {
            parallax_image.alpha = 1f
            rv_recharge_product.resetLayout()
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            rv_recharge_product.layoutManager = layoutManager
            rv_recharge_product.adapter = RechargeCustomBannerProductCardAdapter(section.items)

            rv_recharge_product.addOnScrollListener(getParallaxEffect())
        }
    }

    private fun getParallaxEffect(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                    firstView?.let {
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * 0.2f
                        itemView.parallax_view.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            itemView.parallax_image.alpha = 1 - alpha
                        }
                    }
                }
            }
        }
    }

    private fun RecyclerView.resetLayout() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        this.layoutParams = carouselLayoutParams
    }


}
