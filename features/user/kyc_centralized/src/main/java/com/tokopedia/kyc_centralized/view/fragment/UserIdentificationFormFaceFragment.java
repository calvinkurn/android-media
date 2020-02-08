package com.tokopedia.kyc_centralized.view.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieTask;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity;
import com.tokopedia.kyc_centralized.view.di.DaggerUserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.view.di.UserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user_identification_common.KycUrl;

import javax.inject.Inject;

import static com.tokopedia.kyc_centralized.view.fragment.UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_FACE;

/**
 * @author by alvinatin on 09/11/18.
 */

public class UserIdentificationFormFaceFragment extends
        BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>
        implements UserIdentificationFormActivity.Listener {

    @Inject
    protected RemoteConfig remoteConfig;

    public static Fragment createInstance() {
        Fragment fragment = new UserIdentificationFormFaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        analytics.eventViewSelfiePage();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void setContentView() {
        title.setText(R.string.face_title);
        subtitle.setText(R.string.face_subtitle);
        button.setText(R.string.face_button);
        button.setOnClickListener(v -> {
            analytics.eventClickNextSelfiePage();
            isKycSelfie = remoteConfig.getBoolean(RemoteConfigKey.KYC_USING_SELFIE, false);
            if(isKycSelfie){
                goToKycSelfie();
            } else {
                goToKycLiveness();
            }
        });
        setLottieAnimation();
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_form_face));
        }
    }

    private void setLottieAnimation(){
        LottieTask<LottieComposition> lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(requireContext(), KycUrl.SCAN_FACE);
        lottieCompositionLottieTask.addListener(result -> {
            onboardingImage.setComposition(result);
            onboardingImage.setRepeatCount(ValueAnimator.INFINITE);
            onboardingImage.playAnimation();
        });
    }

    private void goToKycSelfie(){
        Intent intent = UserIdentificationCameraActivity.createIntent(getContext(), PARAM_VIEW_MODE_FACE);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
    }

    private void goToKycLiveness(){
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.LIVENESS_DETECTION);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_KTP_PATH, stepperModel.getKtpFile());
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
    }

    @Override
    public void trackOnBackPressed() {
        analytics.eventClickBackSelfiePage();
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            UserIdentificationCommonComponent daggerUserIdentificationComponent =
                    DaggerUserIdentificationCommonComponent.builder()
                            .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                            .build();

            daggerUserIdentificationComponent.inject(this);
        }
    }
}
