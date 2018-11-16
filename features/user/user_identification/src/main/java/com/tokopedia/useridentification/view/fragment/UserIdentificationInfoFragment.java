package com.tokopedia.useridentification.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.view.activity.UserIdentificationFormActivity;
import com.tokopedia.useridentification.view.KYCConstant;
import com.tokopedia.useridentification.di.DaggerUserIdentificationComponent;
import com.tokopedia.useridentification.di.UserIdentificationComponent;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;

import javax.inject.Inject;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoFragment extends BaseDaggerFragment implements UserIdentificationInfo.View {

    private ImageView image;
    private TextView title;
    private TextView text;
    private View progressBar;
    private View mainView;
    private TextView button;

    @Inject
    UserIdentificationInfo.Presenter presenter;

    public static UserIdentificationInfoFragment createInstance() {
        UserIdentificationInfoFragment fragment = new UserIdentificationInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_user_identification_info, container, false);
        initView(parentView);
        return parentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            UserIdentificationComponent daggerUserIdentificationComponent =
                    DaggerUserIdentificationComponent.builder()
                            .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                            .build();

            daggerUserIdentificationComponent.inject(this);
            presenter.attachView(this);
        }
    }

    private void initView(View parentView) {
        mainView = parentView.findViewById(R.id.main_view);
        image = parentView.findViewById(R.id.main_image);
        title = parentView.findViewById(R.id.title);
        text = parentView.findViewById(R.id.text);
        button = parentView.findViewById(R.id.button);
        progressBar = parentView.findViewById(R.id.progress_bar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getStatusInfo();
    }

    private void getStatusInfo() {
        showLoading();
        presenter.getStatus();
    }

    @Override
    public void onSuccessGetInfo(int status) {
        hideLoading();
        switch (status) {
            case KYCConstant.STATUS_REJECTED:
                showStatusRejected();
                break;
            case KYCConstant.STATUS_PENDING:
                showStatusPending();
                break;
            case KYCConstant.STATUS_VERIFIED:
                showStatusVerified();
                break;
            case KYCConstant.STATUS_EXPIRED:
                break;
            case KYCConstant.STATUS_NOT_VERIFIED:
                showStatusNotVerified();
                break;
            default:
                onErrorGetInfo(String.format("%s (%s)", getString(R.string
                        .default_request_error_unknown), KYCConstant.ERROR_STATUS_UNKNOWN));
                break;
        }
    }

    private void showStatusNotVerified() {
        title.setText(R.string.kyc_intro_title);
        text.setText(R.string.kyc_intro_text);
        button.setText(R.string.kyc_intro_button);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundResource(R.drawable.green_button_rounded);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToFormActivityButton());
    }

    private void showStatusVerified() {
        title.setText(R.string.kyc_verified_title);
        text.setText(R.string.kyc_verified_text);
        button.setText(R.string.kyc_verified_button);
        button.setTextColor(getResources().getColor(R.color.black_38));
        button.setBackgroundResource(R.drawable.white_button_rounded);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToTermsButton());
    }

    private void showStatusPending() {
        title.setText(R.string.kyc_pending_title);
        text.setText(R.string.kyc_pending_text);
    }

    private void showStatusRejected() {
        title.setText(R.string.kyc_failed_title);
        text.setText(R.string.kyc_failed_text);
        button.setText(R.string.kyc_failed_button);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundResource(R.drawable.green_button_rounded);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToFormActivityButton());
    }

    @Override
    public void onErrorGetInfo(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getContext(), mainView, () -> presenter.getStatus());
    }

    @Override
    public void showLoading() {
        mainView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mainView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private View.OnClickListener onGoToFormActivityButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIdentificationFormActivity.getIntent(getContext());
            }
        };
    }

    private View.OnClickListener onGoToTermsButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO alvinatin add url
            }
        };
    }
}
