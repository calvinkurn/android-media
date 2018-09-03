package com.tokopedia.browse.homepage.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory;

/**
 * @author by furqan on 03/09/18.
 */

public class DigitalBrowseMarketplaceViewModel implements Visitable<DigitalBrowseMarketplaceAdapterTypeFactory> {
    @Override
    public int type(DigitalBrowseMarketplaceAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
