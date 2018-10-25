package com.tokopedia.contactus.common.analytics;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;

/**
 * Created by baghira on 16/07/18.
 */

public class ContactUsTracking extends UnifyTracking {

    public static void eventInboxClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, "Inbox"),
                ContactUsEventTracking.Label.LabelHome
        ).getEvent());
    }

    public static void eventPopularArticleClick(String label) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickArticle,
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventLihatBantuanClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickLihat, "Bantuan"),
                String.format(ContactUsEventTracking.Label.LabelGeneric, "")
        ).getEvent());
    }

    public static void eventHomeInvoiceClick(String action, String label) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickInvoice, action),
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventLihatTransaksiClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickLihat, "Transaksi"),
                String.format(ContactUsEventTracking.Label.LabelGeneric, "")
        ).getEvent());
    }

    public static void eventHomeHubungiKamiClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickHubungi,
                ContactUsEventTracking.Label.LabelHome
        ).getEvent());
    }

    public static void eventArticleHubungiKamiClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickHubungi,
                ContactUsEventTracking.Label.LabelArticle
        ).getEvent());
    }

    public static void eventSuccessClick(String labelInvoice) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, "Submit"),
                String.format(ContactUsEventTracking.Label.LabelFormList, labelInvoice)
        ).getEvent());
    }

    public static void eventOkClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, "Inbox"),
                ContactUsEventTracking.Label.LabelSubmitTicket
        ).getEvent());
    }

    public static void eventChatBotOkClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickChatBot,
                String.format(ContactUsEventTracking.Label.LabelGeneric, "")
        ).getEvent());
    }

    public static void sendGTMInboxTicket(String event, String category, String action, String label) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(event, category, action, label).getEvent());
    }
}
