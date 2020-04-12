package com.tokopedia.product.addedit.imagepicker.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerEditPhotoActivity;
import com.tokopedia.product.addedit.imagepicker.view.fragment.ImagePickerCatalogFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

@CatalogImageScope
@Component(modules = ImagePickerCatalogModule.class, dependencies = BaseAppComponent.class)
public interface ImagePickerCatalogComponent {
    void inject(ImagePickerCatalogFragment imagePickerCatalogFragment);
}
