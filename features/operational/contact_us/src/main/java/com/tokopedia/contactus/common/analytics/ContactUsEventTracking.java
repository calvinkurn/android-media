package com.tokopedia.contactus.common.analytics;

import com.tokopedia.core.analytics.AppEventTracking;

/**
 * Created by baghira on 16/07/18.
 */

public class ContactUsEventTracking implements AppEventTracking {
    interface Event {
        String GenericClickBantuanEvent = "clickBantuan";
    }

    interface Category {
        String EventNewBantuan = "New Bantuan";
    }

    interface Action {
        String EventClickTicket = "Click %s Ticket";
        String EventClickArticle = "Click Article";
        String EventClickLihat = "Click Lihat Selengkapnya %s";
        String EventClickInvoice = "Click Invoice - %s";
        String EventClickHubungi = "Click Hubungi Kami";
        String EventClickChatBot = "Click Chat dengan Bot";
    }

    interface Label {
        String LabelHome = "Home";
        String LabelGeneric = "%s";
        String LabelArticle = "Article";
        String LabelSubmitTicket = "After Submit Ticket";
        String LabelFormList = "%s - Barang yang diterima tidak sesuai";

    }
}
