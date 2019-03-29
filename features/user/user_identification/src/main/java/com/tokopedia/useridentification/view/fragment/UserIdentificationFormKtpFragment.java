package com.tokopedia.useridentification.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.useridentification.KycUrl;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.useridentification.view.activity.UserIdentificationFormActivity;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_KTP;
import static com.tokopedia.useridentification.view.fragment.UserIdentificationCameraFragment
        .PARAM_VIEW_MODE_KTP;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormKtpFragment extends
        BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>
        implements UserIdentificationFormActivity.Listener {

    public static Fragment createInstance() {
        Fragment fragment = new UserIdentificationFormKtpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        title.setText(R.string.ktp_title);
        subtitle.setText(R.string.ktp_subtitle);
        button.setText(R.string.ktp_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickNextKtpPage();
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                        PARAM_VIEW_MODE_KTP);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_KTP);
            }
        });
        ImageHandler.LoadImage(correctImage, KycUrl.KTP_OK);
        ImageHandler.LoadImage(wrongImage, KycUrl.KTP_FAIL);
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_form_ktp));
        }
    }

    @Override
    public void trackOnBackPressed() {
        analytics.eventClickBackKtpPage();
    }
}
