package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationPassengerViewModel implements Parcelable{

    private String passengerId;
    private int type;
    private int title;
    private String titleString;
    private String firstName;
    private String lastName;
    private String relationId;
    private List<String> relations;

    public FlightCancellationPassengerViewModel() {
    }

    protected FlightCancellationPassengerViewModel(Parcel in) {
        passengerId = in.readString();
        type = in.readInt();
        title = in.readInt();
        titleString = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        relationId = in.readString();
        relations = in.createStringArrayList();
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCancellationPassengerViewModel> CREATOR = new Creator<FlightCancellationPassengerViewModel>() {
        @Override
        public FlightCancellationPassengerViewModel createFromParcel(Parcel in) {
            return new FlightCancellationPassengerViewModel(in);
        }

        @Override
        public FlightCancellationPassengerViewModel[] newArray(int size) {
            return new FlightCancellationPassengerViewModel[size];
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

    @Override
    public boolean equals(Object obj) {
        boolean isEquals = false;

        if (obj != null && obj instanceof FlightCancellationPassengerViewModel) {
            isEquals = this.getRelationId().equals(((FlightCancellationPassengerViewModel)obj).getRelationId()) &&
                    this.getPassengerId().equals(((FlightCancellationPassengerViewModel)obj).getPassengerId()) &&
                    this.getFirstName().equals(((FlightCancellationPassengerViewModel)obj).getFirstName()) &&
                    this.getLastName().equals(((FlightCancellationPassengerViewModel)obj).getLastName());
        }

        return isEquals;
    }
}
