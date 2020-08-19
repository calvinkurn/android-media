package com.tokopedia.kyc_centralized.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.imagepicker.common.util.FileUtils;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity;
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity;
import com.tokopedia.kyc_centralized.di.DaggerUserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.KycCommonUrl;
import com.tokopedia.user_identification_common.KycUrl;
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics;
import com.tokopedia.user_identification_common.subscriber.GetKtpStatusSubscriber;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

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
    private ImageView resultImageKtp;
    private ImageView resultImageFace;
    private TextView resultTextKtp;
    private TextView resultTextFace;
    private LinearLayout bulletTextLayout;
    private TextView info;
    private TextView subtitle;
    private TextView uploadButton;
    private UserIdentificationStepperModel stepperModel;

    private StepperListener stepperListener;
    private UserIdentificationCommonAnalytics analytics;

    private ArrayList<Integer> listRetake;

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
        Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                viewMode);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
        startActivityForResult(intent, requestCode);
    }

    private void openLivenessView(){
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.LIVENESS_DETECTION);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_KTP_PATH, stepperModel.getKtpFile());
        if(!stepperModel.getListRetake().contains(2)){
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH, stepperModel.getFaceFile());
        }
        startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
    }

    private void setContentView() {
        loadingLayout.setVisibility(View.GONE);
        if(isKycSelfie()){
            setKycSelfieView();
        } else {
            setKycLivenessView();
        }
    }

    private void setKycSelfieView(){
        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_form_upload));
        }
        setResultViews(KycUrl.KTP_VERIF_OK, KycUrl.FACE_VERIF_OK, "", getString(R.string.form_final_info),
                ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_f531353b, null),
                ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_f531353b, null),
                getString(R.string.upload_button));
        generateLink();
        uploadButton.setOnClickListener(v -> {
            analytics.eventClickUploadPhotos();
            checkKtp();
        });
    }

    private void setKycLivenessView(){
        listRetake = stepperModel.getListRetake();
        String imageKtp = KycUrl.KTP_VERIF_OK;
        String imageFace = KycUrl.FACE_VERIF_OK;
        Integer colorKtp = ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_f531353b, null);
        Integer colorFace = ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_f531353b, null);

        if(listRetake != null) {
            for(int i=0; i<listRetake.size(); i++){
                switch (listRetake.get(i)){
                    case KYCConstant.KTP_RETAKE : {
                        imageKtp = KycUrl.KTP_VERIF_FAIL;
                        colorKtp = null;
                        setKtpUploadButtonListener();
                        break;
                    }
                    case KYCConstant.FACE_RETAKE : {
                        imageFace = KycUrl.FACE_VERIF_FAIL;
                        colorFace = null;
                        setFaceUploadButtonListener();
                        break;
                    }
                }
            }

            if(listRetake.size() == 2){
                setKtpFaceUploadButtonListener();
            }
        }

        setResultViews(imageKtp, imageFace, stepperModel.getTitleText(), stepperModel.getSubtitleText(), colorKtp, colorFace, stepperModel.getButtonText());

        if (getActivity() instanceof UserIdentificationFormActivity) {
            ((UserIdentificationFormActivity) getActivity())
                    .updateToolbarTitle(getString(R.string.title_kyc_form_fail_verification));
        }
    }

    private void setKtpUploadButtonListener(){
        uploadButton.setOnClickListener(v -> {
            analytics.eventClickChangeKtpFinalFormPage();
            openCameraView(PARAM_VIEW_MODE_KTP, REQUEST_CODE_CAMERA_KTP);
        });
    }

    private void setFaceUploadButtonListener(){
        uploadButton.setOnClickListener(v -> {
            analytics.eventClickChangeSelfieFinalFormPage();
            openLivenessView();
        });
    }

    private void setKtpFaceUploadButtonListener(){
        uploadButton.setOnClickListener(v -> {
            analytics.eventClickChangeKtpSelfieFinalFormPage();
            openCameraView(PARAM_VIEW_MODE_KTP, REQUEST_CODE_CAMERA_KTP);
        });
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

        ArrayList<String> listMessage = stepperModel.getListMessage();
        if(listMessage != null){
            for (int i=0; i<listMessage.size(); i++){
                ((UserIdentificationFormActivity) Objects.requireNonNull(getActivity())).setTextViewWithBullet(listMessage.get(i), getContext(), bulletTextLayout);
            }
        }
    }

    private Boolean isKycSelfie(){
        try {
            if(UserIdentificationFormActivity.isSupportedLiveness) {
                return !RemoteConfigInstance.getInstance().getABTestPlatform().getString(KYCConstant.KYC_AB_KEYWORD).equals(KYCConstant.KYC_AB_KEYWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void checkKtp(){
        showLoading();
        presenter.checkKtp(stepperModel.getKtpFile());
    }

    private void uploadImage() {
        showLoading();
        presenter.uploadImage(stepperModel, projectId);
    }

    private void initView(View view) {
        loadingLayout = view.findViewById(R.id.user_identification_final_loading_layout);
        mainLayout = view.findViewById(R.id.layout_main);
        resultImageKtp = view.findViewById(R.id.result_image_ktp);
        resultImageFace = view.findViewById(R.id.result_image_face);
        resultTextKtp = view.findViewById(R.id.result_text_ktp);
        resultTextFace = view.findViewById(R.id.result_text_face);
        bulletTextLayout = view.findViewById(R.id.layout_info_bullet);
        subtitle = view.findViewById(R.id.text_subtitle);
        info = view.findViewById(R.id.text_info);
        uploadButton = view.findViewById(R.id.upload_button);
    }

    private void getLivenessResult(Intent data){
        boolean isSuccessRegister = data.getBooleanExtra(ApplinkConst.Liveness.EXTRA_IS_SUCCESS_REGISTER, false);
        if(!isSuccessRegister){
            stepperModel.setListRetake(data.getIntegerArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_RETAKE));
            stepperModel.setListMessage(data.getStringArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_MESSAGE));
            stepperModel.setTitleText(data.getStringExtra(ApplinkConst.Liveness.EXTRA_TITLE));
            stepperModel.setSubtitleText(data.getStringExtra(ApplinkConst.Liveness.EXTRA_SUBTITLE));
            stepperModel.setButtonText(data.getStringExtra(ApplinkConst.Liveness.EXTRA_BUTTON));
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }else{
            getActivity().setResult(Activity.RESULT_OK);
            stepperListener.finishPage();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            retakeAction(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void retakeAction(int requestCode, Intent data){
        if(!isKycSelfie()){
            switch (requestCode) {
                case REQUEST_CODE_CAMERA_KTP:
                    stepperModel.setKtpFile(data.getStringExtra(EXTRA_STRING_IMAGE_RESULT));
                    openLivenessView();
                    break;
                case REQUEST_CODE_CAMERA_FACE:
                    stepperModel.setFaceFile(data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH));
                    getLivenessResult(data);
                    break;
                default:
                    break;
            }
        } else {
            String imagePath = data.getStringExtra(EXTRA_STRING_IMAGE_RESULT);
            if(requestCode == REQUEST_CODE_CAMERA_KTP){
                stepperModel.setKtpFile(imagePath);
                analytics.eventClickUploadPhotos();
                checkKtp();
            }
        }
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
                ds.setColor(getResources().getColor(R.color.kyc_centralized_42b549));
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
    public void onSuccessUpload() {
        hideLoading();
        Timber.w("P2#KYC_SELFIE_UPLOAD_RESULT#'SuccessUpload';" +
                "ktpPath='"+ stepperModel.getKtpFile() +"';" +
                "facePath='"+ stepperModel.getFaceFile() +"';" +
                "tkpdProjectId='"+ projectId +"';");
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
        mainLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mainLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void showKtpInvalidView(){
        uploadButton.setOnClickListener(v -> {
            analytics.eventClickChangeKtpFinalFormPage();
            openCameraView(PARAM_VIEW_MODE_KTP, REQUEST_CODE_CAMERA_KTP);
        });
        setResultViews(KycUrl.KTP_VERIF_FAIL, KycUrl.FACE_VERIF_OK, getString(R.string.kyc_ktp_fail_face_ok_verification_subtitle),
                getString(R.string.kyc_ktp_fail_face_ok_verification_info),
                null, ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_f531353b, null),
                getString(R.string.kyc_ktp_fail_face_ok_button));
        ((UserIdentificationFormActivity) getActivity())
                .setTextViewWithBullet(getString(R.string.kyc_ktp_fail_face_ok_info_1), getContext(), bulletTextLayout);
        ((UserIdentificationFormActivity) getActivity())
                .setTextViewWithBullet(getString(R.string.kyc_ktp_fail_face_ok_info_2), getContext(), bulletTextLayout);
    }

    public void clickBackAction(){
        if(!isKycSelfie()){
            if(listRetake.size() == 1){
                switch (listRetake.get(0)){
                    case KYCConstant.KTP_RETAKE : {
                        analytics.eventClickBackChangeKtpFinalFormPage();
                    }
                    case KYCConstant.FACE_RETAKE : {
                        analytics.eventClickBackChangeSelfieFinalFormPage();
                    }
                }
            }else if(listRetake.size() == 2){
                analytics.eventClickBackChangeKtpSelfieFinalFormPage();
            }
        }else{
            analytics.eventClickBackFinalForm();
        }
        FileUtils.deleteFileInTokopediaFolder(stepperModel.getKtpFile());
        FileUtils.deleteFileInTokopediaFolder(stepperModel.getFaceFile());
    }

    @Override
    public void trackOnBackPressed() {}

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