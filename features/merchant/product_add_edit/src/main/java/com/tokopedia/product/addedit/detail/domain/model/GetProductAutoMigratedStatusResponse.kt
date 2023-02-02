package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.SerializedName

data class GetProductAutoMigratedStatusResponse(
    @SerializedName("isProductAutomigrated")
    val isProductAutoMigrated: IsProductAutoMigrated = IsProductAutoMigrated()
)

data class IsProductAutoMigrated(
    @SerializedName("productMigrateStatus")
    val productMigrateStatus: ProductMigrateStatus = ProductMigrateStatus()
)

data class ProductMigrateStatus(
    @SerializedName("is_automigrated")
    val isAutoMigrated: Boolean = false
)
