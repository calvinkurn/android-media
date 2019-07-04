package com.tokopedia.tkpd.home.adapter.viewholder

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tkpd.library.utils.ImageHandler

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.core.`var`.ProductItem
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.customwidget.FlowLayout
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage
import com.tokopedia.tkpd.R
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistProductViewModel
import com.tokopedia.tkpd.home.presenter.WishListView
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistProductListViewHolder(itemView: View,
                                    val wishlistAnalytics: WishlistAnalytics,
                                    var wishlistView: WishListView) : AbstractViewHolder<WishlistProductViewModel>(itemView) {

    val context = itemView.context

    override fun bind(element: WishlistProductViewModel) {
        var product = element.productItem
        ImageHandler.loadImageFit2(context, itemView.findViewById<ImageView>(R.id.product_image), element.productItem.imgUri)
        itemView.findViewById<TextView>(R.id.title).setText(Html.fromHtml(product.name))
        itemView.findViewById<TextView>(R.id.shop_name).setText(Html.fromHtml(product.shop))
        itemView.findViewById<TextView>(R.id.price).setText(product.price)

        if (product.getOfficial()!!) {
            itemView.findViewById<TextView>(R.id.location).setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core2.R.drawable.ic_icon_authorize_grey, 0, 0, 0)
            itemView.findViewById<TextView>(R.id.location).setText(context.getResources().getString(com.tokopedia.core2.R.string.authorized))
        } else {
            itemView.findViewById<TextView>(R.id.location).setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core2.R.drawable.ic_icon_location_grey_wishlist, 0, 0, 0)
            itemView.findViewById<TextView>(R.id.location).setText(product.getShopLocation())
        }
        setBadges(product)
        setLabels(product)
        itemView.findViewById<View>(R.id.container).setOnClickListener {
            onProductItemClicked(product, adapterPosition)
        }

        if (product.isWishlist!!) {
            itemView.findViewById<View>(R.id.wishlist).setVisibility(View.VISIBLE)
            itemView.findViewById<View>(R.id.wishlist).setOnClickListener(onDeleteWishlistClicked(product.getId(), position))
            var buyWishlistBtn = itemView.findViewById<TextView>(R.id.buy_button)
            if (product.isAvailable!!) {
                setBuyButtonAvailable(buyWishlistBtn)
                buyWishlistBtn.setOnClickListener(onBuyButtonClicked(product.getId()))
            } else {
                setBuyButtonUnavailable(buyWishlistBtn)
                buyWishlistBtn.setOnClickListener(null)
            }
        }
    }

    fun getColor(context: Context, id: Int): Int {
        val version = Build.VERSION.SDK_INT
        return if (version >= 23) {
            ContextCompat.getColor(context, id)
        } else {
            context.resources.getColor(id)
        }
    }

    private fun setBuyButtonUnavailable(buyButtonUnavailable: TextView) {
        buyButtonUnavailable.setBackgroundResource(R.drawable.btn_buy_unvailable)
        buyButtonUnavailable.setTextColor(getColor(context, R.color.grey_700))
        buyButtonUnavailable.setText(R.string.title_empty_stock)
    }

    private fun setBuyButtonAvailable(buyButtonUnavailable: TextView) {
        buyButtonUnavailable.setBackgroundResource(R.drawable.rect_orange)
        buyButtonUnavailable.setTextColor(getColor(context, R.color.white))
        buyButtonUnavailable.setText(R.string.title_buy)
    }

    private fun onDeleteWishlistClicked(productId: String, position: Int): View.OnClickListener {
        return View.OnClickListener {
            if (wishlistView != null)
                wishlistView.displayDeleteWishlistDialog(productId, position)
        }
    }

    private fun onBuyButtonClicked(productId: String): View.OnClickListener {
        return View.OnClickListener { v ->
            UnifyTracking.eventWishlistBuy(v.context)
            if (wishlistView != null) wishlistView.displayAddToCart(productId)
        }
    }

    fun onProductItemClicked(data: ProductItem, position: Int): View.OnClickListener {
        return View.OnClickListener { view ->
            UnifyTracking.eventWishlistView(view.context, data.name)
            wishlistAnalytics.trackEventClickOnProductWishlist((position + 1).toString(), data.getProductAsObjectDataLayerForWishlistClick(position + 1))
            val intent = getProductIntent(data.id)
            context.startActivity(intent)
        }
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    private fun setLabels(data: ProductItem) {
        var container = itemView.findViewById<FlowLayout>(R.id.label_container)
        container.removeAllViews()
        if (data.getLabels() != null) {
            for (label in data.getLabels()) {
                val view = LayoutInflater.from(context).inflate(R.layout.label_layout, null)
                val labelText = view.findViewById<View>(R.id.label) as TextView
                labelText.text = label.title
                if (label.color.toLowerCase() != context.getString(R.string.white_hex_color)) {
                    labelText.setBackgroundResource(R.drawable.bg_label)
                    labelText.setTextColor(ContextCompat.getColor(context, R.color.white))
                    val tint = ColorStateList.valueOf(Color.parseColor(label.color))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        labelText.backgroundTintList = tint
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint)
                    }
                }
                container.addView(view)
            }
        }
    }
    private fun setBadges(data: ProductItem) {
        var container = itemView.findViewById<LinearLayout>(R.id.badges_container)
        container.removeAllViews()
        if (data.getBadges() != null) {
            for (badges in data.getBadges()) {
                LuckyShopImage.loadImage(context, badges.imageUrl, container)
            }
        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.listview_product_item
    }
}
