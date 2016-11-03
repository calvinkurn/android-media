package com.tokopedia.tkpd.catalog.interactor;

import com.tokopedia.tkpd.catalog.model.CatalogDetailData;
import com.tokopedia.tkpd.catalog.model.CatalogDetailListData;
import com.tokopedia.tkpd.catalog.model.CatalogListWrapperData;
import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;

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
}
