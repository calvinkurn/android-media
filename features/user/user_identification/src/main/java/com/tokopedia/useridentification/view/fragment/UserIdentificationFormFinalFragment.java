package com.tokopedia.useridentification.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.user_identification_common.KycCommonUrl;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.analytics.UserIdentificationAnalytics;
import com.tokopedia.useridentification.di.DaggerUserIdentificationComponent;
import com.tokopedia.useridentification.di.UserIdentificationComponent;
import com.tokopedia.useridentification.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.useridentification.view.activity.UserIdentificationFormActivity;
import com.tokopedia.useridentification.view.activity.UserIdentificationInfoActivity;
import com.tokopedia.useridentification.view.listener.UserIdentificationUploadImage;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

import java.io.File;

import javax.inject.Inject;

import static com.tokopedia.user_identification_common.KYCConstant.EXTRA_STRING_IMAGE_RESULT;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_FACE;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_KTP;
import static com.tokopedia.useridentification.view.fragment.UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE;
import static com.tokopedia.useridentification.view.fragment.UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP;

/**
 * @author by alvinatin on 15/11/18.
 */

public class UserIdentificationFormFinalFragment extends BaseDaggerFragment
        implements UserIdentificationUploadImage.View, UserIdentificationFormActivity.Listener {

    public static final String PARAM_PROJECTID = "TRADEIN_PROJECT";

    private ImageView imageKtp;
    private ImageView imageFace;
    private TextView buttonKtp;
    private TextView buttonFace;
    private TextView info;
    private TextView uploadButton;
    private View progressBar;
    private UserIdentificationStepperModel stepperModel;

    private StepperListener stepperListener;
    private UserIdentificationAnalytics analytics;

    private int projectId;

    @Inject
    UserIdentificationUploadImage.Presenter presenter;

    public static Fragment createInstance(int projectid) {
        UserIdentificationFormFinalFragment fragment = new UserIdentificationFormFinalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_PROJECTID, projectid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof StepperListener) {
            stepperListener = (StepperListener) getContext();
        }
        if (getArguments() != null && savedInstanceState == null) {
            stepperModel = getArguments().getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
        } else if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(BaseUserIdentificationStepperFragment
                    .EXTRA_KYC_STEPPER_MODEL);
        }
        if (getActivity() != null) {
            analytics = UserIdentificationAnalytics.createInstance(getActivity()
                    .getApplicationContext());
        }
        projectId = getArguments().getInt(PARAM_PROJECTID, -1);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(BaseUserIdentificationStepperFragment.EXTRA_KYC_STEPPER_MODEL, stepperModel);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_identification_final, container, false);
        if (projectId == 4) //TradeIn project Id
            uploadButton.setText(R.string.upload_button_tradein);
        initView(view);
        setContentView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideLoading();
        analytics.eventViewFinalForm();
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            UserIdentificationComponent daggerUserIdentificationComponent =
                    DaggerUserIdentificationComponent.builder()
                            .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                            .build();

            daggerUserIdentificationComponent.inject(this);
            presenter.attachView(this);
        }
    }

    private void setContentView() {
        setImageKtp(stepperModel.getKtpFile());
        setImageFace(stepperModel.getFaceFile());
        generateLink();
        buttonKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickChangeKtpFinalFormPage();
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                        PARAM_VIEW_MODE_KTP);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_KTP);
            }
        });

        buttonFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickChangeSelfieFinalFormPage();
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                        PARAM_VIEW_MODE_FACE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickUploadPhotos();
                uploadImage();
            }
        });
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_form_upload));
        }
    }

    private void uploadImage() {
        showLoading();
        presenter.uploadImage(stepperModel, projectId);
    }

    private void setImageKtp(String imagePath) {
        File ktpFile = new File(imagePath);
        if (ktpFile.exists()) {
            ImageHandler.loadImageFromFile(getContext(), imageKtp, ktpFile);
        }
    }

    private void setImageFace(String imagePath) {
        File faceFile = new File(imagePath);
        if (faceFile.exists()) {
            ImageHandler.loadImageFromFile(getContext(), imageFace, faceFile);
        }
    }

    private void initView(View view) {
        imageKtp = view.findViewById(R.id.image_ktp);
        imageFace = view.findViewById(R.id.image_face);
        buttonKtp = view.findViewById(R.id.change_ktp);
        buttonFace = view.findViewById(R.id.change_face);
        info = view.findViewById(R.id.text_info);
        uploadButton = view.findViewById(R.id.upload_button);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String imagePath = data.getStringExtra(EXTRA_STRING_IMAGE_RESULT);
            switch (requestCode) {
                case REQUEST_CODE_CAMERA_KTP:
                    stepperModel.setKtpFile(imagePath);
                    setImageKtp(imagePath);
                    break;
                case REQUEST_CODE_CAMERA_FACE:
                    stepperModel.setFaceFile(imagePath);
                    setImageFace(imagePath);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void generateLink() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                analytics.eventClickTermsFinalFormPage();
                RouteManager.route(getActivity(), KycCommonUrl.APPLINK_TERMS_AND_CONDITION);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.tkpd_main_green));
            }
        };

        SpannableString infoText = new SpannableString(info.getText());
        String linked = getResources().getString(R.string.terms_and_condition);
        int startIndex = info.getText().toString().indexOf(linked);
        infoText.setSpan(clickableSpan, startIndex, startIndex + linked.length(), Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        info.setHighlightColor(Color.TRANSPARENT);
        info.setMovementMethod(LinkMovementMethod.getInstance());
        info.setText(infoText, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void goToNextActivity() {
        Intent intent = new Intent(getContext(), UserIdentificationInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccessUpload() {
        hideLoading();
        getActivity().setResult(Activity.RESULT_OK);
        stepperListener.finishPage();
    }

    @Override
    public void onErrorUpload(String error) {
        hideLoading();
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity()).showError(error, this::uploadImage);
        }
    }

    @Override
    public void showLoading() {
        imageKtp.setVisibility(View.GONE);
        imageFace.setVisibility(View.GONE);
        buttonKtp.setVisibility(View.GONE);
        buttonFace.setVisibility(View.GONE);
        info.setVisibility(View.GONE);
        uploadButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        imageKtp.setVisibility(View.VISIBLE);
        imageFace.setVisibility(View.VISIBLE);
        buttonKtp.setVisibility(View.VISIBLE);
        buttonFace.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        uploadButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void trackOnBackPressed() {
        analytics.eventClickBackFinalForm();
    }
}
