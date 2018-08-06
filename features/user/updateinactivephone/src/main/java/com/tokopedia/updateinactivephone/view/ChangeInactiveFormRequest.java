package com.tokopedia.updateinactivephone.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public class ChangeInactiveFormRequest {

    public interface View extends CustomerView {

        void dismissLoading();

        void showLoading();

        void onForbidden();

    }

    public interface Presenter extends CustomerPresenter<ChangeInactiveFormRequest.View> {

        // TODO: 8/6/18 add upload file function
    }

}
