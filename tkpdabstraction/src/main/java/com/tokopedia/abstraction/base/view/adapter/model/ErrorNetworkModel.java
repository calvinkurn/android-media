package com.tokopedia.abstraction.base.view.adapter.model;


import com.tokopedia.abstraction.base.view.adapter.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * @author Kulomady on 1/25/17.
 */

public class ErrorNetworkModel implements Visitable<AdapterTypeFactory> {

    @Override
    public int type(AdapterTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}