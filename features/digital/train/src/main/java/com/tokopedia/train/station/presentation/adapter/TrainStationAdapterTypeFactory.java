package com.tokopedia.train.station.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.train.station.presentation.adapter.viewholder.TrainAllStationsViewHolder;
import com.tokopedia.train.station.presentation.adapter.viewholder.TrainPopularStationViewHolder;
import com.tokopedia.train.station.presentation.adapter.viewholder.TrainStationGroupViewHolder;
import com.tokopedia.train.station.presentation.adapter.viewholder.TrainStationInCityViewHolder;
import com.tokopedia.train.station.presentation.adapter.viewholder.TrainStationViewHolder;
import com.tokopedia.train.station.presentation.adapter.viewholder.TrainStationsCityGroupViewHolder;
import com.tokopedia.train.station.presentation.adapter.viewholder.listener.TrainStationActionListener;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainAllStationsViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationCityViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationGroupViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationsCityGroupViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationAdapterTypeFactory extends BaseAdapterTypeFactory implements TrainStationTypeFactory {
    private TrainStationActionListener trainStationActionListener;

    public TrainStationAdapterTypeFactory(TrainStationActionListener trainStationActionListener) {
        this.trainStationActionListener = trainStationActionListener;
    }

    @Override
    public int type(TrainStationViewModel trainStationViewModel) {
        return TrainStationViewHolder.LAYOUT;
    }

    @Override
    public int type(TrainStationsCityGroupViewModel trainStationsCityGroupViewModel) {
        return TrainStationsCityGroupViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TrainStationViewHolder.LAYOUT) {
            return new TrainStationViewHolder(parent, trainStationActionListener);
        } else if (type == TrainStationsCityGroupViewHolder.LAYOUT) {
            return new TrainStationsCityGroupViewHolder(parent, trainStationActionListener);
        } else if (type == TrainPopularStationViewHolder.LAYOUT) {
            return new TrainPopularStationViewHolder(parent, trainStationActionListener);
        } else if (type == TrainStationGroupViewHolder.LAYOUT) {
            return new TrainStationGroupViewHolder(parent, trainStationActionListener);
        } else if (type == TrainAllStationsViewHolder.LAYOUT) {
            return new TrainAllStationsViewHolder(parent);
        } else if (type == TrainStationInCityViewHolder.LAYOUT) {
            return new TrainStationInCityViewHolder(parent, trainStationActionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(TrainPopularStationViewModel trainPopularStationViewModel) {
        return TrainPopularStationViewHolder.LAYOUT;
    }

    @Override
    public int type(TrainStationGroupViewModel trainStationGroupViewModel) {
        return TrainStationGroupViewHolder.LAYOUT;
    }

    @Override
    public int type(TrainAllStationsViewModel trainAllStationsViewModel) {
        return TrainAllStationsViewHolder.LAYOUT;
    }

    @Override
    public int type(TrainStationCityViewModel trainStationCityViewModel) {
        return TrainStationInCityViewHolder.LAYOUT;
    }
}
