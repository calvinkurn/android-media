package com.tokopedia.tkpd.home.favorite.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.home.favorite.di.modul.FavoriteModule;
import com.tokopedia.tkpd.home.favorite.di.scope.FavoriteScope;
import com.tokopedia.tkpd.home.favorite.view.FragmentFavorite;

import dagger.Component;

/**
 * @author Kulomady on 1/20/17.
 */

@FavoriteScope
@Component(modules = FavoriteModule.class, dependencies = AppComponent.class)
public interface FavoriteComponent {
    void inject(FragmentFavorite fragmentFavorite);
}
