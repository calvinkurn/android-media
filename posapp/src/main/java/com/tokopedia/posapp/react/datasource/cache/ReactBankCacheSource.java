package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.factory.BankFactory;
import com.tokopedia.posapp.data.pojo.bank.BankItemResponse;
import com.tokopedia.posapp.data.pojo.bank.InstallmentResponse;
import com.tokopedia.posapp.data.pojo.base.ListResponse;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.InstallmentDomain;
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
    private BankFactory bankFactory;

    public ReactBankCacheSource(BankFactory bankFactory, Gson gson) {
        this.gson = gson;
        this.bankFactory = bankFactory;
    }

    @Override
    public Observable<String> getData(String id) {
        return bankFactory.local().getBank(id).map(mapData());
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return bankFactory.local().getBanks(offset, limit).map(mapListData());
    }

    @Override
    public Observable<String> getDataAll() {
        return bankFactory.local().getAllBank().map(mapListData());
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

    @Override
    public Observable<String> insert(String data) {
        return Observable.error(new RuntimeException("Method not implemented yet"));
    }

    private Func1<BankDomain, String> mapData() {
        return new Func1<BankDomain, String>() {
            @Override
            public String call(BankDomain bankDomain) {
                CacheResult<BankItemResponse> result = new CacheResult<>();
                result.setData(getBankItemResponse(bankDomain));

                return gson.toJson(result);
            }
        };
    }

    private Func1<List<BankDomain>, String> mapListData() {
        return new Func1<List<BankDomain>, String>() {
            @Override
            public String call(List<BankDomain> bankDomains) {
                CacheResult<ListResponse<BankItemResponse>> result = new CacheResult<>();
                ListResponse<BankItemResponse> bankItemReponses = new ListResponse<>();
                List<BankItemResponse> list = new ArrayList<>();

                for(BankDomain domain: bankDomains) {
                    list.add(getBankItemResponse(domain));
                }
                bankItemReponses.setList(list);

                result.setData(bankItemReponses);
                return gson.toJson(result);
            }
        };
    }

    private BankItemResponse getBankItemResponse(BankDomain domain) {
        BankItemResponse bankItemResponse = new BankItemResponse();
        bankItemResponse.setBankId(domain.getBankId());
        bankItemResponse.setBankName(domain.getBankName());
        bankItemResponse.setBankLogo(domain.getBankLogo());
        bankItemResponse.setAllowInstallment(domain.getAllowInstallment());

        List<InstallmentResponse> installmentResponseList = new ArrayList<>();

        for(InstallmentDomain installmentDomain : domain.getInstallmentList()) {
            InstallmentResponse installmentResponse = new InstallmentResponse();
            installmentResponse.setTerm(installmentDomain.getTerm());
            installmentResponse.setFeeValue(installmentDomain.getFeeValue());
            installmentResponse.setFeeType(installmentDomain.getFeeType());
            installmentResponse.setInterest(installmentDomain.getInterest());
            installmentResponse.setMinimumAmount(installmentDomain.getMinimumAmount());

            installmentResponseList.add(installmentResponse);
        }
        bankItemResponse.setInstallmentList(installmentResponseList);
        bankItemResponse.setValidateBin(domain.getBin());
        bankItemResponse.setInstallmentBin(domain.getBinInstallment());

        return bankItemResponse;
    }
}
