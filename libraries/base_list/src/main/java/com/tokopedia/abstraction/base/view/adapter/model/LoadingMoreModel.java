package com.tokopedia.baselist.adapter.model;


import com.tokopedia.baselist.adapter.Visitable;
import com.tokopedia.baselist.adapter.factory.AdapterTypeFactory;

/**
 * @author Kulomady on 1/25/17.
 */

public class LoadingMoreModel implements Visitable<AdapterTypeFactory> {

    @Override
    public int type(AdapterTypeFactory adapterTypeFactory) {
        return adapterTypeFactory.type(this);
    }
}