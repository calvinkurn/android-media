package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

/**
 * Created By @ilhamsuaib on 19/03/21
 */

interface PMWidgetListener : PMDeactivateWidget.PMDeactivateWidgetListener,
        ErrorStateWidget.Listener, TickerWidget.Listener, CancelDeactivationSubmissionWidget.Listener,
        RegistrationHeaderWidget.Listener, ExpandableWidget.Listener, UpgradePmProWidget.Listener