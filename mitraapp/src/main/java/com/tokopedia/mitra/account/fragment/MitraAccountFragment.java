package com.tokopedia.mitra.account.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.account.contract.MitraAccountContract;
import com.tokopedia.mitra.account.di.DaggerMitraAccountComponent;
import com.tokopedia.mitra.account.di.MitraAccountComponent;
import com.tokopedia.mitra.account.presenter.MitraAccountPresenter;
import com.tokopedia.mitra.common.MitraComponentInstance;
import com.tokopedia.mitra.common.di.DaggerMitraComponent;
import com.tokopedia.mitra.common.di.MitraComponent;
import com.tokopedia.mitra.homepage.activity.MitraParentHomepageActivity;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MitraAccountFragment extends BaseDaggerFragment implements MitraAccountContract.View {

    private AppCompatTextView nameTextView;
    private AppCompatTextView phoneNumberTextView;
    private LinearLayout logoutLayout;
    private LinearLayout accountLayout;
    private ProgressBar progressBar;

    @Inject
    MitraAccountPresenter presenter;

    public MitraAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mitra_account, container, false);
        nameTextView = view.findViewById(R.id.tv_name);
        phoneNumberTextView = view.findViewById(R.id.tv_phone_number);
        logoutLayout = view.findViewById(R.id.logout_layout);
        accountLayout = view.findViewById(R.id.account_layout);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewListener();
        presenter.attachView(this);
        presenter.onViewCreated();
    }

    private void setupViewListener() {
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLogoutClicked();
            }
        });
    }

    public static MitraAccountFragment newInstance() {
        return new MitraAccountFragment();
    }

    @Override
    protected void initInjector() {
        MitraAccountComponent component = DaggerMitraAccountComponent.builder()
                .mitraComponent(MitraComponentInstance.getComponent(getActivity().getApplication()))
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderName(String name) {
        nameTextView.setText(name);
    }

    @Override
    public void renderPhoneNumber(String phone) {
        phoneNumberTextView.setText(phone);
    }

    @Override
    public void showLogoutConfirmationDialog() {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.mitra_account_logout_title));
        dialog.setDesc(getString(R.string.mitra_account_logout_desc));
        dialog.setBtnCancel(getString(R.string.mitra_account_logout_cancel_button_label));
        dialog.setBtnOk(getString(R.string.mitra_account_logout_confirm_button_label));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLogoutConfirmed();
                dialog.dismiss();
            }
        });

        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void navigateToHomepage() {
        startActivity(MitraParentHomepageActivity.getCallingIntent(getActivity()));
        getActivity().finish();
    }

    @Override
    public void showLogoutLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAccountPage() {
        accountLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLogoutLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAccountPage() {
        accountLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLogoutErrorMessage(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }
}
