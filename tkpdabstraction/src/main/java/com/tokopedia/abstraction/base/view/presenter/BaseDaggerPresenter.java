package com.tokopedia.abstraction.base.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * @author kulomady on 11/20/16.
 */

public class BaseDaggerPresenter<T extends CustomerView> implements CustomerPresenter<T> {

    private T view;

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void checkViewAttached() {
        if (isViewNotAttached()) {
            throw new CustomerViewNotAttachedException();
        }
    }

    public T getView() {
        return view;
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

    public boolean isViewNotAttached() {
        return view == null;
    }
}
