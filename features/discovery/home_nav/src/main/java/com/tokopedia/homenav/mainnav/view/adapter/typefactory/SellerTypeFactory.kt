package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel

/**
 * Created by dhaba
 */
interface SellerTypeFactory : AdapterTypeFactory {
    fun type(profileSellerDataModel: ProfileSellerDataModel): Int
    fun type(profileAffiliateDataModel: ProfileAffiliateDataModel): Int
}