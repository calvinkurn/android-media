package com.tokopedia.train.station.domain.model.mapper;

import com.tokopedia.train.station.data.database.TrainStationTable;
import com.tokopedia.train.station.data.entity.TrainCityEntity;
import com.tokopedia.train.station.data.entity.TrainStationEntity;
import com.tokopedia.train.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.train.station.domain.model.TrainStation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 01/02/19.
 */
public class TrainStationMapper {

    @Inject
    public TrainStationMapper() {
    }

    public List<TrainStationTable> transformToTable(List<TrainStationIslandEntity> entities) {
        List<TrainStationTable> trainStationTables = new ArrayList<>();
        for (TrainStationIslandEntity islandEntity : entities) {
            for (TrainCityEntity trainCityEntity : islandEntity.getCities()) {
                for (TrainStationEntity trainStationEntity : trainCityEntity.getStations()) {
                    TrainStationTable table = new TrainStationTable();
                    table.setCityId(trainCityEntity.getId());
                    table.setCityName(trainCityEntity.getName());
                    table.setIslandName(islandEntity.getName());
                    table.setStationCode(trainStationEntity.getCode());
                    table.setStationId(trainStationEntity.getId());
                    table.setStationName(trainStationEntity.getName());
                    table.setStationDisplayName(trainStationEntity.getDisplayName());
                    table.setPopularityOrder(trainStationEntity.getPopularityOrder());
                    trainStationTables.add(table);
                }
            }
        }
        return trainStationTables;
    }

    public List<TrainStation> transformToModel(List<TrainStationTable> tables) {
        List<TrainStation> models = new ArrayList<>();
        if (tables != null && tables.size() > 0) {
            for (TrainStationTable table : tables) {
                models.add(transform(table));
            }
        }
        return models;
    }

    public TrainStation transform(TrainStationTable table) {
        TrainStation model = new TrainStation();
        model.setCityId(table.getCityId());
        model.setCityName(table.getCityName());
        model.setIslandName(table.getIslandName());
        model.setStationCode(table.getStationCode());
        model.setStationId(table.getStationId());
        model.setStationCode(table.getStationCode());
        model.setStationName(table.getStationName());
        return model;
    }

}
