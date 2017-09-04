package com.tokopedia.core.react.data.factory;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.react.data.datasource.ReactNetworkDataSource;

/**
 * @author ricoharisin .
 */

public class ReactNetworkAuthFactory {

    private CommonService commonService;

    public ReactNetworkAuthFactory(CommonService commonService) {
        this.commonService = commonService;
    }

    public ReactNetworkDataSource createReactNetworkDataSource() {
        return new ReactNetworkDataSource(commonService);
    }
}
