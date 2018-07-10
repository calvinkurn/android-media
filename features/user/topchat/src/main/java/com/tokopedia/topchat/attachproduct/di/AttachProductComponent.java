package com.tokopedia.topchat.attachproduct.di;

import com.tokopedia.topchat.attachproduct.view.fragment.AttachProductFragment;

import dagger.Component;

/**
 * Created by Hendri on 06/03/18.
 */
@AttachProductScope
@Component(modules = {AttachProductModule.class})
public interface AttachProductComponent {
    void inject(AttachProductFragment attachProductFragment);
}
