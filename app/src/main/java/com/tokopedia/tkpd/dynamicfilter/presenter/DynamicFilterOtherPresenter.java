package com.tokopedia.tkpd.dynamicfilter.presenter;

import android.support.v4.app.FragmentActivity;
import android.widget.Filter;

import com.tokopedia.tkpd.session.base.BaseImpl;

/**
 * Created by noiz354 on 7/12/16.
 */
public abstract class DynamicFilterOtherPresenter extends BaseImpl<DynamicFilterOtherView>{
    public DynamicFilterOtherPresenter(DynamicFilterOtherView view) {
        super(view);
    }

    public static final String FILTER_DATA = "FILTER_DATA";

    public abstract void loadMore(FragmentActivity activity);

    public abstract Filter getFilter();
}
