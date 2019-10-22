package com.tokopedia.notifications.inApp.viewEngine;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMLayout;

import java.util.Map;

public class CmInAppBundleConvertor {

    public static CMInApp getCmInApp(RemoteMessage remoteMessage) {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Map<String, String> map = remoteMessage.getData();
            CMInApp cmInApp = new CMInApp();

            if (!map.containsKey("notificationId"))
                return null;
            cmInApp.setId(getLongFromStr(map.get("notificationId")));

            if (map.containsKey("st"))
                cmInApp.setStartTime(getLongFromStr(map.get("st")));

            if (map.containsKey("et"))
                cmInApp.setEndTime(getLongFromStr(map.get("et")));

            /*if (map.containsKey("ct"))
                cmInApp.set(getLongFromStr(map.get("ct")));*/

            if (map.containsKey("freq"))
                cmInApp.setFreq(getIntFromStr(map.get("freq")));

            if (map.containsKey("d"))
                cmInApp.setCancelable(getBooleanFromString(map.get("d")));

            if (map.containsKey("notificationType"))
                cmInApp.setType(map.get("notificationType"));

            if (map.containsKey("s"))
                cmInApp.setScreen(map.get("s"));
            else
                return null;

            if (!map.containsKey("ui")) {
                return null;
            }

            String ui = map.get("ui");
            CMLayout cmLayout = gson.fromJson(ui, CMLayout.class);
            cmInApp.setCmLayout(cmLayout);
            return cmInApp;
        } catch (Exception e) {
            return null;
        }
    }


    private static boolean getBooleanFromString(String strBoolean) {
        try {
            return Boolean.parseBoolean(strBoolean);
        } catch (Exception e) {
            return false;
        }
    }

    private static int getIntFromStr(String strInt) {
        try {
            return Integer.parseInt(strInt);
        } catch (Exception e) {
            return 0;
        }
    }


    private static long getLongFromStr(String strLong) {
        try {
            return Long.parseLong(strLong);
        } catch (Exception e) {
            return 0;
        }
    }


}
