package com.tokopedia.tkpd.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.tkpd.library.utils.data.model.ListCity;
import com.tokopedia.tkpd.database.DatabaseConstant;
import com.tokopedia.tkpd.database.DbFlowDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 2/2/16.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class City extends BaseModel implements DatabaseConstant, Convert<ListCity.Cities, City>{
    public static final String CITY_PROVINCE_ID = "city_province_id";
    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";

    List<District> districts;

    @Column
    @ForeignKey(saveForeignKeyModel = false, onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    ForeignKeyContainer<Province> provinceForeignKeyContainer;

    public void associateProvince(Province province) {
        provinceForeignKeyContainer =
                new ForeignKeyContainer<>(FlowManager.getContainerAdapter(Province.class)
                        .toForeignKeyContainer(province));
    }

    private Province province;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String cityId;

    @Column
    public String cityName;

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        associateProvince(province);
        this.province = province;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<District> getDistricts(){
        return getAllDistricts();
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "districts")
    public List<District> getAllDistricts() {
        if (districts == null || districts.isEmpty()) {
            districts = SQLite.select()
                    .from(District.class)
                    .where(District_Table.districtForeignKeyContainer_Id.eq(Id))
                    .queryList();
        }
        return districts;
    }

    public static List<City> toDbs(List<ListCity.Cities> citiesList){
        ArrayList<City> result = new ArrayList<>();
        for(ListCity.Cities a : citiesList){
            City temp = new City();
            result.add(temp.toDb(a));
        }
        return result;
    }

    public static City fromNetwork(String cityId){
        return new Select()
                .from(City.class)
                .where(City_Table.cityId.eq(cityId))
                .querySingle();
    }

    @Override
    public City toDb(ListCity.Cities cities) {
        City city = new City();
        city.setCityId(cities.getCity_id());
        city.setCityName(cities.getCity_name());
        return city;
    }

    @Override
    public ListCity.Cities toNetwork(City city) {
        throw new RuntimeException("not yet implemented");
    }

    @Override
    public String toString() {
        return "City{" +
                "province=" + province +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
