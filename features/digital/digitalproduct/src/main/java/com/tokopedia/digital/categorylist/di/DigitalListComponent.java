package com.tokopedia.digital.categorylist.di;


import com.tokopedia.digital.categorylist.view.fragment.DigitalCategoryListFragment;
import com.tokopedia.digital.common.di.DigitalComponent;

import dagger.Component;

@DigitalListScope
@Component(modules = DigitalListModule.class, dependencies = DigitalComponent.class)
public interface DigitalListComponent {
    void inject(DigitalCategoryListFragment fragment);
}
