package com.tokopedia.abstraction.base.view.adapter.model;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;

/**
 * Created by zulfikarrahman on 3/9/18.
 */

public class LoadingModelShimmeringList extends LoadingModel {

    @Override
    public int type(AdapterTypeFactory adapterTypeFactory) {
        return adapterTypeFactory.type(this);
    }
}
