package com.tokopedia.saldodetails.commom.analytics;

public class SaldoDetailsConstants {

    public static final String SALDO_HELP_URL = "";

    interface Event {
        String EVENT_CLICK_FINTECH_MICROSITE = "clickFintechMicrosite";
    }

    interface Category {
        String SALDO_MAIN_SCREEN = "fin - android main screen";
    }

    interface Action {
        String SALDO_ANCHOR_EVENT_ACTION = "sal - %s click";
    }

    public interface EventLabel {
        String SALDO_PAGE = "saldo page";
    }
}
