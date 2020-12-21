package com.tokopedia.campaign.shake.landing.view.subscriber;

import com.tokopedia.campaign.shake.landing.view.presenter.ShakeDetectPresenter;

import rx.Subscriber;

public class SecondShakeSubscriber extends Subscriber<Long> {

    private ShakeDetectPresenter presenter;

    public SecondShakeSubscriber (ShakeDetectPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(Long l) {
        if (l == ShakeDetectPresenter.SHAKE_SHAKE_WAIT_TIME_SEC && !presenter.secondShakeHappen) {
            presenter.finishShake();
            return;
        }
    }
}
