package com.tokopedia.flight.searchV2.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightSearchShimmeringViewHolder;
import com.tokopedia.flight.search.view.model.EmptyResultViewModel;
import com.tokopedia.flight.searchV2.presentation.adapter.viewholder.FlightSearchViewHolder;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;

/**
 * @author by furqan on 02/10/18.
 */

public class FlightSearchAdapterTypeFactory extends BaseAdapterTypeFactory
        implements AdapterTypeFactory, ErrorNetworkViewHolder.OnRetryListener {

    private OnFlightSearchListener onFlightSearchListener;

    public FlightSearchAdapterTypeFactory(OnFlightSearchListener onFlightSearchListener) {
        this.onFlightSearchListener = onFlightSearchListener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightSearchViewHolder.LAYOUT) {
            return new FlightSearchViewHolder(parent, onFlightSearchListener);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            return new ErrorNetworkViewHolder(parent);
        } else if (type == FlightSearchShimmeringViewHolder.LAYOUT) {
            return new FlightSearchShimmeringViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public void onRetryClicked() {
        onFlightSearchListener.onRetryClicked();
    }

    public int type(FlightJourneyViewModel flightJourneyViewModel) {
        return FlightSearchViewHolder.LAYOUT;
    }

    public int type(LoadingModel loadingModel) {
        return FlightSearchShimmeringViewHolder.LAYOUT;
    }

    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    public interface OnFlightSearchListener {
        void onRetryClicked();

        void onDetailClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition);

        void onItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition);
    }

}
