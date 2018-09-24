package com.tokopedia.train.seat.domain.model;

import com.tokopedia.train.seat.data.entity.TrainChangeSeatEntity;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerSeatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainPassengerSeatMapper {
    @Inject
    public TrainPassengerSeatMapper() {
    }

    public TrainPassengerSeat transform(TrainChangeSeatEntity entity) {
        TrainPassengerSeat seat = null;
        if (entity != null) {
            seat = new TrainPassengerSeat();
            seat.setName(entity.getName());
            TrainSeatPassengerSeatViewModel seatViewModel = new TrainSeatPassengerSeatViewModel();
            seatViewModel.setWagonCode(entity.getWagonCode());
            String row = entity.getSeat();
            seatViewModel.setRow(row.replaceAll("[^\\d.]", ""));
            String column = entity.getSeat();
            seatViewModel.setColumn(column.replaceAll("[0-9]", ""));
            seat.setSeat(seatViewModel);
        }
        return seat;
    }

    public List<TrainPassengerSeat> transform(List<TrainChangeSeatEntity> entities) {
        List<TrainPassengerSeat> seats = new ArrayList<>();
        TrainPassengerSeat seat;
        if (entities != null && entities.size() > 0) {
            for (TrainChangeSeatEntity entity : entities) {
                seat = transform(entity);
                if (seat != null) {
                    seats.add(seat);
                }
            }
        }
        return seats;
    }
}
