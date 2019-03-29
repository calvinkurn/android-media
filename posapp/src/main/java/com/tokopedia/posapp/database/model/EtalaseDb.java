package com.tokopedia.posapp.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.posapp.database.PosDatabase;

/**
 * Created by okasurya on 9/18/17.
 */

@ModelContainer
@Table(database = PosDatabase.class, insertConflict = ConflictAction.REPLACE)
public class EtalaseDb extends BaseModel {
    @Column
    @PrimaryKey
    private String etalaseId;

    @Column
    private String etalaseName;

    @Column
    private String etalaseAlias;

    @Column
    private int useAce;

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public String getEtalaseAlias() {
        return etalaseAlias;
    }

    public void setEtalaseAlias(String etalaseAlias) {
        this.etalaseAlias = etalaseAlias;
    }

    public int getUseAce() {
        return useAce;
    }

    public void setUseAce(int useAce) {
        this.useAce = useAce;
    }
}
