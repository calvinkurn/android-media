package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentSelfieIdPreviewAndUpload extends BaseDaggerFragment implements View.OnClickListener{
    private ActivityListener activityListener;
    private View selfieIdIntroView;
    private View selfieIdPreviewAndUpload;
    private Button selfieIdIntroOkBtn;
    private Button useImageBtn;
    private TextView retakeImageBtn;
    private ImageView selfieidImg;
    public static String TAG = "selfieid_preview_upload";

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.selfieid_intro_proceed){
            KycUtil.createKYCIdCameraFragment(getContext(), activityListener, getSefieIdImageAction(), Constants.Keys.KYC_SELFIEID_CAMERA);
        }
        else if(i == R.id.use_img){
            goToTandCPage();
        }
        else if(i == R.id.retake_image){
            KycUtil.createKYCIdCameraFragment(getContext(), activityListener, getSefieIdImageAction(), Constants.Keys.KYC_SELFIEID_CAMERA);
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
        selfieidImg = view.findViewById(R.id.selfieid_img);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FragmentSelfieIdPreviewAndUpload newInstance(){
        FragmentSelfieIdPreviewAndUpload fragmentSelfieIdPreviewAndUpload = new FragmentSelfieIdPreviewAndUpload();
        return fragmentSelfieIdPreviewAndUpload;
    }

    private ActionCreator getSefieIdImageAction(){
        ActionCreator<HashMap<String, Object>, Integer> actionCreator = new ActionCreator<HashMap<String, Object>, Integer>() {
            @Override
            public void actionSuccess(int actionId, HashMap<String, Object> dataObj) {
                ArrayList<String> keysList = (new CardIdDataKeyProvider()).getData(1, null);
                selfieIdIntroView.setVisibility(View.GONE);
                selfieIdPreviewAndUpload.setVisibility(View.VISIBLE);
                activityListener.showHideActionbar(true);
                KycUtil.saveDataToPersistentStore(getContext(), Constants.Keys.SELFIE_IMG_PATH,  dataObj.get(keysList.get(0)));
                KycUtil.saveDataToPersistentStore(getContext(), Constants.Keys.FLIP_SELFIE_IMG, dataObj.get(keysList.get(1)));
                KycUtil.setCameraCapturedImage((String) dataObj.get(keysList.get(0)),
                        (Boolean) dataObj.get(keysList.get(1)), selfieidImg);
            }

            @Override
            public void actionError(int actionId, Integer dataObj) {

            }
        };
        return actionCreator;
    }

    private void goToTandCPage(){
        activityListener.addReplaceFragment(FragmentTermsAndConditions.newInstance(), true,
                FragmentTermsAndConditions.TAG);
    }
}
