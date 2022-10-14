package com.tokopedia.search.result.product.video

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.video_widget.carousel.InspirationCarouselVideoViewHolder
import kotlin.reflect.KClass

object VideoAutoplayConstants {

    val videoCarouselSupportedVideoAutoplayType: List<KClass<out RecyclerView.ViewHolder>> = listOf(
        InspirationCarouselVideoViewHolder::class,
    )

    val productCardSupportedVideoAutoplayType: List<KClass<out RecyclerView.ViewHolder>> = listOf(
        ProductItemViewHolder::class,
        SmallGridProductItemViewHolder::class,
        BigGridProductItemViewHolder::class,
        ListProductItemViewHolder::class,
    )

    val allSupportedVideoAutoplayType =
        videoCarouselSupportedVideoAutoplayType + productCardSupportedVideoAutoplayType
}
