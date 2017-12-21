package com.tokopedia.abstraction.base.view.adapter;

/**
 * Created by alvarisi on 12/21/17.
 */

public interface BaseListCheckableTypeFactory<T extends Visitable> extends AdapterTypeFactory {
    int type(T viewModel);
}
