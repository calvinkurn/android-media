package com.tokopedia.pdp.fintech.helper

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass

object Utils {

    fun safeLet(listOfAllChecker: List<Any?>): Any {
        var counter = 0
        for(i in listOfAllChecker.indices)
        {
            if(listOfAllChecker[i] == null) {
                counter = -1
                break;
            }
        }
        return counter != -1

    }


    fun returnRouteObject(chipsData: ChipsData):FintechRedirectionWidgetDataClass{
      return  FintechRedirectionWidgetDataClass(
            cta = chipsData.cta?.type?:0,
            redirectionUrl = chipsData.cta?.androidUrl,
            tenure =  chipsData.tenure?:0,
            gatewayId = chipsData.gatewayId?:"0",
            gatewayCode =  chipsData.productCode,
            widgetBottomSheet = chipsData.cta?.bottomsheet,
            userStatus =  chipsData.userStatus,
            linkingStatus = chipsData.linkingStatus,
            gatewayPartnerName =  chipsData.name,
            installmentAmout = chipsData.installmentAmount,
            promoName = chipsData.promoName
        )
    }


    fun setListOfData(chipsData: ChipsData):List<Any?>{
      return  listOf( chipsData.cta?.androidUrl,
            chipsData.cta?.type, chipsData.tenure,
            chipsData.productCode, chipsData.cta?.bottomsheet,
            chipsData.gatewayId, chipsData.userStatus,
            chipsData.name,  chipsData.linkingStatus,
            chipsData.installmentAmount)

    }


    fun createRedirectionAppLink(fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass,
                                 productID:String?):String
    {
        return fintechRedirectionWidgetDataClass.redirectionUrl +
                "?productID=${productID}" +
                "&tenure=${fintechRedirectionWidgetDataClass.tenure}" +
                "&gatewayCode=${fintechRedirectionWidgetDataClass.gatewayCode}" +
                "&gatewayID=${fintechRedirectionWidgetDataClass.gatewayId}" +
                "&productURL=${setProductUrl(productID)}"
    }

    private fun setProductUrl(productID: String?): String {
        return UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, productID).encodeToUtf8()
    }
}
