package com.tokopedia.recommendation_widget_common.di.recomwidget

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
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
    fun inject(application: BaseMainApplication)

    fun getViewModelFactory(): ViewModelProvider.Factory

}