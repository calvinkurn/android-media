package com.tokopedia.review.stub.reviewlist.di.component

import com.tokopedia.review.feature.reviewlist.di.component.ReviewProductListComponent
import com.tokopedia.review.feature.reviewlist.di.module.ReviewProductListModule
import com.tokopedia.review.feature.reviewlist.di.module.ReviewProductListViewModelModule
import com.tokopedia.review.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.review.stub.reviewcommon.di.component.ReviewComponentStub
import dagger.Component

@ReviewProductListScope
@Component(modules = [ReviewProductListModule::class, ReviewProductListViewModelModule::class], dependencies = [ReviewComponentStub::class])
interface ReviewProductListComponentStub: ReviewProductListComponent