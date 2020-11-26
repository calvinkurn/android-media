package com.tokopedia.seller_migration_common.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.seller_migration_common.R

abstract class AccountSetting(
        @StringRes val titleRes: Int,
        @StringRes val descRes: Int
)

object AccountSettingData: AccountSetting(
        titleRes = R.string.seller_migration_account_setting_ticker_title,
        descRes = R.string.seller_migration_account_setting_ticker_description
)