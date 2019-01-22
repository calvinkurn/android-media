package com.tokopedia.flight.search.presentation.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/1/2017.
 */

public class FlightFilterModel implements Parcelable, Cloneable {

    public static final Creator<FlightFilterModel> CREATOR = new Creator<FlightFilterModel>() {
        @Override
        public FlightFilterModel createFromParcel(Parcel in) {
            return new FlightFilterModel(in);
        }

        @Override
        public FlightFilterModel[] newArray(int size) {
            return new FlightFilterModel[size];
        }
    };

    private int priceMin = Integer.MIN_VALUE;
    private int priceMax = Integer.MAX_VALUE;
    private int durationMin = Integer.MIN_VALUE;
    private int durationMax = Integer.MAX_VALUE;
    private List<TransitEnum> transitTypeList;
    private List<String> airlineList;
    private List<DepartureTimeEnum> departureTimeList;
    private List<RefundableEnum> refundableTypeList;
    private boolean isHasFilter = false;
    private boolean isSpecialPrice = false;
    private boolean isBestPairing = false;
    private boolean isReturn = false;
    private String journeyId = "";

    public FlightFilterModel() {
    }

    protected FlightFilterModel(Parcel in) {
        this.priceMin = in.readInt();
        this.priceMax = in.readInt();
        this.durationMin = in.readInt();
        this.durationMax = in.readInt();
        this.transitTypeList = new ArrayList<>();
        in.readList(this.transitTypeList, TransitEnum.class.getClassLoader());
        this.airlineList = in.createStringArrayList();
        this.departureTimeList = new ArrayList<>();
        in.readList(this.departureTimeList, DepartureTimeEnum.class.getClassLoader());
        this.refundableTypeList = new ArrayList<>();
        in.readList(this.refundableTypeList, RefundableEnum.class.getClassLoader());
        this.isHasFilter = in.readByte() != 0;
        this.isSpecialPrice = in.readByte() != 0;
        this.isBestPairing = in.readByte() != 0;
        this.isReturn = in.readByte() != 0;
        this.journeyId = in.readString();
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public int getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(int durationMax) {
        this.durationMax = durationMax;
    }

    public List<String> getAirlineList() {
        return airlineList;
    }

    public void setAirlineList(List<String> airlineList) {
        this.airlineList = airlineList;
    }

    public List<TransitEnum> getTransitTypeList() {
        return transitTypeList;
    }

    public void setTransitTypeList(List<TransitEnum> transitTypeList) {
        this.transitTypeList = transitTypeList;
    }

    public List<DepartureTimeEnum> getDepartureTimeList() {
        return departureTimeList;
    }

    public void setDepartureTimeList(List<DepartureTimeEnum> departureTimeList) {
        this.departureTimeList = departureTimeList;
    }

    public List<RefundableEnum> getRefundableTypeList() {
        return refundableTypeList;
    }

    public void setRefundableTypeList(List<RefundableEnum> refundableTypeList) {
        this.refundableTypeList = refundableTypeList;
    }

    public boolean isBestPairing() {
        return isBestPairing;
    }

    public void setBestPairing(boolean bestPairing) {
        isBestPairing = bestPairing;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public boolean hasFilter() {
        return isHasFilter;
    }

    public FlightFilterModel copy() {
        FlightFilterModel flightFilterModel = new FlightFilterModel();
        flightFilterModel.setPriceMin(getPriceMin());
        flightFilterModel.setPriceMax(getPriceMax());
        flightFilterModel.setDurationMin(getDurationMin());
        flightFilterModel.setDurationMax(getDurationMax());
        flightFilterModel.setTransitTypeList(getCopyOfTransitList());
        flightFilterModel.setAirlineList(getCopyOfAirlineList());
        flightFilterModel.setDepartureTimeList(getCopyOfDepartureList());
        flightFilterModel.setRefundableTypeList(getCopyOfRefundableList());
        flightFilterModel.setSpecialPrice(isSpecialPrice());
        flightFilterModel.setBestPairing(isBestPairing());
        flightFilterModel.setReturn(isReturn());
        flightFilterModel.setJourneyId(getJourneyId());
        return flightFilterModel;
    }

    private List<TransitEnum> getCopyOfTransitList(){
        List<TransitEnum> transitEnumList = new ArrayList<>();
        if (getTransitTypeList()!=null) {
            for (int i = 0, sizei = getTransitTypeList().size(); i < sizei; i++) {
                transitEnumList.add(getTransitTypeList().get(i));
            }
        }
        return transitEnumList;
    }

    private List<DepartureTimeEnum> getCopyOfDepartureList(){
        List<DepartureTimeEnum> departureTimeEnumList = new ArrayList<>();
        if (getDepartureTimeList()!= null) {
            for (int i = 0, sizei = getDepartureTimeList().size(); i < sizei; i++) {
                departureTimeEnumList.add(getDepartureTimeList().get(i));
            }
        }
        return departureTimeEnumList;
    }

    private List<String> getCopyOfAirlineList(){
        List<String> airlineList = new ArrayList<>();
        if (getAirlineList()!= null) {
            for (int i = 0, sizei = getAirlineList().size(); i < sizei; i++) {
                airlineList.add(getAirlineList().get(i));
            }
        }
        return airlineList;
    }

    private List<RefundableEnum> getCopyOfRefundableList(){
        List<RefundableEnum> refundableEnumList = new ArrayList<>();
        if (getRefundableTypeList()!=null) {
            for (int i = 0, sizei = getRefundableTypeList().size(); i < sizei; i++) {
                refundableEnumList.add(getRefundableTypeList().get(i));
            }
        }
        return refundableEnumList;
    }

    public void setHasFilter(FlightSearchStatisticModel flightSearchStatisticModel) {
        int priceMinStat;
        int priceMaxStat;
        int durMinStat;
        int durMaxStat;

        if (flightSearchStatisticModel == null) {
            priceMinStat = Integer.MIN_VALUE;
            priceMaxStat = Integer.MAX_VALUE;
            durMinStat = Integer.MIN_VALUE;
            durMaxStat = Integer.MAX_VALUE;
        } else {
            priceMinStat = flightSearchStatisticModel.getMinPrice();
            priceMaxStat = flightSearchStatisticModel.getMaxPrice();
            durMinStat = flightSearchStatisticModel.getMinDuration();
            durMaxStat = flightSearchStatisticModel.getMaxDuration();
        }
        isHasFilter = (this.priceMin > priceMinStat || this.priceMax < priceMaxStat ||
                this.durationMin > durMinStat || this.durationMax < durMaxStat ||
                (this.transitTypeList != null && this.transitTypeList.size() > 0) ||
                (this.airlineList != null && this.airlineList.size() > 0) ||
                (this.departureTimeList != null && this.departureTimeList.size() > 0) ||
                (this.refundableTypeList != null && this.refundableTypeList.size() > 0));
    }

    public boolean isSpecialPrice() {
        return isSpecialPrice;
    }

    public void setSpecialPrice(boolean specialPrice) {
        isSpecialPrice = specialPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.priceMin);
        dest.writeInt(this.priceMax);
        dest.writeInt(this.durationMin);
        dest.writeInt(this.durationMax);
        dest.writeList(this.transitTypeList);
        dest.writeStringList(this.airlineList);
        dest.writeList(this.departureTimeList);
        dest.writeList(this.refundableTypeList);
        dest.writeByte((byte) (isHasFilter ? 1 : 0));
        dest.writeByte((byte) (isSpecialPrice ? 1 : 0));
        dest.writeByte((byte) (isBestPairing ? 1 : 0));
        dest.writeByte((byte) (isReturn ? 1 : 0));
        dest.writeString(this.journeyId);
    }

    public void setHasFilter(boolean hasFilter) {
        this.isHasFilter = hasFilter;
    }
}
