package com.tokopedia.kyc_centralized.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.kyc_centralized.KYCConstant;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.analytics.UserIdentificationCommonAnalytics;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;

import static com.tokopedia.kyc_centralized.KYCConstant.EXTRA_STRING_IMAGE_RESULT;
import static com.tokopedia.kyc_centralized.KYCConstant.REQUEST_CODE_CAMERA_FACE;
import static com.tokopedia.kyc_centralized.KYCConstant.REQUEST_CODE_CAMERA_KTP;

/**
 * @author by alvinatin on 12/11/18.
 */

public abstract class BaseUserIdentificationStepperFragment<T extends
        UserIdentificationStepperModel> extends TkpdBaseV4Fragment {

    public final static String EXTRA_KYC_STEPPER_MODEL = "kyc_stepper_model";

    protected ImageView correctImage;
    protected ImageView wrongImage;
    protected TextView title;
    protected TextView subtitle;
    protected TextView button;
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
            projectId = getActivity().getIntent().getIntExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, 1);
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
        correctImage = view.findViewById(R.id.correct_image);
        wrongImage = view.findViewById(R.id.wrong_image);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        button = view.findViewById(R.id.button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_CAMERA_FACE) {
                String faceFile = data.getStringExtra(EXTRA_STRING_IMAGE_RESULT);
                stepperModel.setFaceFile(faceFile);
                stepperListener.goToNextPage(stepperModel);

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
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    protected abstract void setContentView();
}
