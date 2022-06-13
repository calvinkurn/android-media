package com.tokopedia.review.stub.reviewreply.di.component

import com.tokopedia.review.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.review.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.review.feature.reviewreply.di.scope.ReviewReplyScope
import com.tokopedia.review.stub.reviewcommon.di.component.ReviewComponentStub
import dagger.Component

@ReviewReplyScope
@Component(modules = [ReviewReplyModule::class], dependencies = [ReviewComponentStub::class])
interface ReviewReplyComponentStub: ReviewReplyComponent