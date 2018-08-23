package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TrainSoftbook implements Parcelable {

    private String reservationId;
    private String reservationCode;
    private String expiryTimestamp;
    private List<TrainTrip> departureTrips;
    private List<TrainTrip> returnTrips;

    public TrainSoftbook(String reservationId, String reservationCode, String expiryTimestamp,
                         List<TrainTrip> departureTrips, List<TrainTrip> returnTrips) {
        this.reservationId = reservationId;
        this.reservationCode = reservationCode;
        this.expiryTimestamp = expiryTimestamp;
        this.departureTrips = departureTrips;
        this.returnTrips = returnTrips;
    }

    public TrainSoftbook() {
    }

    protected TrainSoftbook(Parcel in) {
        reservationId = in.readString();
        reservationCode = in.readString();
        expiryTimestamp = in.readString();
        departureTrips = in.createTypedArrayList(TrainTrip.CREATOR);
        returnTrips = in.createTypedArrayList(TrainTrip.CREATOR);
    }

    public static final Creator<TrainSoftbook> CREATOR = new Creator<TrainSoftbook>() {
        @Override
        public TrainSoftbook createFromParcel(Parcel in) {
            return new TrainSoftbook(in);
        }

        @Override
        public TrainSoftbook[] newArray(int size) {
            return new TrainSoftbook[size];
        }
    };

    public String getReservationId() {
        return reservationId;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public String getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public List<TrainTrip> getDepartureTrips() {
        return departureTrips;
    }

    public List<TrainTrip> getReturnTrips() {
        return returnTrips;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reservationId);
        parcel.writeString(reservationCode);
        parcel.writeString(expiryTimestamp);
        parcel.writeTypedList(departureTrips);
        parcel.writeTypedList(returnTrips);
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public void setExpiryTimestamp(String expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public void setDepartureTrips(List<TrainTrip> departureTrips) {
        this.departureTrips = departureTrips;
    }

    public void setReturnTrips(List<TrainTrip> returnTrips) {
        this.returnTrips = returnTrips;
    }

    public static TrainSoftbook dummy() {
        TrainSoftbook softbook = new TrainSoftbook();
        softbook.setReservationId("12345");
        softbook.setReservationCode("TKP12345");
        softbook.setExpiryTimestamp("2018-08-02T14:35:00Z");
        List<TrainTrip> trainTrips = new ArrayList<>();
        TrainTrip trainTrip = new TrainTrip();
        trainTrip.setAdultPrice(100000);
        trainTrip.setDisplayAdultPrice("Rp.100000");
        trainTrip.setTotalPriceAdult(100000);
        trainTrip.setDisplayTotalPriceAdult("Rp.100000");
        trainTrip.setBookBalance(1);
        trainTrip.setBookCode("QWERTY");
        trainTrip.setBookBalance(100000);
        trainTrip.setDes("BD");
        trainTrip.setOrg("GMR");
        trainTrip.setDiscount(1000);
        trainTrip.setExtraFee(1000);
        trainTrip.setNormalSales("100000");
        trainTrip.setSubclass("C");
        trainTrip.setDisplayNormalSales("Rp.1000000");
        trainTrip.setTrainNo("11G");
        trainTrip.setArrivalTimestamp("2018-08-20T19:35:00Z");
        trainTrip.setDepartureTimestamp("2018-08-20T17:35:00Z");
        List<TrainPaxPassenger> trainPaxPassengers = new ArrayList<>();
        TrainPaxPassenger trainPaxPassenger1 = new TrainPaxPassenger();
        trainPaxPassenger1.setIdNumber("1");
        trainPaxPassenger1.setName("John 1");
        trainPaxPassenger1.setPaxType(1);
        TrainSeat seat1 = new TrainSeat();
        seat1.setColumn("A");
        seat1.setRow("10");
        seat1.setWagonNo("EKO_AC-1");
        trainPaxPassenger1.setSeat(seat1);
        trainPaxPassengers.add(trainPaxPassenger1);
        TrainPaxPassenger trainPaxPassenger2 = new TrainPaxPassenger();
        trainPaxPassenger2.setIdNumber("ID2");
        trainPaxPassenger2.setName("John 2");
        trainPaxPassenger2.setPaxType(1);
        TrainSeat seat2 = new TrainSeat();
        seat2.setColumn("B");
        seat2.setRow("11");
        seat2.setWagonNo("EKO_AC-1");
        trainPaxPassenger2.setSeat(seat2);
        trainPaxPassengers.add(trainPaxPassenger2);
        TrainPaxPassenger trainPaxPassenger3 = new TrainPaxPassenger();
        trainPaxPassenger3.setIdNumber("ID3");
        trainPaxPassenger3.setName("John 3");
        trainPaxPassenger3.setPaxType(1);
        TrainSeat seat3 = new TrainSeat();
        seat3.setColumn("C");
        seat3.setRow("11");
        seat3.setWagonNo("EKO_AC-1");
        trainPaxPassenger3.setSeat(seat3);
        trainPaxPassengers.add(trainPaxPassenger3);
        TrainPaxPassenger trainPaxPassenger4 = new TrainPaxPassenger();
        trainPaxPassenger4.setIdNumber("ID4");
        trainPaxPassenger4.setName("John 4");
        trainPaxPassenger4.setPaxType(1);
        TrainSeat seat4 = new TrainSeat();
        seat4.setColumn("D");
        seat4.setRow("11");
        seat4.setWagonNo("EKO_AC-1");
        trainPaxPassenger4.setSeat(seat4);
        trainPaxPassengers.add(trainPaxPassenger4);
        trainTrip.setPaxPassengers(trainPaxPassengers);
        trainTrips.add(trainTrip);
        softbook.setDepartureTrips(trainTrips);
        softbook.setReturnTrips(trainTrips);
        return softbook;
    }

}
