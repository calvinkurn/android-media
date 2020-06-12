package com.tokopedia.vouchercreation.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.SocmedType

object VoucherCreationTracking {

    fun sendOpenScreenTracking(screenName: String,
                               isLoggedIn: Boolean,
                               userId: String) {
        val map = mapOf(
                VoucherCreationAnalyticConstant.Key.EVENT to VoucherCreationAnalyticConstant.Event.OPEN_SCREEN,
                VoucherCreationAnalyticConstant.Key.SCREEN_NAME to screenName,
                VoucherCreationAnalyticConstant.Key.IS_LOGGED_IN_STATUS to isLoggedIn,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER,
                VoucherCreationAnalyticConstant.Key.USER_ID to userId,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun sendCreateVoucherClickTracking(@VoucherCreationStep step: Int,
                                       action: String,
                                       label: String = "",
                                       userId: String,
                                       isDuplicate: Boolean = false) {
        sendGeneralTracking(
                event = VoucherCreationAnalyticConstant.Event.CLICK_ADS_CREATION,
                category =
                        when(step) {
                            VoucherCreationStep.TARGET -> VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.TARGET
                            VoucherCreationStep.BENEFIT -> VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.TYPE_BUDGET
                            VoucherCreationStep.PERIOD -> VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.PERIOD
                            VoucherCreationStep.REVIEW -> {
                                if (isDuplicate) {
                                    VoucherCreationAnalyticConstant.EventCategory.VOUCHER_DUPLICATE_REVIEW
                                } else {
                                    VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.REVIEW
                                }
                            }
                            else -> ""
                        },
                action = action,
                label = label,
                screenName =
                    when(step) {
                        VoucherCreationStep.TARGET -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.TARGET
                        VoucherCreationStep.BENEFIT -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.TYPE_BUDGET
                        VoucherCreationStep.PERIOD -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.PERIOD
                        VoucherCreationStep.REVIEW -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.REVIEW
                        else -> ""
                    },
                userId = userId
        )
    }

    fun sendVoucherListClickTracking(action: String,
                                     isActive: Boolean,
                                     label: String = "",
                                     userId: String) {
        sendGeneralTracking(
                event = VoucherCreationAnalyticConstant.Event.CLICK_VOUCHER_LIST,
                category =
                        if (isActive) {
                            VoucherCreationAnalyticConstant.EventCategory.VoucherList.ACTIVE
                        } else {
                            VoucherCreationAnalyticConstant.EventCategory.VoucherList.HISTORY
                        },
                action = action,
                label = label,
                screenName =
                        if (isActive) {
                            VoucherCreationAnalyticConstant.ScreenName.VoucherList.ACTIVE
                        } else {
                            VoucherCreationAnalyticConstant.ScreenName.VoucherList.HISTORY
                        },
                userId = userId
        )
    }

    fun sendVoucherDetailClickTracking(isDetailEvent: Boolean = true,
                                       @VoucherStatusConst status: Int,
                                       action: String,
                                       label: String = "",
                                       userId: String) {
        sendGeneralTracking(
                event =
                        if (isDetailEvent) {
                            VoucherCreationAnalyticConstant.Event.CLICK_VOUCHER_DETAIL
                        } else {
                            VoucherCreationAnalyticConstant.Event.CLICK_VOUCHER_LIST
                        },
                category =
                        when(status) {
                            VoucherStatusConst.NOT_STARTED -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.UPCOMING
                            VoucherStatusConst.ONGOING -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.ONGOING
                            VoucherStatusConst.STOPPED -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.CANCELLED
                            VoucherStatusConst.ENDED -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.ENDED
                            else -> ""
                        },
                action = action,
                label = label,
                screenName =
                        when(status) {
                            VoucherStatusConst.NOT_STARTED -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.UPCOMING
                            VoucherStatusConst.ONGOING -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.ONGOING
                            VoucherStatusConst.STOPPED -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.CANCELLED
                            VoucherStatusConst.ENDED -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.ENDED
                            else -> ""
                        },
                userId = userId
        )
    }

    fun sendShareClickTracking(@SocmedType socMedType: Int,
                               userId: String) {

    }

    fun sendVoucherListImpressionTracking(action: String,
                                          isActive: Boolean,
                                          userId: String) {
        sendGeneralTracking(
                event = VoucherCreationAnalyticConstant.Event.VIEW_VOUCHER_LIST_IRIS,
                category =
                        if (isActive) {
                            VoucherCreationAnalyticConstant.EventCategory.VoucherList.ACTIVE
                        } else {
                            VoucherCreationAnalyticConstant.EventCategory.VoucherList.HISTORY
                        },
                action = action,
                label = "",
                screenName =
                        if (isActive) {
                            VoucherCreationAnalyticConstant.ScreenName.VoucherList.ACTIVE
                        } else {
                            VoucherCreationAnalyticConstant.ScreenName.VoucherList.HISTORY
                        },
                userId = userId
        )
    }

    fun sendCreateVoucherImpressionTracking(@VoucherCreationStep step: Int,
                                            action: String,
                                            label: String = "",
                                            userId: String,
                                            isDuplicate: Boolean = false) {
        sendGeneralTracking(
                event = VoucherCreationAnalyticConstant.Event.VIEW_ADS_CREATION_IRIS,
                category =
                when(step) {
                    VoucherCreationStep.TARGET -> VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.TARGET
                    VoucherCreationStep.BENEFIT -> VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.TYPE_BUDGET
                    VoucherCreationStep.PERIOD -> VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.PERIOD
                    VoucherCreationStep.REVIEW -> {
                        if (isDuplicate) {
                            VoucherCreationAnalyticConstant.EventCategory.VOUCHER_DUPLICATE_REVIEW
                        } else {
                            VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.REVIEW
                        }
                    }
                    else -> ""
                },
                action = action,
                label = label,
                screenName =
                when(step) {
                    VoucherCreationStep.TARGET -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.TARGET
                    VoucherCreationStep.BENEFIT -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.TYPE_BUDGET
                    VoucherCreationStep.PERIOD -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.PERIOD
                    VoucherCreationStep.REVIEW -> VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.REVIEW
                    else -> ""
                },
                userId = userId
        )
    }

    fun sendVoucherDetailImpressionTracking(@VoucherStatusConst status: Int,
                                            action: String,
                                            label: String = "",
                                            userId: String) {
        sendGeneralTracking(
                event = VoucherCreationAnalyticConstant.Event.VIEW_VOUCHER_DETAIL_IRIS,
                category =
                    when(status) {
                        VoucherStatusConst.NOT_STARTED -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.UPCOMING
                        VoucherStatusConst.ONGOING -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.ONGOING
                        VoucherStatusConst.STOPPED -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.CANCELLED
                        VoucherStatusConst.ENDED -> VoucherCreationAnalyticConstant.EventCategory.VoucherDetail.ENDED
                        else -> ""
                    },
                action = action,
                label = label,
                screenName =
                    when(status) {
                        VoucherStatusConst.NOT_STARTED -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.UPCOMING
                        VoucherStatusConst.ONGOING -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.ONGOING
                        VoucherStatusConst.STOPPED -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.CANCELLED
                        VoucherStatusConst.ENDED -> VoucherCreationAnalyticConstant.ScreenName.VoucherDetail.ENDED
                        else -> ""
                    },
                userId = userId
        )
    }

    private fun sendGeneralTracking(event: String,
                                    category: String,
                                    action: String,
                                    label: String,
                                    screenName: String,
                                    userId: String) {
        val map = mapOf(
                VoucherCreationAnalyticConstant.Key.EVENT to event,
                VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to category,
                VoucherCreationAnalyticConstant.Key.EVENT_ACTION to action,
                VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
                VoucherCreationAnalyticConstant.Key.SCREEN_NAME to screenName,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER,
                VoucherCreationAnalyticConstant.Key.USER_ID to userId,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

}