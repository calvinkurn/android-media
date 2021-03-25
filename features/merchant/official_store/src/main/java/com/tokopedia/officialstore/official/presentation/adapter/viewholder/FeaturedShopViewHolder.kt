package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.presentation.widget.FeaturedShopAdapter.OnItemClickListener
import com.tokopedia.unifyprinciples.Typography

class FeaturedShopViewHolder(
    itemView: View,
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.ViewHolder(itemView) {

    private val container: ConstraintLayout? = itemView.findViewById(R.id.featured_shop_background)
    private val imageView: ImageView? = itemView.findViewById(R.id.image_featured_shop)
    private val itemDesc: Typography? = itemView.findViewById(R.id.title_featured_shop)
    private val itemLogo: ImageView? = itemView.findViewById(R.id.logo_featured_shop)
    private val imageLoading: ImageView? = itemView.findViewById(R.id.image_featured_loading)
    private val template = "%s %s"

    fun bind(shop: Grid) {
        try {
            container?.setBackgroundColor(Color.parseColor(shop.backColor))
        }catch (ex: Exception){
        }
        imageView?.let {
            Glide.with(it.context)
                .load(shop.productImageUrl)
                .dontAnimate()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(imageRequestListener())
                .into(it)
        }
        itemLogo?.let{
            Glide.with(it.context)
                    .load(shop.imageUrl)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(imageRequestListener())
                    .into(it)
        }
        itemDesc?.text = shop.label

        setOnClickListener(shop)
    }

    private fun imageRequestListener() = object: RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean = false

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            imageLoading?.visibility = View.GONE
            imageView?.setImageDrawable(resource)
            return false
        }
    }

    private fun setOnClickListener(shop: Grid) {
        itemView.setOnClickListener {
            if (adapterPosition > RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(adapterPosition, shop)
            }
        }
    }

}