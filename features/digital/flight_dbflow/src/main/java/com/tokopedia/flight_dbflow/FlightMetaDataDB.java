package com.tokopedia.flight_dbflow;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Rizky on 25/10/18.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightMetaDataDB extends BaseModel implements Parcelable {

    @PrimaryKey
    @Column(name = "departure_airport")
    private String departureAirport;

    @PrimaryKey
    @Column(name = "arrival_airport")
    private String arrivalAirport;

    @PrimaryKey
    @Column(name = "date")
    private String date;

    @Column(name = "need_refresh")
    private boolean isNeedRefresh;

    @Column(name = "refresh_time")
    private int refreshTime;

    @Column(name = "max_retry")
    private int maxRetry;

    @Column(name = "retry_no")
    private int retryNo;

    @Column(name = "lastPulled")
    private long lastPulled;

    public FlightMetaDataDB(String departureAirport, String arrivalAirport, String date,
                            boolean isNeedRefresh, int refreshTime, int maxRetry, int retryNo,
                            long lastPulled) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.date = date;
        this.isNeedRefresh = isNeedRefresh;
        this.refreshTime = refreshTime;
        this.maxRetry = maxRetry;
        this.retryNo = retryNo;
        this.lastPulled = lastPulled;
    }

    public FlightMetaDataDB() {}

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isNeedRefresh() {
        return isNeedRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        isNeedRefresh = needRefresh;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public int getRetryNo() {
        return retryNo;
    }

    public void setRetryNo(int retryNo) {
        this.retryNo = retryNo;
    }

    public long getLastPulled() {
        return lastPulled;
    }

    public void setLastPulled(long lastPulled) {
        this.lastPulled = lastPulled;
    }

    protected FlightMetaDataDB(Parcel in) {
        departureAirport = in.readString();
        arrivalAirport = in.readString();
        date = in.readString();
        isNeedRefresh = in.readByte() != 0;
        refreshTime = in.readInt();
        maxRetry = in.readInt();
        retryNo = in.readInt();
        lastPulled = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(departureAirport);
        dest.writeString(arrivalAirport);
        dest.writeString(date);
        dest.writeByte((byte) (isNeedRefresh ? 1 : 0));
        dest.writeInt(refreshTime);
        dest.writeInt(maxRetry);
        dest.writeInt(retryNo);
        dest.writeLong(lastPulled);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightMetaDataDB> CREATOR = new Creator<FlightMetaDataDB>() {
        @Override
        public FlightMetaDataDB createFromParcel(Parcel in) {
            return new FlightMetaDataDB(in);
        }

        @Override
        public FlightMetaDataDB[] newArray(int size) {
            return new FlightMetaDataDB[size];
        }
    };

}
