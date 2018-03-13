package com.tokopedia.topads.sdk.domain.mapper;

import android.content.Context;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Mapper;
import com.tokopedia.topads.sdk.domain.model.Status;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.network.JsonResponseConverter;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;
import com.tokopedia.topads.sdk.base.Config;

import org.json.JSONObject;

/**
 * @author by errysuprayogi on 4/3/17.
 */

public class TopAdsMapper extends Mapper<TopAdsModel> {

    private static final String TAG = TopAdsMapper.class.getSimpleName();
    private RawHttpRequestExecutor executor;
    private JsonResponseConverter converter;
    private Context context;
    private String errorMessage;
    private int position;

    public TopAdsMapper(Context context, RawHttpRequestExecutor executor, int position) {
        this.executor = executor;
        this.context = context;
        this.position = position;
        errorMessage = context.getString(R.string.error_response_message);
        converter = JsonResponseConverter.newInstance();
    }

    @Override
    public TopAdsModel getModel() {
        try {
            JSONObject object = converter.convertResponse(executor.makeRequest());
            TopAdsModel model = new TopAdsModel(object);
            model.setAdsPosition(position);
            return model;
        } catch (Exception e) {
            errorMessage = e.getLocalizedMessage();
        }
        return mappingInvalidResponse();
    }

    private TopAdsModel mappingInvalidResponse() {
        TopAdsModel model = new TopAdsModel();
        model.setStatus(new Status(Config.ERROR_CODE_INVALID_RESPONSE, errorMessage));
        return model;
    }

}
