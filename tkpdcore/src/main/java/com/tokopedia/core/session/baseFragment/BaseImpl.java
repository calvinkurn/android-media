package com.tokopedia.core.session.baseFragment;

import android.os.Bundle;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.rxjava.RxUtils;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by m.normansyah on 1/27/16.
 * this class handle flag for rotation and store view by default.
 * {@link Base#getRotationData(Bundle)} for get data after rotation
 */
public abstract class BaseImpl<T extends BaseView> implements Base {

    protected T view;
    protected boolean isAfterRotate;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    public BaseImpl(T view){
        if(view ==null)
            throw new RuntimeException(BaseImpl.class.getSimpleName()+ "please supply view that implements BaseView");
        this.view = view;
    }

    @Override
    public void fetchRotationData(Bundle argument) {
        isAfterRotate = argument != null;

        if(isAfterRotate){
            getRotationData(argument);
        }
    }

    @Override
    public void subscribe() {
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }
}
