package com.tokopedia.topads.sdk.domain.mapper;

import android.content.Context;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Mapper;
import com.tokopedia.topads.sdk.domain.model.PreferedCategory;
import com.tokopedia.topads.sdk.network.JsonResponseConverter;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;

import org.json.JSONObject;

/**
 * @author by errysuprayogi on 4/20/17.
 */

public class PreferedCategoryMapper extends Mapper<PreferedCategory> {

    private static final String TAG = TopAdsMapper.class.getSimpleName();
    private RawHttpRequestExecutor executor;
    private JsonResponseConverter converter;
    private Context context;
    private String errorMessage;

    public PreferedCategoryMapper(Context context, RawHttpRequestExecutor executor) {
        this.executor = executor;
        this.context = context;
        errorMessage = context.getString(R.string.error_response_message);
        converter = JsonResponseConverter.newInstance();
    }

    @Override
    public PreferedCategory getModel() {
        try {
            JSONObject object = converter.convertResponse(executor.makeRequest());
            return new PreferedCategory(object);
        } catch (Exception e) {
            errorMessage = e.getLocalizedMessage();
        }
        return mappingInvalidResponse();
    }

    private PreferedCategory mappingInvalidResponse() {
        PreferedCategory preferedCategory = new PreferedCategory();
        preferedCategory.setErrorMessage(errorMessage);
        return preferedCategory;
    }
}
