package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tkpd.library.utils.data.model.ListBank;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DatabaseConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 2/3/16.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class Bank extends BaseModel implements DatabaseConstant, Convert<ListBank.Bank, Bank>{

    public static final String BANK_ID = "bank_id";
    public static final String BANK_NAME = "bank_name";
    public static final String BANK_CLEARING_CODE = "bank_clearing_code";
    public static final String BANK_ABBREVATION = "bank_abbrevation";

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

    public static Bank fromNetwork(ListBank.Bank bank){
        return new Select()
                .from(Bank.class)
                .where(Bank_Table.bankId.eq(bank.getBankId()))
                .querySingle();
    }

    public static List<Bank> toDbs(List<ListBank.Bank> banks){
        ArrayList<Bank> result = new ArrayList<>();
        for(ListBank.Bank b : banks){
            Bank bank = new Bank();
            result.add(bank.toDb(b));
        }
        return result;
    }

    @Override
    public Bank toDb(ListBank.Bank bank) {
        Bank result = new Bank();
        result.setBankId(bank.getBankId());
        result.setBankName(bank.getBankName());
        result.setBankAbbrevation(bank.getBankAbbreviation());
        result.setBankClearingCode(bank.getBankClearingCode());
        return result;
    }

    @Override
    public ListBank.Bank toNetwork(Bank bank) {
        return null;
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
