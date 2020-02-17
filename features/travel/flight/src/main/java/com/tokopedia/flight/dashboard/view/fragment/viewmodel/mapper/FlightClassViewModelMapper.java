package com.tokopedia.flight.dashboard.view.fragment.viewmodel.mapper;

import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassViewModelMapper {
    @Inject
    public FlightClassViewModelMapper() {
    }

    public FlightClassViewModel transform(FlightClassEntity entity) {
        FlightClassViewModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightClassViewModel();
            viewModel.setTitle(entity.getAttributes().getLabel());
            viewModel.setId(entity.getId());
        }
        return viewModel;
    }

    public List<FlightClassViewModel> transform(List<FlightClassEntity> entities) {
        List<FlightClassViewModel> viewModels = new ArrayList<>();
        FlightClassViewModel viewModel;
        for (FlightClassEntity entity : entities) {
            viewModel = transform(entity);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
