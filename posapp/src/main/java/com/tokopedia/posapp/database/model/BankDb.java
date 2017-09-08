package com.tokopedia.posapp.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.posapp.database.PosDatabase;

import java.util.List;

/**
 * Created by okasurya on 9/8/17.
 */

@ModelContainer
@Table(database = PosDatabase.class)
public class BankDb extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    private int Id;

    @Column
    private int bankId;

    @Column
    private String bankName;

    List<InstallmentDb> installmentDbs;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "installmentDbs")
    public List<InstallmentDb> getInstallmentDbs() {
        if(installmentDbs == null || installmentDbs.isEmpty()){
            installmentDbs = SQLite.select()
                    .from(InstallmentDb.class)
                    .where(InstallmentDb_Table.bankDbContainer_Id.eq(bankId))
                    .queryList();
        }
        return installmentDbs;
    }
}
