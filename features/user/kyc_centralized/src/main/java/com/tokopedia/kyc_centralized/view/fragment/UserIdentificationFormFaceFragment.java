package com.tokopedia.kyc_centralized.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.user_identification_common.KycUrl;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;

import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_FACE;
import static com.tokopedia.kyc_centralized.view.fragment.UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE;

/**
 * @author by alvinatin on 09/11/18.
 */

public class UserIdentificationFormFaceFragment extends
        BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>
        implements UserIdentificationFormActivity.Listener {

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickNextSelfiePage();
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                        PARAM_VIEW_MODE_FACE);
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
            }
        });
        ImageHandler.LoadImage(correctImage, KycUrl.SELFIE_OK);
        ImageHandler.LoadImage(wrongImage, KycUrl.SELFIE_FAIL);
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_form_face));
        }
    }

    @Override
    public void trackOnBackPressed() {
        analytics.eventClickBackSelfiePage();
    }
}
