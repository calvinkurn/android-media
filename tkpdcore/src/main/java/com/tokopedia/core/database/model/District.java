package com.tokopedia.core.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.tkpd.library.utils.data.model.ListDistricts;
import com.tokopedia.core.database.DatabaseConstant;
import com.tokopedia.core.database.DbFlowDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 2/2/16.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class District extends BaseModel implements DatabaseConstant, Convert<ListDistricts.Districts, District>,
        Parcelable
{
    public static final String DISTRICT_CITY_ID = "district_city_id";
    public static final String DISTRICT_ID = "district_id";
    public static final String DISTRICT_NAME = "district_name";
    public static final String DISTRICT_JNE_CODE = "district_jne_code";

    @Column
    @ForeignKey(saveForeignKeyModel = false, onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    ForeignKeyContainer<City> districtForeignKeyContainer;

    private City districtCity;

    public void associateCity(City city) {
        districtForeignKeyContainer =
                new ForeignKeyContainer<>(FlowManager.getContainerAdapter(City.class)
                        .toForeignKeyContainer(city));
    }

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String districtId;

    @Column
    public String districtName;

    @Column
    public String districtJneCode;

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    public City getDistrictCity() {
        if(districtForeignKeyContainer != null){
            return districtForeignKeyContainer.toModelForce();
        }else {
            return districtCity;
        }
    }

    public void setDistrictCity(City districtCity) {
        associateCity(districtCity);
        this.districtCity = districtCity;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictJneCode() {
        return districtJneCode;
    }

    public void setDistrictJneCode(String districtJneCode) {
        this.districtJneCode = districtJneCode;
    }

    public static District fromNetwork(ListDistricts.Districts districts){
        return new Select()
                .from(District.class)
                .where(District_Table.districtId.eq(districts.getDistrictId()))
                .querySingle();
    }

    public static List<District> toDbs(List<ListDistricts.Districts> districtsList){
        ArrayList<District> districts = new ArrayList<>();
        for(ListDistricts.Districts a : districtsList){
            District district = new District();
            districts.add(district.toDb(a));
        }
        return districts;
    }

    @Override
    public District toDb(ListDistricts.Districts districts) {
        District district = new District();
        district.setDistrictId(districts.getDistrictId());
        district.setDistrictJneCode(districts.getJneCode());
        district.setDistrictName(districts.getDistrictName());
        return district;
    }

    @Override
    public ListDistricts.Districts toNetwork(District district) {
        throw new RuntimeException("dont yet implemented");
    }

    protected District(Parcel in) {
        districtCity = in.readParcelable(City.class.getClassLoader());
        districtId = in.readString();
        districtName = in.readString();
        districtJneCode = in.readString();
        Id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(districtCity, flags);
        dest.writeString(districtId);
        dest.writeString(districtName);
        dest.writeString(districtJneCode);
        dest.writeLong(Id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override
        public District createFromParcel(Parcel in) {
            return new District(in);
        }

        @Override
        public District[] newArray(int size) {
            return new District[size];
        }
    };
}
