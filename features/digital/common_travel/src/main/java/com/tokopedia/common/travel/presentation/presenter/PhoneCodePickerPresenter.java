package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public interface PhoneCodePickerPresenter extends CustomerPresenter<PhoneCodePickerView> {
    void getPhoneCodeList();

    void getPhoneCodeList(String text);

    void onDestroyView();
}
