package com.tokopedia.mvc.data.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.mvc.data.response.MerchantPromotionGetQuotaUsage
import com.tokopedia.mvc.data.response.Sources
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import java.util.ArrayList
import javax.inject.Inject
import com.tokopedia.mvc.R

class GetVoucherQuotaMapper @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private const val DAY_PERIOD = 30
    }

    fun mapRemoteModelToUiModel(remoteModel: MerchantPromotionGetQuotaUsage): VoucherCreationQuota =
        remoteModel.data.let {
            VoucherCreationQuota(
                used = it.quota.used,
                remaining = it.quota.remaining,
                total = it.quota.total,
                statusSource = it.statusSource,
                sources = it.sources.mapToUiModel(),
                tickerTitle = it.ticker.title,
                ctaText = it.ctaText,
                ctaLink = it.ctaLink,
                quotaUsageFormatted = listOf(it.quota.remaining, it.quota.total).joinToString(
                    context.getString(R.string.smvc_voucherlist_voucher_usage_delimiter)
                ),
                quotaErrorMessage = if (it.quota.remaining.isZero())
                    context.getString(R.string.smvc_voucherlist_error_quota_format,
                        it.quota.total, DAY_PERIOD) else ""
            )
        }

    private fun ArrayList<Sources>.mapToUiModel() = map {
        VoucherCreationQuota.Sources(
            name = it.name,
            used = it.used,
            remaining = it.remaining,
            total = it.total,
            expired = it.expired,
        )
    }
}
