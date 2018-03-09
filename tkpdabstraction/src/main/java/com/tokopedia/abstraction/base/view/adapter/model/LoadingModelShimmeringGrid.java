package com.tokopedia.abstraction.base.view.adapter.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;

/**
 * Created by zulfikarrahman on 3/9/18.
 */

public class LoadingModelShimmeringGrid extends LoadingModel implements Visitable<AdapterTypeFactory> {
    @Override
    public int type(AdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
