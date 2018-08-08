package com.tokopedia.events.view.customview;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by naveengoyal on 1/17/18.
 */

public class SeatLayoutInfo implements Parcelable{

    private String movieName;

    private String theatreName;

    private String theatreClass;

    private String showTiming;

    private String url;

    private Integer ticketPrice;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getTheatreClass() {
        return theatreClass;
    }

    public void setTheatreClass(String theatreClass) {
        this.theatreClass = theatreClass;
    }

    public String getShowTiming() {
        return showTiming;
    }

    public void setShowTiming(String showTiming) {
        this.showTiming = showTiming;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public SeatLayoutInfo() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieName);
        dest.writeString(this.theatreName);
        dest.writeString(this.theatreClass);
        dest.writeString(this.showTiming);
        dest.writeString(this.url);
        dest.writeValue(this.ticketPrice);
    }

    protected SeatLayoutInfo(Parcel in) {
        this.movieName = in.readString();
        this.theatreName = in.readString();
        this.theatreClass = in.readString();
        this.showTiming = in.readString();
        this.url = in.readString();
        this.ticketPrice = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SeatLayoutInfo> CREATOR = new Creator<SeatLayoutInfo>() {
        @Override
        public SeatLayoutInfo createFromParcel(Parcel source) {
            return new SeatLayoutInfo(source);
        }

        @Override
        public SeatLayoutInfo[] newArray(int size) {
            return new SeatLayoutInfo[size];
        }
    };
}
