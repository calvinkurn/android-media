package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.bank.data.factory.BankFactory;
import com.tokopedia.posapp.bank.data.pojo.BankItemResponse;
import com.tokopedia.posapp.bank.data.pojo.InstallmentResponse;
import com.tokopedia.posapp.base.data.pojo.ListResponse;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.InstallmentDomain;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.react.datasource.model.CacheResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/8/17.
 */

public class ReactBankCacheSource extends ReactDataSource {
    private BankFactory bankFactory;

    @Inject
    public ReactBankCacheSource(BankFactory bankFactory, Gson gson) {
        super(gson);
        this.bankFactory = bankFactory;
    }

    @Override
    public Observable<String> getData(String id) {
        return bankFactory.local().getBank(id).map(mapData()).map(mapToJson());
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return bankFactory.local().getBanks(offset, limit).map(mapListData()).map(mapToJson());
    }

    @Override
    public Observable<String> getDataAll() {
        return bankFactory.local().getAllBank().map(mapListData()).map(mapToJson());
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

    private Func1<BankDomain, CacheResult> mapData() {
        return new Func1<BankDomain, CacheResult>() {
            @Override
            public CacheResult call(BankDomain bankDomain) {
                CacheResult<BankItemResponse> result = new CacheResult<>();
                result.setData(getBankItemResponse(bankDomain));
                return result;
            }
        };
    }

    private Func1<List<BankDomain>, CacheResult> mapListData() {
        return new Func1<List<BankDomain>, CacheResult>() {
            @Override
            public CacheResult call(List<BankDomain> bankDomains) {
                CacheResult<ListResponse<BankItemResponse>> result = new CacheResult<>();
                ListResponse<BankItemResponse> bankItemReponses = new ListResponse<>();
                List<BankItemResponse> list = new ArrayList<>();

                for(BankDomain domain: bankDomains) {
                    list.add(getBankItemResponse(domain));
                }
                bankItemReponses.setList(list);

                result.setData(bankItemReponses);
                return result;
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
