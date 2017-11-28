package com.tokopedia.tkpd.home.favorite.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.home.favorite.di.modul.FavoriteModule;
import com.tokopedia.tkpd.home.favorite.di.scope.FavoriteScope;
import com.tokopedia.tkpd.home.favorite.view.FragmentFavorite;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;

import dagger.Component;

/**
 * @author Kulomady on 1/20/17.
 */

@FavoriteScope
@Component(modules = FavoriteModule.class, dependencies = TopAdsComponent.class)
public interface FavoriteComponent {
    void inject(FragmentFavorite fragmentFavorite);
}
