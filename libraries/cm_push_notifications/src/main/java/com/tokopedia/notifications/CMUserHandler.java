package com.tokopedia.notifications;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
import com.tokopedia.usecase.RequestParams;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import rx.Completable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author lalit.singh
 */
public class CMUserHandler {

    static String TAG = CMUserHandler.class.getSimpleName();

    Context mContext;

    private UpdateFcmTokenUseCase updateFcmTokenUseCase;

    String token;

    Handler handler = new Handler(Looper.getMainLooper());

    public CMUserHandler(Context context) {
        mContext = context;
    }

    public void updateToken(String token, long remoteDelaySeconds, boolean userAction) {
        try {
            long delay = getRandomDelay(remoteDelaySeconds);
            if (userAction)
                delay = 0L;
            this.token = token;
            handler.postDelayed(runnable, delay);
        } catch (Exception e) {
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Completable.fromAction(() -> sendFcmTokenToServer(token))
                    .subscribeOn(Schedulers.io()).subscribe();
        }
    };

    public void cancelRunnable() {
        try {
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            if (updateFcmTokenUseCase != null)
                updateFcmTokenUseCase.unsubscribe();
        } catch (Exception e) {
        }
    }


    private void sendFcmTokenToServer(String token) {
        try {
            if (getTempFcmId().equalsIgnoreCase(token)) {
                //ignore temporary fcm token
                return;
            }
            String userId = getUserId();
            String gAdId = getGoogleAdId();
            String appVersionName = CMNotificationUtils.getCurrentAppVersionName(mContext);

            if (CMNotificationUtils.tokenUpdateRequired(mContext, token) ||
                    CMNotificationUtils.mapTokenWithUserRequired(mContext, getUserId()) ||
                    CMNotificationUtils.mapTokenWithGAdsIdRequired(mContext, gAdId) ||
                    CMNotificationUtils.mapTokenWithAppVersionRequired(mContext, appVersionName)) {

                updateFcmTokenUseCase = new UpdateFcmTokenUseCase();
                RequestParams requestParams = updateFcmTokenUseCase.createRequestParams(
                        userId,
                        token,
                        CMNotificationUtils.getSdkVersion(),
                        CMNotificationUtils.getUniqueAppId(mContext),
                        appVersionName,
                        CMNotificationUtils.getUserStatus(mContext, userId), CMNotificationUtils.getCurrentLocalTimeStamp());

                updateFcmTokenUseCase.execute(requestParams, new Subscriber<Map<Type, RestResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "CMPushNotificationManager: sendFcmTokenToServer " + e.getMessage());
                    }

                    @Override
                    public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                        RestResponse restResponse = typeRestResponseMap.get(String.class);
                        if (restResponse.getCode() == 200) {
                            CMNotificationUtils.saveToken(mContext, token);
                            CMNotificationUtils.saveUserId(mContext, userId);
                            CMNotificationUtils.saveGAdsIdId(mContext, gAdId);
                            CMNotificationUtils.saveAppVersion(mContext, appVersionName);
                        }
                    }
                });
            }
        } catch (Exception e) {
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

    private static String getTempFcmId() {
        return UUID.randomUUID().toString();
    }

    private long getRandomDelay(long randomDelaySeconds) {
        Random rand = new Random();
        int millis = rand.nextInt(1000) + 1; //in millis delay
        int seconds = rand.nextInt((int) randomDelaySeconds) + 1; //in seconds
        return (seconds * 1000 + millis);
    }

}

