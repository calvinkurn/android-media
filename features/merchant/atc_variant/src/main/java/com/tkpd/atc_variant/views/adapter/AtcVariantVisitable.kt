package com.tkpd.atc_variant.views.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Yehezkiel on 06/05/21
 */

interface AtcVariantVisitable : Visitable<AtcVariantTypeFactory> {
    fun uniqueId(): Long
    fun isEqual(newData: AtcVariantVisitable): Boolean
}