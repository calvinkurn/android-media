package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.res.stringResource
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.R.string
import com.tokopedia.nest.components.NestLocalLoad

fun LazyGridScope.CentralizedPromoError(
    throwable: Throwable,
    isLoading: Boolean,
    onRefreshButtonClicked: () -> Unit
) =
    item(span = { GridItemSpan(2) }) {
        val title = stringResource(string.sah_label_on_going_promotion_retry)
        val cause = throwable.cause
        val description = if (cause is MessageErrorException
            && !cause.message.isNullOrBlank()
        ) {
            cause.message
        } else {
            stringResource(string.sah_label_on_going_promotion_error)
        }
        NestLocalLoad(
            title = title,
            isLoading = isLoading,
            description = description.toString(),
            onRefreshButtonClicked = {
                if (!isLoading) {
                    onRefreshButtonClicked.invoke()
                }
            }
        )
    }