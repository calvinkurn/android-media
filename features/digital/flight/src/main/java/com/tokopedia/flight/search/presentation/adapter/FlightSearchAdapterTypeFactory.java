package com.tokopedia.flight.search.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.common.travel.presentation.adapter.TravelSearchShimmeringViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchSeeAllViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchSeeOnlyBestPairingViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchViewHolder;
import com.tokopedia.flight.search.presentation.model.EmptyResultViewModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeAllResultViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeOnlyBestPairingViewModel;
import com.tokopedia.flight.searchV3.presentation.adapter.viewholder.FlightSearchTitleRouteViewHolder;
import com.tokopedia.flight.searchV3.presentation.model.FlightSearchTitleRouteViewModel;

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
        } else if (type == FlightSearchTitleRouteViewHolder.Companion.getLAYOUT()) {
            return new FlightSearchTitleRouteViewHolder(parent);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            return new ErrorNetworkViewHolder(parent);
        } else if (type == TravelSearchShimmeringViewHolder.LAYOUT) {
            return new TravelSearchShimmeringViewHolder(parent);
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
        return TravelSearchShimmeringViewHolder.LAYOUT;
    }

    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    public int type(FlightSearchSeeAllResultViewModel viewModel) {
        return FlightSearchSeeAllViewHolder.LAYOUT;
    }

    public int type(FlightSearchSeeOnlyBestPairingViewModel viewModel) {
        return FlightSearchSeeOnlyBestPairingViewHolder.LAYOUT;
    }

    public int type(FlightSearchTitleRouteViewModel viewModel) {
        return FlightSearchTitleRouteViewHolder.Companion.getLAYOUT();
    }

    public interface OnFlightSearchListener {
        void onRetryClicked();

        void onDetailClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition);

        void onItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition);

        void onShowAllClicked();

        void onShowBestPairingClicked();
    }

}
