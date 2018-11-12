package com.tokopedia.common.travel.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TravelPassenger implements Parcelable {

    private String idPassenger;
    private String id;
    private int userId;
    private int title;
    private int paxType;
    private String name;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String nationality;
    private String passportNo;
    private String passportCountry;
    private String passportExpiry;
    private String idNumber;
    private String headerTitle;
    private int travelId;
    private int isBuyer;
    private String salutationTitle;
    private boolean isSelected;
    private int idLocal;

    public TravelPassenger() {
    }


    protected TravelPassenger(Parcel in) {
        idPassenger = in.readString();
        id = in.readString();
        userId = in.readInt();
        title = in.readInt();
        paxType = in.readInt();
        name = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        birthDate = in.readString();
        nationality = in.readString();
        passportNo = in.readString();
        passportCountry = in.readString();
        passportExpiry = in.readString();
        idNumber = in.readString();
        headerTitle = in.readString();
        travelId = in.readInt();
        isBuyer = in.readInt();
        salutationTitle = in.readString();
        isSelected = in.readByte() != 0;
        idLocal = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPassenger);
        dest.writeString(id);
        dest.writeInt(userId);
        dest.writeInt(title);
        dest.writeInt(paxType);
        dest.writeString(name);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(birthDate);
        dest.writeString(nationality);
        dest.writeString(passportNo);
        dest.writeString(passportCountry);
        dest.writeString(passportExpiry);
        dest.writeString(idNumber);
        dest.writeString(headerTitle);
        dest.writeInt(travelId);
        dest.writeInt(isBuyer);
        dest.writeString(salutationTitle);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(idLocal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TravelPassenger> CREATOR = new Creator<TravelPassenger>() {
        @Override
        public TravelPassenger createFromParcel(Parcel in) {
            return new TravelPassenger(in);
        }

        @Override
        public TravelPassenger[] newArray(int size) {
            return new TravelPassenger[size];
        }
    };

    public String getIdPassenger() {
        return idPassenger;
    }

    public void setIdPassenger(String idPassenger) {
        this.idPassenger = idPassenger;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPaxType() {
        return paxType;
    }

    public void setPaxType(int paxType) {
        this.paxType = paxType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getSalutationTitle() {
        return salutationTitle;
    }

    public void setSalutationTitle(String salutationTitle) {
        this.salutationTitle = salutationTitle;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportCountry() {
        return passportCountry;
    }

    public void setPassportCountry(String passportCountry) {
        this.passportCountry = passportCountry;
    }

    public String getPassportExpiry() {
        return passportExpiry;
    }

    public void setPassportExpiry(String passportExpiry) {
        this.passportExpiry = passportExpiry;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public int getTravelId() {
        return travelId;
    }

    public void setTravelId(int travelId) {
        this.travelId = travelId;
    }

    public int isBuyer() {
        return isBuyer;
    }

    public void setBuyer(int buyer) {
        isBuyer = buyer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TravelPassenger && ((TravelPassenger) obj).getName().equals(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= prime * idLocal;
        return result;
    }
}
