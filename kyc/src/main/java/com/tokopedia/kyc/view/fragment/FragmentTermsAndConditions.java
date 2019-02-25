package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class FragmentTermsAndConditions extends BaseDaggerFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivityListener activityListener;
    private View cardIdContainer;
    private View selfieIdContainer;
    private CheckBox tncCheckBox;
    private Button proceedFurther;
    private ImageView cardIdImage;
    private ImageView selfieIdImage;
    public static String TAG = "tandc_page";
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.cardid_container){

        }
        else if(i == R.id.selfieid_container){

        }
        else if(i == R.id.proceed_tnc){

        }
    }

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.KYC_TNC_ACCEPTANDSUBMIT_SCR;
    }

    public static FragmentTermsAndConditions newInstance(){
        FragmentTermsAndConditions fragmentTermsAndConditions = new FragmentTermsAndConditions();
        return fragmentTermsAndConditions;
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
        View view = inflater.inflate(R.layout.kyc_ids_tnc_page, container, false);
        cardIdContainer = view.findViewById(R.id.cardid_container);
        selfieIdContainer = view.findViewById(R.id.selfieid_container);
        tncCheckBox = view.findViewById(R.id.chkbx_tnc);
        tncCheckBox.setOnCheckedChangeListener(this);
        proceedFurther = view.findViewById(R.id.proceed_tnc);
        cardIdImage = view.findViewById(R.id.cardid_img);
        selfieIdImage = view.findViewById(R.id.selfieid_img);
        setImages();
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.chkbx_tnc){
            if(isChecked) proceedFurther.setEnabled(true);
            else proceedFurther.setEnabled(false);
        }
    }

    private void setImages(){
        KycUtil.setCameraCapturedImage(
                (String) KycUtil.getDataFromPersistentStore(getContext(),
                        Constants.Keys.CARD_IMG_PATH, String.class,""),
                (boolean) KycUtil.getDataFromPersistentStore(getContext(),
                        Constants.Keys.FLIP_CARD_IMG, Boolean.TYPE, false), cardIdImage);
    }
}
