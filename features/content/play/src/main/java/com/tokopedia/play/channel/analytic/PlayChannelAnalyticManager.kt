package com.tokopedia.play.channel.analytic

import androidx.lifecycle.Lifecycle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.ProductAnalyticHelper
import com.tokopedia.play.channel.ui.component.ProductCarouselUiComponent
import com.tokopedia.play_common.eventbus.EventBus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
class PlayChannelAnalyticManager @AssistedInject constructor(
    private val analytic: PlayAnalytic,
    private val dispatchers: CoroutineDispatchers,
    @Assisted private val productAnalyticHelper: ProductAnalyticHelper,
) {

    @AssistedFactory
    interface Factory {
        fun create(
            productAnalyticHelper: ProductAnalyticHelper,
        ) : PlayChannelAnalyticManager
    }

    fun observe(
        scope: CoroutineScope,
        event: EventBus<Any>,
        lifecycle: Lifecycle,
    ) {
        scope.launch(dispatchers.computation) {
            event.subscribe().collect {
                when (it) {
                    is ProductCarouselUiComponent.Event.OnClicked -> {
                        analytic.clickFeaturedProduct(it.product, it.position)
                    }
                    is ProductCarouselUiComponent.Event.OnImpressed -> {
                        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) ||
                                it.products.isEmpty()) return@collect

                        productAnalyticHelper.trackImpressedProducts(it.products)
                    }
                }
            }
        }
    }

    fun sendPendingTrackers() {
        productAnalyticHelper.sendImpressedFeaturedProducts()
    }
}
