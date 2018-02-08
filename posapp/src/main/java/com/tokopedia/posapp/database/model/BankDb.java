package com.tokopedia.posapp.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
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
@Table(database = PosDatabase.class, insertConflict = ConflictAction.REPLACE)
public class BankDb extends BaseModel {
    @Column
    @PrimaryKey
    private int bankId;

    @Column
    private String bankName;

    @Column
    private String bankLogo;

    @Column
    private Boolean allowInstallment;

    List<InstallmentDb> installmentDbs;

    List<BinDb> binDbs;

    List<BinInstallmentDb> binInstallmentDbs;

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

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public Boolean isAllowInstallment() {
        return allowInstallment;
    }

    public void setAllowInstallment(Boolean allowInstallment) {
        this.allowInstallment = allowInstallment;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "installmentDbs")
    public List<InstallmentDb> getInstallmentDbs() {
        if(installmentDbs == null || installmentDbs.isEmpty()){
            installmentDbs = SQLite.select()
                    .from(InstallmentDb.class)
                    .where(InstallmentDb_Table.bankDbContainer_bankId.eq(bankId))
                    .queryList();
        }
        return installmentDbs;
    }

    public void setInstallmentDbs(List<InstallmentDb> installmentDbs) {
        this.installmentDbs = installmentDbs;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "binDbs")
    public List<BinDb> getBinDbs() {
        if(binDbs == null || binDbs.isEmpty()){
            binDbs = SQLite.select()
                        .from(BinDb.class)
                        .where(BinDb_Table.bankDbContainer_bankId.eq(bankId))
                        .queryList();
        }
        return binDbs;
    }

    public void setBinDbs(List<BinDb> binDbs) {
        this.binDbs = binDbs;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "binInstallmentDbs")
    public List<BinInstallmentDb> getBinInstallmentDbs() {
        if(binInstallmentDbs == null || binInstallmentDbs.isEmpty()){
            binInstallmentDbs = SQLite.select()
                    .from(BinInstallmentDb.class)
                    .where(BinInstallmentDb_Table.bankDbContainer_bankId.eq(bankId))
                    .queryList();
        }
        return binInstallmentDbs;
    }

    public void setBinInstallmentDbs(List<BinInstallmentDb> binInstallmentDbs) {
        this.binInstallmentDbs = binInstallmentDbs;
    }
}
