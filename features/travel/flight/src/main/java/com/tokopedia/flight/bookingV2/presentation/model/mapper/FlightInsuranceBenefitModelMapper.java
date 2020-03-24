package com.tokopedia.flight.bookingV2.presentation.model.mapper;

import com.tokopedia.flight.bookingV2.data.cloud.entity.BenefitEntity;
import com.tokopedia.flight.bookingV2.presentation.model.FlightInsuranceBenefitModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightInsuranceBenefitModelMapper {
    @Inject
    public FlightInsuranceBenefitModelMapper() {
    }

    public FlightInsuranceBenefitModel transform(BenefitEntity entity) {
        FlightInsuranceBenefitModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightInsuranceBenefitModel();
            viewModel.setIcon(entity.getIcon());
            viewModel.setTitle(entity.getTitle());
            viewModel.setDescription(entity.getDescription());
        }
        return viewModel;
    }

    public List<FlightInsuranceBenefitModel> transform(List<BenefitEntity> entities) {
        List<FlightInsuranceBenefitModel> viewModels = new ArrayList<>();
        FlightInsuranceBenefitModel viewModel;
        if (entities != null && entities.size() > 0) {
            for (BenefitEntity entity : entities) {
                viewModel = transform(entity);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }
}
