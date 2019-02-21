/*
 * Created By Kulomady on 11/25/16 11:55 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:55 PM
 */

package com.tokopedia.core.discovery.catalog.listener;

import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.linker.model.LinkerData;

/**
 * @author anggaprasetiyo on 10/18/16.
 */

public interface ICatalogActionFragment {

    /**
     * inflate fragment catalog product list
     *
     * @param catalogId untuk dilempar ke fragment yang dituju
     */
    void navigateToCatalogProductList(String catalogId);

    /**
     * kirim share data ke activity, karena activity yang akan handle menu share
     *
     * @param shareData share data object
     */
    void deliverCatalogShareData(LinkerData shareData);
}
