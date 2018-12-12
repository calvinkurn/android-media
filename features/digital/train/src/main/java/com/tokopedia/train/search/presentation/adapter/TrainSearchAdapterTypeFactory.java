package com.tokopedia.train.search.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.common.travel.presentation.adapter.TravelSearchShimmeringViewHolder;
import com.tokopedia.train.common.util.TrainAnalytics;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/14/18.
 */

public class TrainSearchAdapterTypeFactory extends BaseAdapterTypeFactory
        implements AdapterTypeFactory, ErrorNetworkViewHolder.OnRetryListener {

    private OnTrainSearchListener listener;
    private TrainAnalytics trainAnalytics;

    public TrainSearchAdapterTypeFactory(OnTrainSearchListener listener, TrainAnalytics trainAnalytics) {
        this.listener = listener;
        this.trainAnalytics = trainAnalytics;
    }

    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return TrainErrorNetworkViewHolder.LAYOUT;
    }

    public int type(TrainScheduleViewModel viewModel) {
        return TrainSearchViewHolder.LAYOUT;
    }

    public int type(LoadingModel flightSearchShimmeringViewModel) {
        return TravelSearchShimmeringViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TrainSearchViewHolder.LAYOUT) {
            return new TrainSearchViewHolder(parent, listener, trainAnalytics);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent);
        } else if (type == TrainErrorNetworkViewHolder.LAYOUT) {
            return new TrainErrorNetworkViewHolder(parent);
        } else if (type == TravelSearchShimmeringViewHolder.LAYOUT) {
            return new TravelSearchShimmeringViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public void onRetryClicked() {
        listener.onRetryClicked();
    }

    public interface OnTrainSearchListener {
        void onRetryClicked();

        void onDetailClicked(TrainScheduleViewModel trainScheduleViewModel, int adapterPosition);

        void onSheduleClicked(TrainScheduleViewModel trainScheduleViewModel);
    }
}
