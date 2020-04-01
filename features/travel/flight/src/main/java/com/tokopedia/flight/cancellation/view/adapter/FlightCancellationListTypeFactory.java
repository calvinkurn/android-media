package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

/**
 * @author by furqan on 30/04/18.
 */

public interface FlightCancellationListTypeFactory extends AdapterTypeFactory {

    int type(FlightCancellationListViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
