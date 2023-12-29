package com.tokopedia.tokofood.stub.purchase.promo.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.purchase.promopage.di.TokoFoodPromoComponent
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.tokofood.stub.purchase.promo.presentation.fragment.TokoFoodPromoFragmentStub
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TokoFoodPromoModuleStub::class,
        TokoFoodPromoViewModelModuleStub::class
    ],
    dependencies = [
        BaseAppComponentStub::class
    ]
)
interface TokoFoodPromoComponentStub: TokoFoodPromoComponent {

    fun inject(tokoFoodPromoFragmentStub: TokoFoodPromoFragmentStub)

    fun graphqlRepository(): GraphqlRepository

    fun promoListTokoFoodUseCase(): PromoListTokoFoodUseCase

}
