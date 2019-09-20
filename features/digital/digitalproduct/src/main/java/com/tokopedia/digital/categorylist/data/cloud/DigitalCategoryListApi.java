package com.tokopedia.digital.categorylist.data.cloud;

import com.tokopedia.authentication.AuthConstant;
import com.tokopedia.digital.common.constant.DigitalUrl;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

public interface DigitalCategoryListApi {
    @GET(DigitalUrl.API_HOME_CATEGORY_MENU)
    Observable<Response<String>> getDigitalCategoryList(@Header("X-User-ID") String user_id,
                                                        @Header(AuthConstant.HEADER_X_TKPD_APP_NAME) String appName,
                                                        @Header(AuthConstant.HEADER_DEVICE) String deviceVersion);
}
