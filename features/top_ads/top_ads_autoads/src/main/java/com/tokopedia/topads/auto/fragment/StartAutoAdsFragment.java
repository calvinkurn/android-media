package com.tokopedia.topads.auto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.topads.auto.R;
import com.tokopedia.topads.auto.activity.DailyBudgetActivity;
import com.tokopedia.topads.auto.activity.StartAutoAdsActivity;

/**
 * Author errysuprayogi on 07,May,2019
 */
public class StartAutoAdsFragment extends BaseDaggerFragment {

    private Button btnStart;

    public static StartAutoAdsFragment newInstance() {

        Bundle args = new Bundle();

        StartAutoAdsFragment fragment = new StartAutoAdsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_new_ads_onboarding, container, false);

        btnStart = view.findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DailyBudgetActivity.class));
            }
        });
        return view;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
