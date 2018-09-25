package com.tokopedia.affiliate.feature.onboarding.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.view.activity.DomainInputActivity;
import com.tokopedia.affiliate.feature.onboarding.view.activity.OnboardingActivity;

public class OnboardingFragment extends Fragment {

    private View goBtn;

    public static OnboardingFragment newInstance() {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_onboarding, container, false);
        goBtn = view.findViewById(R.id.goBtn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goBtn.setOnClickListener(view1 -> {
            startActivity(DomainInputActivity.createIntent(getContext()));
        });
    }
}
