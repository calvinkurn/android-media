package com.tokopedia.kyc.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.kyc.PersistentStore;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.model.ConfirmRequestDataContainer;
import com.tokopedia.kyc.model.EligibilityBase;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

import rx.Subscriber;

public class KycUtil {
    public static void createKYCIdCameraFragment(Context context,
                                                 ActivityListener activityListener, ActionCreator actionCreator, int cameraType) {
        activityListener.showHideActionbar(false);
        activityListener.addReplaceFragment((
                        (KYCRouter) context.getApplicationContext()).getKYCCameraFragment(actionCreator,
                new CardIdDataKeyProvider(), cameraType),
                false, Constants.Values.TAG_CAMERA_PAGE);
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
                EligibilityBase.class, gqlMutationDataMap);
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

    public static void saveDataToPersistentStore(Context context, String key, Object value){
        PersistentStore.getInstance(context).getCacheManager().put(key, value);
    }

    public static Object getDataFromPersistentStore(Context context, String key, Type type, Object defaultValue){
        return PersistentStore.getInstance(context).getCacheManager().get(key,type, defaultValue);
    }

    public static Bundle getConfirmReqDataContainerBundle(ConfirmRequestDataContainer confirmRequestDataContainer){
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Keys.CONFIRM_DATA_REQ_CONTAINER, confirmRequestDataContainer);
        return bundle;
    }
}
