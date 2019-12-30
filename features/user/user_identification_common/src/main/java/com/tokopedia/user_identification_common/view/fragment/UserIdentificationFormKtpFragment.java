package com.tokopedia.user_identification_common.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.user_identification_common.KycUrl;
import com.tokopedia.user_identification_common.R;
import com.tokopedia.user_identification_common.view.activity.UserIdentificationFormActivity;
import com.tokopedia.user_identification_common.view.viewmodel.UserIdentificationStepperModel;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormKtpFragment extends
        BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>
        implements UserIdentificationFormActivity.Listener {

    private static int REQUEST_CODE_CAMERA_KTP = 1001;

    protected TextView subtitleBody;

    public static Fragment createInstance() {
        Fragment fragment = new UserIdentificationFormKtpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        subtitleBody = view.findViewById(R.id.subtitle_body);
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
        subtitle.setText(MethodChecker.fromHtml(getString(R.string.ktp_subtitle)));
        subtitle.setGravity(Gravity.LEFT);
        subtitleBody.setText(MethodChecker.fromHtml(getString(R.string.ktp_body)));
        subtitleBody.setVisibility(View.VISIBLE);
        button.setText(R.string.ktp_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                analytics.eventClickNextKtpPage();
//                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
//                        PARAM_VIEW_MODE_KTP);
//                intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
//                startActivityForResult(intent, REQUEST_CODE_CAMERA_KTP);
                Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.LIVENESS_DETECTION);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void trackOnBackPressed() {
        analytics.eventClickBackKtpPage();
    }
}
