package com.tokopedia.search.result.product.banner

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.track.TrackApp

class BannerListenerDelegate(
    private val iris: Iris,
    context: Context?,
) : BannerListener,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

    override fun onBannerClicked(bannerDataView: BannerDataView) {
        openApplink(context, bannerDataView.applink)
        bannerDataView.click(TrackApp.getInstance().gtm)
    }

    override fun onBannerImpressed(bannerDataView: BannerDataView) {
        bannerDataView.impress(iris)
    }
}