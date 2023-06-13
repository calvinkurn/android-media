package com.tokopedia.epharmacy.component

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory

interface BaseEPharmacyDataModel : Visitable<EPharmacyAdapterFactory> {
    fun type(): String
    fun name(): String
    fun equalsWith(newData: BaseEPharmacyDataModel): Boolean
    fun getChangePayload(newData: BaseEPharmacyDataModel) : Bundle?
}