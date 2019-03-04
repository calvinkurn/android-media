package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.domain.UploadDocumentUseCase;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.model.KYCDocumentUploadResponse;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.view.interfaces.LoaderUiListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;


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
    private Button retakePhoto;
    private String imagePath;
    private String docType = "KTP";
    private boolean toBeFlipped;
    private int kycReqId;
    public static String TAG = "cardId_upload";
    public static String CARDID_IMG_PATH = "cardid_img_path";
    public static String FLAG_IMG_FLIP = "flag_img_flip";
    private Snackbar errorSnackbar;

    private ActivityListener activityListener;
    private LoaderUiListener loaderUiListener;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.confirmation_btn){
            if(setNumberWrapperError()) return;
            if(setNameWrapperError()) return;

            kycReqId = activityListener.getDataContatainer().getKycReqId();
            loaderUiListener.showProgressDialog();
            UploadDocumentUseCase uploadDocumentUseCase = new UploadDocumentUseCase(null,
                    getContext(), imagePath, docType, kycReqId);
            uploadDocumentUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    showErrorSnackbar();
                    loaderUiListener.hideProgressDialog();
                }
                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                    loaderUiListener.hideProgressDialog();
                    KYCDocumentUploadResponse kycDocumentUploadResponse =
                            (typeRestResponseMap.get(KYCDocumentUploadResponse.class)).getData();
                    if(kycDocumentUploadResponse != null &&
                            kycDocumentUploadResponse.getKycImageUploadDataClass() != null &&
                            kycDocumentUploadResponse.getKycImageUploadDataClass().getDocumentId() > 0){
                        activityListener.getDataContatainer().setDocumentNumber(edtxtNumber.getText().toString());
                        activityListener.getDataContatainer().setMothersMaidenName(edtxtName.getText().toString());
                        activityListener.getDataContatainer().setCardIdDocumentId(
                                kycDocumentUploadResponse.getKycImageUploadDataClass().getDocumentId());
                        activityListener.getDataContatainer().setDocumentType(
                                kycDocumentUploadResponse.getKycImageUploadDataClass().getDocumentType());
                        if(getArguments().getBoolean(Constants.Keys.FROM_RETAKE_FLOW)){
                            goToTandCPage();
                        }
                        else {
                            goToSelfieIdIntroPage();

                        }
                    }
                    else {
                        showErrorSnackbar();
                    }
                }
            });
        }
        else if(i == R.id.retake_photo){
            ActionCreator<HashMap<String, Object>, Integer> actionCreator = new ActionCreator<HashMap<String, Object>, Integer>() {
                @Override
                public void actionSuccess(int actionId, HashMap<String, Object> dataObj) {
                    ArrayList<String> keysList = (new CardIdDataKeyProvider()).getData(1, null);
                    imagePath = ((String) dataObj.get(keysList.get(0)));
                    toBeFlipped = ((Boolean) dataObj.get(keysList.get(1)));
                    activityListener.getDataContatainer().setFlipCardIdImg(toBeFlipped);
                    activityListener.getDataContatainer().setCardIdImage(imagePath);
                    activityListener.showHideActionbar(true);
                    KycUtil.setCameraCapturedImage(imagePath, toBeFlipped, idCardImageView);
                }

                @Override
                public void actionError(int actionId, Integer dataObj) {

                }
            };
            KycUtil.createKYCIdCameraFragment(getContext(),
                    activityListener, actionCreator, Constants.Keys.KYC_CARDID_CAMERA, true);
        }
        else if(i == R.id.btn_ok){
            if(errorSnackbar.isShownOrQueued()) errorSnackbar.dismiss();
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

    public static FragmentCardIDUpload newInstance(Bundle bundle){
        FragmentCardIDUpload fragmentCardIDUpload = newInstance();
        fragmentCardIDUpload.setArguments(bundle);
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
        loaderUiListener = (LoaderUiListener) context;
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
        edtxtNumber.addTextChangedListener(getNumberTextWatcher());
        wrapperName = view.findViewById(R.id.wrapper_name);
        edtxtName = view.findViewById(R.id.edtxt_name);
        edtxtName.addTextChangedListener(getNameTextWatcher());
        confirmationBtn = view.findViewById(R.id.confirmation_btn);
        confirmationBtn.setOnClickListener(this::onClick);
        retakePhoto = view.findViewById(R.id.retake_photo);
        retakePhoto.setOnClickListener(this::onClick);
        imagePath = activityListener.getDataContatainer().getCardIdImage();
        toBeFlipped = activityListener.getDataContatainer().isFlipCardIdImg();
        KycUtil.setCameraCapturedImage(imagePath, toBeFlipped, idCardImageView);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.KTP_WNI && isChecked) {
            docType = "KTP";
            wrapperNumberKtp.setLabel(Constants.Values.NOMOR_KTP);
            wrapperName.setLabel(Constants.Values.NAMA_GADIS);
            edtxtName.setHint(Constants.Values.NAMA_GADIS);
            edtxtNumber.setHint(Constants.Values.NOMOR_KTP);
        }
        else if(i == R.id.passport && isChecked){
            docType = "PASSPORT";
            wrapperNumberKtp.setLabel(Constants.Values.PASSPORT_NUMBER);
            wrapperName.setLabel(Constants.Values.MOTHERS_MAIDEN_NAME);
            edtxtNumber.setHint(Constants.Values.PASSPORT_NUMBER);
            edtxtName.setHint(Constants.Values.MOTHERS_MAIDEN_NAME);
        }
        setNameWrapperError();
        setNumberWrapperError();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void goToSelfieIdIntroPage(){
        activityListener.addReplaceFragment(FragmentSelfieIdPreviewAndUpload.newInstance(), true,
                FragmentSelfieIdPreviewAndUpload.TAG);
    }


    private TextWatcher getNameTextWatcher(){
        return new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    setNameWrapperLable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setNameWrapperError();
            }
        };
    }

    private TextWatcher getNumberTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    setNumberWrapperLable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setNumberWrapperError();
            }
        };
    }

    private void goToTandCPage(){
        activityListener.addReplaceFragment(FragmentTermsAndConditions.newInstance(), true,
                FragmentTermsAndConditions.TAG);
    }

    private void setNameWrapperLable(){
        if(KTPWNISelection.isChecked()){
            wrapperName.setLabel(Constants.Values.NAMA_GADIS);
        }
        else if(passportSelection.isChecked()){
            wrapperName.setLabel(Constants.Values.MOTHERS_MAIDEN_NAME);
        }
    }

    private void setNumberWrapperLable(){
        if(KTPWNISelection.isChecked()){
            wrapperNumberKtp.setLabel(Constants.Values.NOMOR_KTP);
        }
        else if(passportSelection.isChecked()){
            wrapperNumberKtp.setLabel(Constants.Values.PASSPORT_NUMBER);
        }
    }

    private boolean setNumberWrapperError(){
        if(TextUtils.isEmpty(edtxtNumber.getText())){
            if(KTPWNISelection.isChecked()){
                wrapperNumberKtp.setError(Constants.ErrorMsg.KTP_NUMBER);

            }
            else if(passportSelection.isChecked()){
                wrapperNumberKtp.setError(Constants.ErrorMsg.PASSPORT_NUMBER);
            }
            return true;
        }
        else {
            if(KTPWNISelection.isChecked() && edtxtNumber.getText().length() != 16){
                wrapperNumberKtp.setError(Constants.ErrorMsg.KTP_NUMBER);
                return true;
            }
            wrapperNumberKtp.setError("");
        }
        return false;
    }

    private boolean setNameWrapperError(){
        if(TextUtils.isEmpty(edtxtName.getText())){
            if(KTPWNISelection.isChecked()){
                wrapperName.setError(Constants.ErrorMsg.MOTHERS_NAME);
            }
            else if(passportSelection.isChecked()) {
                wrapperName.setError(Constants.ErrorMsg.FORIEGNER_MOTHERS_NAME);
            }
            return true;
        }
        else if(!(edtxtName.getText().toString().matches(Constants.RegEx.name)) ||
                !(edtxtName.getText().length() <= Constants.Values.MAIDEN_NAME_LENGTH)){
            if(KTPWNISelection.isChecked()){
                wrapperName.setError(Constants.ErrorMsg.MOTHERS_NAME);
            }
            else if(passportSelection.isChecked()) {
                wrapperName.setError(Constants.ErrorMsg.FORIEGNER_MOTHERS_NAME);
            }
            return true;
        }
        else {
            wrapperName.setError("");
        }
        return false;
    }

    private void showErrorSnackbar(){
        errorSnackbar = KycUtil.createErrorSnackBar(getActivity(), this::onClick);
        errorSnackbar.show();
    }

}
