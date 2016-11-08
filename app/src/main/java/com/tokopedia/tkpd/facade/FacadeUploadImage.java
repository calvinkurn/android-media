package com.tokopedia.tkpd.facade;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.tkpd.library.utils.RestClient;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.util.UploadImageReVamp;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.tkpd.library.kirisame.Kirisame;
import com.tkpd.library.kirisame.network.entity.NetError;
import com.tkpd.library.kirisame.network.entity.UploadImageUrlConnection;

/**
 * Created by Tkpd_Eka on 7/2/2015.
 */
public class FacadeUploadImage {

    public interface OnUploadImageListenerAWS {
        void onSuccess(JSONObject result);

        void onFailure();

        void onMessageError(ArrayList<String> MessageError);
    }

    public interface OnGenerateHostListener {
        void onGetGenerateURL(String url, String serverID);
    }

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36";

    private Context context;
    private UploadImageReVamp.Model model;
    private OnUploadImageListenerAWS listener;
    private OnGenerateHostListener generateHostListener;

    public static FacadeUploadImage createInstance(Context context, UploadImageReVamp.Model model) {
        FacadeUploadImage facade = new FacadeUploadImage();
        facade.context = context;
        facade.model = model;
        return facade;
    }

    public void actionUploadImage(OnUploadImageListenerAWS listener) {
        this.listener = listener;
        actionGenerateHost();
    }

    //==================================================== Generate Host ==========================================================

    public void setGenerateHostListener(OnGenerateHostListener listener) {
        generateHostListener = listener;
    }

    private void actionGenerateHost() {
        NetworkHandler network = new NetworkHandler(context, TkpdUrl.GENERATE_HOST);
        network.AddParam("new_add", "1");
        network.Commit(onGenerateHostListener());
    }

    private NetworkHandler.NetworkHandlerListener onGenerateHostListener() {
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
                if (!status) {
                    listener.onFailure();
                    Toast.makeText(context, context.getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void getResponse(JSONObject Result) {
                try {
                    model.uploadURL = getUploadURL(Result);
                    model.serverId = getUploadServerID(Result);
                    model.paramValue.add(getUploadServerID(Result));
                    if (generateHostListener != null) {
                        generateHostListener.onGetGenerateURL("https://" + Result.getString("upload_host"), model.serverId);
                    }
                    actionUploadImageToServer();
                } catch (JSONException e) {
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }
        };
    }

    private String getUploadURL(JSONObject Result) throws JSONException {
        return "https://" + Result.getString("upload_host")
                + model.webService;
    }

    private String getUploadServerID(JSONObject Result) throws JSONException {
        return Result.getString("server_id");
    }

    //================================================= Upload Image ==========================================================

    private void actionUploadImageToServer() {
//        UploadImageNetwork uploadAsync = new UploadImageNetwork();
//        uploadAsync.execute();
        uploadImageWithUrlConnection();
    }

    @Deprecated
    private String actionUploadToServer() throws Exception {
        RestClient rc = getRestClient();
        rc.ExecuteMultipart();
        return rc.getResponse();
    }

    private void onUploadToServerSuccess(String response) {
        JSONObject result = null;
        try {
            JSONObject resJS = new JSONObject(response);
            if (!resJS.isNull("message_error")) generateMessageError(resJS);
            result = resJS.getJSONObject("result");
            listener.onSuccess(result);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure();
        } catch (NullPointerException e) {
            e.printStackTrace();
            listener.onFailure();
        }
    }

    private RestClient getRestClient() throws UnsupportedEncodingException {
        RestClient restClient = new RestClient(model.uploadURL, 1);
        restClient.addEntityPart(
                "User-Agent", USER_AGENT);
        restClient.addEntityPart("user_id", SessionHandler.getLoginID(context));
        restClient.addEntityPart("new_add", "1");
        restClient.addEntityPart("server_id", model.serverId);
        addParams(restClient);
        restClient.addEntityByte(model.fileName, getBitmapToBAB(model.uploadImage));
        return restClient;
    }

    private void addParams(RestClient rc) throws UnsupportedEncodingException {
        int total = model.paramKey.size();
        for (int i = 0; i < total; i++) {
            rc.addEntityPart(model.paramKey.get(i), model.paramValue.get(i));
            System.out.println("Magic addParam " + model.paramKey.get(i) + " : " + model.paramValue.get(i));
        }
    }

    private ByteArrayBody getBitmapToBAB(Bitmap bitmap) {
        return new ByteArrayBody(getBitmapToByte(bitmap), "image.jpg");
    }

    private byte[] getBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
        return bao.toByteArray();
    }

    //=======================================================================================================================


    private void generateMessageError(JSONObject JsonResponse) {
        try {
            JSONArray JsonErrorList = new JSONArray(JsonResponse.getString("message_error"));
            ArrayList<String> ErrorList = new ArrayList<String>();
            for (int i = 0; i < JsonErrorList.length(); i++) {
                ErrorList.add(JsonErrorList.getString(i));
            }
            if (ErrorList.size() > 0)
                listener.onMessageError(ErrorList);
        } catch (JSONException e) {

        }
    }

    //======================================================================================================================

    private class UploadURLConnection extends UploadImageUrlConnection {

        @Override
        public void onConnectionSuccess(String s) {
            Log.d("UploadImageURLCon", "Result: " + s);
            onUploadToServerSuccess(s);
        }

        @Override
        public void onConnectionError(NetError netError, int i) {
            Log.d(FacadeUploadImage.class.getSimpleName(), netError.toString());
        }

        @Override
        public String getUrl() {
            return model.uploadURL;
        }
    }

    private void uploadImageWithUrlConnection() {
        Kirisame.print("Using URL CONNECTION");
        UploadURLConnection con = new UploadURLConnection();
        con.addPart(
                "User-Agent", USER_AGENT);
        con.addPart("user_id", SessionHandler.getLoginID(context));
        con.addPart("new_add", "1");
        con.addPart("server_id", model.serverId);
        int total = model.paramKey.size();
        for (int i = 0; i < total; i++) {
            con.addPart(model.paramKey.get(i), model.paramValue.get(i));
        }
        con.addPart(model.fileName, "image.jpg", getBitmapToByte(model.uploadImage));
        con.openConnection();
    }
}
