package com.tokopedia.core.network.retrofit.coverters;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class DigitalResponseConverter extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (TkpdDigitalResponse.class == type) {
            return new Converter<ResponseBody, TkpdDigitalResponse>() {
                @Override
                public TkpdDigitalResponse convert(ResponseBody value) throws IOException {
                    return TkpdDigitalResponse.factory(value.string());
                }
            };
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        if (TkpdDigitalResponse.class == type) {
            return new Converter<TkpdDigitalResponse, RequestBody>() {
                @Override
                public RequestBody convert(TkpdDigitalResponse value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value.getStrResponse());
                }
            };
        }
        return null;
    }

    public static DigitalResponseConverter create() {
        return new DigitalResponseConverter();
    }
}
