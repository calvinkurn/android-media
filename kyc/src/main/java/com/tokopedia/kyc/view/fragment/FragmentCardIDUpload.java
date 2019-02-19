package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class FragmentCardIDUpload extends BaseDaggerFragment implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView idCardImageView;
    private RadioButton KTPWNISelection;
    private RadioButton passportSelection;
    private TkpdHintTextInputLayout wrapperNumberKtp;
    private EditText edtxtNumber;
    private TkpdHintTextInputLayout wrapperName;
    private EditText edtxtName;
    private Button confirmationBtn;
    private String TAG = "cardId_upload";

    private ActivityListener activityListener;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.confirmation_btn){

        }
    }

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.KYC_CARDID_UPLOAD_SCR;
    }

    public static FragmentCardIDUpload newInstance(){
        FragmentCardIDUpload fragmentCardIDUpload = new FragmentCardIDUpload();
        return fragmentCardIDUpload;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kyc_idcard_form, container, false);
        idCardImageView = view.findViewById(R.id.card_id_img);
        KTPWNISelection = view.findViewById(R.id.KTP_WNI);
        KTPWNISelection.setOnCheckedChangeListener(this);
        passportSelection = view.findViewById(R.id.passport);
        passportSelection.setOnCheckedChangeListener(this);
        wrapperNumberKtp = view.findViewById(R.id.wrapper_number_ktp);
        edtxtNumber = view.findViewById(R.id.edtxt_number);
        wrapperName = view.findViewById(R.id.wrapper_name);
        edtxtName = view.findViewById(R.id.edtxt_name);
        confirmationBtn = view.findViewById(R.id.confirmation_btn);
        confirmationBtn.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.KTP_WNI) {
        }
        else if(i == R.id.passport){

        }
    }

}
