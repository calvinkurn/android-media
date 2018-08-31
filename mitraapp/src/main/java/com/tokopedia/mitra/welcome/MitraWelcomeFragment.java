package com.tokopedia.mitra.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.activity.MitraHomepageActivity;

public class MitraWelcomeFragment extends BaseDaggerFragment {

    AppCompatButton continueButton;

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
        View view = inflater.inflate(R.layout.fragment_mitra_welcome, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        continueButton = view.findViewById(R.id.btn_continue);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewListener();
    }

    private void setupViewListener() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MitraHomepageActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    public static MitraWelcomeFragment newInstance() {
        MitraWelcomeFragment fragment = new MitraWelcomeFragment();
        return fragment;
    }
}
