package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.base.domain.Interactor;
import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;
import com.tokopedia.posapp.data.pojo.BankItemResponse;
import com.tokopedia.posapp.data.pojo.BankListResponse;
import com.tokopedia.posapp.data.pojo.InstallmentResponse;
import com.tokopedia.posapp.database.model.BankDb;
import com.tokopedia.posapp.database.model.BankDb_Table;
import com.tokopedia.posapp.database.model.InstallmentDb;
import com.tokopedia.posapp.react.datasource.model.CacheResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/8/17.
 */

public class ReactBankCacheSource implements ReactCacheSource {
    private Gson gson;

    public ReactBankCacheSource() {
        gson = new Gson();
    }

    @Override
    public Observable<String> getData(String id) {
        try {
            int bankId = Integer.parseInt(id);
            BankDb bankDb = SQLite.select().from(BankDb.class)
                                .where(BankDb_Table.bankId.eq(bankId)).querySingle();
            BankItemResponse bankItemResponse = getBankItemResponse(bankDb);

            return Observable.just(bankItemResponse)
                    .map(new Func1<BankItemResponse, String>() {
                        @Override
                        public String call(BankItemResponse bankItemResponse) {
                            return gson.toJson(bankItemResponse);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getListData(int offset, int limit) {
        List<BankDb> bankDbList = SQLite.select().from(BankDb.class).offset(offset).limit(limit).queryList();
        return mapListData(bankDbList);
    }

    @Override
    public Observable<String> getAllData() {
        List<BankDb> bankDbList = SQLite.select().from(BankDb.class).queryList();

        return mapListData(bankDbList);
    }

    @Override
    public Observable<String> deleteAll() {
        return null;
    }

    @Override
    public Observable<String> deleteItem(String id) {
        return null;
    }

    @Override
    public Observable<String> update(String data) {
        return null;
    }

    private Observable<String> mapListData(List<BankDb> bankDbList) {
        BankListResponse bankListResponse = new BankListResponse();
        List<BankItemResponse> bankItemResponseList = new ArrayList<>();
        for(BankDb bankDb: bankDbList) {
            bankItemResponseList.add(getBankItemResponse(bankDb));
        }
        bankListResponse.setList(bankItemResponseList);

        CacheResult<BankListResponse> result = new CacheResult<>();
        result.setData(bankListResponse);

        return Observable.just(result)
                .map(new Func1<CacheResult, String>() {
                    @Override
                    public String call(CacheResult result) {
                        return gson.toJson(result);
                    }
                });
    }

    private BankItemResponse getBankItemResponse(BankDb bankDb) {
        BankItemResponse bankItemResponse = new BankItemResponse();
        bankItemResponse.setBankId(bankDb.getBankId());
        bankItemResponse.setBankName(bankDb.getBankName());

        List<InstallmentResponse> installmentResponseList = new ArrayList<>();

        for(InstallmentDb installmentDb : bankDb.getInstallmentDbs()) {
            InstallmentResponse installmentResponse = new InstallmentResponse();
            installmentResponse.setTerm(installmentDb.getTerm());
            installmentResponse.setFeeValue(installmentDb.getFeeValue());
            installmentResponse.setFeeType(installmentDb.getFeeType());
            installmentResponse.setInterest(installmentDb.getInterest());
            installmentResponse.setMinimumAmount(installmentDb.getMinimumAmount());

            installmentResponseList.add(installmentResponse);
        }
        bankItemResponse.setInstallmentList(installmentResponseList);

        return bankItemResponse;
    }
}
