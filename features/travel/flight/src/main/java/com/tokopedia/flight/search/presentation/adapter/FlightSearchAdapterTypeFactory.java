package com.tokopedia.flight.search.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchSeeAllViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchSeeOnlyBestPairingViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchViewHolder;
import com.tokopedia.flight.search.presentation.model.EmptyResultModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeAllResultModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeOnlyBestPairingModel;
import com.tokopedia.flight.searchV4.presentation.adapter.viewholder.FlightSearchShimmeringViewHolder;

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
        } else if (type == FlightSearchSeeAllViewHolder.LAYOUT) {
            return new FlightSearchSeeAllViewHolder(parent, onFlightSearchListener);
        } else if (type == FlightSearchSeeOnlyBestPairingViewHolder.LAYOUT) {
            return new FlightSearchSeeOnlyBestPairingViewHolder(parent, onFlightSearchListener);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            return new ErrorNetworkViewHolder(parent);
        } else if (type == FlightSearchShimmeringViewHolder.Companion.getLAYOUT()) {
            return new FlightSearchShimmeringViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public void onRetryClicked() {
        onFlightSearchListener.onRetryClicked();
    }

    public int type(FlightJourneyModel flightJourneyViewModel) {
        return FlightSearchViewHolder.LAYOUT;
    }

    public int type(LoadingModel loadingModel) {
        return FlightSearchShimmeringViewHolder.Companion.getLAYOUT();
    }

    public int type(EmptyResultModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    public int type(FlightSearchSeeAllResultModel viewModel) {
        return FlightSearchSeeAllViewHolder.LAYOUT;
    }

    public int type(FlightSearchSeeOnlyBestPairingModel viewModel) {
        return FlightSearchSeeOnlyBestPairingViewHolder.LAYOUT;
    }

    public interface OnFlightSearchListener {
        void onRetryClicked();

        void onDetailClicked(FlightJourneyModel journeyViewModel, int adapterPosition);

        void onItemClicked(FlightJourneyModel journeyViewModel, int adapterPosition);

        void onShowAllClicked();

        void onShowBestPairingClicked();

    }

}
