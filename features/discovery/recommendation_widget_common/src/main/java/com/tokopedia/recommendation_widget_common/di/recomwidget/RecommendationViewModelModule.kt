package com.tokopedia.recommendation_widget_common.di.recomwidget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.recommendation_widget_common.presenter.RecommendationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by yfsx on 02/08/21.
 */
@Module
abstract class RecommendationViewModelModule {

    @RecommendationWidgetScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @RecommendationWidgetScope
    @Binds
    @IntoMap
    @ViewModelKey(RecommendationViewModel::class)
    abstract fun provideNavigationViewModel(viewModel: RecommendationViewModel): ViewModel
}