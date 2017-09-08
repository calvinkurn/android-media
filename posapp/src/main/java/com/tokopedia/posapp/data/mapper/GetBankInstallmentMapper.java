package com.tokopedia.posapp.data.mapper;

import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;
import com.tokopedia.posapp.data.pojo.BankItemResponse;
import com.tokopedia.posapp.data.pojo.BankListResponse;
import com.tokopedia.posapp.data.pojo.InstallmentResponse;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.bank.InstallmentDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/5/17.
 */

public class GetBankInstallmentMapper implements Func1<Response<BankInstallmentResponse>, BankInstallmentDomain> {
    @Override
    public BankInstallmentDomain call(Response<BankInstallmentResponse> response) {
        if(response.body() != null && response.isSuccessful()) {
            return getBankInstallmentDomain(response.body().getData().getList());
        }
        return null;
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