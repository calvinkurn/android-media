package com.tokopedia.browse.homepage.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapterTypeFactory;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseServiceViewModel implements Visitable<DigitalBrowseServiceAdapterTypeFactory> {
    @Override
    public int type(DigitalBrowseServiceAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
