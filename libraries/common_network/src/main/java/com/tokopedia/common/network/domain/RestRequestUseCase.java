package com.tokopedia.common.network.domain;

import com.tokopedia.common.network.data.ObservableFactory;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.kotlin.util.NullCheckerKt;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Rest api call UseCase
 *
 * @See {@link RestRequestSupportInterceptorUseCase} class for add custom interceptor
 */
public abstract class RestRequestUseCase extends UseCase<Map<Type, RestResponse>> {

    public RestRequestUseCase() {
    }

    @Override
    public Observable<Map<Type, RestResponse>> createObservable(RequestParams requestParams) {
        return ObservableFactory.create(buildRequest(requestParams)).map(checkForNull());
    }

    private Func1<Map<Type, RestResponse>, Map<Type, RestResponse>> checkForNull() {
        return responseMap -> {
            for (Map.Entry<Type, RestResponse> pair : responseMap.entrySet()) {
                if (shouldThrowException()) {
                    NullCheckerKt.throwIfNull(pair.getValue().getData(),
                            RestRequestUseCase.class);
                }
            }
            return responseMap;
        };
    }

    /**
     * For creating requests object, please follow below rule.
     *
     * #1. Mandatory parameter - Valid implementation require
     * typeOfT The specific genericized type of src. You can obtain this type by using the
     * {@link com.google.gson.reflect.TypeToken} class. For example, to get the type for
     * {@code Collection<Foo>}, you should use:
     * <pre>
     * Type typeOfT = new TypeToken&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     * </pre>
     * e.g. TestModel.class or XYZ.class
     *
     *
     * #2. Mandatory parameter - Valid implementation require
     * Full URL of the endpoint including base url. This value is mandatory. Url should be valid else UseCase wil throw exception (RuntimeException("Please set valid request url into your UseCase class"))
     * e.g. https://tokopedia.com/your/path/method/xyz
     *
     *
     * #3. Optional parameter
     * For providing extra headers to the apis, As Key-Value pair of header.
     *
     *
     * #4. Optional parameter
     * For providing query parameter to the apis, Map -> Key-Value pair of query parameter (No need to encode, library will take care of this)
     *
     *
     * #5.Optional parameter
     * For providing Http method type, by default GET will be treated if not provided any method.
     * E.g RequestType enum  (e.g RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE)
     * default is RequestType.GET if you will return null
     *
     *
     * #6. Mandatory parameter (If Method type is POST or PUT else it Optional)- Valid implementation require
     * For providing query params to the apis.
     *
     * E.g. Return argument can any one of from below
     *     1. Map Object -->Key-Value pair of query. (For content-type -> @FormUrlEncoded) (No need to encode, library will take care of this)
     *     2. String --> Any string which can be become part of body. (For content-type -> application/json)
     *     3. Java POJO class object -> which can be serialize and deserialize later. (For content-type -> application/json)
     *     4. String --> Path of the file which are going to upload. (For content-type -> @Multipart)
     * <p>
     * If you will trying to set other then above object, then it will trow exception later while executing the network request
     *
     *
     * #7. Optional parameter
     * For providing CacheStrategy, by Default no caching will be perform if not provided
     *
     * E.g. Object - RestCacheStrategy (RestCacheStrategy.Builder to create your RestCacheStrategy)
     * Default is NONE caching
     *
     *
     * @return List of RestRequest object which may or may not contain above parameter
     */
    protected abstract List<RestRequest> buildRequest(RequestParams requestParams);

    /**
     * A function to indicate whether the use case needs to throw exception when null is found in the response
     *
     * Please override this function and return `false`
     * if you want the use case to _NOT_ throw exception
     **/
    protected boolean shouldThrowException() {
        return false;
    }
}
