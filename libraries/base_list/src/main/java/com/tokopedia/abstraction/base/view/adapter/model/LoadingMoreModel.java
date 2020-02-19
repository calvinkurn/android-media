package com.tokopedia.abstraction.base.view.adapter.model;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;

/**
 * @author Kulomady on 1/25/17.
 */

public class LoadingMoreModel implements Visitable<AdapterTypeFactory> {

    @Override
    public int type(AdapterTypeFactory adapterTypeFactory) {
        return adapterTypeFactory.type(this);
    }
}