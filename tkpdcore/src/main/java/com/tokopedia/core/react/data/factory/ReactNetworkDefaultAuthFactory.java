package com.tokopedia.core.react.data.factory;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.react.data.datasource.ReactNetworkDataSource;

/**
 * @author ricoharisin .
 */

public class ReactNetworkDefaultAuthFactory {

    private CommonService commonService;

    public ReactNetworkDefaultAuthFactory(CommonService commonService) {
        this.commonService = commonService;
    }

    public ReactNetworkDataSource createReactNetworkDataSource() {
        return new ReactNetworkDataSource(commonService);
    }
}
