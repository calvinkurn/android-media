package com.tokopedia.train.passenger.domain;

import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.data.cloud.entity.TrainPaxPassengerEntity;
import com.tokopedia.train.passenger.data.cloud.entity.TrainSoftbookEntity;
import com.tokopedia.train.passenger.data.cloud.entity.TrainTripEntity;
import com.tokopedia.train.passenger.domain.model.TrainPaxPassenger;
import com.tokopedia.train.passenger.domain.model.TrainSeat;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.model.TrainTrip;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainSoftBookingMapper implements Func1<TrainSoftbookEntity, TrainSoftbook> {

    public TrainSoftBookingMapper() {
    }

    @Override
    public TrainSoftbook call(TrainSoftbookEntity trainSoftbookEntity) {
        TrainSoftbook trainSoftbook = new TrainSoftbook();
        if (trainSoftbookEntity.getReturnTrips() != null) {
            trainSoftbook.setReturnTrips(getTrainTrips(trainSoftbookEntity.getReturnTrips()));
        }
        trainSoftbook.setDepartureTrips(getTrainTrips(trainSoftbookEntity.getDepartureTrips()));
        trainSoftbook.setExpiryTimestamp(trainSoftbookEntity.getExpiryTimestamp());
        trainSoftbook.setReservationId(trainSoftbookEntity.getReservationId());
        trainSoftbook.setReservationCode(trainSoftbookEntity.getReservationCode());
        return trainSoftbook;
    }

    private List<TrainTrip> getTrainTrips(List<TrainTripEntity> trainTripEntityList) {
        List<TrainTrip> trainTripList = new ArrayList<>();

        for (int i = 0; i < trainTripEntityList.size(); i++) {
            TrainTrip trainTrip = new TrainTrip();
            trainTrip.setBookCode(trainTripEntityList.get(i).getBookCode());
            trainTrip.setOrg(trainTripEntityList.get(i).getOrg());
            trainTrip.setDes(trainTripEntityList.get(i).getDes());
            trainTrip.setTrainNo(trainTripEntityList.get(i).getTrainNo());
            trainTrip.setTrainClass(trainTripEntityList.get(i).getTrainClass());
            trainTrip.setSubclass(trainTripEntityList.get(i).getSubclass());
            trainTrip.setDepartureTimestamp(trainTripEntityList.get(i).getDepartureTimestamp());
            trainTrip.setArrivalTimestamp(trainTripEntityList.get(i).getArrivalTimestamp());
            trainTrip.setErrCode(trainTripEntityList.get(i).getErrCode());
            trainTrip.setNumCode(trainTripEntityList.get(i).getNumCode());

            List<TrainPaxPassenger> trainPaxPassengerList = new ArrayList<>();
            int numOfInfant = 0;
            int numOfAdult = 0;
            for (int j = 0; j < trainTripEntityList.get(i).getPaxPassengers().size(); j++) {
                TrainPaxPassengerEntity trainPaxPassengerEntity = trainTripEntityList
                        .get(i).getPaxPassengers().get(j);
                TrainPaxPassenger trainPaxPassenger = new TrainPaxPassenger();
                trainPaxPassenger.setIdNumber(trainPaxPassengerEntity.getIdNumber());
                trainPaxPassenger.setName(trainPaxPassengerEntity.getName());
                trainPaxPassenger.setPaxType(trainPaxPassengerEntity.getPaxType());

                if (trainPaxPassengerEntity.getPaxType() == TrainBookingPassenger.INFANT) {
                    numOfInfant++;
                } else if (trainPaxPassengerEntity.getPaxType() == TrainBookingPassenger.ADULT) {
                    numOfAdult++;
                }

                TrainSeat trainSeat = new TrainSeat();
                trainSeat.setColumn(trainPaxPassengerEntity.getSeat().getColumn());
                trainSeat.setRow(trainPaxPassengerEntity.getSeat().getRow());
                trainSeat.setWagonNo(trainPaxPassengerEntity.getSeat().getWagonNo());
                trainPaxPassenger.setSeat(trainSeat);

                trainPaxPassengerList.add(trainPaxPassenger);
            }
            trainTrip.setPaxPassengers(trainPaxPassengerList);

            trainTrip.setNumOfAdultPassenger(numOfAdult);
            trainTrip.setNumOfInfantPassenger(numOfInfant);

            trainTrip.setAdultPrice(trainTripEntityList.get(i).getAdultPrice());
            trainTrip.setBookBalance(trainTripEntityList.get(i).getBookBalance());
            trainTrip.setDiscount(trainTripEntityList.get(i).getDiscount());
            trainTrip.setDisplayAdultPrice(trainTripEntityList.get(i).getDisplayAdultPrice());
            trainTrip.setDisplayBookBalance(trainTripEntityList.get(i).getDisplayBookBalance());
            trainTrip.setDisplayDiscount(trainTripEntityList.get(i).getDisplayDiscount());
            trainTrip.setDisplayExtraFee(trainTripEntityList.get(i).getDisplayExtraFee());
            trainTrip.setDisplayInfantPrice(trainTripEntityList.get(i).getDisplayInfantPrice());
            trainTrip.setDisplayNormalSales(trainTripEntityList.get(i).getDisplayNormalSales());
            trainTrip.setDisplayTotalPrice(trainTripEntityList.get(i).getDisplayTotalPrice());
            trainTrip.setDisplayTotalPriceAdult(trainTripEntityList.get(i).getDisplayTotalPriceAdult());
            trainTrip.setDisplayTotalPriceInfant(trainTripEntityList.get(i).getDisplayTotalPriceInfant());
            trainTrip.setExtraFee(trainTripEntityList.get(i).getExtraFee());
            trainTrip.setInfantPrice(trainTripEntityList.get(i).getInfantPrice());
            trainTrip.setNormalSales(trainTripEntityList.get(i).getNormalSales());
            trainTrip.setTotalPrice(trainTripEntityList.get(i).getTotalPrice());
            trainTrip.setTotalPriceAdult(trainTripEntityList.get(i).getTotalPriceAdult());
            trainTrip.setTotalPriceInfant(trainTripEntityList.get(i).getTotalPriceInfant());

            trainTripList.add(trainTrip);
        }
        return trainTripList;
    }
}
