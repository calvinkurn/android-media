package com.tokopedia.paymentmanagementsystem.changeclickbca.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeClickBcaFragment extends BaseDaggerFragment {

    @Inject
    ChangeClickBcaPresenter changeClickBcaPresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
