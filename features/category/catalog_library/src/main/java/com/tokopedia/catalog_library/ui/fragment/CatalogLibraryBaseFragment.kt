package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.LINK_CATALOG_LIBRARY_LITE
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

abstract class CatalogLibraryBaseFragment : BaseDaggerFragment() {

    @JvmField
    @Inject
    var remoteConfig : RemoteConfig? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (remoteConfig?.getBoolean(RemoteConfigKey.DISABLE_CATALOG_LIBRARY_PAGE, false) == true) {
            activity?.finish()
            RouteManager.route(activity,LINK_CATALOG_LIBRARY_LITE)
        }
    }
}
