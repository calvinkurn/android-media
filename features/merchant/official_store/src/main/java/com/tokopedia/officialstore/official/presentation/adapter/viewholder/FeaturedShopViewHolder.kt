package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout.LayoutParams
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.widget.FeaturedShopAdapter.OnItemClickListener

class FeaturedShopViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener?
) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView? = itemView.findViewById(R.id.image_featured_shop)
    private val imageLoading: ImageView? = itemView.findViewById(R.id.image_featured_loading)

    init {
        calculateImageSize()
    }

    fun bind(shop: Shop) {
        imageView?.let {
            Glide.with(it.context)
                    .load(shop.imageUrl)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(imageRequestListener())
                    .into(it)
        }

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

    private fun setOnClickListener(shop: Shop) {
        itemView.setOnClickListener {
            if (adapterPosition > RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(adapterPosition, shop)
            }
        }
    }

    private fun calculateImageSize() {
        val display = itemView.context.resources.displayMetrics
        val screenWidth = display.widthPixels

        val aspectRatio = 1.85 / 1
        val imageWidth = screenWidth / 2
        val imageHeight = (imageWidth / aspectRatio).toInt()

        LayoutParams(LayoutParams.MATCH_PARENT, imageHeight).let { layoutParams ->
            imageView?.layoutParams = layoutParams
            imageLoading?.layoutParams = layoutParams
        }
    }
}