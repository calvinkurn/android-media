package com.tokopedia.posapp.data.mapper;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.bank.BankItemResponse;
import com.tokopedia.posapp.data.pojo.bank.CCBinResponse;
import com.tokopedia.posapp.data.pojo.bank.InstallmentResponse;
import com.tokopedia.posapp.data.pojo.base.GeneralResponse;
import com.tokopedia.posapp.data.pojo.base.ListResponse;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.bank.InstallmentDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func2;

/**
 * Created by okasurya on 9/5/17.
 */

public class GetBankInstallmentMapper implements Func2<Response<TkpdResponse>, Response<TkpdResponse>, List<BankDomain>> {
    private Gson gson;

    public GetBankInstallmentMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public List<BankDomain> call(Response<TkpdResponse> bankResponse, Response<TkpdResponse> binResponse) {
        if (bankResponse.body() != null
                && bankResponse.isSuccessful()) {
            Log.d("o2o", "bankInstallment: " + bankResponse.body().getStringData());

            GeneralResponse<ListResponse<BankItemResponse>> bankGeneralResponse =
                    gson.fromJson(
                            bankResponse.body().getStringData(),
                            new TypeToken<GeneralResponse<ListResponse<BankItemResponse>>>() {
                            }.getType()
                    );

            if(bankGeneralResponse != null
                    && bankGeneralResponse.getData() != null
                    && bankGeneralResponse.getData().getList() != null) {

                List<BankDomain> bankDomains =
                        getBankList(bankGeneralResponse.getData().getList());

                if (binResponse.body() != null
                        && binResponse.isSuccessful()) {
                    Log.d("o2o", "binCC: " + bankResponse.body().getStringData());

                    GeneralResponse<ListResponse<CCBinResponse>> binGeneralResponse = gson.fromJson(
                            binResponse.body().getStringData(),
                            new TypeToken<GeneralResponse<ListResponse<CCBinResponse>>>() {
                            }.getType()
                    );

                    if(binGeneralResponse != null
                            && binGeneralResponse.getData() != null
                            && binGeneralResponse.getData().getList() != null) {

                        for (CCBinResponse ccBinResponse : binGeneralResponse.getData().getList()) {
                            for (int i = 0; i < bankDomains.size(); i++) {
                                if (bankDomains.get(i).getBankId() == ccBinResponse.getBankId()) {
                                    bankDomains.get(i).setBankLogo(ccBinResponse.getBankLogo());
                                    bankDomains.get(i).setBin(ccBinResponse.getValidateBin());
                                    bankDomains.get(i).setBinInstallment(ccBinResponse.getInstallmentBin());
                                    bankDomains.get(i).setAllowInstallment(ccBinResponse.getAllowInstallment());
                                    break;
                                }
                            }
                        }
                    }
                }
                return bankDomains;
            }
        }
        return null;
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