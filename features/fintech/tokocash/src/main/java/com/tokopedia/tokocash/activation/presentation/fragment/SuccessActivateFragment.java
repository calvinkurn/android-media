package com.tokopedia.tokocash.activation.presentation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.activation.presentation.contract.SuccessActivateTokocashContract;
import com.tokopedia.tokocash.activation.presentation.presenter.SuccessActivateTokocashPresenter;
import com.tokopedia.tokocash.common.di.TokoCashComponent;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/2/18.
 */

public class SuccessActivateFragment extends BaseDaggerFragment implements SuccessActivateTokocashContract.View {

    private TextView descSuccess;
    private Button backToHomeBtn;
    private ActionListener listener;

    @Inject
    SuccessActivateTokocashPresenter presenter;


    public static SuccessActivateFragment newInstance() {
        SuccessActivateFragment fragment = new SuccessActivateFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ActionListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_activate_tokocash, container, false);
        descSuccess = view.findViewById(R.id.desc_success);
        backToHomeBtn = view.findViewById(R.id.back_to_home_btn);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.getUserPhoneNumber();

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteCacheBalanceTokoCash();
                listener.onBackPressToHome();
                presenter.refreshingWalletToken();
            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        TokoCashComponent tokoCashComponent = TokoCashComponentInstance.getComponent(getActivity().getApplication());
        tokoCashComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    public void failedRefreshToken(Throwable e) {
        String message = ErrorHandler.getErrorMessage(getActivity(), e);
        NetworkErrorHelper.showRedSnackbar(getActivity(), message);
    }

    @Override
    public void showUserPhoneNumber(String phoneNumber) {
        String phoneNumberText = "<b>" + phoneNumber + "</b>";
        String desc = String.format(getActivity().getString(R.string.desc_success_tokocash), phoneNumberText);
        descSuccess.setText(MethodChecker.fromHtml(desc));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    public interface ActionListener {
        void onBackPressToHome();
    }
}
