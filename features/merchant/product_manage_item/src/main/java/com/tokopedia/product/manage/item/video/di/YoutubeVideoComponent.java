package com.tokopedia.product.manage.item.video.di;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.video.view.activity.ProductAddVideoActivity;
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment;

import dagger.Component;

/**
 * @author normansyahputa on 4/11/17.
 */
@ActivityScope
@Component(modules = YoutubeVideoModule.class, dependencies = ProductComponent.class)
public interface YoutubeVideoComponent {

    void inject(ProductAddVideoActivity youtubeAddVideoActivity);

    void inject(ProductAddVideoFragment youtubeAddVideoFragment);
}
