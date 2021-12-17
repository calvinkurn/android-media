package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet

class AffiliateShareModel(var name: String, var iconId: Int?, var serviceFormat : String?, var id : Int? = 0 ,
                          var type: AffiliatePromotionBottomSheet.Companion.SheetType? = AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                          var urlSample : String?,
                          var buttonLoad: Boolean = false, var isChecked : Boolean = false, var isLinkGenerationEnabled : Boolean = true ) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        return this.id == (other as? AffiliateShareModel)?.id
    }

    override fun hashCode(): Int {
        return id ?: super.hashCode()
    }

}
