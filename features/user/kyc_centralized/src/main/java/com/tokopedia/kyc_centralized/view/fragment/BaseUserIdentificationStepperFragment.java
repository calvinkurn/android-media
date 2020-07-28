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

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics;

import static com.tokopedia.kyc_centralized.view.fragment.UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE;
import static com.tokopedia.user_identification_common.KYCConstant.EXTRA_STRING_IMAGE_RESULT;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_FACE;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_KTP;

/**
 * @author by alvinatin on 12/11/18.
 */

public abstract class BaseUserIdentificationStepperFragment<T extends
        UserIdentificationStepperModel> extends BaseDaggerFragment {

    public final static String EXTRA_KYC_STEPPER_MODEL = "kyc_stepper_model";

    protected LottieAnimationView onboardingImage;
    protected TextView title;
    protected TextView subtitle;
    protected TextView button;
    protected ImageView correctImage;
    protected ImageView wrongImage;
    protected UserIdentificationCommonAnalytics analytics;
    protected int projectId;

    protected T stepperModel;

    protected StepperListener stepperListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof StepperListener) {
            stepperListener = (StepperListener) getContext();
        }
        if (getArguments() != null && savedInstanceState == null) {
            stepperModel = getArguments().getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
        } else if (savedInstanceState != null){
            stepperModel = savedInstanceState.getParcelable(EXTRA_KYC_STEPPER_MODEL);
        }
        if (getActivity() != null) {
            projectId = getActivity().getIntent().getIntExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, -1);
            analytics = UserIdentificationCommonAnalytics.createInstance(projectId);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(EXTRA_KYC_STEPPER_MODEL, stepperModel);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_identification_form, container, false);
        initView(view);
        setContentView();
        return view;
    }

    protected void initView(View view) {
        onboardingImage = view.findViewById(R.id.form_onboarding_image);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        button = view.findViewById(R.id.button);
        correctImage = view.findViewById(R.id.image_selfie_correct);
        wrongImage = view.findViewById(R.id.image_selfie_wrong);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_CAMERA_FACE) {
                if(isKycSelfie()){
                    String faceFile = data.getStringExtra(EXTRA_STRING_IMAGE_RESULT);
                    stepperModel.setFaceFile(faceFile);
                    stepperListener.goToNextPage(stepperModel);
                } else {
                    getLivenessResult(data);
                }
            } else if (requestCode == REQUEST_CODE_CAMERA_KTP) {
                String ktpFile = data.getStringExtra(EXTRA_STRING_IMAGE_RESULT);
                stepperModel.setKtpFile(ktpFile);
                stepperListener.goToNextPage(stepperModel);
            }
        } else if (resultCode == KYCConstant.IS_FILE_IMAGE_TOO_BIG) {
            sendAnalyticErrorImageTooLarge(requestCode);
            NetworkErrorHelper.showRedSnackbar(getActivity(), getResources().getString(R.string.error_text_image_file_too_big));
        } else if (resultCode == KYCConstant.IS_FILE_IMAGE_NOT_EXIST) {
            NetworkErrorHelper.showRedSnackbar(getActivity(), getResources().getString(R.string.error_text_image_cant_be_accessed));
        } else if (resultCode == KYCConstant.IS_FILE_LIVENESS_IMAGE_NOT_EXIST) {
            NetworkErrorHelper.showRedSnackbar(getActivity(), getResources().getString(R.string.error_text_liveness_image_cant_be_accessed));
        } else if (resultCode == KYCConstant.NOT_SUPPORT_LIVENESS && requestCode == REQUEST_CODE_CAMERA_FACE) {
            UserIdentificationFormActivity.isSupportedLiveness = false;
            Intent intent = UserIdentificationCameraActivity.createIntent(getContext(), PARAM_VIEW_MODE_FACE);
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
            startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getLivenessResult(Intent data){
        boolean isSuccessRegister = data.getBooleanExtra(ApplinkConst.Liveness.EXTRA_IS_SUCCESS_REGISTER, false);
        stepperModel.setFaceFile(data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH));
        if(isSuccessRegister){
            getActivity().setResult(Activity.RESULT_OK);
            stepperListener.finishPage();
        } else {
            stepperModel.setFaceFile(data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH));
            stepperModel.setListRetake(data.getIntegerArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_RETAKE));
            stepperModel.setListMessage(data.getStringArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_MESSAGE));
            stepperModel.setTitleText(data.getStringExtra(ApplinkConst.Liveness.EXTRA_TITLE));
            stepperModel.setSubtitleText(data.getStringExtra(ApplinkConst.Liveness.EXTRA_SUBTITLE));
            stepperModel.setButtonText(data.getStringExtra(ApplinkConst.Liveness.EXTRA_BUTTON));
            stepperListener.goToNextPage(stepperModel);
        }
    }

    private void sendAnalyticErrorImageTooLarge(int requestCode) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_KTP:
                analytics.eventViewErrorImageTooLargeKtpPage();
                break;
            case REQUEST_CODE_CAMERA_FACE:
                analytics.eventViewErrorImageTooLargeSelfiePage();
                break;
            default:
                break;
        }
    }

    protected Boolean isKycSelfie() {
        try {
            if(UserIdentificationFormActivity.isSupportedLiveness) {
                return !RemoteConfigInstance.getInstance().getABTestPlatform().getString(KYCConstant.KYC_AB_KEYWORD).equals(KYCConstant.KYC_AB_KEYWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected abstract void initInjector();

    protected abstract void setContentView();
}
