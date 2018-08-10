package com.tokopedia.train.station.presentation.adapter.viewmodel.mapper;

import com.tokopedia.train.station.domain.model.TrainStation;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 3/8/18.
 */

public class TrainStationViewModelMapper {
    @Inject
    public TrainStationViewModelMapper() {
    }

    public TrainStationViewModel transform(TrainStation station) {
        TrainStationViewModel viewModel = null;
        if (station != null) {
            viewModel = new TrainStationViewModel();
            viewModel.setCityCode(String.valueOf(station.getCityId()));
            viewModel.setCityName(station.getCityName());
            viewModel.setStationId(station.getStationId());
            viewModel.setStationCode(station.getStationCode());
            viewModel.setStationName(station.getStationName());
            viewModel.setIslandName(station.getIslandName());
        }
        return viewModel;
    }

    public List<TrainStationViewModel> transform(List<TrainStation> stations) {
        List<TrainStationViewModel> stationViewModels = new ArrayList<>();
        TrainStationViewModel viewModel = null;
        if (stations != null) {
            for (TrainStation station : stations) {
                viewModel = transform(station);
                if (viewModel != null) {
                    stationViewModels.add(viewModel);
                }
            }
        }
        return stationViewModels;
    }
}
