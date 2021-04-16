package com.tokopedia.favorite.view.adapter.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tkpd.library.utils.ImageHandler
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.favorite.R
import com.tokopedia.favorite.utils.TrackingConst
import com.tokopedia.favorite.view.viewmodel.FavoriteShopUiModel
import com.tokopedia.track.TrackApp
import java.util.*

/**
 * @author kulomady on 1/24/17.
 */
class FavoriteShopViewHolder(itemView: View) : AbstractViewHolder<FavoriteShopUiModel?>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.favorite_listview_manage_favorited_shop
    }

    init {
        initView(itemView)
    }

    private var favoriteShop: FavoriteShopUiModel? = null
    private val context: Context = itemView.context
    private var avatarImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var locationTextview: TextView? = null
    private var favoriteImageView: ImageView? = null
    private var badgeIcon: ImageView? = null

    private fun initView(itemView: View) {
        avatarImageView = itemView.findViewById(R.id.shop_avatar)
        nameTextView = itemView.findViewById(R.id.shop_name)
        locationTextview = itemView.findViewById(R.id.location)
        favoriteImageView = itemView.findViewById(R.id.fav_button)
        badgeIcon = itemView.findViewById(R.id.image_badge)
        val shopLayout = itemView.findViewById<View>(R.id.shop_layout)
        shopLayout.setOnClickListener { onShopLayoutClicked() }
    }

    override fun bind(favoriteShop: FavoriteShopUiModel?) {
        this.favoriteShop = favoriteShop
        if (favoriteShop == null) return

        if (favoriteShop.shopName != null) {
            nameTextView?.text = MethodChecker.fromHtml(favoriteShop.shopName)
        }
        if (favoriteShop.shopLocation != null) {
            locationTextview!!.text = favoriteShop.shopLocation
        }
        favoriteImageView!!.setImageResource(
                if (favoriteShop.isFavoriteShop) R.drawable.ic_faved else R.drawable.ic_fav)
        if (favoriteShop.shopAvatarImageUrl != null) {
            ImageHandler.loadImageFit2(
                    itemView.context, avatarImageView, favoriteShop.shopAvatarImageUrl)
        }
        val badgeUrl = favoriteShop.badgeUrl
        if (badgeUrl != null && !badgeUrl.isEmpty()) {
            badgeIcon!!.visibility = View.VISIBLE
            ImageHandler.loadImageFit2(itemView.context, badgeIcon, favoriteShop.badgeUrl)
        } else {
            badgeIcon!!.visibility = View.GONE
        }
    }

    fun onShopLayoutClicked() {
        eventFavoriteShop()
        val intent = RouteManager.getIntent(context, ApplinkConst.SHOP, favoriteShop!!.shopId)
        context.startActivity(intent)
    }

    fun eventFavoriteShop() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackingConst.Event.FAVORITE,
                TrackingConst.Category.HOMEPAGE.toLowerCase(Locale.getDefault()),
                TrackingConst.Action.CLICK_SHOP_FAVORITE,
                "")
    }

}
