package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeProductCardCustomBannerModel
import com.tokopedia.digital.home.presentation.adapter.RechargeCustomBannerProductCardAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.view_recharge_home_product_card_custom_banner.view.*
import kotlinx.android.synthetic.main.view_recharge_home_product_card_custom_banner_item.view.*
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
        when {
            section.items.size == 1 -> {
                setUpBackground(section)
                setUpSingleItemView(section)
                showItemView()
            }
            section.items.isNotEmpty() -> {
                setUpList(section)
                setUpBackground(section)
                setSnapEffect()
                showItemView()
                itemView.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            }
            else -> {
                hideItemView()
                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    private fun setUpSingleItemView(section: RechargeHomepageSections.Section) {
        val item = section.items.firstOrNull()
        item?.let { element ->
            with(itemView) {
                rv_recharge_product.hide()
                layout_single_item.show()

                layout_single_item.run {
                    tv_recharge_product_title.text = element.title
                    tv_recharge_product_content.text = MethodChecker.fromHtml(element.subtitle)
                    tv_recharge_product_ori_price.text = MethodChecker.fromHtml(element.label1)
                    tv_recharge_product_ori_price.paintFlags = tv_recharge_product_discount_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tv_recharge_product_discount_price.text = MethodChecker.fromHtml(element.label2)

                    iv_recharge_product_image.loadImageRounded(element.mediaUrl)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        iv_recharge_product_image.clipToOutline = true
                    }

                    if (element.label3.isNotEmpty()) {
                        tv_recharge_product_tag.text = element.label3
                        tv_recharge_product_tag.show()
                    } else {
                        tv_recharge_product_tag.hide()
                    }
                    setOnClickListener {
                        listener.onRechargeSectionItemClicked(element)
                        RouteManager.route(context, element.applink)
                    }
                }
            }
        }
    }

    private fun setSnapEffect() {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(itemView.rv_recharge_product)
    }

    private fun hideItemView() {
        itemView.parallax_view.visibility = View.INVISIBLE
        itemView.background_loader.visibility = View.VISIBLE
        itemView.rv_recharge_product.hide()
        itemView.layout_single_item.hide()
    }

    private fun showItemView() {
        itemView.parallax_view.visibility = View.VISIBLE
        itemView.background_loader.visibility = View.INVISIBLE
    }

    private fun setUpBackground(section: RechargeHomepageSections.Section) {
        itemView.parallax_image.loadImage(section.mediaUrl)
        try {
            if (section.label1.isNotEmpty()) itemView.parallax_background.setBackgroundColor(Color.parseColor(section.label1))
        } catch (e: Throwable) {
            itemView.parallax_background.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
    }

    private fun setUpList(section: RechargeHomepageSections.Section) {
        with(itemView) {
            rv_recharge_product.show()
            layout_single_item.hide()

            parallax_image.alpha = 1f
            rv_recharge_product.resetLayout()
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            rv_recharge_product.layoutManager = layoutManager
            rv_recharge_product.adapter = RechargeCustomBannerProductCardAdapter(section.items, listener)

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
                        val distanceFromLeft = it.left - itemView.resources.getDimensionPixelSize(com.tokopedia.home_component.R.dimen.product_card_flashsale_width)
                        val translateX = distanceFromLeft * 0.2f
                        itemView.parallax_view.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            itemView.parallax_image.alpha = 1 - alpha
                        } else {
                            itemView.parallax_image.alpha = 1f
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
