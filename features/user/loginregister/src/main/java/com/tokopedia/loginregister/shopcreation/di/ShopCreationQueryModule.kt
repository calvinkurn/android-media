package com.tokopedia.loginregister.shopcreation.di

import com.tokopedia.loginregister.shopcreation.domain.query.MutationRegisterCheck
import com.tokopedia.loginregister.shopcreation.domain.query.QueryShopInfo
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-27.
 * ade.hadian@tokopedia.com
 */

@Module
class ShopCreationQueryModule {

    @ShopCreationScope
    @Provides
    @Named(ShopCreationQueryConstant.MUTATION_REGISTER_CHECK)
    fun provideQueryMutationRegisterCheck(): String = MutationRegisterCheck.getQuery()

    @ShopCreationScope
    @Provides
    @Named(ShopCreationQueryConstant.QUERY_SHOP_INFO)
    fun provideQueryShopInfo(): String = QueryShopInfo.getQuery()
}