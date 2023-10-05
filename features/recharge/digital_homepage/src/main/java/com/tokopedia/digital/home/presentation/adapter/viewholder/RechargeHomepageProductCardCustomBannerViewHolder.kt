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
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardCustomBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeProductCardCustomBannerModel
import com.tokopedia.digital.home.presentation.adapter.RechargeCustomBannerProductCardAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
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
        val bind = ViewRechargeHomeProductCardCustomBannerBinding.bind(itemView)
        val section = element.section
        when {
            section.items.size == 1 -> {
                setUpBackground(bind, section)
                setUpSingleItemView(bind, section)
                showItemView(bind)
            }
            section.items.isNotEmpty() -> {
                setUpList(bind, section)
                setUpBackground(bind, section)
                setSnapEffect(bind)
                showItemView(bind)
                itemView.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            }
            else -> {
                hideItemView(bind)
                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    private fun setUpSingleItemView(
        bind: ViewRechargeHomeProductCardCustomBannerBinding,
        section: RechargeHomepageSections.Section
    ) {
        val item = section.items.firstOrNull()
        item?.let { element ->
            with(bind) {
                rvRechargeProduct.hide()

                layoutSingleItem.root.show()

                layoutSingleItem.run {
                    tvRechargeProductTitle.text = element.title
                    tvRechargeProductContent.text = MethodChecker.fromHtml(element.subtitle)
                    tvRechargeProductOriPrice.text = MethodChecker.fromHtml(element.label1)
                    tvRechargeProductOriPrice.paintFlags = tvRechargeProductDiscountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvRechargeProductDiscountPrice.text = MethodChecker.fromHtml(element.label2)

                    ivRechargeProductImage.loadImageRounded(element.mediaUrl)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ivRechargeProductImage.clipToOutline = true
                    }

                    if (element.label3.isNotEmpty()) {
                        tvRechargeProductTag.text = element.label3
                        tvRechargeProductTag.show()
                    } else {
                        tvRechargeProductTag.hide()
                    }
                    root.setOnClickListener {
                        listener.onRechargeSectionItemClicked(element)
                    }
                }
            }
        }
    }

    private fun setSnapEffect(bind: ViewRechargeHomeProductCardCustomBannerBinding) {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(bind.rvRechargeProduct)
    }

    private fun hideItemView(bind: ViewRechargeHomeProductCardCustomBannerBinding) {
        bind.parallaxView.visibility = View.INVISIBLE
        bind.backgroundLoader.visibility = View.VISIBLE

        bind.rvRechargeProduct.hide()
        bind.layoutSingleItem.root.hide()
    }

    private fun showItemView(bind: ViewRechargeHomeProductCardCustomBannerBinding) {
        bind.parallaxView.visibility = View.VISIBLE
        bind.backgroundLoader.visibility = View.INVISIBLE
    }

    private fun setUpBackground(
        bind: ViewRechargeHomeProductCardCustomBannerBinding,
        section: RechargeHomepageSections.Section
    ) {
        with(bind) {
            parallaxImage.loadImage(section.mediaUrl)
            try {
                if (section.label1.isNotEmpty()) parallaxBackground.setBackgroundColor(Color.parseColor(section.label1))
            } catch (e: IllegalArgumentException) {
                parallaxBackground.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            }
        }
    }

    private fun setUpList(
        bind: ViewRechargeHomeProductCardCustomBannerBinding,
        section: RechargeHomepageSections.Section
    ) {
        with(bind) {
            rvRechargeProduct.show()
            layoutSingleItem.root.hide()

            parallaxImage.alpha = 1f
            rvRechargeProduct.resetLayout()
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            rvRechargeProduct.layoutManager = layoutManager
            rvRechargeProduct.adapter = RechargeCustomBannerProductCardAdapter(section.items, listener)

            rvRechargeProduct.addOnScrollListener(getParallaxEffect(bind))
        }
    }

    private fun getParallaxEffect(bind: ViewRechargeHomeProductCardCustomBannerBinding): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                    firstView?.let {
                        val distanceFromLeft = it.left - itemView.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.product_card_flashsale_width)
                        val translateX = distanceFromLeft * 0.2f
                        bind.parallaxView.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            bind.parallaxImage.alpha = 1 - alpha
                        } else {
                            bind.parallaxImage.alpha = 1f
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
