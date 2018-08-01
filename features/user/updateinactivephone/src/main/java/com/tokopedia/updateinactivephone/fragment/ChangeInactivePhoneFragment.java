package com.tokopedia.updateinactivephone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.updateinactivephone.R;

public class ChangeInactivePhoneFragment extends BaseDaggerFragment {
    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_inactive_phone, parent, false);
        return view;
    }

    public static Fragment getInstance() {
        return new ChangeInactivePhoneFragment();
    }
}
