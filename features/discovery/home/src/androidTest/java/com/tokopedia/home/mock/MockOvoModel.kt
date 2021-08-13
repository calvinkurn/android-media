package com.tokopedia.home.mock

import com.tokopedia.home.beranda.data.model.SectionContentItem
import com.tokopedia.home.beranda.data.model.TagAttributes
import com.tokopedia.home.beranda.data.model.TextAttributes
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction

object MockOvoModel {
    fun get(): HeaderDataModel {
        val headerDataModel = HeaderDataModel(
                homeHeaderWalletActionData =
                HomeHeaderWalletAction(
                        labelTitle="OVO",
                        balance="Rp 8.100",
                        redirectUrlBalance="",
                        appLinkBalance="tokopedia://webview?url=https://www.tokopedia.com/ovo",
                        typeAction=0,
                        isVisibleActionButton=false,
                        labelActionButton="Topup",
                        appLinkActionButton="tokopedia://tokocash",
                        isLinked=true,
                        abTags= listOf(),
                        pointBalance="0",
                        rawPointBalance=0,
                        cashBalance="Rp 8.100",
                        rawCashBalance=8100,
                        walletType="OVO",
                        isShowAnnouncement=false,
                        isShowTopup=false,
                        topupUrl="https://tokopedia.link/ovo",
                        topupLimit=0),
                tokoPointDrawerData=null,
                tokopointsDrawerHomeData= TokopointsDrawer(
                        redirectURL="https://www.tokopedia.com/rewards/kupon-saya",
                        iconImageURL="https://ecs7.tokopedia.net/img/blog/promo/2020/05/kupon-sayaxxxhdpi.png",
                        sectionContent= listOf(
                                SectionContentItem(textAttributes= TextAttributes(colour="", text="Kupon Saya", isBold=true), tagAttributes= TagAttributes(text="", backgroundColour=""), type="text"),
                                SectionContentItem(textAttributes=TextAttributes(colour="", text="Kupon Baru", isBold=true), tagAttributes=TagAttributes(text="", backgroundColour=""), type="text")
                        ),
                        redirectAppLink="tokopedia://rewards/kupon-saya", mainPageTitle=""), cashBackData=null, isPendingTokocashChecked=false, isWalletDataError=false, isTokoPointDataError=false, isUserLogin=true)
        return headerDataModel
    }
}