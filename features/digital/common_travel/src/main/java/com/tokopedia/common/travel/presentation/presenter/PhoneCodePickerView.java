package com.tokopedia.common.travel.presentation.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public interface PhoneCodePickerView extends BaseListViewListener<CountryPhoneCode> {
    Activity getActivity();
}
