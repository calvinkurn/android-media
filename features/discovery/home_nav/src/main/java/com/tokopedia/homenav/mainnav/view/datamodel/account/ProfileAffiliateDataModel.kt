package com.tokopedia.homenav.mainnav.view.datamodel.account

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.SellerTypeFactory

/**
 * Created by dhaba
 */
data class ProfileAffiliateDataModel (
    var isRegister: Boolean = false,
    var affiliateName: String = "",
    var affiliateAppLink: String = "",

    /**
     * Status
     */
    var isGetAffiliateLoading: Boolean = false,
    var isGetAffiliateError: Boolean = false
) : Visitable<SellerTypeFactory> {
    override fun type(typeFactory: SellerTypeFactory): Int {
        return typeFactory.type(this)
    }
}