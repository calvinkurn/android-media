package com.tokopedia.core.dynamicfilter.presenter;

import com.tokopedia.core.session.base.BaseImpl;

/**
 * Created by noiz354 on 7/11/16.
 */
public abstract class DynamicFilterBase extends BaseImpl<DynamicFilterBaseDetailView> {
    public DynamicFilterBase(DynamicFilterBaseDetailView view) {
        super(view);
    }
    public abstract void hitHadesDemo();
}
