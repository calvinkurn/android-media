package com.tokopedia.feedcomponent.util

import android.content.Context
import android.graphics.Rect
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.domain.mapper.*
import com.tokopedia.feedcomponent.util.util.globalVisibleRect
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.util.*


object FeedScrollListenerNew {
    private const val THRESHOLD_VIDEO_HEIGHT_SHOWN = 90
    private const val TOTAL_VIDEO_HEIGHT_PERCENT = 100
    private const val VIDEO_HEIGHT_ZERO_PERCENT = 0
    private const val IMAGE_ITEM_IMPRESSED = "image_item_impressed"
    private const val IMAGE_ASGC_CTA_IMPRESSED = "image_asgc_cta_impressed"
    private const val VOD_ITEM_IMPRESSED = "vod_item_impressed"

    private const val CTA_BUTTON_VISIBLE_PERCENT_THRESHOLD = 50
    private const val TYPE_VIDEO = "video"
    private const val TYPE_LONG_VIDEO = "long-video"
    fun  onFeedScrolled(recyclerView: RecyclerView, list: List<Visitable<*>>) {
        if (canAutoplayVideo(recyclerView)) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val firstPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
            val lastPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
            for (i in firstPosition..lastPosition) {
                val item = getCardViewModel(list, i)
                val card = getFeedXCard(list, i)
                val topadsItem = getTopadsCardViewModel(list,i)
                card?.let {
                    if (isVideoCard(card) && isWifiEnabled(recyclerView.context)) {
                        if (item != null) {
                            getVideoModelScrollListener(layoutManager, recyclerView, i, item)
                        }
                    } else if (isImageCard(card)) {
                        if (item != null) {
                            getImagePostScrollListener(layoutManager, recyclerView, i, item)
                        }
                    } else if (isVODCard(card) && isWifiEnabled(recyclerView.context)) {
                        if (item != null) {
                            getVODModelScrollListener(layoutManager, recyclerView, i, item)
                        }
                    } else if (isLongVideoCard(card) && isWifiEnabled(recyclerView.context)) {
                        if (item != null) {
                            getVODModelScrollListener(layoutManager, recyclerView, i, item)
                        }
                    } else if (isTopadsImageCard(list, i)) {
                        if (topadsItem != null) {
                            getImagePostScrollListener(
                                layoutManager,
                                recyclerView,
                                i,
                                topadsItem
                            )
                        }
                    }
                }
                topadsItem?.let {
                    getImagePostScrollListener(
                        layoutManager,
                        recyclerView,
                        i,
                        topadsItem
                    )
                }
            }
        }
    }
    fun  onCDPScrolled(recyclerView: RecyclerView, list: List<FeedXCard>) {
        if (canAutoplayVideo(recyclerView)) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val firstPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
            val lastPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
            for (i in firstPosition..lastPosition) {
                val card = getCDPCardViewModel(list, i)
                val media = getFeedXCardMedia(list,i)

                if (card != null && media != null) {
                    if (isVideoCard(card) && isWifiEnabled(recyclerView.context)) {
                            getVideoModelScrollListener(layoutManager, recyclerView, i, media, true)
                    } else if (isImageCard(card)) {
                            getImagePostScrollListener(layoutManager, recyclerView, i, media, true)
                    } else if (isVODCard(card) && isWifiEnabled(recyclerView.context)) {
                            getVODModelScrollListener(layoutManager, recyclerView, i, media, true)
                    } else if (isLongVideoCard(card) && isWifiEnabled(recyclerView.context)) {
                            getVODModelScrollListener(layoutManager, recyclerView, i, media, true)
                    }
                }

            }
        }
    }


    @Suppress("MagicNumber")
    private fun getImagePostScrollListener(
        layoutManager: LinearLayoutManager?,
        recyclerView: RecyclerView,
        i: Int,
        item: FeedXMedia,
        isCDPScroll: Boolean = false
    ) {
        val rvRect = recyclerView.globalVisibleRect
        val currentView = layoutManager?.findViewByPosition(i) ?: return
        val rowRect = currentView.globalVisibleRect
        val imageView = currentView.findViewById<View>(R.id.post_image)
        val videoViewRect = imageView?.globalVisibleRect ?: Rect()
        if (imageView != null) {
            var percentVideo: Int = -1
            val visibleVideo: Int = if (rowRect.bottom >= rvRect.bottom) {
                rvRect.bottom - videoViewRect.top
            } else {
                videoViewRect.bottom - rvRect.top
            }
            percentVideo = try {
                visibleVideo * TOTAL_VIDEO_HEIGHT_PERCENT / imageView.height
            } catch (e: Exception) {
                VIDEO_HEIGHT_ZERO_PERCENT
            }
            val isStateChanged: Boolean = percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN
            if (isStateChanged && item.isImageImpressedFirst) {
                    item.isImageImpressedFirst = false
                if (isCDPScroll) {
                    val impressPayload = Bundle().apply {
                        putBoolean(IMAGE_ITEM_IMPRESSED, true)
                    }
                    Objects.requireNonNull(recyclerView.adapter)
                        .notifyItemChanged(i, impressPayload)
                }
                else
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, DynamicPostNewViewHolder.PAYLOAD_POST_VISIBLE)
            }
            if(percentVideo <= 0)
                item.isImageImpressedFirst = true
        }

        val ctaView = currentView.findViewById<View>(R.id.top_ads_detail_card)
        if (ctaView == null || !ctaView.isVisible) return
        val ctaRect = ctaView.globalVisibleRect
        val ctaVisiblePercent = try {
            (ctaRect.bottom - ctaRect.top) / ctaView.height.toFloat()
        } catch (e: Exception) {
            0f
        }
        if (ctaRect.top >= rvRect.top &&
            ctaRect.bottom <= rvRect.bottom &&
                ctaVisiblePercent > CTA_BUTTON_VISIBLE_PERCENT_THRESHOLD / 100f) {
            if (isCDPScroll) {
                val impressCTAPayload = Bundle().apply {
                    putBoolean(IMAGE_ASGC_CTA_IMPRESSED, true)
                }
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, impressCTAPayload)
            } else {
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, DynamicPostNewViewHolder.PAYLOAD_CTA_VISIBLE)
            }
        }
    }

    private fun getVideoModelScrollListener(
        layoutManager: LinearLayoutManager?,
        recyclerView: RecyclerView,
        i: Int,
        item: FeedXMedia,
        isCDPScroll: Boolean = false
    ) {
        val rvRect = Rect()
        recyclerView.getGlobalVisibleRect(rvRect)
        val rowRect = Rect()
        layoutManager?.findViewByPosition(i)?.getGlobalVisibleRect(rowRect)
        val videoViewRect = Rect()
        layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.videoPreviewImage)
            ?.getGlobalVisibleRect(videoViewRect)
        val imageView =
            layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.videoPreviewImage)
        if (imageView != null) {
            var percentVideo: Int
            val visibleVideo: Int = if (rowRect.bottom >= rvRect.bottom) {
                rvRect.bottom - videoViewRect.top
            } else {
                videoViewRect.bottom - rvRect.top
            }
            percentVideo = try {
                visibleVideo * TOTAL_VIDEO_HEIGHT_PERCENT / imageView.height
            } catch (e: Exception) {
                VIDEO_HEIGHT_ZERO_PERCENT
            }


            var isStateChanged = false
            if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                if (!item.canPlay) isStateChanged = true
                item.canPlay = true
            } else {
                if(percentVideo <= 0)
                    item.isImageImpressedFirst = true
                item.canPlay = false
            }

            if (isStateChanged && item.isImageImpressedFirst) {
                item.isImageImpressedFirst = false
                if (isCDPScroll) {
                    val impressPayload = Bundle().apply {
                        putBoolean(IMAGE_ITEM_IMPRESSED, true)
                    }
                    Objects.requireNonNull(recyclerView.adapter)
                        .notifyItemChanged(i, impressPayload)
                }
                else
                Objects.requireNonNull(recyclerView.adapter)
                    .notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VIDEO)
            }
        }
    }

    private fun getVODModelScrollListener(
            layoutManager: LinearLayoutManager?,
            recyclerView: RecyclerView,
            i: Int,
            item: FeedXMedia,
            isCDPScroll: Boolean = false
    ) {
        val rvRect = Rect()
        recyclerView.getGlobalVisibleRect(rvRect)
        val rowRect = Rect()
        layoutManager?.findViewByPosition(i)?.getGlobalVisibleRect(rowRect)
        val videoViewRect = Rect()
        layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.vod_videoPreviewImage)
                ?.getGlobalVisibleRect(videoViewRect)
        val imageView =
                layoutManager?.findViewByPosition(i)?.findViewById<View>(R.id.vod_videoPreviewImage)
        if (imageView != null) {
            val percentVideo: Int
            val visibleVideo: Int = if (rowRect.bottom >= rvRect.bottom) {
                rvRect.bottom - videoViewRect.top
            } else {
                videoViewRect.bottom - rvRect.top
            }

            percentVideo = try {
                visibleVideo * TOTAL_VIDEO_HEIGHT_PERCENT / imageView.height
            } catch (e: Exception) {
                VIDEO_HEIGHT_ZERO_PERCENT
            }

            var isStateChanged = false
            if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                if (!item.canPlay) isStateChanged = true
                item.canPlay = true
            } else {
                if(percentVideo <= 0)
                    item.isImageImpressedFirst = true
                item.canPlay = false
            }

            if (isStateChanged && item.isImageImpressedFirst) {
                item.isImageImpressedFirst = false
                if (isCDPScroll) {
                    val impressPayload = Bundle().apply {
                        putBoolean(VOD_ITEM_IMPRESSED, true)
                    }
                    Objects.requireNonNull(recyclerView.adapter)
                        .notifyItemChanged(i, impressPayload)
                }
                else
                Objects.requireNonNull(recyclerView.adapter)
                        .notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VOD)
            }
        }
    }

    private fun getFeedXCard(list: List<Visitable<*>>, position: Int): FeedXCard? {
        return if (list.size > position && list[position] is DynamicPostUiModel)
            (list[position] as DynamicPostUiModel).feedXCard
        else
            null
    }
    private fun getFeedXCardMedia(list: List<FeedXCard>, position: Int): FeedXMedia? {
        return if (list.size > position) {
            val card = (list[position])
            card.media[card.lastCarouselIndex]
        } else
            null
    }

    private fun isVideoCard(card: FeedXCard): Boolean {
        return (card.typename == TYPE_FEED_X_CARD_POST
                && card.media.isNotEmpty() && (card.media.find {
            it.type == TYPE_VIDEO
        } != null))
    }

    private fun isVODCard(card: FeedXCard): Boolean {
        return (card.typename == TYPE_FEED_X_CARD_PLAY
                && card.media.isNotEmpty())
    }

    private fun isLongVideoCard(card: FeedXCard): Boolean {
        return (card.typename == TYPE_FEED_X_CARD_POST
                && card.media.isNotEmpty() && (card.media.find {
            it.type == TYPE_LONG_VIDEO
        } != null))
    }

    private fun isImageCard(card: FeedXCard): Boolean {
        return (card.typename == TYPE_FEED_X_CARD_POST || card.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT)
                && card.media.isNotEmpty() && (card.media.find {
            it.type == TYPE_IMAGE
        } != null)
    }

    private fun getCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            val feedXCard = (list[position] as DynamicPostUiModel).feedXCard
            return feedXCard.media[feedXCard.lastCarouselIndex]
        } catch (e: Exception) {
            e.localizedMessage
        }
        return null
    }
    private fun getCDPCardViewModel(list: List<FeedXCard>, position: Int): FeedXCard? {
        try {
            return (list[position])
        } catch (e: Exception) {
            e.localizedMessage
        }
        return null
    }

    private fun isTopadsImageCard(list: List<Visitable<*>>, position: Int): Boolean {
        return (list.size > position && list[position] is TopadsHeadLineV2Model && (list[position] as TopadsHeadLineV2Model).feedXCard.typename == TYPE_TOPADS_HEADLINE_NEW
                && (list[position] as TopadsHeadLineV2Model).feedXCard.media.isNotEmpty() && ((list[position] as TopadsHeadLineV2Model).feedXCard.media.find {
            it.type == TYPE_IMAGE
        } != null))
    }

    private fun getTopadsCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            val feedXCard = (list[position] as TopadsHeadLineV2Model).feedXCard
            return feedXCard.media[feedXCard.lastCarouselIndex]
        } catch (e: Exception) {
            e.localizedMessage
        }
        return null
    }

    private fun canAutoplayVideo(recyclerView: RecyclerView): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(recyclerView.context)
        return config.getBoolean(RemoteConfigKey.CONFIG_AUTOPLAY_VIDEO_WIFI, false)
    }
    private fun isWifiEnabled(context: Context) : Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }
}
