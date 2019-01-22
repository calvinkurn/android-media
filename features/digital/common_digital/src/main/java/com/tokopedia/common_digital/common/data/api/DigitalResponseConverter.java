package com.tokopedia.common_digital.common.data.api;

import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Rizky on 30/08/18.
 */
public class DigitalResponseConverter extends Converter.Factory {

    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (TkpdDigitalResponse.class == type) {
            return (Converter<ResponseBody, TkpdDigitalResponse>) value -> TkpdDigitalResponse.factory(value.string());
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        if (TkpdDigitalResponse.class == type) {
            return (Converter<TkpdDigitalResponse, RequestBody>) value -> RequestBody.create(MEDIA_TYPE, value.getStrResponse());
        }
        return null;
    }

    public static DigitalResponseConverter create() {
        return new DigitalResponseConverter();
    }

}
