package com.tokopedia.kyc.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.model.ConfirmSubmitResponse;
import com.tokopedia.kyc.model.EligibilityBase;
import com.tokopedia.kyc.model.KYCDocumentUploadResponse;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

import java.io.File;
import java.util.HashMap;

import rx.Subscriber;

public class KycUtil {
    public static void createKYCIdCameraFragment(Context context,
                                                 ActivityListener activityListener,
                                                 ActionCreator actionCreator,
                                                 int cameraType, boolean replace) {
        activityListener.showHideActionbar(false);
        activityListener.addReplaceFragment((
                        (KYCRouter) context.getApplicationContext()).getKYCCameraFragment(actionCreator,
                new CardIdDataKeyProvider(), cameraType),
                replace, Constants.Values.TAG_CAMERA_PAGE);
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
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (myBitmap != null) {
                    if (toBeFlipped) {
                        imageView.setImageBitmap(ImageHandler.flip(myBitmap, true, false));
                    } else {
                        imageView.setImageBitmap(myBitmap);
                    }
                }
            }
        } catch (Throwable e) {

        }
    }

    public static void sendEmail(Context context){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"cs@ovo.id"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[Upgrade Ke OVO Premier] Dalam Proses Issue");

        context.startActivity(Intent.createChooser(emailIntent, "Kirim mail..."));
    }

    public static void makeCall(Context context){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:1500696"));
        context.startActivity(Intent.createChooser(intent, "Lakukan panggilan"));
    }

    public static AlertDialog.Builder getErrorDialogBuilder(Activity context, View.OnClickListener onClickListener){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.error_layout, null);
        dialogView.findViewById(R.id.error_btn_confirm).setOnClickListener(onClickListener);
        dialogBuilder.setView(dialogView);
        return dialogBuilder;
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

