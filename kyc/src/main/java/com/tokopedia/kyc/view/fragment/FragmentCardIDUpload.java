package com.tokopedia.kyc.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.tokopedia.kyc.model.ConfirmRequestDataContainer;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<String> permissionsToRequest;
    private boolean isPermissionGotDenied;
    protected static final int REQUEST_CAMERA_PERMISSIONS = 932;


    private ActivityListener activityListener;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.confirmation_btn){

            if(TextUtils.isEmpty(edtxtNumber.getText())){
                wrapperNumberKtp.setError("Invalid KTP/Passport number");
                return;
            }
            else {
                wrapperNumberKtp.setError("");
            }
            if(TextUtils.isEmpty(edtxtName.getText())){
                wrapperName.setError("Invalid Mother's maiden name");
                return;
            }
            else
            if(!(edtxtName.getText().toString().matches(Constants.RegEx.name)) ||
                    !(edtxtName.getText().length() <= Constants.Values.MAIDEN_NAME_LENGTH)){
                wrapperName.setError("Invalid Mother's maiden name");
                return;
            }
            else {
                wrapperName.setError("");
            }

            kycReqId = ((ConfirmRequestDataContainer)getArguments().
                    getParcelable(Constants.Keys.CONFIRM_DATA_REQ_CONTAINER)).getKycReqId() ;
            UploadDocumentUseCase uploadDocumentUseCase = new UploadDocumentUseCase(null,
                    getContext(), imagePath, docType, kycReqId);
            uploadDocumentUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {

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
                    activityListener.showHideActionbar(true);
                    KycUtil.saveDataToPersistentStore(getContext(), Constants.Keys.CARD_IMG_PATH, imagePath);
                    KycUtil.saveDataToPersistentStore(getContext(), Constants.Keys.FLIP_CARD_IMG, toBeFlipped);
                    KycUtil.setCameraCapturedImage(imagePath, toBeFlipped, idCardImageView);
                }

                @Override
                public void actionError(int actionId, Integer dataObj) {

                }
            };
            KycUtil.createKYCIdCameraFragment(getContext(), activityListener, actionCreator, Constants.Keys.KYC_CARDID_CAMERA);
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
        retakePhoto = view.findViewById(R.id.retake_photo);
        retakePhoto.setOnClickListener(this::onClick);
        imagePath = getArguments().getString(CARDID_IMG_PATH);
        toBeFlipped = getArguments().getBoolean(FLAG_IMG_FLIP);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAndAskForPermissions();
    }

    private void goToSelfieIdIntroPage(){
        activityListener.addReplaceFragment(FragmentSelfieIdPreviewAndUpload.newInstance(), true,
                FragmentSelfieIdPreviewAndUpload.TAG);
    }

    private void checkAndAskForPermissions(){
        if (isPermissionGotDenied) {
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String[] permissions;
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsToRequest != null && grantResults.length == permissionsToRequest.size()) {
            int grantCount = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionGotDenied = true;
                    break;
                }
                grantCount++;
            }
            if (grantCount == grantResults.length) {
                isPermissionGotDenied = false;
            }
        }
    }

    private TextWatcher getNameTextWatcher(){
        return new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }


}
