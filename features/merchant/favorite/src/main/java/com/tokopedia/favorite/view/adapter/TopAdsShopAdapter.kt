package com.tokopedia.favorite.view.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
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
import com.tokopedia.config.GlobalConfig
import com.tokopedia.favorite.R
import com.tokopedia.favorite.utils.TrackingConst
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem
import com.tokopedia.topads.sdk.utils.ImageLoader
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.track.TrackApp
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by erry on 30/01/17.
 */
class TopAdsShopAdapter(
        private val favoriteClickListener: FavoriteClickListener?
) : RecyclerView.Adapter<TopAdsShopAdapter.ViewHolder>() {

    companion object {
        private const val className = "com.tokopedia.favorite.view.adapter.TopAdsShopAdapter"
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
        imageLoader!!.loadImage(shopItem.shopImageUrl, shopItem.shopImageEcs, holder.shopIcon)
        setShopCover(holder, shopItem.shopCoverUrl, shopItem.shopCoverEcs)
        setFavorite(holder, shopItem)
        holder.mainContent.setOnClickListener(onShopClicked(shopItem))
        holder.favButton.setOnClickListener(
                if (shopItem.isFav) null else onFavClicked(holder, shopItem)
        )
    }

    private fun glideRequestListener(coverUri: String): RequestListener<Drawable?> {
        return object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
            ): Boolean {
                if (coverUri.contains(PATH_VIEW) && !isFirstResource) {
                    ImpresionTask(className).execute(coverUri)
                }
                return false
            }
        }
    }

    private fun setShopCover(holder: ViewHolder, coverUri: String?, ecs: String?) {
        if (coverUri == null) {
            holder.shopCover.setImageResource(com.tokopedia.abstraction.R.drawable.ic_loading_toped)
        } else {
            Glide.with(context!!)
                    .load(ecs)
                    .dontAnimate()
                    .placeholder(com.tokopedia.topads.sdk.R.drawable.loading_page)
                    .error(com.tokopedia.topads.sdk.R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(375, 97)
                    .listener(glideRequestListener(coverUri))
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
        anim = ScaleAnimation(1f, 1.25f, // from x and y
                1f, 1.25f, // to x and y
                Animation.RELATIVE_TO_SELF, 0.2.toFloat(), // pivot x type and value
                Animation.RELATIVE_TO_SELF, 0.2.toFloat()) // pivot y type and value
        anim!!.duration = 250
        anim!!.repeatCount = Animation.INFINITE
        anim!!.repeatMode = Animation.REVERSE
        anim!!.fillAfter = false
    }

    private fun onShopClicked(item: TopAdsShopItem): View.OnClickListener {
        return View.OnClickListener { view ->
            val context = view.context
            FireTopAdsActionAsyncTask().execute(item.shopClickUrl)
            eventFavoriteViewRecommendation()
            val intent = RouteManager.getIntent(context, ApplinkConst.SHOP, item.shopId)
            context.startActivity(intent)
        }
    }

    /**
     * Hack solution using AsyncTask
     * This is to handled fire and forget url to shopfavorit
     * Previously was using Volley (TopadsUtil.clickTopAdsAction)
     */
    class FireTopAdsActionAsyncTask : AsyncTask<String?, Void?, Void?>() {

        override fun doInBackground(vararg params: String?): Void? {
            val url: URL
            val urlConnection: HttpURLConnection
            try {
                val stringBuilder = params[0] +
                        "?device=android&os_type=1&appversion=" +
                        GlobalConfig.VERSION_CODE
                url = URL(stringBuilder)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    readStream(urlConnection.inputStream)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }

        private fun readStream(`in`: InputStream) {
            var reader: BufferedReader? = null
            val response = StringBuilder()
            try {
                reader = BufferedReader(InputStreamReader(`in`))
                var line: String? = ""
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun eventFavoriteViewRecommendation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackingConst.Event.FAVORITE,
                TrackingConst.Category.HOMEPAGE.toLowerCase(),
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

}
