package com.tokopedia.topads.debit.autotopup.data.extensions

import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus

val AutoTopUpStatus.selectedPrice: AutoTopUpItem
    get() = availableNominals.firstOrNull { it.id == this.id } ?: AutoTopUpItem()