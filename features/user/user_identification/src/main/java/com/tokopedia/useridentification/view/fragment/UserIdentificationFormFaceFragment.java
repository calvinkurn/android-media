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

import static com.tokopedia.useridentification.view.KYCConstant.EXTRA_STRING_FACE;

/**
 * @author by alvinatin on 09/11/18.
 */

public class UserIdentificationFormFaceFragment extends BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>{

    public static Fragment createInstance(){
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
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(), 1);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA
                && resultCode == Activity.RESULT_OK) {
            if (getActivity() != null && getActivity().getIntent() != null) {
                Bundle bundle = getActivity().getIntent().getExtras();
                if (bundle != null) {
                    stepperModel.setFaceFile(bundle.getString(EXTRA_STRING_FACE));
                    stepperListener.goToNextPage(stepperModel);
                } else {
                    Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
