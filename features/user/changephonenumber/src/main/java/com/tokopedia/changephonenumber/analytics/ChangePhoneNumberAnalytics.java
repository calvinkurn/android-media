package com.tokopedia.changephonenumber.analytics;


import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import javax.inject.Inject;

/**
 * @author by alvinatin on 06/06/18.
 */

public class ChangePhoneNumberAnalytics {


    @Inject
    public ChangePhoneNumberAnalytics() {
    }

    public static class Event {
        static final String CLICK_PROFILE = "clickUserProfile";
    }

    public static class Category {
        static final String WARNING_PAGE = "warning page";
        static final String CHANGE_PHONE_NUMBER_PAGE = "ubah no telepon page";
    }

    public static class Action {
        static final String VIEW_WARNING_MESSAGE = "view on warning message";
        static final String CLICK_ON_SALDO = "click on saldo";
        static final String CLICK_ON_NEXT = "click on lanjutkan";
        static final String CLICK_ON_INFORMATION = "click on information icon";
    }

    public static class Label {
        static final String WARNING_1 = "warning 1";
        static final String WARNING_2 = "warning 2";
        static final String WARNING_3 = "warning 3";
        static final String EMPTY = "";
    }

    public void getEventViewWarningMessageTokocash() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.VIEW_WARNING_MESSAGE,
                Label.WARNING_1
        ));
    }

    public void getEventViewWarningMessageSaldo() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.VIEW_WARNING_MESSAGE,
                Label.WARNING_2
        ));
    }

    public void getEventViewWarningMessageBoth() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.VIEW_WARNING_MESSAGE,
                Label.WARNING_3
        ));
    }

    public void getEventWarningPageClickOnWithdraw() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.CLICK_ON_SALDO,
                Label.EMPTY
        ));
    }

    public void getEventChangePhoneNumberClickOnNext() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_PROFILE,
                Category.CHANGE_PHONE_NUMBER_PAGE,
                Action.CLICK_ON_NEXT,
                Label.EMPTY
        ));
    }

    public void getEventChangePhoneNumberClickOnInfo() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_PROFILE,
                Category.CHANGE_PHONE_NUMBER_PAGE,
                Action.CLICK_ON_INFORMATION,
                Label.EMPTY
        ));
    }
}
