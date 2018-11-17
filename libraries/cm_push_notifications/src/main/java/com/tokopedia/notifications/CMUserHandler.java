package com.tokopedia.notifications;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.domain.UpdateFcmTokenUseCase;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import rx.Subscriber;

/**
 * @author lalit.singh
 */
public class CMUserHandler {

    static String TAG = CMUserHandler.class.getSimpleName();

    Context mContext;

    private UpdateFcmTokenUseCase updateFcmTokenUseCase;

    public CMUserHandler(Context context) {
        mContext = context;
    }

    public void sendFcmTokenToServer(String token) {
        String userId = getUserId();
        String accessToken = ((CMRouter) mContext).getAccessToken();
        String gAdId = getGoogleAdId();

        if (CMNotificationUtils.tokenUpdateRequired(mContext, token) ||
                CMNotificationUtils.mapTokenWithUserRequired(mContext, getUserId()) ||
                CMNotificationUtils.mapTokenWithGAdsIdRequired(mContext, getGoogleAdId())) {

            updateFcmTokenUseCase = new UpdateFcmTokenUseCase();
            updateFcmTokenUseCase.createRequestParams(
                    userId,
                    token,
                    CMNotificationUtils.getSdkVersion(),
                    CMNotificationUtils.getUniqueAppId(mContext),
                    CMNotificationUtils.getCurrentAppVersion(mContext));

            updateFcmTokenUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {
                    Log.e(TAG, "onCompleted: sendFcmTokenToServer ");
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "CMPushNotificationManager: sendFcmTokenToServer " + e.getMessage());
                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                    RestResponse res1 = typeRestResponseMap.get(String.class);
                    Log.e("code", "" + res1.getCode());
                    Log.e("data", "" + res1.getData());
                    Log.e("error", "" + res1.getErrorBody());

                    if (true) {
                        CMNotificationUtils.saveToken(mContext, token);
                        CMNotificationUtils.saveUserId(mContext, userId);
                        CMNotificationUtils.saveGAdsIdId(mContext, gAdId);
                    }
                }
            });
        }
    }

    private String getGoogleAdId() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(mContext, TkpdCache.ADVERTISINGID);
        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
        if (adsId != null && !TextUtils.isEmpty(adsId.trim())) {
            return adsId;
        } else {
            AdvertisingIdClient.Info adInfo;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
            } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
                return "";
            }

            if (adInfo != null) {
                String adID = adInfo.getId();

                if (!TextUtils.isEmpty(adID)) {
                    localCacheHandler.putString(TkpdCache.Key.KEY_ADVERTISINGID, adID);
                    localCacheHandler.applyEditor();
                }
                return adID;
            }
        }
        return "";
    }

    private String getUserId() {
        return ((CMRouter) mContext).getUserId();
    }

}
