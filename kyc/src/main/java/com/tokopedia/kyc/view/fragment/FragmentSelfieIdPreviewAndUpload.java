package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common.network.data.model.RestResponse;
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

public class FragmentSelfieIdPreviewAndUpload extends BaseDaggerFragment implements View.OnClickListener{
    private ActivityListener activityListener;
    private LoaderUiListener loaderUiListener;
    private View selfieIdIntroView;
    private View selfieIdPreviewAndUpload;
    private Button selfieIdIntroOkBtn;
    private Button useImageBtn;
    private TextView retakeImageBtn;
    private ImageView selfieidImg;
    public static String TAG = "selfieid_preview_upload";
    private int kycReqId;
    private String imagePath;
    private boolean flipSelfieIdImg;
    private Snackbar errorSnackbar;


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.selfieid_intro_proceed){
            KycUtil.createKYCIdCameraFragment(getContext(),
                    activityListener, getSefieIdImageAction(),
                    Constants.Keys.KYC_SELFIEID_CAMERA, false);
        }
        else if(i == R.id.use_img){
            loaderUiListener.showProgressDialog();
            kycReqId = activityListener.getDataContatainer().getKycReqId();
            UploadDocumentUseCase uploadDocumentUseCase = new UploadDocumentUseCase(null,
                    getContext(), imagePath, Constants.Values.SELFIE, kycReqId);
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
                        activityListener.getDataContatainer().setSelfieIdDocumentId(
                                kycDocumentUploadResponse.getKycImageUploadDataClass().getDocumentId());
                        goToTandCPage();
                    }
                    else {
                        showErrorSnackbar();
                    }
                }
            });
        }
        else if(i == R.id.retake_image){
            KycUtil.createKYCIdCameraFragment(getContext(),
                    activityListener,
                    getSefieIdImageAction(),
                    Constants.Keys.KYC_SELFIEID_CAMERA, false);
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
        loaderUiListener = (LoaderUiListener)context;
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

    public static FragmentSelfieIdPreviewAndUpload newInstance(Bundle bundle){
        FragmentSelfieIdPreviewAndUpload fragmentSelfieIdPreviewAndUpload = newInstance();
        fragmentSelfieIdPreviewAndUpload.setArguments(bundle);
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
                imagePath = (String) dataObj.get(keysList.get(0));
                flipSelfieIdImg = (Boolean) dataObj.get(keysList.get(1));
                activityListener.getDataContatainer().setSelfieIdImage(imagePath);
                activityListener.getDataContatainer().setFlipSelfieIdImg(flipSelfieIdImg);
                KycUtil.setCameraCapturedImage(imagePath, flipSelfieIdImg, selfieidImg);
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

    private void showErrorSnackbar(){
        errorSnackbar = KycUtil.createErrorSnackBar(getActivity(), this::onClick) ;
        errorSnackbar.show();
    }
}
