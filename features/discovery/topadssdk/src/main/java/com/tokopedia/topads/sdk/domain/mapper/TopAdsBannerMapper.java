package com.tokopedia.topads.sdk.domain.mapper;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Mapper;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.Error;
import com.tokopedia.topads.sdk.network.JsonResponseConverter;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;

import org.json.JSONObject;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class TopAdsBannerMapper extends Mapper<CpmModel> {

    private static final String TAG = TopAdsBannerMapper.class.getSimpleName();
    private RawHttpRequestExecutor executor;
    private JsonResponseConverter converter;

    public TopAdsBannerMapper(RawHttpRequestExecutor executor) {
        this.executor = executor;
        this.converter = JsonResponseConverter.newInstance();
    }

    @Override
    public CpmModel getModel() {
        CpmModel cpmModel = new CpmModel();
        try{
            JSONObject object = converter.convertResponse(executor.makeRequest());
            cpmModel = new CpmModel(object);
        }catch (Exception e){
            e.printStackTrace();
            Error error = new Error();
            error.setCode(Config.ERROR_CODE_INVALID_RESPONSE);
            error.setTitle(e.getLocalizedMessage());
            error.setDetail(e.getLocalizedMessage());
            cpmModel.setError(error);
        }
        return cpmModel;
    }

}
