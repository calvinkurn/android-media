package com.tokopedia.tokofood.stub.purchase.di.component

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailByIdUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailUseCase
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointWithAddressIdUseCase
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.TokoFoodPurchaseComponent
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.TokoFoodPurchaseScope
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.tokofood.stub.purchase.di.module.TokoFoodPurchaseModuleStub
import com.tokopedia.tokofood.stub.purchase.di.module.TokoFoodPurchaseViewModelModuleStub
import com.tokopedia.tokofood.stub.purchase.presentation.fragment.TokoFoodPurchaseFragmentStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@TokoFoodPurchaseScope
@Component(
    modules = [
        TokoFoodPurchaseModuleStub::class,
        TokoFoodPurchaseViewModelModuleStub::class
    ],
    dependencies = [
        BaseAppComponentStub::class
    ]
)
interface TokoFoodPurchaseComponentStub : TokoFoodPurchaseComponent {

    fun inject(fragment: TokoFoodPurchaseFragmentStub)

    fun coroutineDispatcher(): CoroutineDispatchers

    fun graphqlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun keroEditAddressUseCase(): UpdatePinpointWithAddressIdUseCase

    fun keroGetAddressUseCase(): GetAddressDetailByIdUseCase

    fun updatePinpointUseCase(): UpdatePinpointUseCase

    fun getAddressDetailUseCase(): GetAddressDetailUseCase

    fun checkoutTokofoodUseCase(): CheckoutTokoFoodUseCase

    fun checkoutGeneralTokoFoodUseCase(): CheckoutGeneralTokoFoodUseCase

    fun promoListTokoFoodUseCase(): PromoListTokoFoodUseCase
}
