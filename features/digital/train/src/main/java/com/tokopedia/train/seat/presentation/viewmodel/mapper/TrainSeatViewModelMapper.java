package com.tokopedia.train.seat.presentation.viewmodel.mapper;

import com.tokopedia.train.seat.data.entity.TrainSeatEntity;
import com.tokopedia.train.seat.data.entity.TrainWagonEntity;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class TrainSeatViewModelMapper {
    String[] seating = new String[]{"A", "B", "C", "D", "E"};
    private Map<String, Integer> seatAlphabet = new HashMap<>();
    private int maxColumn = 0;
    private int maxRow = 0;

    @Inject
    public TrainSeatViewModelMapper() {
        seatAlphabet.put("A", 0);
        seatAlphabet.put("B", 1);
        seatAlphabet.put("C", 2);
        seatAlphabet.put("D", 3);
        seatAlphabet.put("E", 4);
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

        for (int i = 0; i < entities.size(); i++) {
            TrainSeatEntity seat = entities.get(i);
            if (seat.getRow() > maxRow) {
                maxRow = seat.getRow();
            }
            int asciiChar = seat.getColumn().toUpperCase().charAt(0);
            int col = asciiChar - 64;
            if (maxColumn < col) {
                maxColumn = col;
            }
        }
        List<TrainSeatViewModel> newViewModels = new ArrayList<>();
        int j = 0, size = entities.size();
        while (j < size) {
            for (int i = 0; i < maxColumn; i++) {
                if (j >= size) break;
                int currentIndexSeat = seatAlphabet.get(entities.get(j).getColumn());
                if (i < currentIndexSeat) {
                    newViewModels.addAll(buildPreAlphabet(entities.get(j).getRow(), currentIndexSeat));
                    newViewModels.add(transform(entities.get(j)));
                    i = currentIndexSeat;
                    j++;
                } else if (i > currentIndexSeat) {
                    newViewModels.addAll(buildPastColumn(entities.get(j).getRow(), i, maxColumn));
                    i = -1;
                } else {
                    newViewModels.add(transform(entities.get(j)));
                    j++;
                }
            }
        }
        return newViewModels;
    }

    private List<TrainSeatViewModel> buildPreAlphabet(int row, int pos) {
        List<TrainSeatViewModel> viewModels = new ArrayList<>();
        TrainSeatViewModel viewModel;
        for (int i = 0; i < pos; i++) {
            viewModel = new TrainSeatViewModel();
            viewModel.setColumn(seating[i]);
            viewModel.setRow(row);
            viewModel.setAvailable(false);
            viewModel.setEmpty(true);
            viewModels.add(viewModel);
        }
        return viewModels;
    }

    private List<TrainSeatViewModel> buildPastColumn(int row, int pos, int maxCol) {
        List<TrainSeatViewModel> viewModels = new ArrayList<>();
        TrainSeatViewModel viewModel;
        for (int i = pos; i < maxCol; i++) {
            viewModel = new TrainSeatViewModel();
            viewModel.setColumn(seating[i]);
            viewModel.setRow(row);
            viewModel.setAvailable(false);
            viewModel.setEmpty(true);
            viewModels.add(viewModel);
        }
        return viewModels;
    }

    public TrainWagonViewModel transform(TrainWagonEntity entity) {
        TrainWagonViewModel viewModel = new TrainWagonViewModel();
        viewModel.setWagonCode(entity.getWagonCode());
        viewModel.setSeats(transform(entity.getSeatDetailEntities()));
        viewModel.setMaxColumn(maxColumn);
        viewModel.setMaxRow(maxRow);
        return viewModel;
    }
}
