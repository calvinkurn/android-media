package com.tokopedia.core.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tkpd.library.utils.data.model.ListProvince;
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
public class Province extends BaseModel implements DatabaseConstant, Convert<ListProvince.Province, Province>, Parcelable{

    public static final String PROVINCE_ID = "province_id";
    public static final String PROVINCE_NAME = "province_name";
    public static final String EXPIRED_TIME = "expiredTime";

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String provinceId;

    @Column
    public String provinceName;

    @Column
    public long expiredTime = 0;

    List<City> cities;



    protected Province(Parcel in) {
        Id = in.readLong();
        provinceId = in.readString();
        provinceName = in.readString();
        expiredTime = in.readLong();
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public static List<Province> toDbs(java.util.List<ListProvince.Province> provinceList){
        ArrayList<Province> result = new ArrayList<>();
        for(ListProvince.Province a : provinceList){
            Province province = new Province();
            result.add(province.toDb(a));
        }
        return result;
    }

    public List<City> getCities(){
        return getAllCities();
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "cities")
    public List<City> getAllCities() {
        if (cities == null || cities.isEmpty()) {
            cities = SQLite.select()
                    .from(City.class)
                    .where(City_Table.provinceForeignKeyContainer_Id.eq(Id))
                    .queryList();
        }
        return cities;
    }

    public static Province fromNetwork(ListProvince.Province province){
        return new Select()
                .from(Province.class)
                .where(Province_Table.provinceId.eq(province.getProvinceId()))
                .querySingle();
    }

    @Override
    public Province toDb(ListProvince.Province province) {
        Province result = new Province();
        result.setProvinceId(province.getProvinceId());
        result.setProvinceName(province.getProvinceName());
        return result;
    }

    @Override
    public ListProvince.Province toNetwork(Province province) {
        throw new RuntimeException("dont use this or implemented by yourself");
    }

    @Override
    public String toString() {
        return "Province{" +
                "provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", expiredTime=" + expiredTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeString(provinceId);
        dest.writeString(provinceName);
        dest.writeLong(expiredTime);
    }
}
