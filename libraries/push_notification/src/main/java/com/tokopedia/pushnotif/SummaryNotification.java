package com.tokopedia.pushnotif;

import android.content.Context;

import com.tokopedia.pushnotif.db.model.HistoryNotificationDB;
import com.tokopedia.pushnotif.model.SummaryNotificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ricoharisin .
 */

public class SummaryNotification {

    public static SummaryNotificationModel generateSummaryNotificationModel(Context context, int notificationType) {
        SummaryNotificationModel summaryNotificationModel = new SummaryNotificationModel();
        summaryNotificationModel.setHistoryString(
                convertHistoryToString(HistoryNotification.getListHistoryNotification(context, notificationType))
        );
        summaryNotificationModel.setSummaryText(generateSummaryText(notificationType,
                summaryNotificationModel.getTotalHistory()));

        return summaryNotificationModel;
    }

    private static List<String> convertHistoryToString(List<HistoryNotificationDB> historyNotificationDBList) {
        List<String> stringList = new ArrayList<>();
        for (HistoryNotificationDB historyNotificationDB : historyNotificationDBList) {
            stringList.add(historyNotificationDB.getSenderName()+" : "+historyNotificationDB.getMessage());
        }
        return stringList;
    }

    public static String generateSummaryText(int notificationType, int count) {
        switch (notificationType) {
            case Constant.NotificationId.TALK:
                return "Tokopedia - Diskusi";
            case Constant.NotificationId.CHAT:
                return "Tokopedia - Chat";
            default:
                return "Tokopedia - Notifikasi";
        }
    }


}
