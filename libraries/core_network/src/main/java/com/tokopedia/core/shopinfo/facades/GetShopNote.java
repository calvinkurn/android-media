package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.apiservices.shop.NotesService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.shopinfo.models.NoteModel;
import com.tokopedia.core.shopinfo.models.shopnotes.GetShopNotes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tkpd_Eka on 10/16/2015.
 */
public class GetShopNote {

    public static final String STUART = "STUART";

    public interface OnGetNoteListListener {
        void onCompleteDataSuccess(GetShopNotes.Data data);

        void onSuccess(List<NoteModel> notesList);

        void onFailure();
    }

    public interface OnGetNoteDetailListener {
        void onSuccess(NoteModel model);

        void onFailure();
    }

    private Context context;
    OnGetNoteListListener onGetNoteListListener;
    OnGetNoteDetailListener onGetNoteDetailListener;
    NotesService service = new NotesService();
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    public void setCompositeSubscription(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    public GetShopNote(Context context) {
        this.context = context;
    }

    public void setOnGetNoteListListener(OnGetNoteListListener listener) {
        onGetNoteListListener = listener;
    }

    public void setOnGetNoteDetailListener(OnGetNoteDetailListener listener) {
        onGetNoteDetailListener = listener;
    }

    //===================================== GET NOTE LIST =====================================================

    public void getNoteListV4(String shopId, String shopDomain) {
        HashMap<String, String> params = new HashMap<>();
        params.put("shop_id", shopId);
        params.put("shop_domain", shopDomain);
        compositeSubscription.add(new ShopService().getApi()
                .getNotes(AuthUtil.generateParams(context, params))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                onGetNoteListListener.onFailure();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    List<String> errorMessages = response.getErrorMessages();
                                    if (CommonUtils.checkNullMessageError(errorMessages).size() > 0) {
                                        onGetNoteListListener.onFailure();
                                        return;
                                    }

                                    Gson gson = new GsonBuilder().create();
                                    GetShopNotes.Data data =
                                            gson.fromJson(jsonObject.toString(), GetShopNotes.Data.class);
                                    processGetShopNotes(data);

                                    onGetNoteListListener.onCompleteDataSuccess(data);
                                } catch (JSONException je) {
                                    Log.e(STUART, GetShopNote.class.getSimpleName() + je.getLocalizedMessage());
                                }
                            }
                        }
                ));
    }

    private void processGetShopNotes(GetShopNotes.Data data) {
        List<NoteModel> models = new ArrayList<>();
        for (GetShopNotes.List temp : data.getList()) {
            NoteModel model = new NoteModel();
            model.title = temp.getNoteTitle();
            model.status = temp.getNoteStatus() + "";
            model.id = temp.getNoteId();
            models.add(model);
        }
        onGetNoteListListener.onSuccess(models);
    }

    //================== GET NOTE DETAIL=========================================================
    public void getNoteDetail2(String shopId, String shopDomain, String noteId) {

        Observable<Response<TkpdResponse>> observable = service.getApi().getNotesDetail(MapNulRemover.removeNull(paramShopInfo(shopId, shopDomain, noteId)));
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetNoteDetailSubscriber()));
    }

    private Subscriber<Response<TkpdResponse>> onGetNoteDetailSubscriber() {
        return new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                onGetNoteDetailListener.onFailure();
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                parseNoteDetail(tkpdResponseResponse.body().getJsonData());
            }
        };
    }

    private Map<String, String> paramShopInfo(String shopId, String shopDomain, String noteId) {
        Map<String, String> params = new ArrayMap<>();
        params.put("shop_id", shopId);
        params.put("shop_domain", shopDomain);
        params.put("note_id", noteId);
        return params;
    }

    private void parseNoteDetail(JSONObject result) {
        try {
            onGetNoteDetailListener.onSuccess(getNoteDetailModel(result.getJSONObject("detail")));
        } catch (JSONException e) {
            e.printStackTrace();
            onGetNoteDetailListener.onFailure();
        }
    }

    private NoteModel getNoteDetailModel(JSONObject note) throws JSONException {
        NoteModel model = new NoteModel();
        model.title = note.getString("notes_title");
        model.status = note.getString("notes_status");
        String notesUpdateTime = note.getString("notes_update_time");
        if(TextUtils.isEmpty(notesUpdateTime)) {
            model.update = note.getString("notes_create_time");
        }else{
            model.update = notesUpdateTime;
        }
        model.id = note.getString("notes_id");
        model.content = note.getString("notes_content");
        return model;
    }
}
