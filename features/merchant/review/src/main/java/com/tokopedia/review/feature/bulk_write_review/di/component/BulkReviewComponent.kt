package com.tokopedia.review.feature.bulk_write_review.di.component

import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.bulk_write_review.di.module.BulkReviewModule
import com.tokopedia.review.feature.bulk_write_review.di.module.BulkReviewViewModelModule
import com.tokopedia.review.feature.bulk_write_review.di.scope.BulkReviewScope
import com.tokopedia.review.feature.bulk_write_review.presentation.fragment.BulkReviewFragment
import dagger.Component

@Component(
    dependencies = [ReviewComponent::class],
    modules = [BulkReviewModule::class, BulkReviewViewModelModule::class, MediaUploaderModule::class]
)
@BulkReviewScope
interface BulkReviewComponent {
    fun inject(bulkReviewFragment: BulkReviewFragment)
}
