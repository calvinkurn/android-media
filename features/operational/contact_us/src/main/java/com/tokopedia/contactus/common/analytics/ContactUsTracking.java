package com.tokopedia.contactus.common.analytics;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * Created by baghira on 16/07/18.
 */

public class ContactUsTracking extends UnifyTracking {

    public static void eventInboxClick(String action) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, action),
                ContactUsEventTracking.Label.LabelHome
        ).getEvent());
    }

    public static void eventPopularArticleClick(String label) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickArticle,
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventLihatBantuanClick(String action, String label) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickLihat, action),
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventHomeInvoiceClick(String action, String label) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickInvoice, action),
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventLihatTransaksiClick(String action, String label) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickLihat, action),
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }

    public static void eventHomeHubungiKamiClick() {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickHubungi,
                ContactUsEventTracking.Label.LabelHome
        ).getEvent());
    }

    public static void eventArticleHubungiKamiClick() {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickHubungi,
                ContactUsEventTracking.Label.LabelArticle
        ).getEvent());
    }

    public static void eventSuccessClick(String action, String labelInvoice, String category) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, action),
                String.format(ContactUsEventTracking.Label.LabelFormList, labelInvoice, category)
        ).getEvent());
    }

    public static void eventOkClick(String action) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                String.format(ContactUsEventTracking.Action.EventClickTicket, action),
                ContactUsEventTracking.Label.LabelSubmitTicket
        ).getEvent());
    }

    public static void eventChatBotOkClick(String label) {
        sendGTMEvent(new EventTracking(
                ContactUsEventTracking.Event.GenericClickBantuanEvent,
                ContactUsEventTracking.Category.EventNewBantuan,
                ContactUsEventTracking.Action.EventClickChatBot,
                String.format(ContactUsEventTracking.Label.LabelGeneric, label)
        ).getEvent());
    }
}










