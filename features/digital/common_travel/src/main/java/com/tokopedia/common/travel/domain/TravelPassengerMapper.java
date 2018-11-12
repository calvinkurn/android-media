package com.tokopedia.common.travel.domain;

import com.tokopedia.common.travel.database.TravelPassengerDb;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 09/11/18.
 */
public class TravelPassengerMapper implements Func1<List<TravelPassengerDb>, List<TravelPassenger>> {

    public TravelPassengerMapper() {
    }

    @Override
    public List<TravelPassenger> call(List<TravelPassengerDb> travelPassengerDbs) {
        List<TravelPassenger> travelPassengerList = new ArrayList<>();
        for (TravelPassengerDb travelPassengerDb : travelPassengerDbs) {
            TravelPassenger travelPassenger = new TravelPassenger();
            travelPassenger.setIdPassenger(travelPassengerDb.getIdPassenger());
            travelPassenger.setId(travelPassengerDb.getId());
            travelPassenger.setName(travelPassengerDb.getNamePassenger());
            travelPassenger.setFirstName(travelPassengerDb.getFirstName());
            travelPassenger.setLastName(travelPassengerDb.getLastName());
            travelPassenger.setBirthDate(travelPassengerDb.getBirthDate());
            travelPassenger.setNationality(travelPassengerDb.getNationality());
            travelPassenger.setPassportNo(travelPassengerDb.getPassportNo());
            travelPassenger.setPassportCountry(travelPassengerDb.getPassportCountry());
            travelPassenger.setPassportExpiry(travelPassengerDb.getPassportExpiry());
            travelPassenger.setTitle(travelPassengerDb.getTitle());
            travelPassenger.setIdNumber(travelPassengerDb.getIdNumber());
            travelPassenger.setBuyer(travelPassengerDb.getIsBuyer());
            travelPassenger.setPaxType(travelPassengerDb.getPaxType());
            travelPassenger.setTravelId(travelPassengerDb.getTravelId());
            travelPassenger.setUserId(travelPassengerDb.getUserId());
            travelPassenger.setSelected(travelPassengerDb.isSelected());
            travelPassengerList.add(travelPassenger);
        }
        return travelPassengerList;
    }
}
