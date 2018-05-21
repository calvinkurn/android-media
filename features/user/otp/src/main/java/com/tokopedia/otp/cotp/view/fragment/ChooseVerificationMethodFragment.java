package com.tokopedia.otp.cotp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.otp.OtpModuleRouter;
import com.tokopedia.otp.R;
import com.tokopedia.otp.common.OTPAnalytics;
import com.tokopedia.otp.common.di.DaggerOtpComponent;
import com.tokopedia.otp.common.di.OtpComponent;
import com.tokopedia.otp.cotp.di.DaggerCotpComponent;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.adapter.VerificationMethodAdapter;
import com.tokopedia.otp.cotp.view.presenter.ChooseVerificationPresenter;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * @author by nisie on 11/29/17.
 */

public class ChooseVerificationMethodFragment extends BaseDaggerFragment implements
        SelectVerification.View {

    private static final String PASS_MODEL = "pass_model";
    private RecyclerView methodListRecyclerView;
    TextView changePhoneNumberButton;

    @Inject
    ChooseVerificationPresenter presenter;

    @Inject
    OTPAnalytics analytics;

    @Inject
    UserSession userSession;

    VerificationMethodAdapter adapter;
    VerificationPassModel passModel;

    View mainView;
    ProgressBar loadingView;

    @Override
    protected String getScreenName() {
        return OTPAnalytics.Screen.SCREEN_SELECT_VERIFICATION_METHOD;
    }

    @Override
    protected void initInjector() {

        OtpComponent otpComponent = DaggerOtpComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication())
                        .getBaseAppComponent()).build();

        DaggerCotpComponent.builder()
                .otpComponent(otpComponent)
                .build().inject(this);

        presenter.attachView(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());

    }

    public static Fragment createInstance(VerificationPassModel passModel) {
        Fragment fragment = new ChooseVerificationMethodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASS_MODEL, passModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().getParcelable(PASS_MODEL) != null) {
            passModel = getArguments().getParcelable(PASS_MODEL);
        } else {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_verification_method, parent, false);
        mainView = view.findViewById(R.id.main_view);
        loadingView = view.findViewById(R.id.progress_bar);
        methodListRecyclerView = view.findViewById(R.id.method_list);
        changePhoneNumberButton = view.findViewById(R.id.phone_inactive);
        prepareView();
        return view;
    }

    private void prepareView() {
        adapter = VerificationMethodAdapter.createInstance(this);
        methodListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        methodListRecyclerView.setAdapter(adapter);

        if (!userSession.isMsisdnVerified() ||
                passModel.getOtpType() == RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER) {
            changePhoneNumberButton.setVisibility(View.GONE);
        }

        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getApplicationContext() instanceof OtpModuleRouter) {
                    Intent intent = ((OtpModuleRouter) getActivity().getApplicationContext())
                            .getChangePhoneNumberRequestIntent(getActivity());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getMethodList(passModel.getPhoneNumber(),
                passModel.getOtpType());
    }

    @Override
    public void onMethodSelected(MethodItem methodItem) {
        if (getActivity() instanceof VerificationActivity) {
            ((VerificationActivity) getActivity()).goToVerificationPage(methodItem);
        }
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoading() {
        loadingView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessGetList(ListVerificationMethod listVerificationMethod) {
        adapter.setList(listVerificationMethod.getList());
    }

    @Override
    public void onErrorGetList(String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showEmptyState(getActivity(), mainView,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getMethodList(passModel.getPhoneNumber(), passModel.getOtpType());
                        }
                    });
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), mainView, errorMessage,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getMethodList(passModel.getPhoneNumber(), passModel.getOtpType());
                        }
                    });
        }
    }
}
