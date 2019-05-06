package com.tokopedia.topads.auto.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.topads.auto.R;

/**
 * Author errysuprayogi on 07,May,2019
 */
public class AutoAdsActivatedFragment extends BaseDaggerFragment {

    private Button performanceBtn;

    public static AutoAdsActivatedFragment newInstance() {

        Bundle args = new Bundle();

        AutoAdsActivatedFragment fragment = new AutoAdsActivatedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_autoads_activated, container, false);
        performanceBtn = view.findViewById(R.id.see_performance_btn);
        performanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
}
