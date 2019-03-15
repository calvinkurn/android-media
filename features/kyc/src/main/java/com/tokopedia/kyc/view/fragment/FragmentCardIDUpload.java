package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
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
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.model.KYCDocumentUploadResponse;
import com.tokopedia.kyc.util.AnalyticsUtil;
import com.tokopedia.kyc.util.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.view.interfaces.GenericOperationsView;
import com.tokopedia.kyc.view.interfaces.LoaderUiListener;
import com.tokopedia.kyc.view.presenter.DocumentUploadPresenter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;


public class FragmentCardIDUpload extends BaseDaggerFragment implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        GenericOperationsView<KYCDocumentUploadResponse> {

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
    @Inject
    DocumentUploadPresenter documentUploadPresenter;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.confirmation_btn){
            executeConfirmation();
        }
        else if(i == R.id.retake_photo){
            executeRetakePhoto();
        }
        else if(i == R.id.btn_ok){
            makeCardIDUploadRequest();
            if(errorSnackbar.isShownOrQueued()) errorSnackbar.dismiss();
        }
    }

    private void executeConfirmation(){
        makeCardIDUploadRequest();
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                ((KYCRouter)getContext().getApplicationContext()).getUserId(),
                AnalyticsUtil.EventAction.CLK_CONF_STP2);
    }

    private void executeRetakePhoto(){
        ActionCreator<HashMap<String, Object>, Integer> actionCreator = new ActionCreator<HashMap<String, Object>, Integer>() {
            @Override
            public void actionSuccess(int actionId, HashMap<String, Object> dataObj) {
                ArrayList<String> keysList = (new CardIdDataKeyProvider()).getData(1, null);
                imagePath = ((String) dataObj.get(keysList.get(0)));
                toBeFlipped = ((Boolean) dataObj.get(keysList.get(1)));
                if(activityListener != null){
                    activityListener.showHideActionbar(true);
                    if(activityListener.getDataContatainer() != null){
                        activityListener.getDataContatainer().setFlipCardIdImg(toBeFlipped);
                        activityListener.getDataContatainer().setCardIdImage(imagePath);
                    }
                }
                KycUtil.setCameraCapturedImage(imagePath, toBeFlipped, idCardImageView);
            }

            @Override
            public void actionError(int actionId, Integer dataObj) {

            }
        };
        KycUtil.createKYCIdCameraFragment(getContext(),
                activityListener, actionCreator, Constants.Keys.KYC_CARDID_CAMERA, true);
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                ((KYCRouter)getContext().getApplicationContext()).getUserId(),
                AnalyticsUtil.EventAction.CLK_ULN_PIC_STP2);
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
        if(activityListener != null) {
            activityListener.setHeaderTitle(Constants.Values.OVOUPGRADE_STEP_2_TITLE);
        }
        documentUploadPresenter.attachView(this);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if(context instanceof ActivityListener) {
            activityListener = (ActivityListener) context;
        }
        if(context instanceof LoaderUiListener) {
            loaderUiListener = (LoaderUiListener) context;
        }
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
        setNameWrapperLableHint();
        setNumberWrapperLableHint();
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        setNameWrapperLableHint();
        setNumberWrapperLableHint();
        if (i == R.id.KTP_WNI && isChecked) {
            docType = "KTP";
            edtxtName.setHint(Constants.HintMsg.EDTXT_NAMA_GADIAS);
            edtxtNumber.setHint(Constants.HintMsg.EDTXT_KTP_NO);
            edtxtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else if(i == R.id.passport && isChecked){
            docType = "PASSPORT";
            edtxtNumber.setHint(Constants.HintMsg.EDTXT_PASSPORT_NO);
            edtxtName.setHint(Constants.HintMsg.EDTXT_MOTHERS_NAME);
            edtxtNumber.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private void goToSelfieIdIntroPage(){
        if(activityListener != null) {
            activityListener.addReplaceFragment(FragmentSelfieIdPreviewAndUpload.newInstance(), true,
                    FragmentSelfieIdPreviewAndUpload.TAG);
        }
    }


    private TextWatcher getNameTextWatcher(){
        return new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setNameWrapperLableHint();
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                    setNumberWrapperLableHint();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void goToTandCPage(){
        if(activityListener != null) {
            activityListener.addReplaceFragment(FragmentTermsAndConditions.newInstance(), true,
                    FragmentTermsAndConditions.TAG);
        }
    }

    private void setNameWrapperLableHint(){
        if(KTPWNISelection.isChecked()){
            wrapperName.setLabel(Constants.Values.NAMA_GADIS);
            wrapperName.setHelper(Constants.HintMsg.NAMA_GADIAS);
            wrapperName.setError("");
        }
        else if(passportSelection.isChecked()){
            wrapperName.setLabel(Constants.Values.MOTHERS_MAIDEN_NAME);
            wrapperName.setHelper(Constants.HintMsg.MOTHERS_NAME);
            wrapperName.setError("");
        }
    }

    private void setNumberWrapperLableHint(){
        if(KTPWNISelection.isChecked()){
            wrapperNumberKtp.setLabel(Constants.Values.NOMOR_KTP);
            wrapperNumberKtp.setHelper(Constants.HintMsg.KTP);
            wrapperNumberKtp.setError("");
        }
        else if(passportSelection.isChecked()){
            wrapperNumberKtp.setHelper(Constants.HintMsg.PASSPORT);
            wrapperNumberKtp.setLabel(Constants.Values.PASSPORT_NUMBER);
            wrapperNumberKtp.setError("");
        }
    }

    private boolean setNumberWrapperError(){
        wrapperNumberKtp.setHelper("");
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
            if(KTPWNISelection.isChecked()){
                if(!edtxtNumber.getText().toString().matches(Constants.RegEx.ktp)){
                    wrapperNumberKtp.setError(Constants.ErrorMsg.KTP_NUMBER);
                    return true;
                }else if(edtxtNumber.getText().length() != Constants.Values.KTP_NUMBER_LENGTH){
                    wrapperNumberKtp.setError(Constants.ErrorMsg.KTP_NUMBER_LENGTH);
                    return true;
                }
            }else if(passportSelection.isChecked()){
                if(!edtxtNumber.getText().toString().matches(Constants.RegEx.passport)){
                    wrapperNumberKtp.setError(Constants.ErrorMsg.PASSPORT_NUMBER);
                    return true;
                }
                if(edtxtNumber.getText().length() > Constants.Values.PASSPORT_NO_MAX_LENGTH){
                    wrapperNumberKtp.setError(Constants.ErrorMsg.PASSPORT_NUMBER_LENGTH);
                    return true;
                }
            }
            wrapperNumberKtp.setError("");
        }
        return false;
    }

    private boolean setNameWrapperError(){
        wrapperName.setHelper("");
        if(TextUtils.isEmpty(edtxtName.getText()) ||
                !(edtxtName.getText().toString().matches(Constants.RegEx.name))){
            if(KTPWNISelection.isChecked()){
                wrapperName.setError(Constants.ErrorMsg.MOTHERS_NAME);
            }
            else if(passportSelection.isChecked()) {
                wrapperName.setError(Constants.ErrorMsg.FORIEGNER_MOTHERS_NAME);
            }
            return true;
        }
        else if(edtxtName.getText().length() > Constants.Values.MAIDEN_NAME_LENGTH){
            if(KTPWNISelection.isChecked()){
                wrapperName.setError(Constants.ErrorMsg.MOTHERS_NAME_LENGTH);
            }
            else if(passportSelection.isChecked()) {
                wrapperName.setError(Constants.ErrorMsg.FORIEGNER_MOTHERS_NAME_LENGTH);
            }
            return true;
        }
        else {
            wrapperName.setError("");
        }
        return false;
    }

    private void showErrorSnackbar(){
        if(activityListener != null && activityListener.isRetryValid()) {
            errorSnackbar = KycUtil.createErrorSnackBar(getActivity(), this::onClick, "");
            errorSnackbar.show();
        }
        else {
            if(getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private void makeCardIDUploadRequest(){
        if(setNumberWrapperError()) return;
        if(setNameWrapperError()) return;

        if(activityListener != null && activityListener.getDataContatainer() != null) {
            kycReqId = activityListener.getDataContatainer().getKycReqId();
        }
        if(loaderUiListener != null) {
            loaderUiListener.showProgressDialog();
        }
        documentUploadPresenter.makeDocumentUploadRequest(imagePath, docType, kycReqId);

    }

    @Override
    public void success(KYCDocumentUploadResponse data) {
        if(activityListener != null && activityListener.getDataContatainer() != null) {
            activityListener.getDataContatainer().setDocumentNumber(edtxtNumber.getText().toString());
            activityListener.getDataContatainer().setMothersMaidenName(edtxtName.getText().toString());
            activityListener.getDataContatainer().setCardIdDocumentId(
                    data.getKycImageUploadDataClass().getDocumentId());
            activityListener.getDataContatainer().setDocumentType(
                    data.getKycImageUploadDataClass().getDocumentType());
        }
        if (getArguments().getBoolean(Constants.Keys.FROM_RETAKE_FLOW)) {
            goToTandCPage();
        } else {
            goToSelfieIdIntroPage();
        }
    }

    @Override
    public void failure(KYCDocumentUploadResponse data) {
        showErrorSnackbar();
    }

    @Override
    public void showHideProgressBar(boolean showProgressBar) {
        if(loaderUiListener != null) {
            if (showProgressBar) {
                loaderUiListener.showProgressDialog();
            } else {
                loaderUiListener.hideProgressDialog();
            }
        }
    }

    @Override
    public void onDestroy() {
        documentUploadPresenter.detachView();
        super.onDestroy();
    }
}
