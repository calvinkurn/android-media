package com.tokopedia.useridentification.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

import static com.tokopedia.user_identification_common.KYCConstant.EXTRA_STRING_IMAGE_RESULT;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_FACE;
import static com.tokopedia.useridentification.view.fragment.UserIdentificationCameraFragment
        .PARAM_VIEW_MODE_FACE;

/**
 * @author by alvinatin on 09/11/18.
 */

public class UserIdentificationFormFaceFragment extends
        BaseUserIdentificationStepperFragment<UserIdentificationStepperModel> {

    public static Fragment createInstance() {
        Fragment fragment = new UserIdentificationFormFaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                        PARAM_VIEW_MODE_FACE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
            }
        });
        ImageHandler.LoadImage(correctImage, KycUrl.SELFIE_OK);
        ImageHandler.LoadImage(wrongImage, KycUrl.SELFIE_FAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA_FACE
                && resultCode == Activity.RESULT_OK
                && data != null) {
            String faceFile = data.getStringExtra(EXTRA_STRING_IMAGE_RESULT);
            stepperModel.setFaceFile(faceFile);
            stepperListener.goToNextPage(stepperModel);

        } else {
            Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
