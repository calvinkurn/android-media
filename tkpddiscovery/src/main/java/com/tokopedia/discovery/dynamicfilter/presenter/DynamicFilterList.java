package com.tokopedia.discovery.dynamicfilter.presenter;

import com.tokopedia.core.session.base.BaseImpl;

/**
 * Created by noiz354 on 7/11/16.
 */
public abstract class DynamicFilterList extends BaseImpl<DynamicFilterListView> {
    public static final String TITLE_LIST = "TITLE_LIST";
    public static final String DATA_LIST = "DATA_LIST";

    public DynamicFilterList(DynamicFilterListView view) {
        super(view);
    }
}
