package com.tokopedia.abstraction.base.view.adapter;

/**
 * Created by alvarisi on 12/8/17.
 */

public class BaseListAdapterTypeFactory<T extends Visitable> extends BaseAdapterTypeFactory implements BaseListTypeFactory<T> {
    @Override
    public int type(Visitable viewModel) {
        return 0;
    }

}
