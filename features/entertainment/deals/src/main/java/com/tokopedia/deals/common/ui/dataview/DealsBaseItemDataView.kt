package com.tokopedia.deals.common.ui.dataview

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by jessica on 16/06/20
 */

open class DealsBaseItemDataView (
        var isSuccess: Boolean = false,
        var isLoaded: Boolean = false
): ImpressHolder()