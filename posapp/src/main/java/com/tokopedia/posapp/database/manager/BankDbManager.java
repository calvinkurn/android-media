package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.model.BankDb;
import com.tokopedia.posapp.database.model.InstallmentDb;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.bank.InstallmentDomain;

import java.util.List;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankDbManager implements DbManagerOperation<BankDomain,BankDb> {
    @Override
    public void store(BankDomain bankDomain, TransactionListener callback) {
        BankDb bankDb = convertBankDb(bankDomain);
        bankDb.save();
    }

    @Override
    public void store(final List<BankDomain> bankDomainList, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(PosDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for(BankDomain bankDomain : bankDomainList) {
                    BankDb bankDb = convertBankDb(bankDomain);
                    bankDb.save();

                    for(InstallmentDomain installment : bankDomain.getInstallmentList()) {
                        InstallmentDb installmentDb = new InstallmentDb();
                        installmentDb.setBankDb(bankDb);
                        installmentDb.setTerm(installment.getTerm());
                        installmentDb.setFeeValue(installment.getFeeValue());
                        installmentDb.setFeeType(installment.getFeeType());
                        installmentDb.setInterest(installment.getInterest());
                        installmentDb.setMinimumAmount(installment.getMinimumAmount());

                        installmentDb.save();
                    }

                }
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(Transaction transaction) {
                callback.onTransactionSuccess();
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                callback.onError(error);
            }
        }).build();

        transaction.execute();
    }

    @Override
    public void update(BankDomain data, TransactionListener callback) {

    }

    @Override
    public void delete(ConditionGroup conditions, TransactionListener callback) {

    }

    @Override
    public void deleteAll(TransactionListener callback) {

    }

    @Override
    public BankDb first(ConditionGroup conditions) {
        return null;
    }

    @Override
    public BankDb first() {
        return null;
    }

    @Override
    public List<BankDb> getListData(ConditionGroup conditions) {
        return null;
    }

    @Override
    public List<BankDb> getListData(int offset, int limit) {
        return null;
    }

    @Override
    public List<BankDb> getAllData() {
        return null;
    }

    @Override
    public boolean isTableEmpty() {
        return false;
    }

    private BankDb convertBankDb(BankDomain bankDomain) {
        BankDb bankDb = new BankDb();
        bankDb.setBankId(bankDomain.getBankId());
        bankDb.setBankName(bankDomain.getBankName());
        return bankDb;
    }
}
