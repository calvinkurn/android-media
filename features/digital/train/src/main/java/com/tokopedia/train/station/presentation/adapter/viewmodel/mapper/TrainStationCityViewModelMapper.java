package com.tokopedia.train.station.presentation.adapter.viewmodel.mapper;

import com.tokopedia.train.station.domain.model.TrainStation;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationCityViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainStationCityViewModelMapper {
    @Inject
    public TrainStationCityViewModelMapper() {
    }

    public TrainStationCityViewModel transform(TrainStation trainStation) {
        TrainStationCityViewModel viewModel = null;
        if (trainStation != null) {
            viewModel = new TrainStationCityViewModel();
            viewModel.setCityName(trainStation.getCityName());
            viewModel.setIslandName(trainStation.getIslandName());
        }
        return viewModel;
    }

    public List<TrainStationCityViewModel> transform(List<TrainStation> trainStations) {
        List<TrainStationCityViewModel> viewModels = new ArrayList<>();
        TrainStationCityViewModel viewModel;
        for (TrainStation trainStation : trainStations) {
            viewModel = transform(trainStation);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
