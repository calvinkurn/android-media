package com.tokopedia.review.feature.reading.di


import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewFilterBottomSheet
import dagger.Component

@Component(modules = [ReadReviewViewModelModule::class, ReadReviewModule::class], dependencies = [ReviewComponent::class])
@ReadReviewScope
interface ReadReviewComponent {
    fun inject(readReadReviewFragment: ReadReviewFragment)
    fun inject(readReviewFilterBottomSheet: ReadReviewFilterBottomSheet)
}