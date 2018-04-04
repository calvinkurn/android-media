package com.tokopedia.tkpd.tokocash;

import android.content.Context;

import com.tokopedia.core.drawer2.data.pojo.topcash.Action;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.tkpd.R;
import com.tokopedia.tokocash.network.exception.UserInactivateTokoCashException;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

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
                .map(new TokoCashBalanceMapper())
                .onErrorReturn(new Func1<Throwable, TokoCashData>() {
                    @Override
                    public TokoCashData call(Throwable throwable) {
                        if (throwable instanceof UserInactivateTokoCashException) {
                            Action action = new Action();
                            action.setmText(context.getString(R.string.title_activation));
                            action.setmAppLinks(context.getString(R.string.applick_tokocash_activation));
                            TokoCashData tokoCashData = new TokoCashData();
                            tokoCashData.setBalance("");
                            tokoCashData.setText(context.getString(R.string.label_tokocash));
                            tokoCashData.setAction(action);
                            return tokoCashData;
                        }
                        return null;
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                });
    }
}
