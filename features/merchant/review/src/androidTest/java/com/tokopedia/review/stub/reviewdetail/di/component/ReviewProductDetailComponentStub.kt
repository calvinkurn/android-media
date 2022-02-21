package com.tokopedia.review.stub.reviewdetail.di.component

import com.tokopedia.review.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.review.feature.reviewdetail.di.scope.ReviewDetailScope
import com.tokopedia.review.stub.reviewcommon.di.component.ReviewComponentStub
import dagger.Component

@ReviewDetailScope
@Component(
    modules = [ReviewProductDetailModule::class],
    dependencies = [ReviewComponentStub::class]
)
interface ReviewProductDetailComponentStub : ReviewProductDetailComponent