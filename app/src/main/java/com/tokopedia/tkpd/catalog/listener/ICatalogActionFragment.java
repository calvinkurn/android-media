package com.tokopedia.tkpd.catalog.listener;

import com.tokopedia.tkpd.product.model.share.ShareData;

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
    void deliverCatalogShareData(ShareData shareData);
}
