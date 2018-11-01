package com.tokopedia.saldodetails.analytics;

public interface SaldoEventConstant {


    public interface Event {

        String DEPOSIT = "clickSaldo";

    }

    public interface Category {
        String DEPOSIT = "Saldo";
    }

    public interface Action {
        String CLICK = "Click";
    }

    public interface EventLabel {
        String TOPUP = "TopUp";
    }

    public interface ScreenName {

//        public static final String SCREEN_LOYALTY = "Loyalty page";
    }
}
