package com.tokopedia.logisticCommon.data.converter;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.response.GeneratedHostResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * @author Angga.Prasetiyo on 10/12/2015.
 */
public class GeneratedHostConverter extends Converter.Factory {

    private static final String DATA_KEY = "data";
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit
    ) {
        if (GeneratedHostResponse.class == type) {
            return (Converter<ResponseBody, GeneratedHostResponse>) value -> {
                String body = value.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.isNull(DATA_KEY) ||
                            jsonObject.getJSONObject(DATA_KEY).isNull("generated_host")) {
                        return new Gson().fromJson(jsonObject.toString(), GeneratedHostResponse.class);
                    } else {
                        return new Gson().fromJson(jsonObject
                                .getJSONObject(DATA_KEY)
                                .getJSONObject("generated_host")
                                .toString(), GeneratedHostResponse.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            };
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations, Retrofit retrofit
    ) {
        if (String.class == type) {
            return (Converter<String, RequestBody>) value -> RequestBody.create(MEDIA_TYPE, value);
        }
        return null;
    }
}
