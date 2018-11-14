package com.tokopedia.useridentification.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormKtpFragment extends BaseUserIdentificationStepperFragment<UserIdentificationStepperModel> {

    public static Fragment createInstance(){
        Fragment fragment = new UserIdentificationFormKtpFragment();
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
        title.setText(R.string.ktp_title);
        subtitle.setText(R.string.ktp_subtitle);
        button.setText(R.string.ktp_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(), 1);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
    }
}
