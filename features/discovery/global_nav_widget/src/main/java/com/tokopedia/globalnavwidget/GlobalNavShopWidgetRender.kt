package com.tokopedia.globalnavwidget

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.Typography

internal class GlobalNavShopWidgetRender(
    itemView: View
) {

    private val globalNavShopContainer: View? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopWidgetContainer)
    }

    private val imageShopView: AppCompatImageView? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopImage)
    }

    private val badgeShopView: AppCompatImageView? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopImageBadgeShop)
    }

    private val nameTextView: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopTitle)
    }

    private val ratingIconView: AppCompatImageView? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopRatingIcon)
    }

    private val rating: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopRating)
    }

    private val ratingDots: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopRatingDots)
    }

    private val shopLocationView: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopLocation)
    }

    private val seeAllContainer: LinearLayoutCompat? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopSeeAllContainer)
    }

    private val globalNavShopToOtherShop: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.globalNavShopToOtherShop)
    }

    fun setItemGlobalNavigation(
        item: GlobalNavWidgetModel,
        listener: GlobalNavWidgetListener,
    ) {
        val itemShop = item.itemList.firstOrNull() ?: return
        globalNavShopContainer?.setOnClickListener {
            listener.onClickItem(itemShop)
        }
        renderImageShop(itemShop)
        renderShopBadge(itemShop)
        renderName(itemShop)
        renderRating(itemShop)
        renderLocationShop(itemShop)
        renderSeeAll(item) {
            listener.onClickSeeAll(item)
        }
    }

    private fun renderImageShop(itemShop: GlobalNavWidgetModel.Item) {
        imageShopView?.loadImageCircle(itemShop.imageUrl)
    }

    private fun renderShopBadge(itemShop: GlobalNavWidgetModel.Item) {
        badgeShopView?.shouldShowWithAction(itemShop.logoUrl.isNotEmpty()) {
            badgeShopView?.loadImage(itemShop.logoUrl)
        }
    }

    private fun renderName(itemShop: GlobalNavWidgetModel.Item) {
        nameTextView?.text = itemShop.name
    }

    private fun renderRating(itemShop: GlobalNavWidgetModel.Item) {
        ratingIconView?.showWithCondition(itemShop.info.isNotEmpty())
        ratingDots?.showWithCondition(itemShop.info.isNotEmpty())
        rating?.shouldShowWithAction(itemShop.info.isNotEmpty()) {
            rating?.text = itemShop.info
        }
    }

    private fun renderLocationShop(itemShop: GlobalNavWidgetModel.Item) {
        shopLocationView?.shouldShowWithAction(itemShop.subtitle.isNotEmpty()) {
            shopLocationView?.text = itemShop.subtitle
        }
    }

    private fun renderSeeAll(itemShop: GlobalNavWidgetModel, onClickSeeAll: () -> Unit) {
        seeAllContainer?.shouldShowWithAction(itemShop.clickSeeAllApplink.isNotEmpty()) {
            globalNavShopToOtherShop?.text = itemShop.info
            seeAllContainer?.setOnClickListener {
                onClickSeeAll()
            }
        }
    }

}
