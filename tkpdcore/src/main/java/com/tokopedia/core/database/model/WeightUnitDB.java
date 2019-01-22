package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DatabaseConstant;

/**
 * Created by m.normansyah on 12/27/15.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class WeightUnitDB extends BaseModel implements DatabaseConstant{
    public static final String BERAT_UNIT_DESKRIPSI = "berat_unit_deskripsi";
    public static final String BERAT_UNIT_SINGKATAN = "berat_unit_singkatan";
    public static final String BERAT_UNIT_WS_INPUT = "berat_unit_ws_input";


    public WeightUnitDB(String descWeight, String abrvWeight, int wsInput) {
        super();
        this.descWeight = descWeight;
        this.abrvWeight = abrvWeight;
        this.wsInput = wsInput;
    }

    @Deprecated
    public WeightUnitDB(String descWeight, String abrvWeight) {
        super();
        this.descWeight = descWeight;
        this.abrvWeight = abrvWeight;
    }

    public WeightUnitDB(){super();}

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Column
    public String descWeight;
    @Column
    public String abrvWeight;
    @Column
    public int wsInput;

    public String getDescWeight() {
        return descWeight;
    }

    public void setDescWeight(String descWeight) {
        this.descWeight = descWeight;
    }

    public String getAbrvWeight() {
        return abrvWeight;
    }

    public void setAbrvWeight(String abrvWeight) {
        this.abrvWeight = abrvWeight;
    }

    public int getWsInput() {
        return wsInput;
    }

    public void setWsInput(int wsInput) {
        this.wsInput = wsInput;
    }

    @Override
    public long getId() {
        return Id;
    }
}
