package com.tokopedia.seller_migration_common.data.source

import android.content.Context
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.AccountMigrationBenefitUiModel

object AccountMigrationBenefitProvider {
    fun getBenefitList(context: Context) =
            listOf(
                    AccountMigrationBenefitUiModel(
                            context,
                            R.drawable.ic_seller_migration_focus_selling,
                            R.string.seller_migration_account_bottom_sheet_item_title_focus,
                            R.string.seller_migration_account_bottom_sheet_item_desc_focus
                    ),
                    AccountMigrationBenefitUiModel(
                            context,
                            R.drawable.ic_seller_migration_closer_buyer,
                            R.string.seller_migration_account_bottom_sheet_item_title_closer,
                            R.string.seller_migration_account_bottom_sheet_item_desc_closer
                    ),
                    AccountMigrationBenefitUiModel(
                            context,
                            R.drawable.ic_seller_migration_monitor_business,
                            R.string.seller_migration_account_bottom_sheet_item_title_monitor,
                            R.string.seller_migration_account_bottom_sheet_item_desc_monitor
                    ),
                    AccountMigrationBenefitUiModel(
                            context,
                            R.drawable.ic_seller_migration_effective_promotion,
                            R.string.seller_migration_account_bottom_sheet_item_title_effective,
                            R.string.seller_migration_account_bottom_sheet_item_desc_effective
                    )
            )
}