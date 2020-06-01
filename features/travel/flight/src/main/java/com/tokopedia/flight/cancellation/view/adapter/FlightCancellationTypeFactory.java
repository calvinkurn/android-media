package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;

/**
 * @author by furqan on 21/03/18.
 */

public interface FlightCancellationTypeFactory extends AdapterTypeFactory {

    int type(FlightCancellationModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
