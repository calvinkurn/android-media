package com.tokopedia.attachproduct.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.attachproduct.view.fragment.AttachProductFragment;

import dagger.Component;

/**
 * Created by Hendri on 06/03/18.
 */
@AttachProductScope
@Component(modules = {AttachProductModule.class} , dependencies = {BaseAppComponent.class})
public interface AttachProductComponent {
    void inject(AttachProductFragment attachProductFragment);
}
