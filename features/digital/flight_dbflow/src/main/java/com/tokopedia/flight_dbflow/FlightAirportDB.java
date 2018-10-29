package com.tokopedia.flight_dbflow;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Collate;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Rizky on 25/10/18.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightAirportDB extends BaseModel implements Parcelable {

    @PrimaryKey
    @Column(name = "country_id")
    private String countryId;

    @PrimaryKey
    @Column(name = "city_id")
    private String cityId;

    @PrimaryKey
    @Column(name = "airport_id")
    private String airportId;

    @Column(name = "country_name", collate = Collate.NOCASE)
    private String countryName;

    @Column(name = "phone_code")
    private long phoneCode;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "airport_name")
    private String airportName;

    @Column(name = "aliases")
    private String aliases;

    @Column(name = "airport_ids")
    private String airportIds;

    public FlightAirportDB(String countryId, String cityId, String airportId, String countryName,
                           long phoneCode, String cityName, String cityCode, String airportName,
                           String aliases, String airportIds) {
        this.countryId = countryId;
        this.cityId = cityId;
        this.airportId = airportId;
        this.countryName = countryName;
        this.phoneCode = phoneCode;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.airportName = airportName;
        this.aliases = aliases;
        this.airportIds = airportIds;
    }

    public FlightAirportDB() {}

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setPhoneCode(long phoneCode) {
        this.phoneCode = phoneCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public void setAirportIds(String airportIds) {
        this.airportIds = airportIds;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getAirportId() {
        return airportId;
    }

    public String getCountryName() {
        return countryName;
    }

    public long getPhoneCode() {
        return phoneCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAliases() {
        return aliases;
    }

    public String getAirportIds() {
        return airportIds;
    }

    protected FlightAirportDB(Parcel in) {
        countryId = in.readString();
        cityId = in.readString();
        airportId = in.readString();
        countryName = in.readString();
        phoneCode = in.readLong();
        cityName = in.readString();
        cityCode = in.readString();
        airportName = in.readString();
        aliases = in.readString();
        airportIds = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countryId);
        dest.writeString(cityId);
        dest.writeString(airportId);
        dest.writeString(countryName);
        dest.writeLong(phoneCode);
        dest.writeString(cityName);
        dest.writeString(cityCode);
        dest.writeString(airportName);
        dest.writeString(aliases);
        dest.writeString(airportIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightAirportDB> CREATOR = new Creator<FlightAirportDB>() {
        @Override
        public FlightAirportDB createFromParcel(Parcel in) {
            return new FlightAirportDB(in);
        }

        @Override
        public FlightAirportDB[] newArray(int size) {
            return new FlightAirportDB[size];
        }
    };

}
