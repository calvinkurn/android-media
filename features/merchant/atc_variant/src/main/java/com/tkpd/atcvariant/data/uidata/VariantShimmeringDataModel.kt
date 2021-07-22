package com.tkpd.atcvariant.data.uidata

import android.os.Bundle
import com.tkpd.atcvariant.view.adapter.AtcVariantTypeFactory
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable

/**
 * Created by Yehezkiel on 11/05/21
 */
class VariantShimmeringDataModel(
        val position: Long = 0
) : AtcVariantVisitable {
    override fun uniqueId(): Long = position
    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return newData is VariantShimmeringDataModel
    }

    override fun getChangePayload(newData: AtcVariantVisitable): Bundle? {
        return null
    }

    override fun type(typeFactory: AtcVariantTypeFactory): Int {
        return typeFactory.type(this)
    }

}