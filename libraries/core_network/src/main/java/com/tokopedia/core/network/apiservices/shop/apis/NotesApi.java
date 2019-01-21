package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.myproduct.model.NoteDetailModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface NotesApi {

    @GET(TkpdBaseURL.Shop.PATH_GET_NOTES_DETAIL)
    Observable<Response<TkpdResponse>> getNotesDetail(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_NOTES_DETAIL)
    Observable<NoteDetailModel> getNotesDetail2(@FieldMap Map<String, String> params);

}
