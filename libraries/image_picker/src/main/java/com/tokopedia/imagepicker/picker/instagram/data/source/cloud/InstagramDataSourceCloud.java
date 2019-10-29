package com.tokopedia.imagepicker.picker.instagram.data.source.cloud;

import androidx.collection.ArrayMap;

import com.google.gson.Gson;
import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseGetAccessToken;
import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseListMediaInstagram;
import com.tokopedia.imagepicker.picker.instagram.data.source.InstagramDataSource;
import com.tokopedia.imagepicker.picker.instagram.data.source.exception.ShouldLoginInstagramException;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramDataSourceCloud implements InstagramDataSource {

    private InstagramApi instagramApi;

    public InstagramDataSourceCloud(InstagramApi instagramApi) {
        this.instagramApi = instagramApi;
    }

    public Observable<ResponseListMediaInstagram> getListMedia(String cookie,String accessToken, String nextMaxId, String count) {
        return instagramApi.getSelfMedia(cookie, accessToken, nextMaxId, count)
                .map(new Func1<Response<ResponseListMediaInstagram>, ResponseListMediaInstagram>() {
                    @Override
                    public ResponseListMediaInstagram call(Response<ResponseListMediaInstagram> responseListMediaInstagramResponse) {
                        if(responseListMediaInstagramResponse.isSuccessful()){
                            return responseListMediaInstagramResponse.body();
                        }else if(responseListMediaInstagramResponse.code() == 400 && responseListMediaInstagramResponse.errorBody() != null){
                            Gson gson = new Gson();
                            ResponseListMediaInstagram responseListMediaInstagram;
                            try {
                                 responseListMediaInstagram = gson.fromJson(responseListMediaInstagramResponse.errorBody().string(), ResponseListMediaInstagram.class);
                            } catch (IOException e) {
                                return null;
                            }
                            String errorType = responseListMediaInstagram.getMeta().getErrorType();
                            if("OAuthAccessTokenException".equalsIgnoreCase(errorType) || "OAuthParameterException".equalsIgnoreCase(errorType)){
                                throw new ShouldLoginInstagramException();
                            }else{
                                return null;
                            }
                        }else{
                            return null;
                        }
                    }
                });
    }

    @Override
    public Observable<String> getAccessToken(String code) {
        return instagramApi.getAccessToken(getAccessTokenParams(code))
                .map(new Func1<Response<ResponseGetAccessToken>, String>() {
                    @Override
                    public String call(Response<ResponseGetAccessToken> responseGetAccessTokenResponse) {
                        if (responseGetAccessTokenResponse.isSuccessful() && responseGetAccessTokenResponse.body() != null) {
                            return responseGetAccessTokenResponse.body().getAccessToken();
                        } else {
                            return "";
                        }
                    }
                });
    }

    private Map<String, String> getAccessTokenParams(String code) {
        Map<String, String> params = new ArrayMap<>();
        params.put(InstagramConstant.CLIENT_ID_KEY, InstagramConstant.CLIENT_ID);
        params.put(InstagramConstant.CLIENT_SECRET_KEY, InstagramConstant.CLIENT_SECRET);
        params.put(InstagramConstant.REDIRECT_URI_KEY, InstagramConstant.CALLBACK_URL);
        params.put(InstagramConstant.GRANT_TYPE_KEY, InstagramConstant.AUTHORIZATION_CODE);
        params.put(InstagramConstant.CODE_KEY, code);
        return params;
    }
}
