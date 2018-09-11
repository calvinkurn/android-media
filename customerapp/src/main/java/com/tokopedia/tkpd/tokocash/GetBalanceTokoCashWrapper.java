package com.tokopedia.tkpd.tokocash;

import android.content.Context;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.tokocash.balance.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/8/18.
 */

public class GetBalanceTokoCashWrapper {

    private GetBalanceTokoCashUseCase getBalanceTokoCashUseCase;
    private Context context;

    public GetBalanceTokoCashWrapper(Context context, GetBalanceTokoCashUseCase getBalanceTokoCashUseCase) {
        this.getBalanceTokoCashUseCase = getBalanceTokoCashUseCase;
        this.context = context;
    }

    public Observable<TokoCashData> processGetBalance() {
        return getBalanceTokoCashUseCase
                .createObservable(com.tokopedia.usecase.RequestParams.EMPTY)
                .map(new TokoCashBalanceMapper());
    }

    public Observable<WalletModel> getTokoCashAccountBalance() {
        return getBalanceTokoCashUseCase
                .createObservable(RequestParams.EMPTY)
                .map(new TokoCashAccountBalanceMapper());
    }
}
