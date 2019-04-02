package com.tokopedia.train.seat.presentation.viewmodel.mapper;

import com.tokopedia.train.seat.data.entity.TrainSeatEntity;
import com.tokopedia.train.seat.data.entity.TrainWagonEntity;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatRowColumnViewModel;
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

    public TrainSeatRowColumnViewModel transform(List<TrainSeatEntity> entities) {
        int maxColumn = 0;
        int maxRow = 0;
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
        int indexEntity = 0, size = entities.size();
        while (indexEntity < size) {
            for (int indexSeharusnya = 0; indexSeharusnya < maxColumn; indexSeharusnya++) {
                if (indexEntity >= size) break;
                int currentIndexSeat = seatAlphabet.get(entities.get(indexEntity).getColumn());
                if (indexSeharusnya < currentIndexSeat) {
                    int beforeItem = 0;
                    if (newViewModels.size() > 0) {
                        int newBeforeItem = currentIndexSeat - seatAlphabet.get(newViewModels.get(newViewModels.size() - 1).getColumn());
                        if (newBeforeItem >= 0) {
                            beforeItem = newBeforeItem;
                        }
                    }
                    newViewModels.addAll(buildPreAlphabet(entities.get(indexEntity).getRow(), currentIndexSeat, indexSeharusnya));
                    newViewModels.add(transform(entities.get(indexEntity)));
                    indexSeharusnya = currentIndexSeat;
                    indexEntity++;
                } else if (indexSeharusnya > currentIndexSeat) {
                    newViewModels.addAll(buildPastColumn(entities.get(indexEntity).getRow() - 1, indexSeharusnya, maxColumn));
                    indexSeharusnya = -1;
                } else {
                    newViewModels.add(transform(entities.get(indexEntity)));
                    indexEntity++;
                }
            }
        }
        return new TrainSeatRowColumnViewModel(newViewModels, maxColumn, maxRow);
    }

    private List<TrainSeatViewModel> buildPreAlphabet(int row, int pos, int beforeitem) {
        List<TrainSeatViewModel> viewModels = new ArrayList<>();
        TrainSeatViewModel viewModel;
        for (int i = beforeitem; i < pos; i++) {
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
        TrainSeatRowColumnViewModel trainSeatRowColumnViewModel = transform(entity.getSeatDetailEntities());
        viewModel.setSeats(trainSeatRowColumnViewModel.getSeats());
        viewModel.setMaxColumn(trainSeatRowColumnViewModel.getMaxColumn());
        viewModel.setMaxRow(trainSeatRowColumnViewModel.getMaxRow());
        return viewModel;
    }
}
