package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;
import com.tokopedia.posapp.data.pojo.BankItemResponse;
import com.tokopedia.posapp.data.pojo.CreditCardBinResponse;
import com.tokopedia.posapp.data.pojo.InstallmentResponse;
import com.tokopedia.posapp.data.source.cloud.api.CreditCardApi;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.bank.InstallmentDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankCloudSource {
    CreditCardApi creditCardApi;
    GetBankInstallmentMapper getBankInstallmentMapper;

    public BankCloudSource(CreditCardApi creditCardApi,
                           GetBankInstallmentMapper getBankInstallmentMapper) {
        this.creditCardApi = creditCardApi;
        this.getBankInstallmentMapper = getBankInstallmentMapper;
    }

    public Observable<BankInstallmentDomain> getBankInstallment() {
        return Observable.zip(
            creditCardApi.getBankInstallment(),
            creditCardApi.getBins(),
            new Func2<Response<BankInstallmentResponse>, Response<CreditCardBinResponse>, BankInstallmentDomain>() {
                @Override
                public BankInstallmentDomain call(Response<BankInstallmentResponse> bankResponse,
                                                  Response<CreditCardBinResponse> binResponse) {

                    if(bankResponse.body() != null
                            && bankResponse.isSuccessful()
                            && bankResponse.body().getData() != null) {

                        BankInstallmentDomain bankInstallmentDomain =
                                getBankInstallmentDomain(bankResponse.body().getData().getList());

                        if(binResponse.body() != null && binResponse.isSuccessful()) {

                        }
                        return bankInstallmentDomain;
                    }

                    return null;
                }
            }
        );
    }

    private BankInstallmentDomain getBankInstallmentDomain(List<BankItemResponse> bankList) {
        BankInstallmentDomain bankInstallmentDomain = new BankInstallmentDomain();
        List<BankDomain> bankDomainList = new ArrayList<>();

        for(BankItemResponse bankItem : bankList) {
            BankDomain bankDomain = new BankDomain();
            bankDomain.setBankId(bankItem.getBankId());
            bankDomain.setBankName(bankItem.getBankName());
            bankDomain.setInstallmentList(getInstallmentDomain(bankItem.getInstallmentList()));

            bankDomainList.add(bankDomain);
        }
        bankInstallmentDomain.setBankDomainList(bankDomainList);

        return bankInstallmentDomain;
    }

    private List<InstallmentDomain> getInstallmentDomain(List<InstallmentResponse> installmentList) {
        List<InstallmentDomain> installmentDomains = new ArrayList<>();
        for(InstallmentResponse installment : installmentList) {
            InstallmentDomain domain = new InstallmentDomain();
            domain.setTerm(installment.getTerm());
            domain.setFeeType(installment.getFeeType());
            domain.setFeeValue(installment.getFeeValue());
            domain.setInterest(installment.getInterest());
            domain.setMinimumAmount(installment.getMinimumAmount());

            installmentDomains.add(domain);
        }
        return installmentDomains;
    }
}
