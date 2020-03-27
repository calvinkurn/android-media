package com.tokopedia.favorite.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.favorite.view.FragmentFavorite;
import com.tokopedia.favorite.di.modul.FavoriteModule;
import com.tokopedia.favorite.di.scope.FavoriteScope;

import dagger.Component;

/**
 * @author Kulomady on 1/20/17.
 */

@FavoriteScope
@Component(modules = FavoriteModule.class, dependencies = BaseAppComponent.class)
public interface FavoriteComponent {
    void inject(FragmentFavorite fragmentFavorite);
}
