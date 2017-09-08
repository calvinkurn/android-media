package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;
import com.tokopedia.posapp.data.pojo.BankItemResponse;
import com.tokopedia.posapp.data.pojo.BankListResponse;
import com.tokopedia.posapp.data.pojo.InstallmentResponse;
import com.tokopedia.posapp.database.model.BankDb;
import com.tokopedia.posapp.database.model.InstallmentDb;
import com.tokopedia.posapp.domain.model.bank.InstallmentDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/8/17.
 */

public class ReactBankCacheSource implements ReactCacheSource {
    Gson gson;

    public ReactBankCacheSource() {
        gson = new Gson();
    }

    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getListData(int offset, int limit) {
        return null;
    }

    @Override
    public Observable<String> getAllData() {
        List<BankDb> bankDbList = SQLite.select().from(BankDb.class).queryList();

        BankInstallmentResponse bankInstallmentResponse = new BankInstallmentResponse();
        BankListResponse bankListResponse = new BankListResponse();
        List<BankItemResponse> bankItemResponseList = new ArrayList<>();
        for(BankDb bankDb: bankDbList) {
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
            bankItemResponseList.add(bankItemResponse);
        }
        bankListResponse.setList(bankItemResponseList);

        bankInstallmentResponse.setData(bankListResponse);

        return Observable.just(bankInstallmentResponse)
                .map(new Func1<BankInstallmentResponse, String>() {
                    @Override
                    public String call(BankInstallmentResponse bankInstallmentResponse) {
                        return gson.toJson(bankInstallmentResponse);
                    }
                });
    }
}
