package com.tokopedia.flight.bookingV2.presentation.viewmodel.mapper;

import com.tokopedia.flight.bookingV2.data.cloud.entity.BenefitEntity;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightInsuranceBenefitViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightInsuranceBenefitViewModelMapper {
    @Inject
    public FlightInsuranceBenefitViewModelMapper() {
    }

    public FlightInsuranceBenefitViewModel transform(BenefitEntity entity) {
        FlightInsuranceBenefitViewModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightInsuranceBenefitViewModel();
            viewModel.setIcon(entity.getIcon());
            viewModel.setTitle(entity.getTitle());
            viewModel.setDescription(entity.getDescription());
        }
        return viewModel;
    }

    public List<FlightInsuranceBenefitViewModel> transform(List<BenefitEntity> entities) {
        List<FlightInsuranceBenefitViewModel> viewModels = new ArrayList<>();
        FlightInsuranceBenefitViewModel viewModel;
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
