package com.tokopedia.withdraw.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.di.DaggerDoWithdrawComponent;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.view.activity.WithdrawPasswordActivity;
import com.tokopedia.withdraw.view.listener.WithdrawPasswordContract;
import com.tokopedia.withdraw.view.presenter.WithdrawPasswordPresenter;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import javax.inject.Inject;

public class WithdrawPasswordFragment extends BaseDaggerFragment implements WithdrawPasswordContract.View{


    private View withdrawButton;
    private EditText passwordView;

    @Override
    protected String getScreenName() {
        return null;
    }
    
    @Inject
    WithdrawPasswordPresenter presenter;

    @Override
    protected void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

        DaggerDoWithdrawComponent.builder().withdrawComponent(withdrawComponent)
                .build().inject(this);

        presenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new WithdrawPasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_confirm_password, container, false);
        withdrawButton = view.findViewById(R.id.withdraw_button);
        passwordView = view.findViewById(R.id.password);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int withdrawal = (int) StringUtils.convertToNumeric(
                        getArguments().getString(WithdrawPasswordActivity.BUNDLE_WITHDRAW)
                        ,false);

                presenter.doWithdraw(withdrawal, (BankAccountViewModel) getArguments()
                        .get(WithdrawPasswordActivity.BUNDLE_BANK)
                        , passwordView.getText().toString());
            }
        });
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
