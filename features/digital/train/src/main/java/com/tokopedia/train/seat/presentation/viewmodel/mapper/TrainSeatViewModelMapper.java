package com.tokopedia.train.seat.presentation.viewmodel.mapper;

import com.tokopedia.train.seat.data.entity.TrainSeatEntity;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainSeatViewModelMapper {
    @Inject
    public TrainSeatViewModelMapper() {
    }

    public TrainSeatViewModel transform(TrainSeatEntity entity) {
        TrainSeatViewModel viewModel = null;
        if (entity != null) {
            viewModel = new TrainSeatViewModel();
            viewModel.setColumn(entity.getColumn());
            viewModel.setRow(entity.getRow());
            viewModel.setAvailable(entity.getStatus() == 0);
        }
        return viewModel;
    }

    public List<TrainSeatViewModel> transform(List<TrainSeatEntity> entities) {
        List<TrainSeatViewModel> viewModels = new ArrayList<>();
        TrainSeatViewModel viewModel;
        if (entities != null) {
            for (TrainSeatEntity entity : entities) {
                viewModel = transform(entity);
                if (viewModel != null)
                    viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
