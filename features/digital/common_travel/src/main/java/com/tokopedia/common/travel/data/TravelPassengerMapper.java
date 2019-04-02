package com.tokopedia.common.travel.data;

import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.database.TravelPassengerTable;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 09/11/18.
 */

public class TravelPassengerMapper {

    @Inject
    public TravelPassengerMapper() {
    }

    public List<TravelPassengerTable> transformEntity(List<TravelPassengerEntity> travelPassengerEntities) {
        List<TravelPassengerTable> travelPassengerTables = new ArrayList<>();
        for (TravelPassengerEntity travelPassengerEntity : travelPassengerEntities) {
            travelPassengerTables.add(transform(travelPassengerEntity));
        }
        return travelPassengerTables;
    }

    public TravelPassengerTable transform(TravelPassengerEntity travelPassengerEntity) {
        TravelPassengerTable travelPassengerTable = new TravelPassengerTable();
        Long currentTimestamp = System.currentTimeMillis() / 1000;
        travelPassengerTable.setIdPassenger(travelPassengerEntity.getFirstName() +
                travelPassengerEntity.getLastName() + currentTimestamp.toString());
        travelPassengerTable.setId(travelPassengerEntity.getId());
        travelPassengerTable.setNamePassenger(travelPassengerEntity.getName());
        travelPassengerTable.setFirstName(travelPassengerEntity.getFirstName());
        travelPassengerTable.setLastName(travelPassengerEntity.getLastName());
        travelPassengerTable.setBirthDate(travelPassengerEntity.getBirthDate());
        travelPassengerTable.setNationality(travelPassengerEntity.getNationality());
        travelPassengerTable.setPassportNo(travelPassengerEntity.getPassportNo());
        travelPassengerTable.setPassportCountry(travelPassengerEntity.getPassportCountry());
        travelPassengerTable.setPassportExpiry(travelPassengerEntity.getPassportExpiry());
        travelPassengerTable.setTitle(travelPassengerEntity.getTitle());
        travelPassengerTable.setIdNumber(travelPassengerEntity.getIdNumber());
        travelPassengerTable.setIsBuyer(travelPassengerEntity.isBuyer());
        travelPassengerTable.setPaxType(travelPassengerEntity.getPaxType());
        travelPassengerTable.setTravelId(travelPassengerEntity.getTravelId());
        travelPassengerTable.setUserId(travelPassengerEntity.getUserId());
        travelPassengerTable.setSelected(0);
        return travelPassengerTable;
    }

    public List<TravelPassenger> transformTable(List<TravelPassengerTable> travelPassengerTables) {
        List<TravelPassenger> travelPassengerList = new ArrayList<>();
        for (TravelPassengerTable travelPassengerTable : travelPassengerTables) {
            travelPassengerList.add(mapTableToModel(travelPassengerTable));
        }
        return travelPassengerList;
    }

    private TravelPassenger mapTableToModel(TravelPassengerTable travelPassengerTable) {
        TravelPassenger travelPassenger = new TravelPassenger();
        travelPassenger.setIdPassenger(travelPassengerTable.getIdPassenger());
        travelPassenger.setId(travelPassengerTable.getId());
        travelPassenger.setName(travelPassengerTable.getNamePassenger());
        travelPassenger.setFirstName(travelPassengerTable.getFirstName());
        travelPassenger.setLastName(travelPassengerTable.getLastName());
        travelPassenger.setBirthDate(travelPassengerTable.getBirthDate());
        travelPassenger.setNationality(travelPassengerTable.getNationality());
        travelPassenger.setPassportNo(travelPassengerTable.getPassportNo());
        travelPassenger.setPassportCountry(travelPassengerTable.getPassportCountry());
        travelPassenger.setPassportExpiry(travelPassengerTable.getPassportExpiry());
        travelPassenger.setTitle(travelPassengerTable.getTitle());
        travelPassenger.setIdNumber(travelPassengerTable.getIdNumber());
        travelPassenger.setBuyer(travelPassengerTable.getIsBuyer());
        travelPassenger.setPaxType(travelPassengerTable.getPaxType());
        travelPassenger.setTravelId(travelPassengerTable.getTravelId());
        travelPassenger.setUserId(travelPassengerTable.getUserId());
        travelPassenger.setSelected(travelPassengerTable.getSelected() == 1);
        return travelPassenger;
    }
}
