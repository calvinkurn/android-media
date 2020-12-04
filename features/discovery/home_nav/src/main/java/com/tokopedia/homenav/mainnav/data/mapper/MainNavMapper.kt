package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactory
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel

class MainNavMapper (private val mainNavDataFactory: MainNavDataFactory) {

    fun mapMainNavData(userPojo: UserPojo?,
                       walletBalanceModel: WalletBalanceModel?,
                       saldoPojo: SaldoPojo?,
                       userMembershipPojo: MembershipPojo?,
                       shopInfoPojo: ShopInfoPojo?,
                       categoryData: List<DynamicHomeIconEntity.Category>?): MainNavigationDataModel {
        val factory: MainNavDataFactory = mainNavDataFactory.buildVisitableList( )
                .addProfileSection(userPojo, walletBalanceModel, saldoPojo, userMembershipPojo, shopInfoPojo)
                .addSeparatorSection()
                .addBUListSection(categoryData)
        return MainNavigationDataModel(factory.build())
    }

}