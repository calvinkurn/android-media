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
public class CurrencyDB extends BaseModel implements DatabaseConstant {

    public static final String MATA_UANG_SINGKATAN = "mata_uang_singkatan";
    public static final String MATA_UANG_DESKRIPSI = "mata_uang_deskripsi";
    public static final String MATA_UANG_WS_INPUT = "mata_uang_ws_input";

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    public CurrencyDB(){
        super();
    }

    public CurrencyDB(String abrvCurr, String descCurr, int wsInput) {
        super();
        this.abrvCurr = abrvCurr;
        this.descCurr = descCurr;
        this.wsInput = wsInput;
    }
    public String getAbrvCurr() {
        return abrvCurr;
    }

    public void setAbrvCurr(String abrvCurr) {
        this.abrvCurr = abrvCurr;
    }

    public String getDescCurr() {
        return descCurr;
    }

    public void setDescCurr(String descCurr) {
        this.descCurr = descCurr;
    }

    public int getWsInput() {
        return wsInput;
    }

    public void setWsInput(int wsInput) {
        this.wsInput = wsInput;
    }

    @Column
    public String abrvCurr;

    @Column
    public String descCurr;

    @Column
    public int wsInput;;

    @Override
    public long getId() {
        return Id;
    }
}
