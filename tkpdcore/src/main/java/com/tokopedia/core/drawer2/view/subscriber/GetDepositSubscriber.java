package com.tokopedia.core.drawer2.view.subscriber;

import com.tokopedia.core.drawer2.data.pojo.deposit.DepositData;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.view.DrawerDataListener;

import rx.Subscriber;

/**
 * Created by nisie on 5/4/17.
 */

public class GetDepositSubscriber extends Subscriber<DepositModel> {

    private final DrawerDataListener viewListener;

    public GetDepositSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(DepositModel depositModel) {
        if (depositModel.isSuccess())
            viewListener.onGetDeposit(convertToViewModel(depositModel.getDepositData()));
        else
            viewListener.onErrorGetDeposit(depositModel.getErrorMessage());
    }

    private DrawerDeposit convertToViewModel(DepositData depositData) {
        DrawerDeposit drawerDeposit = new DrawerDeposit();
        drawerDeposit.setDeposit(depositData.getDepositTotal());
        return drawerDeposit;
    }
}
