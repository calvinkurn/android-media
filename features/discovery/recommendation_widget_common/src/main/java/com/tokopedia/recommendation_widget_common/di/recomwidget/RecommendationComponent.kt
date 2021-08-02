package com.tokopedia.recommendation_widget_common.di.recomwidget

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView
import dagger.Component

/**
 * Created by yfsx on 02/08/21.
 */

@RecommendationWidgetScope
@Component(modules = [
    RecommendationWidgetModule::class,
    RecommendationViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface RecommendationComponent {
    fun inject(view: RecommendationCarouselWidgetView)

}