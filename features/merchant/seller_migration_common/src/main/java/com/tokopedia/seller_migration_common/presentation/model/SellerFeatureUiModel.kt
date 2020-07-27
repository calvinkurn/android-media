package com.tokopedia.seller_migration_common.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureAdapterTypeFactory

data class SellerFeatureUiModel(
        @DrawableRes
        val imageId: Int,
        @StringRes
        val titleId: Int,
        @StringRes
        val descriptionId: Int
) : Visitable<SellerFeatureAdapterTypeFactory> {
    override fun type(typeFactory: SellerFeatureAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}