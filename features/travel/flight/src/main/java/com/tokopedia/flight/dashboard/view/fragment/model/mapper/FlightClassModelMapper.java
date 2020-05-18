package com.tokopedia.flight.dashboard.view.fragment.model.mapper;

import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassModelMapper {
    @Inject
    public FlightClassModelMapper() {
    }

    public FlightClassModel transform(FlightClassEntity entity) {
        FlightClassModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightClassModel();
            viewModel.setTitle(entity.getAttributes().getLabel());
            viewModel.setId(entity.getId());
        }
        return viewModel;
    }

    public List<FlightClassModel> transform(List<FlightClassEntity> entities) {
        List<FlightClassModel> viewModels = new ArrayList<>();
        FlightClassModel viewModel;
        for (FlightClassEntity entity : entities) {
            viewModel = transform(entity);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
