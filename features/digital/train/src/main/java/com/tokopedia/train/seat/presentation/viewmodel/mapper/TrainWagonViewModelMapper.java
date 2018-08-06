package com.tokopedia.train.seat.presentation.viewmodel.mapper;

import com.tokopedia.train.seat.data.entity.TrainWagonEntity;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainWagonViewModelMapper {
    private TrainSeatViewModelMapper trainSeatViewModelMapper;

    @Inject
    public TrainWagonViewModelMapper(TrainSeatViewModelMapper trainSeatViewModelMapper) {
        this.trainSeatViewModelMapper = trainSeatViewModelMapper;
    }

    public TrainWagonViewModel transform(TrainWagonEntity entity) {
        TrainWagonViewModel viewModel = null;
        if (entity != null) {
            viewModel = trainSeatViewModelMapper.transform(entity);
        }
        return viewModel;
    }

    public List<TrainWagonViewModel> transform(List<TrainWagonEntity> entities) {
        List<TrainWagonViewModel> viewModels = new ArrayList<>();
        TrainWagonViewModel viewModel;
        if (entities != null) {
            for (TrainWagonEntity entity : entities) {
                viewModel = transform(entity);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }
}
