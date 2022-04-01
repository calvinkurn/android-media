package com.tokopedia.kyc.util;

import static com.tokopedia.kyc.Constants.Keys.KYC_CARDID_CAMERA;
import static com.tokopedia.kyc.Constants.Keys.KYC_SELFIEID_CAMERA;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.model.ConfirmSubmitResponse;
import com.tokopedia.kyc.model.EligibilityBase;
import com.tokopedia.kyc.view.fragment.FragmentCardIdCamera;
import com.tokopedia.kyc.view.fragment.FragmentSelfieIdCamera;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import rx.Subscriber;

public class KycUtil {

    private static final int IMAGE_QUALITY = 95;

    public static void createKYCIdCameraFragment(Context context,
                                                 ActivityListener activityListener,
                                                 ActionCreator actionCreator,
                                                 int cameraType, boolean replace) {
        if (activityListener != null) {
            activityListener.showHideActionbar(false);
            activityListener.addReplaceFragment((
                            getKYCCameraFragment(actionCreator,
                                    new CardIdDataKeyProvider(), cameraType)),
                    replace, Constants.Values.TAG_CAMERA_PAGE);
        }
    }

    @SuppressLint("MissingPermission")
    private static BaseDaggerFragment getKYCCameraFragment(ActionCreator<HashMap<String, Object>, Integer> actionCreator, ActionDataProvider<ArrayList<String>, Object> keysListProvider, int cameraType) {
        Bundle bundle = new Bundle();
        BaseDaggerFragment baseDaggerFragment = null;
        switch (cameraType) {
            case KYC_CARDID_CAMERA:
                baseDaggerFragment = FragmentCardIdCamera.newInstance();
                bundle.putSerializable(FragmentCardIdCamera.ACTION_CREATOR_ARG, actionCreator);
                bundle.putSerializable(FragmentCardIdCamera.ACTION_KEYS_PROVIDER_ARG, keysListProvider);
                baseDaggerFragment.setArguments(bundle);
                break;
            case KYC_SELFIEID_CAMERA:
                baseDaggerFragment = new FragmentSelfieIdCamera();
                bundle.putSerializable(FragmentSelfieIdCamera.ACTION_CREATOR_ARG, actionCreator);
                bundle.putSerializable(FragmentSelfieIdCamera.ACTION_KEYS_PROVIDER_ARG, keysListProvider);
                baseDaggerFragment.setArguments(bundle);
                break;
        }
        return baseDaggerFragment;
    }


    public static void executeEligibilityCheck(Context context, Subscriber<GraphqlResponse> subscriber) {
        GraphqlUseCase eligibilityCheckUseCase = new GraphqlUseCase();
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.kyc_eligibility_check),
                EligibilityBase.class);
        eligibilityCheckUseCase.addRequest(graphqlRequest);
        eligibilityCheckUseCase.execute(subscriber);
    }

    public static void executeKycConfirmation(Context context, Subscriber<GraphqlResponse> subscriber, HashMap<String,
            Object> gqlMutationDataMap){
        GraphqlUseCase kycConfirmationUseCase= new GraphqlUseCase();
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.kyc_confirmation_submit),
                ConfirmSubmitResponse.class, gqlMutationDataMap);
        kycConfirmationUseCase.addRequest(graphqlRequest);
        kycConfirmationUseCase.execute(subscriber);
    }

    public static void setCameraCapturedImage(String imagePath, boolean toBeFlipped, ImageView imageView){
        try {
            File file = new File(imagePath);
            if (file !=null && file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (myBitmap != null) {
                    if (toBeFlipped) {
                        loadImageFromBitmap(imageView, ImageHandler.flip(myBitmap, true, false));
                    } else {
                        loadImageFromBitmap(imageView, myBitmap);
                    }
                }
            }
        } catch (Throwable e) {

        }
    }

    private static void loadImageFromBitmap(final ImageView imageView, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int min, max;
        if (width > height) {
            min = height;
            max = width;
        } else {
            min = width;
            max = height;
        }
        boolean loadFitCenter = min != 0 && (max / min) > 2;
        if (loadFitCenter)
            Glide.with(imageView.getContext()).load(bitmapToByte(bitmap)).fitCenter().into(imageView);
        else
            Glide.with(imageView.getContext()).load(bitmapToByte(bitmap)).into(imageView);
    }

    private static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);
        return stream.toByteArray();
    }

    public static void sendEmail(Context context) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType(Constants.Values.TYPE);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Constants.Values.CS_EMAIL});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.Values.CS_EMAIL_SUBJECT);

        context.startActivity(Intent.createChooser(emailIntent, Constants.Values.CHOOSER_TTL_MAIL));
    }

    public static void makeCall(Context context){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(Constants.Values.CS_TEL_NO));
        context.startActivity(Intent.createChooser(intent, Constants.Values.CHOOSER_TTL_CALL));
    }


    public static AlertDialog.Builder getKycConfirmSubmitAlertDialog(Activity context, View.OnClickListener onClickListener){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tnc_confirm_page_submit_dialog, null);
        dialogView.findViewById(R.id.continue_btn).setOnClickListener(onClickListener);
        dialogView.findViewById(R.id.back_btn).setOnClickListener(onClickListener);
        dialogBuilder.setView(dialogView);
        return dialogBuilder;
    }

    public static Snackbar createErrorSnackBar(Activity activity,
                                               View.OnClickListener onClickListener,
                                               String errorMsg){
        Snackbar snackbar = Snackbar.make(activity.findViewById(
                android.R.id.content), "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = inflater.inflate(R.layout.error_snackbar_layout, null);
        snackView.findViewById(R.id.btn_ok).setOnClickListener(onClickListener);
        if(!TextUtils.isEmpty(errorMsg)) ((TextView)snackView.findViewById(R.id.error_msg)).setText(errorMsg);
        layout.addView(snackView, 0);
        return snackbar;
    }
}

