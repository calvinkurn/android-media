package com.tokopedia.review.feature.bulk_write_review.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.bulk_write_review.di.scope.BulkReviewScope
import com.tokopedia.review.feature.bulk_write_review.presentation.viewmodel.BulkReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BulkReviewViewModelModule {

    @Binds
    @BulkReviewScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BulkReviewViewModel::class)
    internal abstract fun bulkReviewViewModel(viewModel: BulkReviewViewModel): ViewModel
}
