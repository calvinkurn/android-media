package com.tokopedia.homenav.mainnav.data.factory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity

interface MainNavDataFactory {

    fun buildVisitableList(): MainNavDataFactory

    fun addSeparatorSection(): MainNavDataFactory
    fun addBUListSection(categoryData: List<DynamicHomeIconEntity.Category>?): MainNavDataFactory

    fun build(): List<Visitable<*>>
}