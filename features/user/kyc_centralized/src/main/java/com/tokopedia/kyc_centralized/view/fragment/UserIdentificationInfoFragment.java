package com.tokopedia.kyc_centralized.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.globalerror.GlobalError;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.KycUrl;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationInfoActivity;
import com.tokopedia.kyc_centralized.analytics.UserIdentificationAnalytics;
import com.tokopedia.kyc_centralized.di.DaggerUserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationInfo;
import com.tokopedia.kyc_centralized.view.subscriber.GetUserProjectInfoSubcriber;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.KycCommonUrl;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import kotlin.Unit;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoFragment extends BaseDaggerFragment
        implements UserIdentificationInfo.View,
        GetUserProjectInfoSubcriber.GetUserProjectInfoListener,
        UserIdentificationInfoActivity.Listener {

    private final static int FLAG_ACTIVITY_KYC_FORM = 1301;

    private GlobalError globalErrorView;
    private ImageView image;
    private TextView title;
    private TextView text;
    private View progressBar;
    private View mainView;
    private UnifyButton button;
    private ConstraintLayout clReason;
    private TextView reasonOne;
    private TextView reasonTwo;
    private View iconOne;
    private View iconTwo;
    private boolean isSourceSeller;
    private UserIdentificationAnalytics analytics;
    private int statusCode;

    private int projectId = -1;

    @Inject
    UserIdentificationInfo.Presenter presenter;

    public static UserIdentificationInfoFragment createInstance(boolean isSourceSeller, int projectid) {
        UserIdentificationInfoFragment fragment = new UserIdentificationInfoFragment();
        Bundle args = new Bundle();
        args.putBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER, isSourceSeller);
        args.putInt(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectid);
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
            projectId = getArguments().getInt(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, KYCConstant.KYC_PROJECT_ID);
        }

        if (isSourceSeller) {
            goToFormActivity();
        }

        analytics = UserIdentificationAnalytics.createInstance(projectId);
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            UserIdentificationCommonComponent daggerUserIdentificationComponent =
                    DaggerUserIdentificationCommonComponent.builder()
                            .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                            .build();

            daggerUserIdentificationComponent.inject(this);
            presenter.attachView(this);
        }
    }

    private void initView(View parentView) {
        globalErrorView = parentView.findViewById(R.id.fragment_user_identification_global_error);
        mainView = parentView.findViewById(R.id.main_view);
        image = parentView.findViewById(R.id.main_image);
        title = parentView.findViewById(R.id.title);
        text = parentView.findViewById(R.id.text);
        button = parentView.findViewById(R.id.button);
        progressBar = parentView.findViewById(R.id.progress_bar);
        clReason = parentView.findViewById(R.id.cl_reason);
        reasonOne = parentView.findViewById(R.id.txt_reason_1);
        reasonTwo = parentView.findViewById(R.id.txt_reason_2);
        iconOne = parentView.findViewById(R.id.ic_x_1);
        iconTwo = parentView.findViewById(R.id.ic_x_2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(projectId != KYCConstant.STATUS_DEFAULT) {
            getStatusInfo();
        }else {
            toggleNotFoundView(true);
        }

    }

    private void getStatusInfo() {
        showLoading();
        presenter.getInfo(projectId);
    }

    @Override
    public void onUserBlacklist() {
        hideLoading();
        showStatusBlacklist();
    }

    @Override
    public void onSuccessGetUserProjectInfo(int status, List<String> reasons) {
        hideLoading();
        statusCode = status;
        switch (status) {
            case KYCConstant.STATUS_REJECTED:
                showStatusRejected(reasons);
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
            case KYCConstant.STATUS_DEFAULT:
                toggleNotFoundView(true);
                break;
            default:
                onErrorGetUserProjectInfo(
                        new MessageErrorException(String.format("%s (%s)",
                        getString(R.string.user_identification_default_request_error_unknown),
                        KYCConstant.ERROR_STATUS_UNKNOWN)));
                break;
        }
    }

    @Override
    public void onErrorGetUserProjectInfo(Throwable throwable) {
        if (getContext() != null) {
            hideLoading();
            String error = ErrorHandler.getErrorMessage(getContext(), throwable);
            NetworkErrorHelper.showEmptyState(getContext(), mainView, error, this::getStatusInfo);
        }
    }

    @Override
    public void onErrorGetUserProjectInfoWithErrorCode(String errorCode) {
        if (getContext() != null) {
            hideLoading();
            String error = String.format("%s (%s)", getContext().getString(R.string.user_identification_default_request_error_unknown), errorCode);
            NetworkErrorHelper.showEmptyState(getContext(), mainView, error, this::getStatusInfo);
        }
    }

    private void toggleNotFoundView(boolean isVisible) {
        if(isVisible) {
            mainView.setVisibility(View.GONE);
            globalErrorView.setType(GlobalError.Companion.getPAGE_NOT_FOUND());
            globalErrorView.setVisibility(View.VISIBLE);
            globalErrorView.setActionClickListener(view -> {
                if(getActivity() != null){
                    RouteManager.route(getContext(), ApplinkConst.HOME);
                }
                return Unit.INSTANCE;
            });
        }else{
            mainView.setVisibility(View.VISIBLE);
            globalErrorView.setVisibility(View.GONE);
        }
    }

    private void showStatusNotVerified() {
        ImageHandler.LoadImage(image, KycUrl.ICON_NOT_VERIFIED);
        title.setText(R.string.kyc_intro_title);
        text.setText(R.string.kyc_intro_text);
        button.setEnabled(true);
        button.setText(R.string.kyc_intro_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToFormActivityButton(KYCConstant.STATUS_NOT_VERIFIED));
        analytics.eventViewOnKYCOnBoarding();
    }

    private void showStatusVerified() {
        ImageHandler.LoadImage(image, KycUrl.ICON_SUCCESS_VERIFY);
        title.setText(R.string.kyc_verified_title);
        text.setText(R.string.kyc_verified_text);
        button.setText(R.string.kyc_verified_button);
        button.setButtonVariant(UnifyButton.Variant.FILLED);
        button.setButtonType(UnifyButton.Type.MAIN);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToTermsButton());
        analytics.eventViewSuccessPage();
    }

    private void showStatusPending() {
        ImageHandler.LoadImage(image, KycUrl.ICON_WAITING);
        title.setText(R.string.kyc_pending_title);
        text.setText(R.string.kyc_pending_text);
        button.setText(R.string.kyc_pending_button);
        button.setButtonVariant(UnifyButton.Variant.GHOST);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToAccountSettingButton(KYCConstant.STATUS_PENDING));
        analytics.eventViewPendingPage();
    }

    private void showStatusRejected(List<String> reasons) {
        ImageHandler.LoadImage(image, KycUrl.ICON_FAIL_VERIFY);
        title.setText(R.string.kyc_failed_title);
        if(!reasons.isEmpty()) {
            text.setText(R.string.kyc_failed_text_with_reason);
            clReason.setVisibility(View.VISIBLE);
            showRejectedReason(reasons);
        } else {
            text.setText(R.string.kyc_failed_text);
            clReason.setVisibility(View.GONE);
        }
        button.setText(R.string.kyc_failed_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToFormActivityButton(KYCConstant.STATUS_REJECTED));
        analytics.eventViewRejectedPage();
    }

    private void showRejectedReason(List<String> reasons) {
        reasonOne.setVisibility(View.VISIBLE);
        iconOne.setVisibility(View.VISIBLE);
        reasonOne.setText(reasons.get(0));
        if(reasons.size() > 1) {
            reasonTwo.setVisibility(View.VISIBLE);
            iconTwo.setVisibility(View.VISIBLE);
            reasonTwo.setText(reasons.get(1));
        } else {
            reasonTwo.setVisibility(View.GONE);
            iconTwo.setVisibility(View.GONE);
        }
    }

    private void showStatusBlacklist() {
        ImageHandler.LoadImage(image, KycUrl.ICON_FAIL_VERIFY);
        title.setText(R.string.kyc_failed_title);
        text.setText(R.string.kyc_blacklist_text);
        button.setText(R.string.kyc_blacklist_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onGoToAccountSettingButton(KYCConstant.STATUS_BLACKLISTED));
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
    public GetUserProjectInfoSubcriber.GetUserProjectInfoListener getUserProjectInfoListener() {
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
            case KYCConstant.STATUS_BLACKLISTED:
                analytics.eventClickBackBlacklistPage();
            default:
                break;
        }
    }

    private View.OnClickListener onGoToFormActivityButton(int status) {
        return v -> {
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
        };
    }

    private View.OnClickListener onGoToAccountSettingButton(int status){
        return v -> {
            switch (status) {
                case KYCConstant.STATUS_PENDING:
                    analytics.eventClickOnButtonPendingPage();
                    break;
                case KYCConstant.STATUS_BLACKLISTED:
                    analytics.eventClickOnButtonBlacklistPage();
                    break;
            }
            getActivity().finish();
        };
    }

    private void goToFormActivity() {
        if(getActivity() != null){
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM, String.valueOf(projectId));
            startActivityForResult(intent, FLAG_ACTIVITY_KYC_FORM);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLAG_ACTIVITY_KYC_FORM && resultCode == Activity.RESULT_OK) {
            getStatusInfo();
            NetworkErrorHelper.showGreenSnackbar(getActivity(), getString(R.string.text_notification_success_upload));
            analytics.eventViewSuccessSnackbarPendingPage();
        }else if(requestCode == FLAG_ACTIVITY_KYC_FORM && resultCode == KYCConstant.USER_EXIT) {
            Objects.requireNonNull(getActivity()).finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener onGoToTermsButton() {
        return v -> {
            analytics.eventClickTermsSuccessPage();
            RouteManager.route(getActivity(), KycCommonUrl.APPLINK_TERMS_AND_CONDITION);
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
