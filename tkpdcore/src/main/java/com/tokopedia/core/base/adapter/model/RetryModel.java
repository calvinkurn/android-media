package com.tokopedia.core.base.adapter.model;

import com.tokopedia.core.base.adapter.AdapterTypeFactory;
import com.tokopedia.core.base.adapter.Visitable;

/**
 * Created by stevenfredian on 5/31/17.
 */

/**
 * Use visitable model from tkpd abstraction
 */
@Deprecated
public class RetryModel implements Visitable<AdapterTypeFactory> {

    @Override
    public int type(AdapterTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

}
