package com.tokopedia.oms.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;

import java.util.concurrent.Executor;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PostVerifyCartWrapper {

    private PostVerifyCartUseCase postVerifyCartUseCase;
    private Context context;

    public PostVerifyCartWrapper(Context context, PostVerifyCartUseCase postVerifyCartUseCase) {
        this.postVerifyCartUseCase = postVerifyCartUseCase;
        this.context = context;
    }

    public Observable<TKPDMapParam<String, Object>> verifyPromo(com.tokopedia.usecase.RequestParams requestParams) {
        return postVerifyCartUseCase.getExecuteObservable(requestParams).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Func1<VerifyMyCartResponse, TKPDMapParam<String, Object>>() {
                    @Override
                    public TKPDMapParam<String, Object> call(VerifyMyCartResponse verifyCartResponse) {
                        TKPDMapParam<String, Object> resultMap = new TKPDMapParam<>();
                        resultMap.put("promocode", verifyCartResponse.getCart().get("promocode").getAsString());
                        resultMap.put("promocode_discount", verifyCartResponse.getCart().get("promocode_discount").getAsInt());
                        resultMap.put("promocode_cashback", verifyCartResponse.getCart().get("promocode_cashback").getAsInt());
                        resultMap.put("promocode_failure_message", verifyCartResponse.getCart().get("promocode_failure_message").getAsString());
                        resultMap.put("promocode_success_message", verifyCartResponse.getCart().get("promocode_success_message").getAsString());
                        resultMap.put("promocode_status", verifyCartResponse.getCart().get("promocode_status").getAsString());
                        return resultMap;
                    }
                });
    }
}
