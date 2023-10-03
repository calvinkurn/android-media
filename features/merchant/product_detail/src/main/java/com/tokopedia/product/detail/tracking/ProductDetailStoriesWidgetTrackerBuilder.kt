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
    private val defaultTrackerBuilder: DefaultTrackerBuilder,
) : StoriesWidgetTracker.Builder by defaultTrackerBuilder {

    override fun onImpressedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data {
        val data = defaultTrackerBuilder.onImpressedEntryPoint(state)
        return data.copy(
            map = data.map + ("productId" to productId)
        )
    }

    override fun onClickedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data {
        val data = defaultTrackerBuilder.onClickedEntryPoint(state)
        return data.copy(
            map = data.map + ("productId" to productId)
        )
    }

    companion object {
        fun create(
            productId: String,
            userSession: UserSessionInterface,
        ): ProductDetailStoriesWidgetTrackerBuilder {
            return ProductDetailStoriesWidgetTrackerBuilder(
                productId,
                DefaultTrackerBuilder(
                    StoriesEntryPoint.ProductDetail,
                    userSession,
                )
            )
        }
    }
}
