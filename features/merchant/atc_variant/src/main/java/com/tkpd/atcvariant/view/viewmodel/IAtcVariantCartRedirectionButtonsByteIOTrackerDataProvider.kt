package com.tkpd.atcvariant.view.viewmodel

import com.tokopedia.product.detail.common.buttons_byte_io_tracker.ICartRedirectionButtonsByteIOTracker
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.ICartRedirectionButtonsByteIOTrackerDataProvider

/**
 * This interface is used as a bridge for providing data for the cart redirection buttons ByteIO
 * tracker handled by the [ICartRedirectionButtonsByteIOTracker]. This interface is created because
 * the data exist on the ATC variant page need to be mapped before it can be used on the tracker and
 * to extract the mapping logic to a separate class implementation.
 *
 * Tracker Sheet: https://bytedance.sg.larkoffice.com/sheets/YVaGsNyMfhqbjzt7HJvlH4FIgof?sheet=3a360f
 */

interface IAtcVariantCartRedirectionButtonsByteIOTrackerDataProvider : ICartRedirectionButtonsByteIOTrackerDataProvider {
    fun registerAtcVariantCartRedirectionButtonsByteIOTrackerDataProvider(mediator: GetVariantDataMediator)
}
