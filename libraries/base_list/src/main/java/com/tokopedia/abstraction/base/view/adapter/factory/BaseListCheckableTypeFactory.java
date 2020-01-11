package com.tokopedia.baselist.adapter.factory;

import com.tokopedia.baselist.adapter.Visitable;

/**
 * Created by alvarisi on 12/21/17.
 */

public interface BaseListCheckableTypeFactory<T extends Visitable> extends AdapterTypeFactory {
    int type(T viewModel);
}
