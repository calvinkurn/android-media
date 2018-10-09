package com.tokopedia.tkpdreactnative.react.data.factory;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.tkpdreactnative.react.data.datasource.ReactNetworkDataSource;

/**
 * @author ricoharisin .
 */

public class ReactNetworkFactory {

    private CommonService commonService;

    public ReactNetworkFactory(CommonService commonService) {
        this.commonService = commonService;
    }

    public ReactNetworkDataSource createReactNetworkDataSource() {
        return new ReactNetworkDataSource(commonService);
    }
}
