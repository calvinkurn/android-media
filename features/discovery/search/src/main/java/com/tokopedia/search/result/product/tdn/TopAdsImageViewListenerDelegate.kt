package com.tokopedia.search.result.product.tdn

import android.content.Context
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider

class TopAdsImageViewListenerDelegate(
    context: Context?,
) : TopAdsImageViewListener,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {
    override fun onTopAdsImageViewImpressed(
        className: String?,
        searchTopAdsImageDataView: SearchProductTopAdsImageDataView,
    ) {
        if (className == null || context == null) return

        com.tokopedia.topads.sdk.utils.TopAdsUrlHitter(context).hitImpressionUrl(
            className,
            searchTopAdsImageDataView.topAdsImageUiModel.adViewUrl,
            "",
            "",
            searchTopAdsImageDataView.topAdsImageUiModel.imageUrl
        )

    }

    override fun onTopAdsImageViewClick(searchTopAdsImageDataView: SearchProductTopAdsImageDataView) {
        val applink = searchTopAdsImageDataView.topAdsImageUiModel.applink ?: return
        openApplink(context, applink)
    }
}
