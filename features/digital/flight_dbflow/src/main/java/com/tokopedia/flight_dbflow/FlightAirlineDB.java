package com.tokopedia.flight_dbflow;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Rizky on 25/10/18.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightAirlineDB extends BaseModel implements Parcelable {

    @PrimaryKey
    @Column(name = "id")
    String id;
    @Column(name = "name")
    String fullname;
    @Column(name = "short_name")
    String shortName;
    @Column(name = "logo")
    String logo;
    @Column(name = "mandatory_dob")
    int mandatoryDob;
    @Column(name = "mandatory_refund_attachment")
    int mandatoryRefundAttachment;

    public FlightAirlineDB(String id, String fullName, String shortName, String logo, int mandatoryDob,
                           int mandatoryRefundAttachment) {
        this.id = id;
        this.fullname = fullName;
        this.shortName = shortName;
        this.logo = logo;
        this.mandatoryDob = mandatoryDob;
        this.mandatoryRefundAttachment = mandatoryRefundAttachment;
    }

    public FlightAirlineDB() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getMandatoryDob() {
        return mandatoryDob;
    }

    public void setMandatoryDob(int mandatoryDob) {
        this.mandatoryDob = mandatoryDob;
    }

    public int getMandatoryRefundAttachment() {
        return mandatoryRefundAttachment;
    }

    public void setMandatoryRefundAttachment(int mandatoryRefundAttachment) {
        this.mandatoryRefundAttachment = mandatoryRefundAttachment;
    }

    protected FlightAirlineDB(Parcel in) {
        id = in.readString();
        fullname = in.readString();
        shortName = in.readString();
        logo = in.readString();
        mandatoryDob = in.readInt();
        mandatoryRefundAttachment = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullname);
        dest.writeString(shortName);
        dest.writeString(logo);
        dest.writeInt(mandatoryDob);
        dest.writeInt(mandatoryRefundAttachment);
    }

    public String getName() {
        if (TextUtils.isEmpty(shortName)) {
            return fullname;
        } else return shortName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightAirlineDB> CREATOR = new Creator<FlightAirlineDB>() {
        @Override
        public FlightAirlineDB createFromParcel(Parcel in) {
            return new FlightAirlineDB(in);
        }

        @Override
        public FlightAirlineDB[] newArray(int size) {
            return new FlightAirlineDB[size];
        }
    };
}
