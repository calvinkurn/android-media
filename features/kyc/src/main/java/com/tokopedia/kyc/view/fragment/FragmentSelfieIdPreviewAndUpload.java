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

public class FragmentSelfieIdPreviewAndUpload extends BaseDaggerFragment implements View.OnClickListener,
        GenericOperationsView<KYCDocumentUploadResponse> {
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
    @Inject
    DocumentUploadPresenter documentUploadPresenter;


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.selfieid_intro_proceed){
            executeSlfeIntroPrcd();
        }
        else if(i == R.id.use_img){
            executeUseImg();
        }
        else if(i == R.id.retake_image){
            executeRetakeImg();
        }
        else if(i == R.id.btn_ok){
            executeBtnOk();
        }
    }

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);

    }

    private void executeSlfeIntroPrcd(){
        KycUtil.createKYCIdCameraFragment(getContext(),
                activityListener, getSefieIdImageAction(),
                Constants.Keys.KYC_SELFIEID_CAMERA, false);
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                ((KYCRouter)getContext().getApplicationContext()).getUserId(),
                AnalyticsUtil.EventAction.CLK_OK_STP3);
    }

    private void executeUseImg(){
        makeSelfieUploadRequest();
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                ((KYCRouter)getContext().getApplicationContext()).getUserId(),
                AnalyticsUtil.EventAction.CLK_GNKN_STP3);
    }

    private void executeRetakeImg(){
        KycUtil.createKYCIdCameraFragment(getContext(),
                activityListener,
                getSefieIdImageAction(),
                Constants.Keys.KYC_SELFIEID_CAMERA, false);
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                ((KYCRouter)getContext().getApplicationContext()).getUserId(),
                AnalyticsUtil.EventAction.CLK_OK_ULN_STP3);
    }

    private void executeBtnOk(){
        makeSelfieUploadRequest();
        if(errorSnackbar.isShownOrQueued()) errorSnackbar.dismiss();
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                ((KYCRouter)getContext().getApplicationContext()).getUserId(),
                AnalyticsUtil.EventAction.CLK_OK_STP3);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.KYC_SELFIEID_PREVIEW_AND_UOLOAD_SCR;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityListener.setHeaderTitle(Constants.Values.OVOUPGRADE_STEP_2_TITLE);
        documentUploadPresenter.attachView(this);
        checkFromTncRetakeFlow();
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
                AnalyticsUtil.sendEvent(getContext(),
                        AnalyticsUtil.EventName.CLICK_OVO,
                        AnalyticsUtil.EventCategory.OVO_KYC,
                        "",
                        ((KYCRouter)getContext().getApplicationContext()).getUserId(),
                        AnalyticsUtil.EventAction.CLK_CPTR_PIC_STP3);
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

    private void checkFromTncRetakeFlow(){

        if(getArguments() != null && getArguments().getBoolean(Constants.Keys.FROM_RETAKE_FLOW)){
            selfieIdIntroView.setVisibility(View.GONE);
            selfieIdPreviewAndUpload.setVisibility(View.VISIBLE);
            imagePath = activityListener.getDataContatainer().getSelfieIdImage();
            flipSelfieIdImg = activityListener.getDataContatainer().isFlipSelfieIdImg();
            KycUtil.setCameraCapturedImage(imagePath, flipSelfieIdImg, selfieidImg);
        }

    }

    private void goToTandCPage(){
        activityListener.addReplaceFragment(FragmentTermsAndConditions.newInstance(), true,
                FragmentTermsAndConditions.TAG);
    }

    private void showErrorSnackbar(){
        if(activityListener.isRetryValid()) {
            errorSnackbar = KycUtil.createErrorSnackBar(getActivity(), this::onClick, "");
            errorSnackbar.show();
        }else {
            getActivity().finish();
        }
    }

    private void makeSelfieUploadRequest(){
        loaderUiListener.showProgressDialog();
        kycReqId = activityListener.getDataContatainer().getKycReqId();
        documentUploadPresenter.makeDocumentUploadRequest(imagePath, Constants.Values.SELFIE, kycReqId);
    }

    @Override
    public void success(KYCDocumentUploadResponse data) {
        activityListener.getDataContatainer().setSelfieIdDocumentId(
                data.getKycImageUploadDataClass().getDocumentId());
        goToTandCPage();
    }

    @Override
    public void failure(KYCDocumentUploadResponse data) {
        showErrorSnackbar();
    }

    @Override
    public void showHideProgressBar(boolean showProgressBar) {
        if(showProgressBar){
            loaderUiListener.showProgressDialog();
        }
        else {
            loaderUiListener.hideProgressDialog();
        }
    }

    @Override
    public void onDestroy() {
        documentUploadPresenter.detachView();
        super.onDestroy();
    }
}
