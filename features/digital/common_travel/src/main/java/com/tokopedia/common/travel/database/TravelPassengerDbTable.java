package com.tokopedia.common.travel.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by nabillasabbaha on 15/08/18.
 */
@Table(database = TkpdTravelDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class TravelPassengerDbTable extends BaseModel {
    @PrimaryKey
    @Column(name = "id")
    private
    int id;
    @Column(name = "userId")
    private
    int userId;
    @Column(name = "idNumber")
    private
    String idNumber;
    @Column(name = "namePassenger")
    private
    String namePassenger;
    @Column(name = "salutationId")
    private
    int salutationId;
    @Column(name = "birthDate")
    private
    String birthDate;
    @Column(name = "phoneNumber")
    private
    String phoneNumber;
    @Column(name = "isBuyer")
    private
    int isBuyer;
    @Column(name = "paxType")
    private
    int paxType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getNamePassenger() {
        return namePassenger;
    }

    public void setNamePassenger(String namePassenger) {
        this.namePassenger = namePassenger;
    }

    public int getSalutationId() {
        return salutationId;
    }

    public void setSalutationId(int salutationId) {
        this.salutationId = salutationId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsBuyer() {
        return isBuyer;
    }

    public void setIsBuyer(int isBuyer) {
        this.isBuyer = isBuyer;
    }

    public int getPaxType() {
        return paxType;
    }

    public void setPaxType(int paxType) {
        this.paxType = paxType;
    }
}
