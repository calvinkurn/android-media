package com.tokopedia.recommendation_widget_common.di.recomwidget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationViewModel
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetViewModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetStateModule
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModel
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by yfsx on 02/08/21.
 */
@Module(
    includes = [
        RecommendationWidgetStateModule::class,
    ]
)
abstract class RecommendationViewModelModule {

    @RecommendationWidgetScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @RecommendationWidgetScope
    @Binds
    @IntoMap
    @ViewModelKey(RecomWidgetViewModel::class)
    abstract fun provideNavigationViewModel(viewModel: RecomWidgetViewModel): ViewModel

    @RecommendationWidgetScope
    @Binds
    @IntoMap
    @ViewModelKey(ViewToViewViewModel::class)
    abstract fun provideViewToViewViewModel(viewViewModel: ViewToViewViewModel): ViewModel

    @RecommendationWidgetScope
    @Binds
    @IntoMap
    @ViewModelKey(RecommendationWidgetViewModel::class)
    abstract fun provideRecommendationWidgetViewModel(
        viewModel: RecommendationWidgetViewModel
    ): ViewModel

    @RecommendationWidgetScope
    @Binds
    @IntoMap
    @ViewModelKey(InfiniteRecommendationViewModel::class)
    abstract fun provideInfiniteRecommendationViewModel(
        viewModel: InfiniteRecommendationViewModel
    ): ViewModel


}
