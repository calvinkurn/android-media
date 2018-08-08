package com.tokopedia.discovery.catalog.interactor;

import com.tokopedia.discovery.catalog.model.CatalogDetailData;
import com.tokopedia.discovery.catalog.model.CatalogDetailListData;
import com.tokopedia.discovery.catalog.model.CatalogListWrapperData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public interface ICataloDataInteractor {

    /**
     * get catalog detail data, bisa dari cache bisa dari network, tergantung ketersediaan
     *
     * @param param      TkpdMapParam untuk network
     * @param subscriber subscriber untuk observable
     */
    void getDetailCatalogData(TKPDMapParam<String, String> param,
                              Subscriber<CatalogDetailData> subscriber);

    /**
     * get catalog product list dari network
     *
     * @param catalogListWrapperData catalog list wrapper data object
     * @param subscriber             subscriber untuk observable
     */
    void getDetailCatalogListData(CatalogListWrapperData catalogListWrapperData,
                                  Subscriber<CatalogDetailListData> subscriber);

    /**
     * unsubscribe when view is destroyed
     */
    void unsubscribe();
}
