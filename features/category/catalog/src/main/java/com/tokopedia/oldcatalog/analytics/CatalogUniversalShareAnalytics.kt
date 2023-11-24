package com.tokopedia.oldcatalog.analytics

import com.tokopedia.oldcatalog.model.util.CatalogConstant

object CatalogUniversalShareAnalytics {

    fun navBarShareButtonClickedGTM(catalogId : String , userId : String){
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.ActionKeys.CLICK_SHARE_BUTTON,
                CatalogDetailAnalytics.CategoryKeys.TOP_NAV_CATALOG, catalogId, catalogId,
                "${CatalogConstant.CATALOG_PAGE}.${CatalogConstant.NULL_STRING}.${CatalogConstant.NULL_STRING}.$catalogId", userId
        )
    }

    fun dismissShareBottomSheetGTM(catalogId : String , userId : String){
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.ActionKeys.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                catalogId, catalogId, "",userId
        )
    }

    fun sharingChannelSelectedGTM(channel: String,catalogId : String , userId : String){
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.ActionKeys.CLICK_SHARING_CHANNEL,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                "$channel - $catalogId", catalogId, "",userId
        )
    }

    fun shareBottomSheetAppearGTM(catalogId : String , userId : String){
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_VIEW_CATALOG_IRIS,
                CatalogDetailAnalytics.ActionKeys.VIEW_ON_SHARING_CHANNEL,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                catalogId, catalogId, "",userId
        )
    }

    fun userTakenScreenShotGTM(catalogId : String , userId : String){
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_VIEW_CATALOG_IRIS,
                CatalogDetailAnalytics.ActionKeys.VIEW_SCREENSHOT_SHARE_BOTTOM_SHEET,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY, catalogId,
                catalogId, "",userId
        )
    }

    fun userClosesScreenShotBottomSheetGTM(catalogId : String , userId : String){
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.ActionKeys.CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                catalogId, catalogId, "",userId
        )
    }

    fun sharingChannelScreenShotSelectedGTM(channel : String,catalogId : String , userId : String){
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.ActionKeys.CLICK_CHANNEL_SBS_SCREENSHOT,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                "$channel - $catalogId", catalogId, "",userId
        )
    }

    fun allowPopupGTM(state : String,catalogId : String , userId : String) {
        CatalogDetailAnalytics.sendSharingExperienceEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.ActionKeys.CLICK_ACCESS_PHOTO_FILES,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                "$state - $catalogId", catalogId, "",userId
        )
    }
}
