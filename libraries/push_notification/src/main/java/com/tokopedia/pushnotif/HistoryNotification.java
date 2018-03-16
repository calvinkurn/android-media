package com.tokopedia.pushnotif;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.pushnotif.model.HistoryNotificationModel;

import java.util.ArrayList;

/**
 * @author ricoharisin .
 */

public class HistoryNotification {

    private ArrayList<HistoryNotificationModel> listHistoryNotificationModel;
    //private LocalCacheHandler localCacheHandler;
    private String key;
    private static final String NOTIFICATION_STORAGE = "NOATIFICATION_STORAGE";
    public static final String KEY_TALK = "KEY_TALK";
    public static final String KEY_CHAT = "KEY_CHAT";

    public HistoryNotification(Context context, String key) {
        /*this.key = key;
        localCacheHandler = new LocalCacheHandler(context, NOTIFICATION_STORAGE);
        ArrayList<String> notificationString = localCacheHandler.getArrayListString(key);

        convertToHistoryNotificationModel(notificationString);*/

    }

    public void storeNotification(String message, String senderName) {
        /*long time = System.currentTimeMillis();
        HistoryNotificationModel model = new HistoryNotificationModel(message,time,senderName);

        listHistoryNotificationModel.add(model);
        ArrayList<String> currHistory = localCacheHandler.getArrayListString(key);
        Gson gson = new Gson();
        currHistory.add(gson.toJson(model));
        localCacheHandler.putArrayListString(key, currHistory);
        localCacheHandler.applyEditor();*/
    }

    private void convertToHistoryNotificationModel(ArrayList<String> arrayString) {
        listHistoryNotificationModel = new ArrayList<>();
        Gson gson = new Gson();
        for (String stringDetail : arrayString) {
            listHistoryNotificationModel.add(gson.fromJson(stringDetail, HistoryNotificationModel.class));
        }
    }

    public ArrayList<HistoryNotificationModel> getListHistoryNotificationModel() {
        return listHistoryNotificationModel;
    }

    public String getSummary() {
        int total = listHistoryNotificationModel.size();
        if (key == KEY_TALK) return total+ " Diskusi Produk";
        return total+ " Chat";
    }


    public static void clearHistoryNotification(Context context, String key) {
        /*LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, NOTIFICATION_STORAGE);
        localCacheHandler.clearCache(key);*/
    }



}
