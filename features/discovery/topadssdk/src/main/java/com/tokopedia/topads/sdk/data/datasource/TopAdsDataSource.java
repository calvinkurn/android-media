package com.tokopedia.topads.sdk.data.datasource;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.MerlinRecomendation;
import com.tokopedia.topads.sdk.domain.model.PreferedCategory;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.base.TKPDMapParam;

import java.util.List;


/**
 * Created by errysuprayogi on 3/27/17.
 */

public interface TopAdsDataSource {

    void setConfig(Config config);

    CpmModel getTopAdsBanner(TKPDMapParam<String, String> param);

}
