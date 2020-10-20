package com.tokopedia.contactus.common.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;


/**
 * Created by baghira on 16/07/18.
 */

public class ContactUsTracking extends UnifyTracking {

    public static void eventInboxClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, "Inbox"),
                ContactUsEventTracking.Label.LabelHome
        ).getEvent());
    }

    public static void eventPopularArticleClick(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickArticle,
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventLihatBantuanClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickLihat, "Bantuan"),
                String.format(ContactUsEventTracking.Label.LabelGeneric, "")
        ).getEvent());
    }

    public static void eventHomeInvoiceClick(Context context,String action, String label) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickInvoice, action),
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventLihatTransaksiClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickLihat, "Transaksi"),
                String.format(ContactUsEventTracking.Label.LabelGeneric, "")
        ).getEvent());
    }

    public static void eventHomeHubungiKamiClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickHubungi,
                ContactUsEventTracking.Label.LabelHome
        ).getEvent());
    }

    public static void eventArticleHubungiKamiClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickHubungi,
                ContactUsEventTracking.Label.LabelArticle
        ).getEvent());
    }

    public static void eventSuccessClick(Context context, String labelInvoice) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, "Submit"),
                String.format(ContactUsEventTracking.Label.LabelFormList, labelInvoice)
        ).getEvent());
    }

    public static void eventOkClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, "Inbox"),
                ContactUsEventTracking.Label.LabelSubmitTicket
        ).getEvent());
    }

    public static void eventChatBotOkClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickChatBot,
                String.format(ContactUsEventTracking.Label.LabelGeneric, "")
        ).getEvent());
    }

    public static void sendGTMInboxTicket(Context context, String event, String category, String action, String label) {
        sendGTMEvent(context, new EventTracking(event, category, action, label).getEvent());
    }
}
