package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.network.response.EPharmacyProduct

data class EPharmacyAccordionProductDataModel(val name : String = "", val type : String = "",
                                              val product : EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?)
    : BaseEPharmacyDataModel {

    override fun name(): String = name

    override fun type(): String  = type

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: BaseEPharmacyDataModel): Boolean {
        return newData == this
    }

    override fun getChangePayload(newData: BaseEPharmacyDataModel): Bundle? {
        return null
    }
}
