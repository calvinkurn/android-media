package com.tokopedia.notifications.factory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMEvents;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.common.PersistentEvent;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.Carousel;
import com.tokopedia.notifications.model.Grid;
import com.tokopedia.notifications.model.Media;
import com.tokopedia.notifications.model.PersistentButton;
import com.tokopedia.notifications.model.ProductInfo;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit.singh
 */
public class CMNotificationFactory {

    private static final String TAG = CMNotificationFactory.class.getSimpleName();

    public static BaseNotification getNotification(Context context, Bundle bundle) {
        BaseNotificationModel baseNotificationModel = convertToBaseModel(bundle);

        IrisAnalyticsEvents.INSTANCE.sendPushReceiveEvent(context,baseNotificationModel);

        if (CMConstant.NotificationType.SILENT_PUSH.equals(baseNotificationModel.getType())) {
            handleSilentPush(context, baseNotificationModel);
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isChannelBlocked(context, baseNotificationModel)) {
            //todo notify to server for Blocked Channel By User.
        } else {
            if (baseNotificationModel.getType() == null)
                return null;
            switch (baseNotificationModel.getType()) {

                case CMConstant.NotificationType.GENERAL:
                    if (CMNotificationUtils.INSTANCE.hasActionButton(baseNotificationModel)) {
                        return new ActionNotification(context.getApplicationContext(), baseNotificationModel);
                    }
                    return (new GeneralNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.ACTION_BUTTONS:
                    return new ActionNotification(context.getApplicationContext(), baseNotificationModel);

                case CMConstant.NotificationType.BIG_IMAGE:
                    if (CMNotificationUtils.INSTANCE.hasActionButton(baseNotificationModel)) {
                        return new ActionNotification(context.getApplicationContext(), baseNotificationModel);
                    }
                    return (new ImageNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.PERSISTENT:
                    CMEvents.postGAEvent(PersistentEvent.EVENT_VIEW_NOTIFICATION, PersistentEvent.EVENT_CATEGORY,
                            PersistentEvent.EVENT_ACTION_PUSH_RECEIVED, PersistentEvent.EVENT_LABEL);
                    return (new PersistentNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.GRID_NOTIFICATION:
                    return (new GridNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.CAROUSEL_NOTIFICATION:
                    return (new CarouselNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.VISUAL_NOTIIFICATION:
                    return new VisualNotification(context.getApplicationContext(), baseNotificationModel);

                case CMConstant.NotificationType.PRODUCT_NOTIIFICATION:
                    return new ProductNotification(context.getApplicationContext(), baseNotificationModel);

                case CMConstant.NotificationType.DELETE_NOTIFICATION:
                    cancelNotification(context, baseNotificationModel.getNotificationId());
                    return null;

            }
        }
        return null;
    }

    private static void handleSilentPush(Context context, BaseNotificationModel baseNotificationModel) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean isChannelBlocked(Context context, BaseNotificationModel baseNotificationModel) {
        try {
            String channelId = baseNotificationModel.getChannelName();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(channelId);
            return null != channel && channel.getImportance() == NotificationManager.IMPORTANCE_NONE;
        } catch (NullPointerException e) {
        }
        return false;
    }

    private static void cancelNotification(Context context, int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    private static BaseNotificationModel convertToBaseModel(Bundle data) {
        BaseNotificationModel model = new BaseNotificationModel();
        model.setIcon(data.getString(CMConstant.PayloadKeys.ICON, ""));
        if (data.containsKey(CMConstant.PayloadKeys.NOTIFICATION_PRIORITY)) {
            model.setPriorityPreOreo(Integer.parseInt(data.getString(CMConstant.PayloadKeys.NOTIFICATION_PRIORITY, "2")));
        }
        model.setSoundFileName(data.getString(CMConstant.PayloadKeys.SOUND, ""));
        model.setNotificationId(Integer.parseInt(data.getString(CMConstant.PayloadKeys.NOTIFICATION_ID, "500")));
        model.setCampaignId(Long.parseLong(data.getString(CMConstant.PayloadKeys.CAMPAIGN_ID, "0")));
        model.setParentId(Long.parseLong(data.getString(CMConstant.PayloadKeys.PARENT_ID, "0")));
        model.setTribeKey(data.getString(CMConstant.PayloadKeys.TRIBE_KEY, ""));
        model.setType(data.getString(CMConstant.PayloadKeys.NOTIFICATION_TYPE, ""));
        model.setChannelName(data.getString(CMConstant.PayloadKeys.CHANNEL, ""));
        model.setTitle(data.getString(CMConstant.PayloadKeys.TITLE, ""));
        model.setDetailMessage(data.getString(CMConstant.PayloadKeys.DESCRIPTION, ""));
        model.setMessage(data.getString(CMConstant.PayloadKeys.MESSAGE, ""));
        model.setMedia(getMedia(data));
        model.setAppLink(data.getString(CMConstant.PayloadKeys.APP_LINK, ApplinkConst.HOME));
        List<ActionButton> actionButtonList = getActionButtons(data);
        if (actionButtonList != null)
            model.setActionButton(actionButtonList);
        model.setPersistentButtonList(getPersistentNotificationData(data));
        model.setVideoPushModel(getVideoNotificationData(data));
        model.setCustomValues(getCustomValues(data));
        List<Carousel> carouselList = getCarouselList(data);
        if (carouselList != null) {
            model.setCarouselList(carouselList);
            model.setCarouselIndex(data.getInt(CMConstant.PayloadKeys.CAROUSEL_INDEX, 0));
        }
        model.setVibration(data.getBoolean(CMConstant.PayloadKeys.VIBRATE, true));
        model.setUpdateExisting(data.getBoolean(CMConstant.PayloadKeys.UPDATE_NOTIFICATION, false));

        List<Grid> gridList = getGridList(data);
        if (gridList != null)
            model.setGridList(gridList);
        model.setProductInfoList(getProductInfoList(data));
        model.setSubText(data.getString(CMConstant.PayloadKeys.SUB_TEXT));
        model.setVisualCollapsedImageUrl(data.getString(CMConstant.PayloadKeys.VISUAL_COLLAPSED_IMAGE));
        model.setVisualExpandedImageUrl(data.getString(CMConstant.PayloadKeys.VISUAL_EXPANDED_IMAGE));
        return model;
    }

    private static Media getMedia(Bundle extras) {
        String actions = extras.getString(CMConstant.PayloadKeys.MEDIA);
        if (TextUtils.isEmpty(actions)) {
            return null;
        }
        try {
            return new Gson().fromJson(actions, Media.class);
        } catch (Exception e) {
            Log.e(TAG, "CM-getMedia", e);
        }
        return null;
    }

    private static JSONObject getCustomValues(Bundle extras) {
        String values = extras.getString(CMConstant.PayloadKeys.CUSTOM_VALUE);
        if (TextUtils.isEmpty(values)) {
            return null;
        }
        try {
            return new JSONObject(values);
        } catch (Exception e) {
            Log.e(TAG, "CM-getCustomValues", e);
        }
        return null;
    }

    private static List<ActionButton> getActionButtons(Bundle extras) {
        String actions = extras.getString(CMConstant.PayloadKeys.ACTION_BUTTON);
        if (TextUtils.isEmpty(actions)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            Type actionButtonListType = new TypeToken<ArrayList<ActionButton>>() {
            }.getType();
            List<ActionButton> actionButtonList = gson.fromJson(actions, actionButtonListType);
            return actionButtonList;
        } catch (Exception e) {
            Log.e(TAG, "CM-getActionButtons", e);
        }
        return null;
    }

    private static List<PersistentButton> getPersistentNotificationData(Bundle bundle) {
        String persistentData = bundle.getString(CMConstant.PayloadKeys.PERSISTENT_DATA);
        if (TextUtils.isEmpty(persistentData)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<PersistentButton>>() {
            }.getType();
            return new Gson().fromJson(persistentData, listType);
        } catch (Exception e) {

            Log.e(TAG, "CM-getPersistentNotificationData", e);
        }
        return null;
    }


    private static ArrayList<ProductInfo> getProductInfoList(Bundle bundle) {
        String productInfoListStr = bundle.getString(CMConstant.PayloadKeys.PRODUCT_INFO_LIST);
        if (TextUtils.isEmpty(productInfoListStr)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<ProductInfo>>() {
            }.getType();
            return new Gson().fromJson(productInfoListStr, listType);
        } catch (Exception e) {
            Log.e(TAG, "CM-getProductInfo", e);
        }
        return null;
    }

    private static List<Grid> getGridList(Bundle bundle) {
        String persistentData = bundle.getString(CMConstant.PayloadKeys.GRID_DATA);
        if (TextUtils.isEmpty(persistentData)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<Grid>>() {
            }.getType();
            return new Gson().fromJson(persistentData, listType);
        } catch (Exception e) {
            Log.e(TAG, "CM-getGridList", e);
        }
        return null;
    }

    private static JSONObject getVideoNotificationData(Bundle bundle) {

        String values = bundle.getString(CMConstant.PayloadKeys.VIDEO_DATA);
        if (TextUtils.isEmpty(values)) {
            return null;
        }
        try {
            return new JSONObject(values);
        } catch (Exception e) {
            Log.e(TAG, "CM-getVideoNotificationData", e);
        }
        return null;
    }

    private static List<Carousel> getCarouselList(Bundle extras) {
        String carouselData = extras.getString(CMConstant.PayloadKeys.CAROUSEL_DATA);
        if (TextUtils.isEmpty(carouselData)) {
            List<Carousel> carouselList = extras.getParcelableArrayList(CMConstant.ReceiverExtraData.CAROUSEL_DATA);
            if (carouselList != null)
                return carouselList;
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<Carousel>>() {
            }.getType();
            return new Gson().fromJson(carouselData, listType);
        } catch (Exception e) {

            Log.e(TAG, "CM-getCarouselList", e);
        }
        return null;
    }

}
