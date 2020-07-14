package com.tokopedia.kyc_centralized.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.imagepicker.common.util.FileUtils;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;
import com.tokopedia.user_identification_common.KycUrl;

import static com.tokopedia.kyc_centralized.view.fragment.UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_KTP;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormKtpFragment extends
        BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>
        implements UserIdentificationFormActivity.Listener {

    protected LinearLayout bulletTextLayout;

    public static Fragment createInstance() {
        Fragment fragment = new UserIdentificationFormKtpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        bulletTextLayout = view.findViewById(R.id.layout_info_bullet);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        analytics.eventViewKtpPage();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void setContentView() {
        int paddingDp = 16;
        float scale = getResources().getDisplayMetrics().density;
        onboardingImage.setPadding(0, (int) (paddingDp * scale + 0.5F), 0, 0);
        setTextView();
        setButtonView();
        ImageHandler.LoadImage(onboardingImage, KycUrl.SCAN_KTP);
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_info));
        }
    }

    private void setTextView(){
        title.setText(R.string.ktp_title);
        subtitle.setText(MethodChecker.fromHtml(getString(R.string.ktp_subtitle)));
        subtitle.setGravity(Gravity.LEFT);
        ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.ktp_body_1), getContext(), bulletTextLayout);
        ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.ktp_body_2), getContext(), bulletTextLayout);
        ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.ktp_body_3), getContext(), bulletTextLayout);
    }

    private void setButtonView(){
        button.setText(R.string.ktp_button);
        button.setOnClickListener(v -> {
            analytics.eventClickNextKtpPage();
            Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                    PARAM_VIEW_MODE_KTP);
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
            startActivityForResult(intent, REQUEST_CODE_CAMERA_KTP);
        });
    }

    @Override
    protected void initInjector() {}

    @Override
    public void trackOnBackPressed() {
        FileUtils.deleteFileInTokopediaFolder(stepperModel.getKtpFile());
        analytics.eventClickBackKtpPage();
    }
}
