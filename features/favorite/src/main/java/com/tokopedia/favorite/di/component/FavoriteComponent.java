package com.tokopedia.home.account.favorite.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.home.account.favorite.view.FragmentFavorite;
import com.tokopedia.home.account.favorite.di.modul.FavoriteModule;
import com.tokopedia.home.account.favorite.di.scope.FavoriteScope;

import dagger.Component;

/**
 * @author Kulomady on 1/20/17.
 */

@FavoriteScope
@Component(modules = FavoriteModule.class, dependencies = AppComponent.class)
public interface FavoriteComponent {
    void inject(FragmentFavorite fragmentFavorite);
}
