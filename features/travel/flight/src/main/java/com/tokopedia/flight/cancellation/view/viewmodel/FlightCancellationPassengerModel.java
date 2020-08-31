package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationPassengerModel implements Parcelable {

    private String passengerId;
    private int type;
    private int title;
    private String titleString;
    private String firstName;
    private String lastName;
    private String relationId;
    private List<String> relations;
    private int status;
    private String statusString;

    public FlightCancellationPassengerModel() {
    }

    protected FlightCancellationPassengerModel(Parcel in) {
        passengerId = in.readString();
        type = in.readInt();
        title = in.readInt();
        titleString = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        relationId = in.readString();
        relations = in.createStringArrayList();
        status = in.readInt();
        statusString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(passengerId);
        dest.writeInt(type);
        dest.writeInt(title);
        dest.writeString(titleString);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(relationId);
        dest.writeStringList(relations);
        dest.writeInt(status);
        dest.writeString(statusString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCancellationPassengerModel> CREATOR = new Creator<FlightCancellationPassengerModel>() {
        @Override
        public FlightCancellationPassengerModel createFromParcel(Parcel in) {
            return new FlightCancellationPassengerModel(in);
        }

        @Override
        public FlightCancellationPassengerModel[] newArray(int size) {
            return new FlightCancellationPassengerModel[size];
        }
    };

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public List<String> getRelations() {
        return relations;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEquals = false;

        if (obj instanceof FlightCancellationPassengerModel) {
            isEquals = this.getRelationId().equals(((FlightCancellationPassengerModel) obj).getRelationId()) &&
                    this.getPassengerId().equals(((FlightCancellationPassengerModel) obj).getPassengerId()) &&
                    this.getFirstName().equals(((FlightCancellationPassengerModel) obj).getFirstName()) &&
                    this.getLastName().equals(((FlightCancellationPassengerModel) obj).getLastName());
        }

        return isEquals;
    }
}
