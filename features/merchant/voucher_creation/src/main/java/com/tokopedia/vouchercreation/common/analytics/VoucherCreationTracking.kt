package com.tokopedia.vouchercreation.common.analytics

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.track.TrackApp
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.ADD_PRODUCT
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_CANCEL
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_CHANGE
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_DETAIL
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_DOWNLOAD
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_DUPLICATE
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_FILL_COUPON_PRODUCT
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_SAVE
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.CLICK_STOP
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.FILTER_ETALASE
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.FILTER_LOCATION
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.FILTER_PRODUCT_CATEGORY
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.FILTER_VOUCHER_STATUS
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.FILTER_VOUCHER_TARGET
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.FILTER_VOUCHER_TYPE
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.SEARCH_PRODUCT
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.SELECT_PRODUCT
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click.SORT_PRODUCT
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.CREATION
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventCategory.VoucherList.VOUCHER_LIST
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductData
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.SocmedType

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

    fun sendBroadCastChatClickTracking(category: String, shopId: String) {
        sendGeneralTracking(
            event = VoucherCreationAnalyticConstant.Event.CLICK_MERCHANT_VOUCHER,
            action = VoucherCreationAnalyticConstant.EventAction.Click.BROADCAST_CREATION,
            category = category,
            label = shopId,
            currentSite = VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
            businessUnit = VoucherCreationAnalyticConstant.Values.COMMUNICATION
        )
    }

    // no 8
    fun clickFillCouponProduct(shopId: String, voucherType: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_FILL_COUPON_PRODUCT, label = "", category = CREATION)
    }

    // no 9
    fun clickSaveInfo(shopId: String, voucherType: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_SAVE, label = "", category = CREATION)
    }

    // no 28
    fun clickFilterVoucherStatus(shopId: String, voucherStatus: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = FILTER_VOUCHER_STATUS, label = getVoucherStatus(voucherStatus), category = VOUCHER_LIST)
    }

    // no 41
    fun clickFilterVoucherType(shopId: String, voucherType: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = FILTER_VOUCHER_TYPE, label = voucherType, category = VOUCHER_LIST)
    }

    // no 42
    fun clickFilterVoucherTarget(shopId: String, voucherType: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = FILTER_VOUCHER_TARGET, label = voucherType, category = VOUCHER_LIST)
    }

    // no 14
    fun clickSearchProduct(shopId: String, productName: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = SEARCH_PRODUCT, label = productName, category = CREATION)
    }

    // no 15
    fun clickFilterLocation(shopId: String, location: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = FILTER_LOCATION, label = location, category = CREATION)
    }

    // no 16
    fun clickFilterProductCategory(shopId: String, categoryName: List<String>) {
        sendGeneralClickProductListTracking(shopId = shopId, action = FILTER_PRODUCT_CATEGORY, label = categoryName, category = CREATION)
    }

    // no 17
    fun clickFilterEtalase(shopId: String, etalaseName: List<String>) {
        sendGeneralClickProductListTracking(shopId = shopId, action = FILTER_ETALASE, label = etalaseName, category = CREATION)
    }

    // no 18
    fun clickSortProductLabel(shopId: String, productLabel: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = SORT_PRODUCT, label = productLabel, category = CREATION)
    }

    // no 19
    fun clickSelectProduct(shopId: String, productId: String) {
        sendGeneralClickProductListTracking(shopId = shopId, action = SELECT_PRODUCT, label = productId, category = CREATION)
    }

    // no 20
    fun clickAddProduct(shopId: String, products: List<ProductUiModel>) {
        val selectedProductIds = mapSelectedProductIds(products)
        val dataLayer = mutableMapOf<String, Any>(
                VoucherCreationAnalyticConstant.Key.EVENT to VoucherCreationAnalyticConstant.Event.CLICK_PG,
                VoucherCreationAnalyticConstant.Key.EVENT_ACTION to ADD_PRODUCT,
                VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to CREATION,
                VoucherCreationAnalyticConstant.Key.EVENT_LABEL to selectedProductIds,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
                VoucherCreationAnalyticConstant.Key.SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    // no 30
    fun clickDownloadOnBottomsheet(shopId: String, voucherStatus: Int) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_DOWNLOAD, label = getVoucherStatus(voucherStatus), category = VOUCHER_LIST)
    }

    // no 31
    fun clickCancelOnBottomsheet(shopId: String, voucherStatus: Int) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_CANCEL, label = getVoucherStatus(voucherStatus), category = VOUCHER_LIST)
    }

    // no 32
    fun clickChangeOnBottomsheet(shopId: String, voucherStatus: Int) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_CHANGE, label = getVoucherStatus(voucherStatus), category = VOUCHER_LIST)
    }

    // no 33
    fun clickStopOnBottomsheet(shopId: String, voucherStatus: Int) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_STOP, label = getVoucherStatus(voucherStatus), category = VOUCHER_LIST)
    }

    // no 34
    fun clickDuplicateOnBottomsheet(shopId: String, voucherStatus: Int) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_DUPLICATE, label = getVoucherStatus(voucherStatus), category = VOUCHER_LIST)
    }

    // no 35
    fun clickDetailOnBottomsheet(shopId: String, voucherStatus: Int) {
        sendGeneralClickProductListTracking(shopId = shopId, action = CLICK_DETAIL, label = getVoucherStatus(voucherStatus), category = VOUCHER_LIST)
    }

    private fun mapSelectedProductIds(products: List<ProductUiModel>): List<String> {
        val list: MutableList<String> = mutableListOf()
        products.map { productUiModel ->
            list.add(productUiModel.id)
        }
        return list
    }

    private fun sendGeneralClickProductListTracking(shopId: String, action: String, label: String, category: String) {
        val dataLayer = mutableMapOf<String, Any>(
                VoucherCreationAnalyticConstant.Key.EVENT to VoucherCreationAnalyticConstant.Event.CLICK_PG,
                VoucherCreationAnalyticConstant.Key.EVENT_ACTION to action,
                VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to category,
                VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
                VoucherCreationAnalyticConstant.Key.SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    private fun sendGeneralClickProductListTracking(shopId: String, action: String, label: List<String>, category: String) {
        val dataLayer = mutableMapOf<String, Any>(
                VoucherCreationAnalyticConstant.Key.EVENT to VoucherCreationAnalyticConstant.Event.CLICK_PG,
                VoucherCreationAnalyticConstant.Key.EVENT_ACTION to action,
                VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to category,
                VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
                VoucherCreationAnalyticConstant.Key.SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    private fun sendGeneralTracking(event: String = "",
                                    category: String = "",
                                    action: String = "",
                                    label: String = "",
                                    screenName: String = "",
                                    userId: String = "",
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

    private fun getVoucherStatus(voucherStatus: Int): String {
        return when {
            voucherStatus == VoucherStatusConst.DELETED -> "DELETED"
            voucherStatus == VoucherStatusConst.ENDED -> "ENDED"
            voucherStatus == VoucherStatusConst.NOT_STARTED -> "NOT_STARTED"
            voucherStatus == VoucherStatusConst.ONGOING -> "ONGOING"
            voucherStatus == VoucherStatusConst.PROCESSING -> "PROCESSING"
            voucherStatus == VoucherStatusConst.STOPPED -> "STOPPED"
            else -> ""
        }
    }

    private fun getVoucherStatus(voucherStatus: String): String {
        return when {
            voucherStatus == VoucherStatus.NOT_STARTED -> "NOT_STARTED"
            voucherStatus == VoucherStatus.ONGOING -> "ONGOING"
            voucherStatus == VoucherStatus.HISTORY -> "HISTORY"
            voucherStatus == VoucherStatus.NOT_STARTED_AND_ONGOING -> "NOT_STARTED_AND_ONGOING"
            else -> ""
        }
    }

}