package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.pojo.BankItemResponse;
import com.tokopedia.posapp.data.pojo.CCBinResponse;
import com.tokopedia.posapp.data.pojo.InstallmentResponse;
import com.tokopedia.posapp.data.pojo.base.ListResponse;
import com.tokopedia.posapp.data.pojo.base.PaymentResponse;
import com.tokopedia.posapp.data.source.cloud.api.CreditCardApi;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.InstallmentDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
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

    public Observable<List<BankDomain>> getBankInstallment() {
        return Observable.zip(
            creditCardApi.getBankInstallment(),
            creditCardApi.getBins(),
            new Func2<Response<PaymentResponse<ListResponse<BankItemResponse>>>, Response<PaymentResponse<ListResponse<CCBinResponse>>>, List<BankDomain>>() {
                @Override
                public List<BankDomain> call(
                        Response<PaymentResponse<ListResponse<BankItemResponse>>> bankResponse,
                        Response<PaymentResponse<ListResponse<CCBinResponse>>> binResponse) {

                    if(bankResponse.body() != null
                            && bankResponse.isSuccessful()
                            && bankResponse.body().getData() != null) {

                        List<BankDomain> bankDomains =
                                getBankList(bankResponse.body().getData().getList());

                        if(binResponse.body() != null
                                && binResponse.isSuccessful()
                                && binResponse.body().getData() != null) {
                            for(CCBinResponse ccBinResponse : binResponse.body().getData().getList()) {
                                for(int i = 0; i < bankDomains.size(); i++) {
                                    if(bankDomains.get(i).getBankId() == ccBinResponse.getBankId()) {
                                        bankDomains.get(i).setBankLogo(ccBinResponse.getBankLogo());
                                        bankDomains.get(i).setBin(ccBinResponse.getValidateBin());
                                        bankDomains.get(i).setBinInstallment(ccBinResponse.getInstallmentBin());
                                        bankDomains.get(i).setAllowInstallment(ccBinResponse.getAllowInstallment());
                                        break;
                                    }
                                }
                            }
                        }
                        return bankDomains;
                    }
                    return null;

                }
            }
        );
    }

    private List<BankDomain> getBankList(List<BankItemResponse> bankList) {
        List<BankDomain> bankDomainList = new ArrayList<>();

        for(BankItemResponse bankItem : bankList) {
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
