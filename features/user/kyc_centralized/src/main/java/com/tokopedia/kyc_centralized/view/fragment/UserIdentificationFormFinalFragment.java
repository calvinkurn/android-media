package com.tokopedia.kyc_centralized.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity;
import com.tokopedia.kyc_centralized.view.di.DaggerUserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.view.di.UserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.user_identification_common.KycUrl;
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics;
import com.tokopedia.user_identification_common.subscriber.GetKtpStatusSubscriber;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;

import static com.tokopedia.kyc_centralized.view.fragment.UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP;
import static com.tokopedia.user_identification_common.KYCConstant.EXTRA_STRING_IMAGE_RESULT;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_FACE;
import static com.tokopedia.user_identification_common.KYCConstant.REQUEST_CODE_CAMERA_KTP;

/**
 * @author by alvinatin on 15/11/18.
 */

public class UserIdentificationFormFinalFragment extends BaseDaggerFragment
        implements UserIdentificationUploadImage.View,
        GetKtpStatusSubscriber.GetKtpStatusListener,
        UserIdentificationFormActivity.Listener {

    private ConstraintLayout loadingLayout;
    private ConstraintLayout mainLayout;
    private ImageView imageKtp;
    private ImageView imageFace;
    private ImageView resultImageKtp;
    private ImageView resultImageFace;
    private TextView resultTextKtp;
    private TextView resultTextFace;
//    private TextView buttonKtp;
//    private TextView buttonFace;
    private LinearLayout bulletTextLayout;
    private TextView info;
    private TextView subtitle;
    private TextView uploadButton;
    private UserIdentificationStepperModel stepperModel;

    private StepperListener stepperListener;
    private UserIdentificationCommonAnalytics analytics;

    private static int projectId;

    @Inject
    UserIdentificationUploadImage.Presenter presenter;

    public static Fragment createInstance(int projectid) {
        UserIdentificationFormFinalFragment fragment = new UserIdentificationFormFinalFragment();
        projectId = projectid;
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
            analytics = UserIdentificationCommonAnalytics.createInstance(getActivity().getIntent().getIntExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, 1));
        }
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
        initView(view);
        setContentView();
        if (projectId == 4) //TradeIn project Id
            uploadButton.setText(R.string.upload_button_tradein);
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
            UserIdentificationCommonComponent daggerUserIdentificationComponent =
                    DaggerUserIdentificationCommonComponent.builder()
                            .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                            .build();

            daggerUserIdentificationComponent.inject(this);
            presenter.attachView(this);
        }
    }

    private void openCameraView(int viewMode, int requestCode){
        analytics.eventClickChangeKtpFinalFormPage();
        Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                viewMode);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
        startActivityForResult(intent, requestCode);
    }

    private void setContentView() {
        loadingLayout.setVisibility(View.GONE);
//        setImageKtp(stepperModel.getKtpFile());
//        setImageFace(stepperModel.getFaceFile());
        int state = 1;
        switch (state) {
            case 0 : {
                setResultViews(KycUrl.KTP_VERIF_OK,
                        KycUrl.FACE_VERIF_FAIL,
                        getString(R.string.kyc_ktp_ok_face_fail_verification_subtitle),
                        getString(R.string.kyc_ktp_ok_face_fail_verification_info),
                        ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_f531353b, null),
                        null,
                        getString(R.string.kyc_ktp_ok_face_fail_button));
                break;
            }
            case 1 : {
                setResultViews(KycUrl.KTP_VERIF_FAIL,
                        KycUrl.FACE_VERIF_FAIL,
                        getString(R.string.kyc_ktp_face_fail_verification_subtitle),
                        getString(R.string.kyc_ktp_face_fail_verification_info),
                        null,
                        null,
                        getString(R.string.kyc_ktp_face_fail_button));
                ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.kyc_ktp_face_fail_info_1), getContext(), bulletTextLayout);
                ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.kyc_ktp_face_fail_info_2), getContext(), bulletTextLayout);
                ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.kyc_ktp_face_fail_info_3), getContext(), bulletTextLayout);
                break;
            }
            case 2 : {
                setResultViews(KycUrl.KTP_VERIF_FAIL,
                        KycUrl.FACE_VERIF_OK,
                        getString(R.string.kyc_ktp_fail_face_ok_verification_subtitle),
                        getString(R.string.kyc_ktp_fail_face_ok_verification_info),
                        null,
                        ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_f531353b, null),
                        getString(R.string.kyc_ktp_fail_face_ok_button));
                ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.kyc_ktp_fail_face_ok_info_1), getContext(), bulletTextLayout);
                ((UserIdentificationFormActivity) getActivity()).setTextViewWithBullet(getString(R.string.kyc_ktp_fail_face_ok_info_2), getContext(), bulletTextLayout);
                break;
            }
        }
//        generateLink();
//        buttonKtp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCameraView(PARAM_VIEW_MODE_KTP, REQUEST_CODE_CAMERA_KTP);
//            }
//        });

//        buttonFace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCameraView(PARAM_VIEW_MODE_FACE, REQUEST_CODE_CAMERA_FACE);
//            }
//        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickUploadPhotos();
                checkKtp();
            }
        });
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_form_fail_verification));
//                    .updateToolbarTitle(getString(R.string.title_kyc_form_upload));
        }
    }

    private void setResultViews(String urlKtp, String urlFace, String subtitleText, String infoText, Integer colorKtp, Integer colorFace, String buttonText){
        ImageHandler.LoadImage(resultImageKtp, urlKtp);
        ImageHandler.LoadImage(resultImageFace, urlFace);
        if(colorKtp != null){
            resultTextKtp.setTextColor(colorKtp);
        }
        if(colorFace != null){
            resultTextFace.setTextColor(colorFace);
        }
        subtitle.setGravity(Gravity.LEFT);
        subtitle.setText(subtitleText);
        info.setGravity(Gravity.LEFT);
        info.setText(infoText);
        uploadButton.setText(buttonText);
//        subtitle.setText(getResources().getString(R.string.form_final_subtitle));
    }

    private void checkKtp(){
        showLoading();
//        presenter.checkKtp(stepperModel.getKtpFile());
    }

    private void uploadImage() {
        showLoading();
//        presenter.uploadImage(stepperModel, projectId);
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
        loadingLayout = view.findViewById(R.id.user_identification_final_loading_layout);
        mainLayout = view.findViewById(R.id.layout_main);
        imageKtp = view.findViewById(R.id.image_ktp);
        imageFace = view.findViewById(R.id.image_face);
        resultImageKtp = view.findViewById(R.id.result_image_ktp);
        resultImageFace = view.findViewById(R.id.result_image_face);
        resultTextKtp = view.findViewById(R.id.result_text_ktp);
        resultTextFace = view.findViewById(R.id.result_text_face);
        bulletTextLayout = view.findViewById(R.id.layout_info_bullet);
//        buttonKtp = view.findViewById(R.id.change_ktp);
//        buttonFace = view.findViewById(R.id.change_face);
        subtitle = view.findViewById(R.id.text_subtitle);
        info = view.findViewById(R.id.text_info);
        uploadButton = view.findViewById(R.id.upload_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String imagePath = data.getStringExtra(EXTRA_STRING_IMAGE_RESULT);
            switch (requestCode) {
                case REQUEST_CODE_CAMERA_KTP:
                    stepperModel.setKtpFile(imagePath);
                    setImageKtp(imagePath);
                    hideKtpInvalidView();
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

//    private void generateLink() {
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                analytics.eventClickTermsFinalFormPage();
//                RouteManager.route(getActivity(), KycCommonUrl.APPLINK_TERMS_AND_CONDITION);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//                ds.setColor(getResources().getColor(R.color.kyc_centralized_42b549));
//            }
//        };
//
//        SpannableString infoText = new SpannableString(info.getText());
//        String linked = getResources().getString(R.string.terms_and_condition);
//        int startIndex = info.getText().toString().indexOf(linked);
//        infoText.setSpan(clickableSpan, startIndex, startIndex + linked.length(), Spanned
//                .SPAN_EXCLUSIVE_EXCLUSIVE);
//        info.setHighlightColor(Color.TRANSPARENT);
//        info.setMovementMethod(LinkMovementMethod.getInstance());
//        info.setText(infoText, TextView.BufferType.SPANNABLE);
//    }

    @Override
    public void onSuccessUpload() {
        hideLoading();
        getActivity().setResult(Activity.RESULT_OK);
        analytics.eventClickUploadPhotosTradeIn("success");
        stepperListener.finishPage();
    }

    @Override
    public void onErrorUpload(String error) {
        hideLoading();
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity()).showError(error, this::uploadImage);
            analytics.eventClickUploadPhotosTradeIn("failed");
        }
    }

    @Override
    public GetKtpStatusSubscriber.GetKtpStatusListener getKtpStatusListener() {
        return this;
    }

    @Override
    public void showLoading() {
//        imageKtp.setVisibility(View.GONE);
//        imageFace.setVisibility(View.GONE);
////        buttonKtp.setVisibility(View.GONE);
////        buttonFace.setVisibility(View.GONE);
//        info.setVisibility(View.GONE);
//        subtitle.setVisibility(View.GONE);
//        uploadButton.setVisibility(View.GONE);
//        resultImageKtp.setVisibility(View.GONE);
//        resultImageFace.setVisibility(View.GONE);
//        resultTextKtp.setVisibility(View.GONE);
//        resultTextFace.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
//        imageKtp.setVisibility(View.VISIBLE);
//        imageFace.setVisibility(View.VISIBLE);
////        buttonKtp.setVisibility(View.VISIBLE);
////        buttonFace.setVisibility(View.VISIBLE);
//        info.setVisibility(View.VISIBLE);
//        subtitle.setVisibility(View.VISIBLE);
//        uploadButton.setVisibility(View.VISIBLE);
//        resultImageKtp.setVisibility(View.VISIBLE);
//        resultImageFace.setVisibility(View.VISIBLE);
//        resultTextKtp.setVisibility(View.VISIBLE);
//        resultTextFace.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void hideKtpInvalidView(){
        uploadButton.setText(getResources().getString(R.string.upload_button));
        uploadButton.setOnClickListener(v -> {
            analytics.eventClickUploadPhotos();
            checkKtp();
        });
        hideLoading();
    }

    private void showKtpInvalidView(){
        subtitle.setText(getResources().getString(R.string.form_final_subtitle_fail));
        uploadButton.setText(getResources().getString(R.string.kyc_centralized_retake_photo));
        uploadButton.setOnClickListener(v -> {
            openCameraView(PARAM_VIEW_MODE_KTP, REQUEST_CODE_CAMERA_KTP);
        });
//        info.setText(R.string.form_reupload_info);
//        generateLink();
//        buttonKtp.setVisibility(View.GONE);
//        buttonFace.setVisibility(View.GONE);
    }

    @Override
    public void trackOnBackPressed() {
        analytics.eventClickBackFinalForm();
    }

    @Override
    public void onErrorGetKtpStatus(@NotNull Throwable error) {
        hideLoading();
        String errMsg = ErrorHandler.getErrorMessage(getActivity(), error);
        ((UserIdentificationFormActivity) getActivity()).showError(errMsg, this::checkKtp);
    }

    @Override
    public void onKtpInvalid(@NotNull String message) {
        hideLoading();
        showKtpInvalidView();
    }

    @Override
    public void onKtpValid() {
        hideLoading();
        uploadImage();
    }
}
