package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;
import com.tokopedia.posapp.data.pojo.BankItemResponse;
import com.tokopedia.posapp.data.pojo.CCBinResponse;
import com.tokopedia.posapp.data.pojo.base.ListResponse;
import com.tokopedia.posapp.data.pojo.base.PaymentResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface CreditCardApi {
    @GET(TkpdBaseURL.Payment.PATH_INSTALLMENT_TERMS +
            PosConstants.Payment.MERCHANT_CODE + "/" + PosConstants.Payment.PROFILE_CODE)
    Observable<Response<PaymentResponse<ListResponse<BankItemResponse>>>> getBankInstallment();

    @GET(TkpdBaseURL.Payment.PATH_CC_BIN)
    Observable<Response<PaymentResponse<ListResponse<CCBinResponse>>>> getBins();
}
