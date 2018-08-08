package com.tokopedia.posapp.bank.data.source.cloud;

import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.bank.data.pojo.BankItemResponse;
import com.tokopedia.posapp.bank.data.pojo.CCBinResponse;
import com.tokopedia.posapp.bank.data.pojo.InstallmentResponse;
import com.tokopedia.posapp.bank.data.source.cloud.api.BankApi;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.InstallmentDomain;
import com.tokopedia.posapp.base.data.pojo.ListResponse;
import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankCloudSource {
    private BankApi bankApi;

    @Inject
    public BankCloudSource(BankApi bankApi) {
        this.bankApi = bankApi;
    }

    public Observable<List<BankDomain>> getBankInstallment() {
        return bankApi.getBankInstallment(PosConstants.Payment.MERCHANT_CODE, PosConstants.Payment.PROFILE_CODE)
                .map(new Func1<Response<PosSimpleResponse<ListResponse<BankItemResponse>>>, List<BankDomain>>() {
                    @Override
                    public List<BankDomain> call(Response<PosSimpleResponse<ListResponse<BankItemResponse>>> response) {
                        return getBankList(response.body().getData().getData().getList());
                    }
                });
    }

    public Observable<List<BankDomain>> getBins() {
        return bankApi.getBins().map(new Func1<Response<PosSimpleResponse<ListResponse<CCBinResponse>>>, List<BankDomain>>() {
            @Override
            public List<BankDomain> call(Response<PosSimpleResponse<ListResponse<CCBinResponse>>> response) {
                List<BankDomain> domains = new ArrayList<>();
                for (CCBinResponse ccBinResponse : response.body().getData().getData().getList()) {
                    BankDomain domain = new BankDomain();
                    domain.setBankId(ccBinResponse.getBankId());
                    domain.setBankLogo(ccBinResponse.getBankLogo());
                    domain.setBin(ccBinResponse.getValidateBin());
                    domain.setBinInstallment(ccBinResponse.getInstallmentBin());
                    domain.setAllowInstallment(ccBinResponse.getAllowInstallment());
                    domains.add(domain);
                }
                return domains;
            }
        });
    }

    private List<BankDomain> getBankList(List<BankItemResponse> bankList) {
        List<BankDomain> bankDomainList = new ArrayList<>();

        for (BankItemResponse bankItem : bankList) {
            BankDomain bankDomain = new BankDomain();
            bankDomain.setBankId(bankItem.getBankId());
            bankDomain.setBankName(bankItem.getBankName());
            bankDomain.setInstallmentList(getInstallmentDomain(bankItem.getInstallmentList()));

            bankDomainList.add(bankDomain);
        }

        return bankDomainList;

    }

    private List<InstallmentDomain> getInstallmentDomain(List<InstallmentResponse> installmentList) {
        List<InstallmentDomain> installmentDomains = new ArrayList<>();
        for (InstallmentResponse installment : installmentList) {
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
