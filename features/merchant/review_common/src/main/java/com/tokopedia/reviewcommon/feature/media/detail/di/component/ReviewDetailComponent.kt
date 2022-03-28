package com.tokopedia.reviewcommon.feature.media.detail.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.reviewcommon.feature.media.detail.di.module.ReviewDetailModule
import com.tokopedia.reviewcommon.feature.media.detail.di.module.ReviewDetailViewModelModule
import com.tokopedia.reviewcommon.feature.media.detail.di.scope.ReviewDetailScope
import com.tokopedia.reviewcommon.feature.media.detail.presentation.bottomsheet.ExpandedReviewDetailBottomSheet
import com.tokopedia.reviewcommon.feature.media.detail.presentation.fragment.ReviewDetailFragment
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent
import dagger.Component

@Component(
    modules = [
        ReviewDetailModule::class,
        ReviewDetailViewModelModule::class
    ],
    dependencies = [
        BaseAppComponent::class,
        DetailedReviewMediaGalleryComponent::class
    ]
)
@ReviewDetailScope
interface ReviewDetailComponent {
    fun inject(fragment: ReviewDetailFragment)
    fun inject(fragment: ExpandedReviewDetailBottomSheet)
}