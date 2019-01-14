package com.tokopedia.digital.categorylist.di;


import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.digital.categorylist.view.fragment.DigitalCategoryListFragment;

import dagger.Component;

@DigitalListScope
@Component(modules = DigitalListModule.class, dependencies = DigitalComponent.class)
public interface DigitalListComponent {
    void inject(DigitalCategoryListFragment fragment);
}
