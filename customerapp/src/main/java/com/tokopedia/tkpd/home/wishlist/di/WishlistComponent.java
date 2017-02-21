package com.tokopedia.tkpd.home.wishlist.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.home.fragment.WishListFragment;

import dagger.Component;

/**
 * @author kulomady on 2/21/17.
 */

@WishlistScope
@Component(modules = WishlistModule.class, dependencies = AppComponent.class)
public interface WishlistComponent {
    void inject(WishListFragment wishListFragment);
}
