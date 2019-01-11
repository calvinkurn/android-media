package com.tokopedia.useridentification.view.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.KycCommonUrl;
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber;
import com.tokopedia.useridentification.KycUrl;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.analytics.UserIdentificationAnalytics;
import com.tokopedia.useridentification.di.DaggerUserIdentificationComponent;
import com.tokopedia.useridentification.di.UserIdentificationComponent;
import com.tokopedia.useridentification.view.activity.UserIdentificationFormActivity;
import com.tokopedia.useridentification.view.activity.UserIdentificationInfoActivity;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;

import javax.inject.Inject;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoFragment extends BaseDaggerFragment
        implements UserIdentificationInfo.View,
        GetApprovalStatusSubscriber.GetApprovalStatusListener,
        UserIdentificationInfoActivity.Listener {

    private final static int FLAG_ACTIVITY_KYC_FORM = 1301;

    private ImageView image;
    private TextView title;
    private TextView text;
    private View progressBar;
    private View mainView;
    private TextView button;
    private boolean isSourceSeller;
    private UserIdentificationAnalytics analytics;
    private int statusCode;

    @Inject
    UserIdentificationInfo.Presenter presenter;

    public static UserIdentificationInfoFragment createInstance(boolean isSourceSeller) {
        UserIdentificationInfoFragment fragment = new UserIdentificationInfoFragment();
        Bundle args = new Bundle();
        args.putBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER, isSourceSeller);
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
        if (getArguments() != null) {
            isSourceSeller = getArguments().getBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER);
        }
        if (isSourceSeller) {
            goToFormActivity();
        }
        analytics = UserIdentificationAnalytics.createInstance(getActivity().getApplicationContext());
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
    public void onSuccessGetShopVerificationStatus(int status) {
        hideLoading();
        statusCode = status;
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
                showStatusNotVerified();
                break;
            case KYCConstant.STATUS_NOT_VERIFIED:
                showStatusNotVerified();
                break;
            default:
                onErrorGetShopVerificationStatus(new MessageErrorException(String.format("%s (%s)", getString(R.string
                        .default_request_error_unknown), KYCConstant.ERROR_STATUS_UNKNOWN)));
                break;
        }
    }

    private void showStatusNotVerified() {
        ImageHandler.LoadImage(image, KycUrl.ICON_NOT_VERIFIED);
        title.setText(R.string.kyc_intro_title);
        text.setText(R.string.kyc_intro_text);
        button.setText(R.string.kyc_intro_button);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundResource(R.drawable.green_button_rounded);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToFormActivityButton(KYCConstant.STATUS_NOT_VERIFIED));
        analytics.eventViewOnKYCOnBoarding();
    }

    private void showStatusVerified() {
        ImageHandler.LoadImage(image, KycUrl.ICON_SUCCESS_VERIFY);
        title.setText(R.string.kyc_verified_title);
        text.setText(R.string.kyc_verified_text);
        button.setText(R.string.kyc_verified_button);
        button.setTextColor(getResources().getColor(R.color.black_38));
        button.setBackgroundResource(R.drawable.white_button_rounded);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToTermsButton());
        analytics.eventViewSuccessPage();
    }

    private void showStatusPending() {
        ImageHandler.LoadImage(image, KycUrl.ICON_WAITING);
        button.setVisibility(View.GONE);
        title.setText(R.string.kyc_pending_title);
        text.setText(R.string.kyc_pending_text);
        analytics.eventViewPendingPage();
    }

    private void showStatusRejected() {
        ImageHandler.LoadImage(image, KycUrl.ICON_FAIL_VERIFY);
        title.setText(R.string.kyc_failed_title);
        text.setText(R.string.kyc_failed_text);
        button.setText(R.string.kyc_failed_button);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundResource(R.drawable.green_button_rounded);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToFormActivityButton(KYCConstant.STATUS_REJECTED));
        analytics.eventViewRejectedPage();
    }

    @Override
    public void onErrorGetShopVerificationStatus(Throwable errorMessage) {
        if (getContext() != null) {
            hideLoading();
            String error = ErrorHandler.getErrorMessage(getContext(), errorMessage);
            NetworkErrorHelper.showEmptyState(getContext(), mainView, error, this::getStatusInfo);
        }
    }

    @Override
    public void onErrorGetShopVerificationStatusWithErrorCode(String errorCode) {
        if (getContext() != null) {
            hideLoading();
            String error = String.format("%s (%s)", getContext().getString(R.string
                    .default_request_error_unknown), errorCode);
            NetworkErrorHelper.showEmptyState(getContext(), mainView, error, this::getStatusInfo);
        }
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

    @Override
    public GetApprovalStatusSubscriber.GetApprovalStatusListener getApprovalStatusListener() {
        return this;
    }

    @Override
    public void onTrackBackPressed() {
        switch (statusCode) {
            case KYCConstant.STATUS_REJECTED:
                analytics.eventClickBackRejectedPage();
                break;
            case KYCConstant.STATUS_PENDING:
                analytics.eventClickBackPendingPage();
                break;
            case KYCConstant.STATUS_VERIFIED:
                analytics.eventClickBackSuccessPage();
                break;
            case KYCConstant.STATUS_EXPIRED:
                analytics.eventClickOnBackOnBoarding();
                break;
            case KYCConstant.STATUS_NOT_VERIFIED:
                analytics.eventClickOnBackOnBoarding();
                break;
            default:
                break;
        }
    }

    private View.OnClickListener onGoToFormActivityButton(int status) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {
                    case KYCConstant.STATUS_NOT_VERIFIED:
                        analytics.eventClickOnNextOnBoarding();
                        break;
                    case KYCConstant.STATUS_REJECTED:
                        analytics.eventClickNextRejectedPage();
                        break;
                    default:
                        break;
                }
                goToFormActivity();
            }
        };
    }

    private void goToFormActivity() {
        Intent intent = UserIdentificationFormActivity.getIntent(getContext());
        startActivityForResult(intent, FLAG_ACTIVITY_KYC_FORM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLAG_ACTIVITY_KYC_FORM && resultCode == Activity.RESULT_OK) {
            getStatusInfo();
            NetworkErrorHelper.showGreenSnackbar(getActivity(), getString(R.string.text_notification_success_upload));
            analytics.eventViewSuccessSnackbarPendingPage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener onGoToTermsButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickTermsSuccessPage();
                RouteManager.route(getActivity(), KycCommonUrl.APPLINK_TERMS_AND_CONDITION);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
