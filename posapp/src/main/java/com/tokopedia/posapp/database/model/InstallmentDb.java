package com.tokopedia.posapp.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.model.container.BankDbContainer;

import static com.tokopedia.core.database.DatabaseConstant.ID;

/**
 * Created by okasurya on 9/8/17.
 */

@ModelContainer
@Table(database = PosDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class InstallmentDb extends BaseModel {
    @Column
    @PrimaryKey
    private int term;

    @Column
    @PrimaryKey
    private int bankId;

    @Column
    private double feeValue;

    @Column
    private String feeType;

    @Column
    private double interest;

    @Column
    private double minimumAmount;

    @Column
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.CASCADE)
    BankDbContainer bankDbContainer;

    private BankDb bankDb;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public double getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(double feeValue) {
        this.feeValue = feeValue;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public BankDb getBankDb() {
        if (bankDbContainer != null) return bankDbContainer.toModel();
        return bankDb;
    }

    public void setBankDb(BankDb bankDb) {
        bankDbContainer = createBankDbContainer(bankDb);
        this.bankDb = bankDb;
    }

    private BankDbContainer createBankDbContainer(BankDb bankDb) {
        return new BankDbContainer(
                FlowManager.getContainerAdapter(BankDb.class).toForeignKeyContainer(bankDb)
        );
    }
}
