package com.tokopedia.product.detail.tracking

import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import com.tokopedia.stories.widget.tracking.DefaultTrackerBuilder
import com.tokopedia.stories.widget.tracking.StoriesWidgetTracker
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 03/10/23
 */
class ProductDetailStoriesWidgetTrackerBuilder private constructor(
    private val productId: String,
    private val defaultTrackerBuilder: DefaultTrackerBuilder
) : StoriesWidgetTracker.Builder by defaultTrackerBuilder {

    override fun onImpressedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data {
        val data = defaultTrackerBuilder.onImpressedEntryPoint(state)
        if (data == StoriesWidgetTracker.Data.Empty) return data

        return data.copy(
            bundle = data.bundle.apply {
                putString(PRODUCT_ID, productId)
            }
        )
    }

    override fun onClickedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data {
        val data = defaultTrackerBuilder.onClickedEntryPoint(state)
        if (data == StoriesWidgetTracker.Data.Empty) return data

        return data.copy(
            bundle = data.bundle.apply {
                putString(PRODUCT_ID, productId)
            }
        )
    }

    companion object {

        private const val PRODUCT_ID = "productId"
        fun create(
            productId: String,
            userSession: UserSessionInterface
        ): ProductDetailStoriesWidgetTrackerBuilder {
            return ProductDetailStoriesWidgetTrackerBuilder(
                productId,
                DefaultTrackerBuilder(
                    StoriesEntryPoint.ProductDetail,
                    userSession
                )
            )
        }
    }
}
