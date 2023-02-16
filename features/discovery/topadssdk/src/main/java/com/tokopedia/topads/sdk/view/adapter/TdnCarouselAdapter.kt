package com.tokopedia.topads.sdk.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_VERTICAL_CAROUSEL
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import timber.log.Timber

class TdnCarouselAdapter(
    private val onTdnBannerClicked: (applink: String) -> Unit,
    private val cornerRadius: Int,
    private val isUsingInfiniteScroll: Boolean,
    private val onLoadFailed: () -> Unit,
    private val onTdnBannerImpressed: () -> Unit
) : RecyclerView.Adapter<TdnCarouselAdapter.TdnCarouselViewHolder>() {

    private val shopAdsProductItemList = arrayListOf<TopAdsImageViewModel>()
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TdnCarouselViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_widget_single_tdn_view, parent, false)
        return TdnCarouselViewHolder(view, cornerRadius, onTdnBannerClicked, onLoadFailed, onTdnBannerImpressed)
    }

    override fun onBindViewHolder(holder: TdnCarouselViewHolder, position: Int) {
        if (shopAdsProductItemList.isNotEmpty()) holder.bind(shopAdsProductItemList[position % shopAdsProductItemList.size])
    }

    override fun getItemCount(): Int {
        return if (isUsingInfiniteScroll && shopAdsProductItemList.size > Int.ONE) Integer.MAX_VALUE else shopAdsProductItemList.size
    }

    fun setList(list: List<TopAdsImageViewModel>) {
        shopAdsProductItemList.clear()
        shopAdsProductItemList.addAll(list)
        notifyDataSetChanged()
    }

    inner class TdnCarouselViewHolder(
        itemView: View,
        private val cornerRadius: Int,
        private val onTdnBannerClicked: (applink: String) -> Unit,
        private val onLoadFailed: () -> Unit,
        private val onTdnBannerImpressed: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tdnBanner = itemView.findViewById<ImageView>(R.id.tdnBanner)
        private var tdnShimmer = itemView.findViewById<ImageView>(R.id.tdnShimmer)

        fun bind(shopProductModelItem: TopAdsImageViewModel?) {
            shopProductModelItem?.let {
                tdnShimmer.layoutParams = getLayoutParams(
                    shopProductModelItem.layoutType,
                    shopProductModelItem.imageWidth,
                    shopProductModelItem.imageHeight
                )
            }
            shopProductModelItem?.let {
                loadImage(
                    it,
                    onTdnBannerClicked = onTdnBannerClicked,
                    cornerRadius = cornerRadius,
                    onLoadFailed = onLoadFailed,
                    onTdnBannerImpressed = onTdnBannerImpressed
                )
            }
        }

        private fun getLayoutParams(
            layoutType: String,
            imageWidth: Int,
            imageHeight: Int
        ): ViewGroup.LayoutParams {
            return if (layoutType == TYPE_VERTICAL_CAROUSEL) {
                val width =
                    widthVerticalCarousel(itemView.context.resources.displayMetrics.widthPixels)
                ConstraintLayout.LayoutParams(
                    width,
                    getHeight(imageWidth, imageHeight, width)
                )
            } else {
                ConstraintLayout.LayoutParams(
                    widthHorizontalCarousel(itemView.context.resources.displayMetrics.widthPixels),
                    itemView.context.resources.getDimensionPixelSize(R.dimen.sdk_dp_112)
                )
            }
        }

        fun loadImage(
            imageData: TopAdsImageViewModel,
            cornerRadius: Int = Int.ZERO,
            onLoadFailed: () -> Unit = {},
            onTdnBannerClicked: (applink: String) -> Unit,
            onTdnBannerImpressed: () -> Unit
        ) {
            if (!imageData.imageUrl.isNullOrEmpty()) {
                val width = itemView.context.resources.displayMetrics.widthPixels
                val calculatedWidth = if (imageData.layoutType == TYPE_VERTICAL_CAROUSEL) {
                    widthVerticalCarousel(width)
                } else {
                    widthHorizontalCarousel(width)
                }
                getRequestBuilder(imageData.imageUrl, cornerRadius).override(
                    calculatedWidth,
                    getHeight(imageData.imageWidth, imageData.imageHeight, calculatedWidth)
                )
                    .addListener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Timber.d("Error in loading TDN Banner")
                            onLoadFailed.invoke()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            recordImpression(imageData, onTdnBannerImpressed)
                            if (!isUsingInfiniteScroll) recyclerView?.smoothScrollBy(Int.ONE, Int.ONE)
                            Timber.d("TDN Banner is loaded successfully")

                            tdnShimmer.hide()

                            recordClick(imageData, onTdnBannerClicked)

                            return false
                        }
                    })
                    .into(tdnBanner)
            } else {
                tdnBanner.hide()
            }
        }

        private fun widthHorizontalCarousel(width: Int): Int {
            return width - (width * 10 / 100)
        }

        private fun recordClick(
            imageData: TopAdsImageViewModel,
            onTdnBannerClicked: (applink: String) -> Unit
        ) {
            itemView.setOnClickListener {
                imageData.applink?.let { applink -> onTdnBannerClicked(applink) }
                Timber.d("TDN Banner is clicked")
                TopAdsUrlHitter(itemView.context).hitClickUrl(
                    this@TdnCarouselAdapter.javaClass.canonicalName,
                    imageData.adClickUrl,
                    "",
                    "",
                    ""
                )
            }
        }

        private fun recordImpression(
            imageData: TopAdsImageViewModel,
            onTdnBannerImpressed: () -> Unit
        ) {
            imageData.ImpressHolder?.let { ImpressHolder ->
                tdnBanner?.addOnImpressionListener(ImpressHolder) {
                    TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                        this.javaClass.name,
                        imageData.adViewUrl,
                        "",
                        "",
                        ""
                    )
                    onTdnBannerImpressed.invoke()
                }
            }
        }

        private fun getRequestBuilder(imageUrl: String?, radius: Int): RequestBuilder<Drawable> {
            return if (radius > Int.ZERO) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .transform(FitCenter(), RoundedCorners(radius))
            } else {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .fitCenter()
            }
        }

        private fun getHeight(width: Int, height: Int, calculatedWidth: Int): Int {
            val deviceWidth = calculatedWidth.toFloat()
            val widthRatio = deviceWidth / width.toFloat()
            return (widthRatio * height).toInt()
        }
    }

    private fun widthVerticalCarousel(width: Int): Int {
        return (width / 2.8).toInt()
    }
}
