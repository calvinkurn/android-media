package com.tokopedia.notifications.inApp.viewEngine;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.notifications.inApp.ruleEngine.RulesUtil;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMLayout;

import java.util.Map;

public class CmInAppBundleConvertor {

    public static final long HOURS_24_IN_MILLIS = 24 * 60 * 60 * 1000L;

    public static CMInApp getCmInApp(RemoteMessage remoteMessage) {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Map<String, String> map = remoteMessage.getData();
            CMInApp cmInApp = new CMInApp();

            if (!map.containsKey(RulesUtil.Constants.Payload.NOTIFICATION_ID))
                return null;
            cmInApp.setId(getLongFromStr(map.get(RulesUtil.Constants.Payload.NOTIFICATION_ID)));

            if (map.containsKey(RulesUtil.Constants.Payload.CAMPAIGN_ID))
                cmInApp.setCampaignId(map.get(RulesUtil.Constants.Payload.CAMPAIGN_ID));

            if (map.containsKey(RulesUtil.Constants.Payload.CAMPAIGN_USER_TOKEN))
                cmInApp.setCampaignUserToken(map.get(RulesUtil.Constants.Payload.CAMPAIGN_USER_TOKEN));

            if (map.containsKey(RulesUtil.Constants.Payload.PARENT_ID))
                cmInApp.setParentId(map.get(RulesUtil.Constants.Payload.PARENT_ID));

            if (map.containsKey(RulesUtil.Constants.Payload.START_TIME))
                cmInApp.setStartTime(getLongFromStr(map.get(RulesUtil.Constants.Payload.START_TIME)));


            if (cmInApp.getStartTime() == 0L) {
                cmInApp.setStartTime(System.currentTimeMillis());
            }

            if (map.containsKey(RulesUtil.Constants.Payload.END_TIME))
                cmInApp.setEndTime(getLongFromStr(map.get(RulesUtil.Constants.Payload.END_TIME)));

            if (cmInApp.getEndTime() == 0L) {
                cmInApp.setEndTime(System.currentTimeMillis() + CMInAppManager.getInstance().getCmInAppEndTimeInterval());
            }

            /*if (map.containsKey("ct"))
                cmInApp.set(getLongFromStr(map.get("ct")));*/

            if (map.containsKey(RulesUtil.Constants.Payload.FREQUENCY))
                cmInApp.setFreq(getIntFromStr(map.get(RulesUtil.Constants.Payload.FREQUENCY)));

            if (map.containsKey(RulesUtil.Constants.Payload.CANCELLABLE))
                cmInApp.setCancelable(getBooleanFromString(map.get(RulesUtil.Constants.Payload.CANCELLABLE)));

            if (map.containsKey(RulesUtil.Constants.Payload.IS_TEST))
                cmInApp.setTest(getBooleanFromString(map.get(RulesUtil.Constants.Payload.IS_TEST)));

            if (map.containsKey(RulesUtil.Constants.Payload.PERST_ON))
                cmInApp.setPersistentToggle(getBooleanFromString(map.get(RulesUtil.Constants.Payload.PERST_ON)));

            if (map.containsKey(RulesUtil.Constants.Payload.NOTIFICATION_TYPE))
                cmInApp.setType(map.get(RulesUtil.Constants.Payload.NOTIFICATION_TYPE));

            if (map.containsKey(RulesUtil.Constants.Payload.SCREEN_NAME))
                cmInApp.setScreen(map.get(RulesUtil.Constants.Payload.SCREEN_NAME));
            else
                return null;

            if (!map.containsKey(RulesUtil.Constants.Payload.UI)) {
                return null;
            }

            String ui = map.get(RulesUtil.Constants.Payload.UI);
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
