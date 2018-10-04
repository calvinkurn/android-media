package com.tokopedia.core.network.retrofit.coverters;


import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 01/12/2015.
 */
@Deprecated
/**
 * @see com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
 */
public class TkpdResponseConverter extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (TkpdResponse.class == type) {
            return new Converter<ResponseBody, TkpdResponse>() {
                @Override
                public TkpdResponse convert(ResponseBody value) throws IOException {
                    return TkpdResponse.factory(value.string());
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
        if (TkpdResponse.class == type) {
            return new Converter<TkpdResponse, RequestBody>() {
                @Override
                public RequestBody convert(TkpdResponse value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value.getStrResponse());
                }
            };
        }
        return null;
    }


}
