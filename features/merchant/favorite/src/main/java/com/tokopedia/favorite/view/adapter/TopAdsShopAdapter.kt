package com.tokopedia.favorite.view.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.favorite.R
import com.tokopedia.favorite.utils.TrackingConst
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem
import com.tokopedia.topads.sdk.utils.ImageLoader
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by erry on 30/01/17.
 */
class TopAdsShopAdapter(
        private val favoriteClickListener: FavoriteClickListener?,
        private val impressionImageLoadedListener: ImpressionImageLoadedListener
) : RecyclerView.Adapter<TopAdsShopAdapter.ViewHolder>() {

    companion object {
        private const val className = "com.tokopedia.favorite.view.adapter.TopAdsShopAdapter"

        fun mainContentDescription(position: Int): String {
            return "top-ads-item-main-content-$position"
        }
    }

    protected var anim: ScaleAnimation? = null
    private val data: MutableList<TopAdsShopItem> = ArrayList()
    private var context: Context? = null
    private val PATH_VIEW = "views"
    private var imageLoader: ImageLoader? = null

    fun setData(data: List<TopAdsShopItem>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        imageLoader = ImageLoader(context)
        val itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.favorite_listview_reccommend_shop, parent, false)
        createScaleAnimation()
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopItem = data[position]
        holder.shopName.text = Html.fromHtml(shopItem.shopName)
        holder.shopLocation.text = Html.fromHtml(shopItem.shopLocation)
        imageLoader?.loadImage(shopItem.shopImageEcs, null, holder.shopIcon)

        val shopImageUrl = shopItem.shopImageUrl
        if (shopImageUrl != null && shopImageUrl.contains(PATH_VIEW)) {
            impressionImageLoadedListener.onImageLoaded(
                    className,
                    shopImageUrl,
                    shopItem.shopId,
                    shopItem.shopName,
                    shopItem.shopImageUrl
            )
        }

        setShopCover(holder, shopItem)
        setFavorite(holder, shopItem)
        holder.mainContent.contentDescription = mainContentDescription(position)
        holder.mainContent.setOnClickListener(onShopClicked(shopItem))
        holder.favButton.setOnClickListener(
                if (shopItem.isFav) null else onFavClicked(holder, shopItem)
        )
    }

    private fun setShopCover(holder: ViewHolder, shopItem: TopAdsShopItem) {
        if (shopItem.shopCoverUrl == null) {
            holder.shopCover.setImageResource(com.tokopedia.abstraction.R.drawable.ic_loading_toped)
        } else {
            Glide.with(context!!)
                    .load(shopItem.shopCoverEcs)
                    .dontAnimate()
                    .placeholder(com.tokopedia.topads.sdk.R.drawable.loading_page)
                    .error(com.tokopedia.topads.sdk.R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(375, 97)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?,
                                                     model: Any,
                                                     target: Target<Drawable?>,
                                                     dataSource: DataSource,
                                                     isFirstResource: Boolean): Boolean {
                            val coverUrl = shopItem.shopCoverUrl
                            if (coverUrl != null
                                    && coverUrl.contains(PATH_VIEW)
                                    && !isFirstResource) {
                                impressionImageLoadedListener.onImageLoaded(
                                        className,
                                        coverUrl,
                                        shopItem.shopId,
                                        shopItem.shopName,
                                        shopItem.shopImageUrl
                                )
                            }
                            return false
                        }
                    })
                    .into(holder.shopCover)
        }
    }

    private fun setFavorite(holder: ViewHolder, shopItem: TopAdsShopItem) {
        try {
            if (shopItem.isFav) {
                holder.favButton.setImageResource(R.drawable.ic_faved)
            } else {
                holder.favButton.setImageResource(R.drawable.ic_fav_white)
            }
        } catch (e: Exception) {
            holder.favButton.setImageResource(R.drawable.ic_fav_white)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun createScaleAnimation() {
        anim = ScaleAnimation(1f, 1.25f, 1f, 1.25f,
                Animation.RELATIVE_TO_SELF,
                0.2.toFloat(), Animation.RELATIVE_TO_SELF, 0.2.toFloat())
        anim!!.duration = 250
        anim!!.repeatCount = Animation.INFINITE
        anim!!.repeatMode = Animation.REVERSE
        anim!!.fillAfter = false
    }

    private fun onShopClicked(item: TopAdsShopItem): View.OnClickListener {
        return View.OnClickListener { view ->
            val context = view.context
            TopAdsUrlHitter(view.context).hitClickUrl(
                    className,
                    item.shopClickUrl,
                    item.shopId,
                    item.shopName,
                    item.shopImageUrl)
            eventFavoriteViewRecommendation()
            val intent = RouteManager.getIntent(context, ApplinkConst.SHOP, item.shopId)
            context.startActivity(intent)
        }
    }

    fun eventFavoriteViewRecommendation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackingConst.Event.FAVORITE,
                TrackingConst.Category.HOMEPAGE.toLowerCase(Locale.getDefault()),
                TrackingConst.Action.CLICK_SHOP_FAVORITE,
                "")
    }

    private fun onFavClicked(holder: ViewHolder, item: TopAdsShopItem): View.OnClickListener {
        return View.OnClickListener { view ->
            if (!item.isFav) {
                view.startAnimation(anim)
                item.isFav = true
                holder.favButton.setImageResource(R.drawable.ic_faved)
            }
            favoriteClickListener?.onFavoriteShopClicked(view, item)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainContent: LinearLayout
        var shopCover: ImageView
        var shopIcon: ImageView
        var shopName: TextView
        var shopLocation: TextView
        var shopInfo: LinearLayout
        var favButton: ImageView
        fun getContext(): Context {
            return itemView.context
        }

        init {
            mainContent = itemView.findViewById<View>(R.id.main_content) as LinearLayout
            shopCover = itemView.findViewById<View>(R.id.shop_cover) as ImageView
            shopIcon = itemView.findViewById<View>(R.id.shop_icon) as ImageView
            shopName = itemView.findViewById<View>(R.id.shop_name) as TextView
            shopLocation = itemView.findViewById<View>(R.id.shop_location) as TextView
            shopInfo = itemView.findViewById<View>(R.id.shop_info) as LinearLayout
            favButton = itemView.findViewById<View>(R.id.fav_button) as ImageView
        }
    }

    interface ImpressionImageLoadedListener {

        fun onImageLoaded(
                className: String?,
                url: String,
                productId: String?,
                productName: String?,
                imageUrl: String?
        )

    }

}
