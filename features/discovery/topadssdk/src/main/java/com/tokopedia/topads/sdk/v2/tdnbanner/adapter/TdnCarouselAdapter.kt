package com.tokopedia.topads.sdk.v2.tdnbanner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.TdnBannerConstants.TYPE_VERTICAL_CAROUSEL
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import timber.log.Timber

class TdnCarouselAdapter(
    private val onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
    private val cornerRadius: Int,
    private val isUsingInfiniteScroll: Boolean,
    private val onLoadFailed: () -> Unit,
    private val onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
) : RecyclerView.Adapter<TdnCarouselAdapter.TdnCarouselViewHolder>() {

    private val shopAdsProductItemList = arrayListOf<TopAdsImageUiModel>()
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
        holder.bind(shopAdsProductItemList[position])
    }

    override fun getItemCount(): Int {
        return shopAdsProductItemList.size
    }

    fun setList(list: List<TopAdsImageUiModel>) {
        val itemCount = itemCount
        shopAdsProductItemList.clear()
        notifyItemRangeRemoved(0, itemCount)

        shopAdsProductItemList.addAll(list)
        notifyItemRangeInserted(0, shopAdsProductItemList.size)
    }

    inner class TdnCarouselViewHolder(
        itemView: View,
        private val cornerRadius: Int,
        private val onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
        private val onLoadFailed: () -> Unit,
        private val onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tdnBanner = itemView.findViewById<ImageView>(R.id.tdnBanner)
        private var tdnShimmer = itemView.findViewById<ImageView>(R.id.tdnShimmer)

        fun bind(shopProductModelItem: TopAdsImageUiModel?) {
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
            imageData: TopAdsImageUiModel,
            cornerRadius: Int = Int.ZERO,
            onLoadFailed: () -> Unit = {},
            onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
            onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
        ) {
            if (!imageData.imageUrl.isNullOrEmpty()) {
                val width = itemView.context.resources.displayMetrics.widthPixels
                val calculatedWidth = if (imageData.layoutType == TYPE_VERTICAL_CAROUSEL) {
                    widthVerticalCarousel(width)
                } else {
                    widthHorizontalCarousel(width)
                }

                tdnBanner.loadImage(imageData.imageUrl, properties = {
                    if (cornerRadius > Int.ZERO) {
                        transforms(listOf(FitCenter(), RoundedCorners(cornerRadius)))
                    } else {
                        fitCenter()
                    }

                    overrideSize(
                        Resize(
                            calculatedWidth,
                            getHeight(imageData.imageWidth, imageData.imageHeight, calculatedWidth)
                        )
                    )

                    listener(onSuccess = { _, _ ->
                        recordImpression(imageData, onTdnBannerImpressed)
                        if (!isUsingInfiniteScroll) recyclerView?.smoothScrollBy(Int.ONE, Int.ONE)
                        Timber.d("TDN Banner is loaded successfully")
                        tdnShimmer.hide()
                        recordClick(imageData, onTdnBannerClicked)
                    }, onError = {
                            Timber.d("Error in loading TDN Banner")
                            onLoadFailed.invoke()
                        })
                })
            } else {
                tdnBanner.hide()
            }
        }

        private fun widthHorizontalCarousel(width: Int): Int {
            return width - (width * 10 / 100)
        }

        private fun recordClick(
            imageData: TopAdsImageUiModel,
            onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit
        ) {
            itemView.setOnClickListener {
                onTdnBannerClicked(imageData)
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
            imageData: TopAdsImageUiModel,
            onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
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
                    onTdnBannerImpressed.invoke(imageData)
                }
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

    fun Context?.isAvailable(): Boolean {
        if (this == null) {
            return false
        } else if (this is FragmentActivity) {
            return !this.isDestroyed
        }
        return true
    }
}
