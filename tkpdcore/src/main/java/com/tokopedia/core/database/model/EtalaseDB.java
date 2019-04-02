package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DatabaseConstant;

/**
 * Created by m.normansyah on 12/27/15.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class EtalaseDB extends BaseModel implements DatabaseConstant {
    public static final String ETALASE_ID = "etalase_id";
    public static final String ETALSE_NAME = "etalse_name";
    public static final String ETALASE_TOTAL = "etalase_total";

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    public EtalaseDB(){
        super();
    }

    /**
     *
     * @param etalaseId etalase id from server
     * @param etalaseName etalase name from server
     * @param etalaseTotal etalase total from server
     */
    public EtalaseDB(int etalaseId, String etalaseName, int etalaseTotal) {
        super();
        this.etalaseId = etalaseId;
        this.etalaseName = etalaseName;
        this.etalaseTotal = etalaseTotal;
    }

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public int etalaseId;

    @Column(name = ETALSE_NAME)
    public String etalaseName;

    @Column(name = ETALASE_TOTAL)
    public int etalaseTotal;

    public int getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(int etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public int getEtalaseTotal() {
        return etalaseTotal;
    }

    public void setEtalaseTotal(int etalaseTotal) {
        this.etalaseTotal = etalaseTotal;
    }

    @Override
    public String toString() {
        return "Etalase{" +
                "etalaseId=" + etalaseId +
                ", etalaseName='" + etalaseName + '\'' +
                ", etalaseTotal=" + etalaseTotal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EtalaseDB etalaseDB = (EtalaseDB) o;

        return etalaseName.equals(etalaseDB.etalaseName);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + etalaseName.hashCode();
        return result;
    }
}
