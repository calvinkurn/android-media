package com.tokopedia.flight.search.presentation.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author by furqan on 02/10/18.
 */

public class FlightSearchAdapter extends BaseAdapter<FlightSearchAdapterTypeFactory> {

    private static final List<Class> REGISTERED_NOT_DATA_CLASSES = Arrays.asList(new Class[]{
            EmptyModel.class, EmptyResultViewModel.class, ErrorNetworkModel.class,
            LoadingModel.class, LoadingMoreModel.class
    });

    public FlightSearchAdapter(FlightSearchAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

    /**
     * remove loading/empty/error (all non T data) in adapter
     */
    public void clearAllNonDataElement() {
        if (hasNonDataElementAtLastIndex()) {
            visitables.remove(getLastIndex());
        }
    }

    private boolean hasNonDataElementAtLastIndex() {
        if (visitables.size() > 0) {
            Visitable visitable = visitables.get(getLastIndex());
            if (REGISTERED_NOT_DATA_CLASSES.contains(visitable.getClass())) {
                return true;
            }
        }

        return false;
    }

}
