package com.tokopedia.seller.active.common.di

import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@UpdateShopActiveScope
@Component(modules = [UpdateShopActiveModule::class])
interface UpdateShopActiveComponent {
    fun inject(service: UpdateShopActiveService)
    fun getGraphqlRepository(): GraphqlRepository
}