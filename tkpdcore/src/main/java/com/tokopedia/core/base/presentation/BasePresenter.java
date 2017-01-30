package com.tokopedia.core.base.presentation;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author kulomady on 11/20/16.
 */

public class BasePresenter<T extends CustomerView> implements CustomerPresenter<T> {

    private T view;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        this.mCompositeSubscription.clear();
    }

    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new CustomerViewNotAttachedException();
        }
    }

    public T getView() {
        return view;
    }

    protected void addSubcription(Subscription subscriptions) {
        this.mCompositeSubscription.add(subscriptions);
    }

    public static class CustomerViewNotAttachedException extends RuntimeException {
        public CustomerViewNotAttachedException() {
            super("Please call Presenter.attachView(CustomerView) before " +
                    "requesting data to the presenter");

        }
    }

    public boolean isViewAttached() {
        return view != null;
    }
}
