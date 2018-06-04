package com.tokopedia.abstraction.common.network.converter;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 14/05/18.
 */
public class TokopediaWsV4ResponseConverter extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (TokopediaWsV4Response.class == type) {
            return new Converter<ResponseBody, TokopediaWsV4Response>() {
                @Override
                public TokopediaWsV4Response convert(ResponseBody value) throws IOException {
                    return TokopediaWsV4Response.factory(value.string());
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
        if (TokopediaWsV4Response.class == type) {
            return new Converter<TokopediaWsV4Response, RequestBody>() {
                @Override
                public RequestBody convert(TokopediaWsV4Response value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value.getStrResponse());
                }
            };
        }
        return null;
    }

}
