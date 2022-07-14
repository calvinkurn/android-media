package com.tokopedia.tokopedianow.searchcategory.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.searchcategory.data.model.QuerySafeModel

fun LocalCacheModel?.mapChooseAddressToQuerySafeModel(isQuerySafe: Boolean): QuerySafeModel {
    return QuerySafeModel(
        warehouseId = this?.warehouse_id.getOrDefaultZeroString(),
        isQuerySafe = isQuerySafe
    )
}

