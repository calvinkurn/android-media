package com.tokopedia.notifications;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.data.model.TokenResponse;

import java.io.IOException;
import java.util.HashMap;
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

    private static final String USER_ID = "userId";
    private static final String SOURCE = "deviceOS";
    private static final String FCM_TOKEN = "notificationToken";
    private static final String APP_ID = "appId";
    private static final String SDK_VERSION = "sdkVersion";
    private static final String APP_VERSION = "appVersion";
    private static final String REQUEST_TIMESTAMP = "requestTimestamp";

    private static final String SOURCE_ANDROID = "android";
    private static final String USER_STATE = "loggedStatus";
    private static final String APP_NAME = "appName";

    static String TAG = CMUserHandler.class.getSimpleName();

    private Context mContext;
    private String token;
    private Handler handler = new Handler(Looper.getMainLooper());
    private GraphqlUseCase graphqlUseCase;

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
            Completable.fromAction(() -> sendFcmTokenToServerGQL(token))
                    .subscribeOn(Schedulers.io()).subscribe();
        }
    };

    public void cancelRunnable() {
        try {
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            if (graphqlUseCase != null)
                graphqlUseCase.unsubscribe();
        } catch (Exception e) {
        }
    }

    private void sendFcmTokenToServerGQL(String token) {
        try {
            if (getTempFcmId().equalsIgnoreCase(token)) {
                //ignore temporary fcm token
                return;
            }
            String gAdId = getGoogleAdId();
            String appVersionName = CMNotificationUtils.getCurrentAppVersionName(mContext);

            if (CMNotificationUtils.tokenUpdateRequired(mContext, token) ||
                    CMNotificationUtils.mapTokenWithUserRequired(mContext, getUserId()) ||
                    CMNotificationUtils.mapTokenWithGAdsIdRequired(mContext, gAdId) ||
                    CMNotificationUtils.mapTokenWithAppVersionRequired(mContext, appVersionName)) {

                Map<String, Object> requestParams = new HashMap<>();

                requestParams.put("macAddress", "");
                requestParams.put(USER_ID, getUserIdAsInt());
                requestParams.put(SOURCE, SOURCE_ANDROID);
                requestParams.put(FCM_TOKEN, token);
                requestParams.put(APP_ID, CMNotificationUtils.getUniqueAppId(mContext));
                requestParams.put(SDK_VERSION, String.valueOf(CMNotificationUtils.getSdkVersion()));
                requestParams.put(APP_VERSION, appVersionName);
                requestParams.put(USER_STATE, CMNotificationUtils.getUserStatus(mContext, getUserId()));
                requestParams.put(REQUEST_TIMESTAMP, CMNotificationUtils.getCurrentLocalTimeStamp() + "");
                requestParams.put(APP_NAME, CMNotificationUtils.getApplicationName(mContext));

                graphqlUseCase = new GraphqlUseCase();

                GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(mContext.getResources(), R.raw.query_send_token_to_server),
                        TokenResponse.class, requestParams, "AddToken");
                graphqlUseCase.clearRequest();
                graphqlUseCase.addRequest(request);
                graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "CMPushNotificationManager: sendFcmTokenToServer " + e.getMessage());
                    }

                    @Override
                    public void onNext(GraphqlResponse gqlResponse) {
                        TokenResponse tokenResponse = gqlResponse.getData(TokenResponse.class);
                        if (tokenResponse.getCmAddToken() != null) {
                            CMNotificationUtils.saveToken(mContext, token);
                            CMNotificationUtils.saveUserId(mContext, getUserId());
                            CMNotificationUtils.saveGAdsIdId(mContext, gAdId);
                            CMNotificationUtils.saveAppVersion(mContext, appVersionName);
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private int getUserIdAsInt() {
        String useridStr = getUserId();
        int userIdInt = 0;
        if (!TextUtils.isEmpty(useridStr)) {
            try {
                userIdInt = Integer.parseInt(useridStr.trim());
            } catch (NumberFormatException e) {

            }
        }
        return userIdInt;
    }

}