package com.tokopedia.inbox.universalinbox.stub.di.recommendationwidget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationWidgetScope
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UniversalInboxRecommendationWidgetViewModelModuleStub {

    @Binds
    @RecommendationWidgetScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @RecommendationWidgetScope
    @IntoMap
    @ViewModelKey(RecommendationWidgetViewModel::class)
    abstract fun provideRecommendationWidgetViewModel(
        viewModel: RecommendationWidgetViewModel
    ): ViewModel
}
