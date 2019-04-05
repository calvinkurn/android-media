package com.tokopedia.digital.categorylist.data.cloud;

import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.network.utils.AuthUtil;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

public interface DigitalCategoryListApi {
    @GET(DigitalUrl.API_HOME_CATEGORY_MENU)
    Observable<Response<String>> getDigitalCategoryList(@Header("X-User-ID") String user_id,
                                                        @Header(AuthUtil.HEADER_X_TKPD_APP_NAME) String appName,
                                                        @Header(AuthUtil.HEADER_DEVICE) String deviceVersion);
}
