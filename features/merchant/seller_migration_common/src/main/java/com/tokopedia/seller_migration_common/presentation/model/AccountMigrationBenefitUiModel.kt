package com.tokopedia.seller_migration_common.presentation.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class AccountMigrationBenefitUiModel(context: Context,
                                     @DrawableRes val drawableRes: Int,
                                     @StringRes private val titleRes: Int,
                                     @StringRes private val descRes: Int) {

    val title = context.getString(titleRes)
    val description = context.getString(descRes)

}
