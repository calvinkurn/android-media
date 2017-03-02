package com.tokopedia.core.base.adapter.model;


import com.tokopedia.core.base.adapter.AdapterTypeFactory;
import com.tokopedia.core.base.adapter.Visitable;

/**
 * @author Kulomady on 1/25/17.
 */

public class EmptyModel implements Visitable<AdapterTypeFactory> {

    @Override
    public int type(AdapterTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}
