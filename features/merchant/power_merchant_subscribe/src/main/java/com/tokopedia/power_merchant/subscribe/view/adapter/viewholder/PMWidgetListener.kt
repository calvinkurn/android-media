package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

/**
 * Created By @ilhamsuaib on 19/03/21
 */

interface PMWidgetListener : PMDeactivateWidget.PMDeactivateWidgetListener,
    TickerWidget.Listener, CancelDeactivationSubmissionWidget.Listener,
    ShopGradeWidget.Listener, ItemPMProNewSellerBenefitWidget.Listener,
    RegistrationHeaderWidget.Listener, PotentialWidget.Listener,
    GradeBenefitWidget.Listener, FeeServiceWidget.Listener