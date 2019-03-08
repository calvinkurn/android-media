package com.tokopedia.kyc.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.model.ConfirmSubmitResponse;
import com.tokopedia.kyc.model.GqlDocModel;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.activity.UpgradeProcessCompleteActivity;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.view.interfaces.LoaderUiListener;


import java.util.ArrayList;
import java.util.HashMap;

import rx.Subscriber;

public class FragmentTermsAndConditions extends BaseDaggerFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivityListener activityListener;
    private LoaderUiListener loaderUiListener;
    private View cardIdContainer;
    private View selfieIdContainer;
    private CheckBox tncCheckBox;
    private Button proceedFurther;
    private ImageView cardIdImage;
    private ImageView selfieIdImage;
    private TextView tncTxtv;
    public static String TAG = "tnc_page";
    private AlertDialog alertDialog;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cardid_container) {
            retakeImage(Constants.Keys.KYC_CARDID_CAMERA);
        } else if (i == R.id.selfieid_container) {
            retakeImage(Constants.Keys.KYC_SELFIEID_CAMERA);
        } else if (i == R.id.proceed_tnc) {
            showConfirmationDialog();
        }
        else if (i == R.id.txtv_tnc){
            ((KYCRouter)getContext().getApplicationContext()).actionOpenGeneralWebView(getActivity(),
                    Constants.URLs.OVO_TNC_PAGE);
        }
        else if(i == R.id.back_btn){
            alertDialog.dismiss();
        }
        else if(i == R.id.continue_btn){
            alertDialog.dismiss();
            submitKycTnCConfirmForm();
        }
    }

    private void submitKycTnCConfirmForm(){
        loaderUiListener.showProgressDialog();
        HashMap<String, Object> gqlDataHashMap = new HashMap<>();
        gqlDataHashMap.put("kyc_request_id", activityListener.getDataContatainer().getKycReqId());
        gqlDataHashMap.put("identity_document_type",
                activityListener.getDataContatainer().getDocumentType());
        gqlDataHashMap.put("identity_document_number",
                activityListener.getDataContatainer().getDocumentNumber());
        gqlDataHashMap.put("mother_maiden_name",
                activityListener.getDataContatainer().getMothersMaidenName());
        GqlDocModel ob1 = new GqlDocModel();
        GqlDocModel ob2 = new GqlDocModel();
        ArrayList<GqlDocModel> list = new ArrayList<>();
        ob1.setKey(activityListener.getDataContatainer().getCardIdDocumentId());
        ob2.setKey(activityListener.getDataContatainer().getSelfieIdDocumentId());
        list.add(ob1);
        list.add(ob2);
        gqlDataHashMap.put("documents", list);

        KycUtil.executeKycConfirmation(getContext(), getTnCAcceptGQLSubscriber(), gqlDataHashMap);
    }


    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.KYC_TNC_ACCEPTANDSUBMIT_SCR;
    }

    public static FragmentTermsAndConditions newInstance() {
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
        activityListener = (ActivityListener) context;
        loaderUiListener = (LoaderUiListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.kyc_ids_tnc_page, container, false);
        cardIdContainer = view.findViewById(R.id.cardid_container);
        cardIdContainer.setOnClickListener(this::onClick);
        selfieIdContainer = view.findViewById(R.id.selfieid_container);
        selfieIdContainer.setOnClickListener(this::onClick);
        tncCheckBox = view.findViewById(R.id.chkbx_tnc);
        tncCheckBox.setOnCheckedChangeListener(this);
        tncCheckBox.setOnClickListener(this::onClick);
        proceedFurther = view.findViewById(R.id.proceed_tnc);
        proceedFurther.setOnClickListener(this::onClick);
        cardIdImage = view.findViewById(R.id.cardid_img);
        selfieIdImage = view.findViewById(R.id.selfieid_img);
        tncTxtv = view.findViewById(R.id.txtv_tnc);
        tncTxtv.setOnClickListener(this::onClick);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tncTxtv.setText(Html.fromHtml(getResources().getString(R.string.ovo_confirm_tnc_text), Html.FROM_HTML_MODE_LEGACY));
        }else {
            tncTxtv.setText(Html.fromHtml(getResources().getString(R.string.ovo_confirm_tnc_text)));
        }
        setImages(cardIdImage, activityListener.getDataContatainer().getCardIdImage());
        setImages(selfieIdImage, activityListener.getDataContatainer().getSelfieIdImage());
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.chkbx_tnc) {
            if (isChecked) proceedFurther.setEnabled(true);
            else proceedFurther.setEnabled(false);
        }
    }

    private void setImages(ImageView imageView, String imagePath) {
        KycUtil.setCameraCapturedImage(imagePath,
                false, imageView);
    }

    private void gotoCustomerCareFollowupPage() {
        Intent statusIntent = new Intent(getActivity(), UpgradeProcessCompleteActivity.class);
        statusIntent.putExtra(Constants.Keys.STATUS, Constants.Status.INPROGRESS);
        getActivity().startActivity(statusIntent);
        getActivity().finish();
    }

    private Subscriber getTnCAcceptGQLSubscriber() {
        return new Subscriber<GraphqlResponse>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                loaderUiListener.hideProgressDialog();
                activityListener.addReplaceFragment(ErrorKycConfirmation.newInstance(), true, ErrorKycConfirmation.TAG);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                loaderUiListener.hideProgressDialog();
                ConfirmSubmitResponse kycDocumentUploadResponse = graphqlResponse.getData(ConfirmSubmitResponse.class);
                if (kycDocumentUploadResponse != null && kycDocumentUploadResponse.getConfirmSubmitResponse().getKycRequestId() > 0) {
                    gotoCustomerCareFollowupPage();
                }
            }
        };
    }

    private void retakeImage(int imageType) {

        ActionCreator<HashMap<String, Object>, Integer> actionCreator = new ActionCreator<HashMap<String, Object>, Integer>() {
            @Override
            public void actionSuccess(int actionId, HashMap<String, Object> dataObj) {
                Bundle bundle = new Bundle();
                ArrayList<String> keysList = (new CardIdDataKeyProvider()).getData(1, null);
                if(imageType == Constants.Keys.KYC_CARDID_CAMERA) {
                    activityListener.getDataContatainer().setFlipCardIdImg((Boolean) dataObj.get(keysList.get(1)));
                    activityListener.getDataContatainer().setCardIdImage((String) dataObj.get(keysList.get(0)));
                    bundle.putBoolean(Constants.Keys.FROM_RETAKE_FLOW, true);
                    activityListener.addReplaceFragment(FragmentCardIDUpload.newInstance(bundle), true,
                            FragmentCardIDUpload.TAG);
                }
                else if(imageType == Constants.Keys.KYC_SELFIEID_CAMERA){
                    activityListener.getDataContatainer().setFlipSelfieIdImg((Boolean) dataObj.get(keysList.get(1)));
                    activityListener.getDataContatainer().setSelfieIdImage((String) dataObj.get(keysList.get(0)));
                    bundle.putBoolean(Constants.Keys.FROM_RETAKE_FLOW, true);
                    activityListener.addReplaceFragment(FragmentSelfieIdPreviewAndUpload.newInstance(bundle), true,
                            FragmentCardIDUpload.TAG);
                }
                activityListener.showHideActionbar(true);
            }

            @Override
            public void actionError(int actionId, Integer dataObj) {

            }
        };
        KycUtil.createKYCIdCameraFragment(getContext(),
                activityListener,
                actionCreator,
                imageType,
                true);
    }

    private void showConfirmationDialog(){
        alertDialog = KycUtil.getKycConfirmSubmitAlertDialog(getActivity(), this::onClick).create();
        alertDialog.show();
    }
}
