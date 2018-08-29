package com.tokopedia.posapp.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.model.container.BankDbContainer;

/**
 * Created by okasurya on 9/19/17.
 */

@ModelContainer
@Table(database = PosDatabase.class, insertConflict = ConflictAction.REPLACE)
public class BinInstallmentDb extends BaseModel {
    @PrimaryKey
    @Column
    private String bin;

    @Column
    private int bankId;

    @Column
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.CASCADE)
    BankDbContainer bankDbContainer;

    private BankDb bankDb;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
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
