package com.tokopedia.common.travel.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.fragment.PhoneCodePickerFragment;
import com.tokopedia.common.travel.utils.CommonTravelUtils;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class PhoneCodePickerActivity extends BaseSimpleActivity implements HasComponent<CommonTravelComponent> {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, PhoneCodePickerActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return new PhoneCodePickerFragment();
    }

    @Override
    public CommonTravelComponent getComponent() {
        return CommonTravelUtils.getTrainComponent(getApplication());
    }
}
