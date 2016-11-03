package com.tokopedia.tkpd.facade;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.fragment.ReturnPolicyDialog;
import com.tokopedia.tkpd.myproduct.model.ReturnPolicyAddModel;
import com.tokopedia.tkpd.network.apiservices.shop.MyShopNoteActService;
import com.tokopedia.tkpd.network.apiservices.shop.ShopService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.shopinfo.facades.GetShopNote;
import com.tokopedia.tkpd.shopinfo.models.NoteModel;
import com.tokopedia.tkpd.shopinfo.models.shopnotes.GetShopNotes;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.tkpd.myproduct.fragment.ReturnPolicyDialog.NOTE_CONTENT;
import static com.tokopedia.tkpd.myproduct.fragment.ReturnPolicyDialog.NOTE_TITLE1;
import static com.tokopedia.tkpd.myproduct.fragment.ReturnPolicyDialog.RETURN_POLICY_TYPE;
import static com.tokopedia.tkpd.myproduct.fragment.ReturnPolicyDialog.TERMS;

/**
 * Created by Kris on 4/10/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class FacadeSendFormDialog {
    private Context context;
    private String shopId;
    private String URL;
    private TkpdProgressDialog progressDialog;
    private String sendCondition;
    private String policyID;

    public interface AddNewPolicyListener {
        void OnSuccess(String NotesID);

        void OnFailure();
    }

    public interface GetPolicyListener extends GetShopNote.OnGetNoteListListener {
        void OnHasContent(JSONObject Result);

        void OnNoContent(JSONObject Result);

        void OnFinishProcess();
    }

    public static FacadeSendFormDialog getFacadeInstance(Context context, String shopId) {
        FacadeSendFormDialog facade = new FacadeSendFormDialog();
        facade.context = context;
        facade.shopId = shopId;
        facade.URL = TkpdUrl.GET_SHOP_NOTES;
        return facade;
    }

    public static FacadeSendFormDialog createFacadeInstance(Context context, String shopId, String sendCondition) {
        FacadeSendFormDialog facade = new FacadeSendFormDialog();
        facade.context = context;
        facade.shopId = shopId;
        facade.URL = TkpdUrl.GET_SHOP_NOTES;
        facade.progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        facade.sendCondition = sendCondition;
        return facade;
    }

    public static FacadeSendFormDialog createDialogFacadeInstance(Context context, String shopId, String sendCondition) {
        FacadeSendFormDialog facade = new FacadeSendFormDialog();
        facade.context = context;
        facade.shopId = shopId;
        facade.URL = TkpdUrl.GET_SHOP_NOTES;
        facade.progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        facade.sendCondition = sendCondition;
        return facade;
    }

    private static final String TAG = "FACADE_SEND_FORM", messageTAG = "result here";

    public void sendForm(final String content, final AddNewPolicyListener listener) {
        progressDialog.showDialog();

        Map<String, String> param = new HashMap<>();
        param.put(NOTE_CONTENT, content);
        param.put(NOTE_TITLE1, context.getString(R.string.title_returnable_policy));
        param.put(TERMS, RETURN_POLICY_TYPE + "");
        new MyShopNoteActService().getApi().addNote(AuthUtil.generateParams(context, param))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(final Throwable e) {
                                ErrorMessage(new ArrayList<String>() {{
                                    add(e.getMessage());
                                }});
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.getStringData());
                                    } catch (JSONException je) {
                                        Log.e(TAG, messageTAG + je.getLocalizedMessage());
                                    }
                                    if (!response.isError()) {
                                        showResultToast();
                                        ReturnPolicyAddModel.Data data = new GsonBuilder().create().fromJson(jsonObject.toString(), ReturnPolicyAddModel.Data.class);
                                        listener.OnSuccess(data.getNoteId());
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.update_returnable_failed), Toast.LENGTH_SHORT).show();
                                        listener.OnFailure();
                                    }
                                } else {
                                    new ErrorHandler(new ErrorListener() {
                                        @Override
                                        public void onUnknown() {
                                            Toast.makeText(context, "Network Unknown Error!", Toast.LENGTH_LONG).show();
                                            listener.OnFailure();
                                        }

                                        @Override
                                        public void onTimeout() {
                                            Toast.makeText(context, "Network Timeout Error!", Toast.LENGTH_LONG).show();
                                            listener.OnFailure();
                                        }

                                        @Override
                                        public void onServerError() {
                                            Toast.makeText(context, "Network Internal Server Error!", Toast.LENGTH_LONG).show();
                                            listener.OnFailure();
                                        }

                                        @Override
                                        public void onBadRequest() {
                                            Toast.makeText(context, "Network Bad Request Error!", Toast.LENGTH_LONG).show();
                                            listener.OnFailure();
                                        }

                                        @Override
                                        public void onForbidden() {
                                            Toast.makeText(context, "Network Forbidden Error!", Toast.LENGTH_LONG).show();
                                            listener.OnFailure();
                                        }
                                    }, responseData.code());
                                }
                            }
                        }
                );
    }

    public void GetPolicyForm(final GetPolicyListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("shop_id", shopId);
        new ShopService().getApi()
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
                            public void onError(final Throwable e) {
                                ErrorMessage(new ArrayList<String>() {{
                                    add(e.getMessage());
                                }});
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    List<String> errorMessages = response.getErrorMessages();
                                    if (CommonUtils.checkNullMessageError(errorMessages).size() > 0) {
                                        ErrorMessage(new ArrayList<String>(errorMessages));
                                        return;
                                    }

                                    Gson gson = new GsonBuilder().create();
                                    GetShopNotes.Data data =
                                            gson.fromJson(jsonObject.toString(), GetShopNotes.Data.class);
                                    List<NoteModel> models = new ArrayList<>();
                                    for (GetShopNotes.List temp : data.getList()) {
                                        NoteModel model = new NoteModel();
                                        model.title = temp.getNoteTitle();
                                        model.status = temp.getNoteStatus() + "";
                                        model.id = temp.getNoteId();
                                        models.add(model);
                                    }

                                    listener.onSuccess(models);

                                    listener.onCompleteDataSuccess(data);
                                } catch (JSONException je) {
                                    Log.e("STUART", GetShopNote.class.getSimpleName() + je.getLocalizedMessage());
                                }
                            }
                        }
                );
    }

    private void ErrorMessage(ArrayList<String> MessageError) {
        for (int i = 0; i < MessageError.size(); i++) {
            Toast.makeText(context, MessageError.get(i), Toast.LENGTH_LONG).show();
            if ((i + 1) < MessageError.size())
                Toast.makeText(context, "No Error", Toast.LENGTH_LONG).show();
        }
    }

    private void showResultToast() {
        if (sendCondition.equals(ReturnPolicyDialog.ADD_SHOP_NOTE_ACTION)) {
            Toast.makeText(context, context.getString(R.string.make_returnable_success), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, context.getString(R.string.update_returnable_success), Toast.LENGTH_SHORT).show();
    }
}
