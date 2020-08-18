package com.tokopedia.favorite.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.favorite.di.modul.FavoriteModule
import com.tokopedia.favorite.di.modul.UserSessionModule
import com.tokopedia.favorite.di.scope.FavoriteScope
import com.tokopedia.favorite.view.FragmentFavorite
import com.tokopedia.favorite.view.FragmentFavoriteUsingViewModel
import dagger.Component

@FavoriteScope
@Component(
        modules = [FavoriteModule::class, UserSessionModule::class],
        dependencies = [BaseAppComponent::class]
)
interface FavoriteComponent {
    fun inject(fragmentFavorite: FragmentFavorite)
    fun inject(fragment: FragmentFavoriteUsingViewModel)
}
