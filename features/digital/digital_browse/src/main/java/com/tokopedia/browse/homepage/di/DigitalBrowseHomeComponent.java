package com.tokopedia.browse.homepage.di;

import com.tokopedia.browse.common.di.DigitalBrowseComponent;
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseBelanjaFragment;
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseLayananFragment;

import dagger.Component;

/**
 * @author by furqan on 30/08/18.
 */

@DigitalBrowseHomeScope
@Component(modules = DigitalBrowseHomeModule.class, dependencies = DigitalBrowseComponent.class)
public interface DigitalBrowseHomeComponent {

    void inject(DigitalBrowseBelanjaFragment digitalBrowseBelanjaFragment);

    void inject(DigitalBrowseLayananFragment digitalBrowseLayananFragment);

}
