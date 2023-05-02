package com.tokopedia.review.stub.bulk_write_review.di.component

import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.review.feature.bulk_write_review.di.component.BulkReviewComponent
import com.tokopedia.review.feature.bulk_write_review.di.module.BulkReviewModule
import com.tokopedia.review.feature.bulk_write_review.di.module.BulkReviewViewModelModule
import com.tokopedia.review.feature.bulk_write_review.di.scope.BulkReviewScope
import com.tokopedia.review.stub.reviewcommon.di.component.ReviewComponentStub
import dagger.Component

@BulkReviewScope
@Component(
    dependencies = [ReviewComponentStub::class],
    modules = [BulkReviewModule::class, BulkReviewViewModelModule::class, MediaUploaderModule::class]
)
interface BulkReviewComponentStub : BulkReviewComponent
