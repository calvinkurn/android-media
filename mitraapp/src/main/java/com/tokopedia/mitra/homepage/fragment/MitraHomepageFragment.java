package com.tokopedia.mitra.homepage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.common.MitraBaseFragment;
import com.tokopedia.mitra.common.MitraComponentInstance;
import com.tokopedia.mitra.common.di.MitraComponent;
import com.tokopedia.mitra.homepage.contract.MitraHomepageContract;
import com.tokopedia.mitra.homepage.di.DaggerMitraHomepageComponent;
import com.tokopedia.mitra.homepage.di.MitraHomepageComponent;
import com.tokopedia.mitra.homepage.presenter.MitraHomepagePresenter;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MitraHomepageFragment extends MitraBaseFragment<MitraHomepageContract.Presenter, MitraHomepageContract.View> implements MitraHomepageContract.View {

    private static final int REQUEST_CODE_LOGIN = 1001;
    LinearLayout loginLayout;
    AppCompatButton loginBtn;

    @Inject
    MitraHomepagePresenter presenter;

    public MitraHomepageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mitra_homepage, container, false);
        loginLayout = view.findViewById(R.id.layout_login);
        loginBtn = view.findViewById(R.id.btn_login);
        return view;
    }

    @Override
    public MitraHomepageContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        setupView(view);
        presenter.onViewCreated();
    }

    private void setupView(View view) {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoginBtnClicked();
            }
        });
    }

    public static MitraHomepageFragment newInstance() {
        return new MitraHomepageFragment();
    }

    @Override
    protected void initInjector() {
        MitraHomepageComponent component = DaggerMitraHomepageComponent.builder()
                .mitraComponent(MitraComponentInstance.getComponent(getActivity().getApplication()))
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoginContainer() {
        loginLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginContainer() {
        loginLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMessageInRedSnackBar(int resId) {
        SnackbarManager.makeRed(getView(), getString(resId), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void navigateToLoginPage() {
        startActivityForResult(LoginPhoneNumberActivity.getCallingIntent(getActivity()), REQUEST_CODE_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                presenter.onLoginResultReceived();
                break;
        }
    }
}
