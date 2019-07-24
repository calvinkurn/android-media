package com.tokopedia.saldodetails.commom.analytics;

import java.util.concurrent.TimeUnit;

public class SaldoDetailsConstants {

    public static final String SALDO_HELP_URL = "https://www.tokopedia.com/help/article/a-1709?refid=st-1005";
    public static final String SALDOLOCK_PAYNOW_URL="tokopedia://webview?url=https://www.tokopedia.com/fm/modal-toko/dashboard/pembayaran";

    // TODO: 24/7/19 remove url from string constant
    public static long cacheDuration = TimeUnit.HOURS.toSeconds(1);

    interface Event {
        String EVENT_CLICK_FINTECH_MICROSITE = "clickFintechMicrosite";
    }

    interface Category {
        String SALDO_MAIN_SCREEN = "fin - android main screen";
        String FIN_SALDO_PAGE = "fin - saldo page";
    }

    interface Action {
        String SALDO_ANCHOR_EVENT_ACTION = "sal - %s click";
        String SALDO_MODAL_TOKO_IMP = "saldo - modaltoko impression";
        String SALDO_MODAL_TOKO_CLICK = "saldo - modaltoko click";
        String SALDO_MODAL_TOKO_ACTION_CLICK = "saldo - modaltoko %s click";
    }

    public interface EventLabel {
        String SALDO_PAGE = "saldo page";
    }
}
