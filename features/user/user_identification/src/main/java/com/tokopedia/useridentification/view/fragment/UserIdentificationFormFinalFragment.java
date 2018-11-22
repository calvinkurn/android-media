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
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.view.activity.UserIdentificationCameraActivity;
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
        implements UserIdentificationUploadImage.View{

    private ImageView imageKtp;
    private ImageView imageFace;
    private TextView buttonKtp;
    private TextView buttonFace;
    private TextView info;
    private TextView uploadButton;
    private UserIdentificationStepperModel stepperModel;

    private StepperListener stepperListener;

    @Inject
    UserIdentificationUploadImage.Presenter presenter;

    public static Fragment createInstance() {
        UserIdentificationFormFinalFragment fragment = new UserIdentificationFormFinalFragment();
        Bundle bundle = new Bundle();
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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_identification_final, container, false);
        initView(view);
        setContentView();
        return view;
    }

    @Override
    protected void initInjector() {

    }

    private void setContentView() {
        setImageKtp(stepperModel.getKtpFile());
        setImageFace(stepperModel.getFaceFile());
        generateLink();
        buttonKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                        PARAM_VIEW_MODE_KTP);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_KTP);
            }
        });

        buttonFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserIdentificationCameraActivity.createIntent(getContext(),
                        PARAM_VIEW_MODE_FACE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_FACE);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.uploadImage(stepperModel);
            }
        });
    }

    private void setImageKtp(String imagePath) {
        File ktpFile = new File(imagePath);
        if (ktpFile.exists()) {
            ImageHandler.loadImageFromFile(getContext(), imageKtp, ktpFile);
        } else {
            Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    private void setImageFace(String imagePath) {
        File faceFile = new File(imagePath);
        if (faceFile.exists()) {
            ImageHandler.loadImageFromFile(getContext(), imageFace, faceFile);
        } else {
            Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(View view) {
        imageKtp = view.findViewById(R.id.image_ktp);
        imageFace = view.findViewById(R.id.image_face);
        buttonKtp = view.findViewById(R.id.change_ktp);
        buttonFace = view.findViewById(R.id.change_face);
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
                    break;
                case REQUEST_CODE_CAMERA_FACE:
                    stepperModel.setFaceFile(imagePath);
                    setImageFace(imagePath);
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_LONG).show();
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
                //TODO alvin add url webview
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
        Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_LONG).show();
    }
}
