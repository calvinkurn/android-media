package com.tokopedia.browse.homepage.di;

import com.tokopedia.browse.common.di.DigitalBrowseComponent;
import com.tokopedia.browse.homepage.presentation.activity.DigitalBrowseHomeActivity;
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseMarketplaceFragment;
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseServiceFragment;

import dagger.Component;

/**
 * @author by furqan on 30/08/18.
 */

@DigitalBrowseHomeScope
@Component(modules = DigitalBrowseHomeModule.class, dependencies = DigitalBrowseComponent.class)
public interface DigitalBrowseHomeComponent {

    void inject(DigitalBrowseMarketplaceFragment digitalBrowseMarketplaceFragment);

    void inject(DigitalBrowseServiceFragment digitalBrowseServiceFragment);

    void inject(DigitalBrowseHomeActivity digitalBrowseHomeActivity);
}
