package com.tokopedia.imagepicker.picker.instagram.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.imagepicker.picker.instagram.view.fragment.ImagePickerInstagramFragment;
import com.tokopedia.imagepicker.picker.instagram.view.fragment.InstagramLoginFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 5/4/18.
 */
@InstagramScope
@Component(modules = InstagramModule.class, dependencies = BaseAppComponent.class)
public interface InstagramComponent {
    void inject(ImagePickerInstagramFragment imagePickerInstagramFragment);
    void inject(InstagramLoginFragment instagramLoginFragment);
}
