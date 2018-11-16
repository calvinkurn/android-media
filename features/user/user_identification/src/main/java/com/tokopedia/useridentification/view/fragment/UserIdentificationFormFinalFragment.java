package com.tokopedia.useridentification.view.fragment;

import android.graphics.BitmapFactory;
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

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

/**
 * @author by alvinatin on 15/11/18.
 */

public class UserIdentificationFormFinalFragment extends BaseDaggerFragment {

    private ImageView imageKtp;
    private ImageView imageFace;
    private TextView buttonKtp;
    private TextView buttonFace;
    private TextView info;
    private TextView nextButton;
    private UserIdentificationStepperModel stepperModel;

    private StepperListener stepperListener;

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
        if (getArguments()!= null && savedInstanceState == null) {
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
        imageKtp.setImageBitmap(BitmapFactory.decodeFile(stepperModel.getKtpFile()));
        imageFace.setImageBitmap(BitmapFactory.decodeFile(stepperModel.getFaceFile()));
        generateLink();
        buttonKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO alvin add go to camera activity
            }
        });

        buttonFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO alvin add go to camera activity
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepperListener.finishPage();
            }
        });
    }

    private void initView(View view) {
        imageKtp = view.findViewById(R.id.image_ktp);
        imageFace = view.findViewById(R.id.image_face);
        buttonKtp = view.findViewById(R.id.change_ktp);
        buttonFace = view.findViewById(R.id.change_face);
        info = view.findViewById(R.id.text_info);
        nextButton = view.findViewById(R.id.next_button);
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
        infoText.setSpan(clickableSpan, startIndex, startIndex + linked.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        info.setHighlightColor(Color.TRANSPARENT);
        info.setMovementMethod(LinkMovementMethod.getInstance());
        info.setText(infoText, TextView.BufferType.SPANNABLE);
    }
}
