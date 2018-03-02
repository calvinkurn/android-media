package com.tokopedia.posapp.bank.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.database.model.BankDb;
import com.tokopedia.posapp.database.model.BankDb_Table;
import com.tokopedia.posapp.database.model.BinDb;
import com.tokopedia.posapp.database.model.BinDb_Table;
import com.tokopedia.posapp.database.model.BinInstallmentDb;
import com.tokopedia.posapp.database.model.BinInstallmentDb_Table;
import com.tokopedia.posapp.database.model.InstallmentDb;
import com.tokopedia.posapp.database.model.InstallmentDb_Table;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.InstallmentDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/19/17.
 */
public class BankDbManager extends PosDbOperation<BankDomain, BankDb> {
    @Override
    protected BankDb mapToDb(BankDomain data) {
        if(data != null) {
            BankDb bankDb = new BankDb();
            bankDb.setBankId(data.getBankId());
            bankDb.setBankName(data.getBankName());
            bankDb.setBankLogo(data.getBankLogo());
            bankDb.setAllowInstallment(data.getAllowInstallment());
            return bankDb;
        }
        return null;
    }

    @Override
    protected List<BankDb> mapToDb(List<BankDomain> data) {
        List<BankDb> bankDbs = new ArrayList<>();
        for(BankDomain bankDomain : data) {
            BankDb bankDb = mapToDb(bankDomain);
            if(bankDb != null) bankDbs.add(bankDb);
        }
        return bankDbs;
    }

    @Override
    protected BankDomain mapToDomain(BankDb db) {
        if(db != null) {
            BankDomain bankDomain = new BankDomain();
            bankDomain.setBankId(db.getBankId());
            bankDomain.setBankName(db.getBankName());
            bankDomain.setBankLogo(db.getBankLogo());
            bankDomain.setAllowInstallment(db.isAllowInstallment());
            bankDomain.setInstallmentList(getInstallmentDomain(db.getInstallmentDbs()));
            bankDomain.setBin(getBinDomain(db.getBinDbs()));
            bankDomain.setBinInstallment(getBInInstallmentDomain(db.getBinInstallmentDbs()));
            return bankDomain;
        }

        return null;
    }

    @Override
    protected List<BankDomain> mapToDomain(List<BankDb> db) {
        List<BankDomain> bankDomains = new ArrayList<>();
        for(BankDb bankDb : db) {
            BankDomain bankDomain = mapToDomain(bankDb);
            if(bankDb != null) bankDomains.add(bankDomain);
        }
        return bankDomains;
    }

    @Override
    protected Class<BankDb> getDbClass() {
        return BankDb.class;
    }

    @Override
    public Observable<DataStatus> delete(BankDomain domain) {
        return executeDelete(getDbClass(), ConditionGroup.clause().and(BankDb_Table.bankId.eq(domain.getBankId())));
    }

    @Override
    public Observable<DataStatus> store(List<BankDomain> domains) {
        return Observable.just(domains)
                .flatMap(new Func1<List<BankDomain>, Observable<DataStatus>>() {
                    @Override
                    public Observable<DataStatus> call(final List<BankDomain> datas) {
                        return Observable.create(new Observable.OnSubscribe<DataStatus>() {
                            @Override
                            public void call(Subscriber<? super DataStatus> subscriber) {
                                getDatabase().beginTransactionAsync(new ITransaction() {
                                    @Override
                                    public void execute(DatabaseWrapper databaseWrapper) {
                                        for(BankDomain data : datas) {
                                            if(data != null) {
                                                BankDb db = mapToDb(data);
                                                db.save();

                                                saveInstallmentDb(data.getInstallmentList(), db);
                                                saveBinDb(data.getBin(), db);
                                                saveBinInstallment(data.getBinInstallment(), db);
                                            }
                                        }
                                    }
                                })
                                .success(defaultSuccessListener(subscriber))
                                .error(defaultErrorListener(subscriber))
                                .build().execute();
                            }
                        });
                    }
                });
    }

    private List<InstallmentDomain> getInstallmentDomain(List<InstallmentDb> installmentDbs) {
        List<InstallmentDomain> domains = new ArrayList<>();
        for(InstallmentDb db: installmentDbs) {
            InstallmentDomain domain = new InstallmentDomain();
            domain.setTerm(db.getTerm());
            domain.setFeeValue(db.getFeeValue());
            domain.setFeeType(db.getFeeType());
            domain.setInterest(db.getInterest());
            domain.setMinimumAmount(db.getMinimumAmount());
            domains.add(domain);
        }
        return domains;
    }

    private List<String> getBinDomain(List<BinDb> binDbs) {
        List<String> bins = new ArrayList<>();
        for(BinDb db: binDbs) {
            bins.add(db.getBin());
        }
        return bins;
    }

    private List<String> getBInInstallmentDomain(List<BinInstallmentDb> binInstallmentDbs) {
        List<String> bins = new ArrayList<>();
        for(BinInstallmentDb db: binInstallmentDbs) {
            bins.add(db.getBin());
        }
        return bins;
    }

    private void saveInstallmentDb(List<InstallmentDomain> installmentList, BankDb bankDb) {
        Delete.table(InstallmentDb.class, InstallmentDb_Table.bankId.eq(bankDb.getBankId()));
        for(InstallmentDomain installment : installmentList) {
            getInstallmentDb(installment, bankDb).save();
        }
    }

    private InstallmentDb getInstallmentDb(InstallmentDomain installment, BankDb bankDb) {
        InstallmentDb installmentDb = new InstallmentDb();
        installmentDb.setBankDb(bankDb);
        installmentDb.setBankId(bankDb.getBankId());
        installmentDb.setTerm(installment.getTerm());
        installmentDb.setFeeValue(installment.getFeeValue());
        installmentDb.setFeeType(installment.getFeeType());
        installmentDb.setInterest(installment.getInterest());
        installmentDb.setMinimumAmount(installment.getMinimumAmount());
        return installmentDb;
    }

    private void saveBinDb(List<String> bins, BankDb bankDb) {
        Delete.table(BinDb.class, BinDb_Table.bankId.eq(bankDb.getBankId()));
        for(String bin : bins) {
            getBinDb(bin, bankDb).save();
        }
    }

    private BinDb getBinDb(String bin, BankDb bankDb) {
        BinDb binDb = new BinDb();
        binDb.setBin(bin);
        binDb.setBankId(bankDb.getBankId());
        binDb.setBankDb(bankDb);
        return binDb;
    }

    private void saveBinInstallment(List<String> binInstallments, BankDb bankDb) {
        Delete.table(BinInstallmentDb.class, BinInstallmentDb_Table.bankId.eq(bankDb.getBankId()));
        for(String binInstallment: binInstallments) {
            getBinInstallmentDb(binInstallment, bankDb).save();
        }
    }

    private BinInstallmentDb getBinInstallmentDb(String binInstallment, BankDb bankDb) {
        BinInstallmentDb binInstallmentDb = new BinInstallmentDb();
        binInstallmentDb.setBin(binInstallment);
        binInstallmentDb.setBankId(bankDb.getBankId());
        binInstallmentDb.setBankDb(bankDb);
        return binInstallmentDb;
    }
}
