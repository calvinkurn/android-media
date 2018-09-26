package com.tokopedia.bankdb;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by noiz354 on 2/3/16.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = BankFlowDatabase.class)
public class Bank extends BaseModel{

    private static final String ID = "Id";
    public static final String BANK_ID = "bank_id";
    private static final String BANK_NAME = "bank_name";
    private static final String BANK_CLEARING_CODE = "bank_clearing_code";
    private static final String BANK_ABBREVATION = "bank_abbrevation";

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String bankId;

    @Column(name = BANK_NAME)
    public String bankName;

    @Column(name = BANK_CLEARING_CODE)
    public String bankClearingCode;

    @Column(name = BANK_ABBREVATION)
    public String bankAbbrevation;

    @Column
    public int bankPage;

    public int getBankPage() {
        return bankPage;
    }

    public void setBankPage(int bankPage) {
        this.bankPage = bankPage;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankClearingCode() {
        return bankClearingCode;
    }

    public void setBankClearingCode(String bankClearingCode) {
        this.bankClearingCode = bankClearingCode;
    }

    public String getBankAbbrevation() {
        return bankAbbrevation;
    }

    public void setBankAbbrevation(String bankAbbrevation) {
        this.bankAbbrevation = bankAbbrevation;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankId='" + bankId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankClearingCode='" + bankClearingCode + '\'' +
                ", bankAbbrevation='" + bankAbbrevation + '\'' +
                ", bankPage=" + bankPage +
                '}';
    }
}
