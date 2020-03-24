package com.tokopedia.flight.bookingV2.presentation.model.mapper;

import com.tokopedia.flight.bookingV2.data.cloud.entity.InsuranceEntity;
import com.tokopedia.flight.bookingV2.presentation.model.FlightInsuranceModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightInsuranceModelMapper {
    private FlightInsuranceBenefitModelMapper benefitViewModelMapper;

    @Inject
    public FlightInsuranceModelMapper(FlightInsuranceBenefitModelMapper benefitViewModelMapper) {
        this.benefitViewModelMapper = benefitViewModelMapper;
    }

    public FlightInsuranceModel transform(InsuranceEntity entity) {
        FlightInsuranceModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightInsuranceModel();
            viewModel.setId(entity.getId());
            viewModel.setDefaultChecked(entity.isDefaultChecked());
            viewModel.setName(entity.getName());
            viewModel.setDescription(entity.getDescription());
            viewModel.setTncUrl(entity.getTncUrl());
            viewModel.setTncAggreement(entity.getTncAggreement());
            viewModel.setTotalPrice(entity.getTotalPriceNumeric());
            viewModel.setBenefits(benefitViewModelMapper.transform(entity.getBenefits()));
        }
        return viewModel;
    }

    public List<FlightInsuranceModel> transform(List<InsuranceEntity> entities) {
        List<FlightInsuranceModel> viewModels = new ArrayList<>();
        FlightInsuranceModel viewModel;
        for (InsuranceEntity entity : entities) {
            viewModel = transform(entity);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
