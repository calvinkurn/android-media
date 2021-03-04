package com.tokopedia.vouchercreation.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.SocmedType

/**
 * docs: https://docs.google.com/spreadsheets/d/1AZjuQ_dg25EvEEWmE8MPMo0f1_DT4IyZPaNpt4cxidA/edit#gid=1732582873
 */
object VoucherCreationTracking {

    fun sendOpenScreenTracking(screenName: String,
                               isLoggedIn: Boolean,
                               userId: String) {
        val map = mapOf(
                VoucherCreationAnalyticConstant.Key.EVENT to VoucherCreationAnalyticConstant.Event.OPEN_SCREEN,
                VoucherCreationAnalyticConstant.Key.SCREEN_NAME to screenName,
                VoucherCreationAnalyticConstant.Key.IS_LOGGED_IN_STATUS to isLoggedIn.toString(),
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
                event =
                        if (isDuplicate) {
                            VoucherCreationAnalyticConstant.Event.CLICK_VOUCHER_DUPLICATE
                        } else {
                            VoucherCreationAnalyticConstant.Event.CLICK_ADS_CREATION
                        },
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
                        VoucherCreationStep.REVIEW -> {
                            if (isDuplicate) {
                                VoucherCreationAnalyticConstant.ScreenName.VOUCHER_DUPLICATE_REVIEW
                            } else {
                                VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.REVIEW
                            }
                        }
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

    fun sendShareClickTracking(@SocmedType socmedType: Int,
                               userId: String,
                               isDetail: Boolean) {
        sendGeneralTracking(
                event = VoucherCreationAnalyticConstant.Event.CLICK_SHARE,
                category = VoucherCreationAnalyticConstant.EventCategory.SHARE_SHEET,
                action =
                        when(socmedType) {
                            SocmedType.COPY_LINK -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_COPY_LINK
                            SocmedType.INSTAGRAM -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_INSTAGRAM
                            SocmedType.FACEBOOK -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_FACEBOOK
                            SocmedType.FACEBOOK_MESSENGER -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_FB_MESSENGER
                            SocmedType.WHATSAPP -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_WHATSAPP
                            SocmedType.LINE -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_LINE
                            SocmedType.TWITTER -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_TWITTER
                            SocmedType.LAINNYA -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_OTHERS
                            SocmedType.BROADCAST -> VoucherCreationAnalyticConstant.EventAction.Click.SHARE_BROADCAST
                            else -> ""
                        },
                label = "",
                screenName = "",
                userId = userId,
                currentSite = when(socmedType) {
                    SocmedType.COPY_LINK -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.INSTAGRAM -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.FACEBOOK -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.FACEBOOK_MESSENGER -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.WHATSAPP -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.LINE -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.TWITTER -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.LAINNYA -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
                    SocmedType.BROADCAST -> VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER_APP
                    else -> ""
                },
                businessUnit = when(socmedType) {
                    SocmedType.COPY_LINK -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.INSTAGRAM -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.FACEBOOK -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.FACEBOOK_MESSENGER -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.WHATSAPP -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.LINE -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.TWITTER -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.LAINNYA -> VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
                    SocmedType.BROADCAST -> VoucherCreationAnalyticConstant.Values.COMMUNICATION
                    else -> ""
                },
                pageSource =
                        when {
                            socmedType != SocmedType.COPY_LINK -> null
                            isDetail -> VoucherCreationAnalyticConstant.PageSource.DETAIL
                            else -> VoucherCreationAnalyticConstant.PageSource.LIST
                        }
        )
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

    fun sendVoucherRecommendationStatus(label: String, shopId: String, userId: String) {
        val map = mutableMapOf<String, Any>(
                VoucherCreationAnalyticConstant.Key.EVENT to VoucherCreationAnalyticConstant.Event.CLICK_MERCHANT_VOUCHER,
                VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.CREATE,
                VoucherCreationAnalyticConstant.Key.EVENT_ACTION to VoucherCreationAnalyticConstant.EventAction.Click.CREATE_VOUCHER,
                VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
                VoucherCreationAnalyticConstant.Key.SHOP_ID to shopId,
                VoucherCreationAnalyticConstant.Key.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun sendGeneralTracking(event: String,
                                    category: String,
                                    action: String,
                                    label: String,
                                    screenName: String,
                                    userId: String,
                                    currentSite: String = VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER,
                                    businessUnit: String = VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS,
                                    pageSource: String? = null) {
        val map = mutableMapOf<String, Any>(
                VoucherCreationAnalyticConstant.Key.EVENT to event,
                VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to category,
                VoucherCreationAnalyticConstant.Key.EVENT_ACTION to action,
                VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
                VoucherCreationAnalyticConstant.Key.SCREEN_NAME to screenName,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to currentSite,
                VoucherCreationAnalyticConstant.Key.USER_ID to userId,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to businessUnit
        )
        pageSource?.run {
            map.put(VoucherCreationAnalyticConstant.Key.PAGE_SOURCE, this)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

}