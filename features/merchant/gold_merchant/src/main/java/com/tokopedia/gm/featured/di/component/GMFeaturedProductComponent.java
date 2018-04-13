package com.tokopedia.gm.featured.di.component;

/**
 * Created by normansyahputa on 9/7/17.
 */

import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.featured.di.module.GMFeaturedProductModule;
import com.tokopedia.gm.featured.di.scope.GMFeaturedProductScope;
import com.tokopedia.gm.featured.view.activity.GMFeaturedProductActivity;
import com.tokopedia.gm.featured.view.fragment.GMFeaturedProductFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 7/6/17.
 */
@GMFeaturedProductScope
@Component(modules = GMFeaturedProductModule.class, dependencies = GMComponent.class)
public interface GMFeaturedProductComponent {
    void inject(GMFeaturedProductActivity gmFeaturedProductActivity);

    void inject(GMFeaturedProductFragment gmFeaturedProductFragment);
}
