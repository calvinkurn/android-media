package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class FragmentSelfieIdPreviewAndUpload extends BaseDaggerFragment implements View.OnClickListener{
    private ActivityListener activityListener;
    private View selfieIdIntroView;
    private View selfieIdPreviewAndUpload;
    private Button selfieIdIntroOkBtn;
    private Button useImageBtn;
    private Button retakeImageBtn;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.selfieid_intro_proceed){

        }
        else if(i == R.id.use_img){

        }
        else if(i == R.id.retake_image){

        }
    }

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);

    }

    @Override
    protected String getScreenName() {
        return Constants.Values.KYC_SELFIEID_PREVIEW_AND_UOLOAD_SCR;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityListener.setHeaderTitle(Constants.Values.OVOUPGRADE_STEP_2_TITLE);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        activityListener = (ActivityListener)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.selfieid_preview, container, false);
        selfieIdIntroView = view.findViewById(R.id.selfieid_intro_container);
        selfieIdPreviewAndUpload = view.findViewById(R.id.selfieid_preview_container);
        selfieIdIntroOkBtn = view.findViewById(R.id.selfieid_intro_proceed);
        selfieIdIntroOkBtn.setOnClickListener(this::onClick);
        useImageBtn = view.findViewById(R.id.use_img);
        useImageBtn.setOnClickListener(this::onClick);
        retakeImageBtn = view.findViewById(R.id.retake_image);
        retakeImageBtn.setOnClickListener(this::onClick);
        return view;
    }
}
