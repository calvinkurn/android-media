package com.tokopedia.abstraction.common.utils.paging;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by m.normansyah on 27/10/2015.
 */
public class PagingHandler implements PagingHandlerRotation {

    public static final String PAGING_ACE = "PAGING_ACE";
    private JSONObject result;
    protected boolean hasNext = false;
    private int Page = 1;
    //	private int NextPage = -1;
    private PagingHandlerModel pagingHandlerModel;

    public boolean CheckNextPage() {
        return hasNext;
    }

    public static class PagingHandlerModel implements Parcelable {

        // use this for api based
        int startIndex;

        @SerializedName("uri_next")
        @Expose
        public String uriNext;
        @SerializedName("uri_previous")
        @Expose
        String uriPrevious;

        public PagingHandlerModel() {
        }

        public String getUriNext() {
            return uriNext;
        }

        public void setUriNext(String uriNext) {
            this.uriNext = uriNext;
        }

        public String getUriPrevious() {
            return uriPrevious;
        }

        public void setUriPrevious(String uriPrevious) {
            this.uriPrevious = uriPrevious;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.startIndex);
            dest.writeString(this.uriNext);
            dest.writeString(this.uriPrevious);
        }

        protected PagingHandlerModel(Parcel in) {
            this.startIndex = in.readInt();
            this.uriNext = in.readString();
            this.uriPrevious = in.readString();
        }

        public static final Creator<PagingHandlerModel> CREATOR = new Creator<PagingHandlerModel>() {
            @Override
            public PagingHandlerModel createFromParcel(Parcel source) {
                return new PagingHandlerModel(source);
            }

            @Override
            public PagingHandlerModel[] newArray(int size) {
                return new PagingHandlerModel[size];
            }
        };
    }

    public static PagingHandlerModel createPagingHandlerModel(int startIndex, String uriNext, String uriPrevious) {
        PagingHandlerModel result = new PagingHandlerModel();
        result.setStartIndex(startIndex);
        result.setUriNext(uriNext);
        result.setUriPrevious(uriPrevious);
        return result;
    }

    public static boolean CheckHasNext(PagingHandlerModel pagingHandlerModel) {
        return CheckHasNext(pagingHandlerModel.uriNext);
    }

    public static boolean CheckHasNext(String uriNext) {
        if (uriNext != null && !uriNext.equals("0") && !uriNext.equals("")) {
            return true;
        }
        return false;
    }

    @Deprecated
    public void setNewParameter(JSONObject Result) {
        Gson gson = new GsonBuilder().create();
        JSONObject pagingString = Result.optJSONObject("paging");
        if (pagingString != null) {
            Log.d("MNORMANSYAH", " check Paging : " + pagingString.toString());
            PagingHandlerModel paging = gson.fromJson(pagingString.toString(), PagingHandlerModel.class);
            if (paging.uriNext != null && !paging.uriNext.equals("0")) {
                hasNext = true;
            } else {
                hasNext = false;
            }

        } else {
            hasNext = false;
        }
    }

    /**
     * since 11-11-2015, paging has change its structure
     * please change to @see PagingHandler#setNewParameter
     *
     * @param Result {@link JSONObject}
     * @param paths  valid path
     */
    @Deprecated
    public void setParameter(JSONObject Result, String... paths) {
        try {
            for (String string : paths) {
                Result = new JSONObject(Result.getString(string));
            }
            result = Result;
            Result = new JSONObject(Result.getString("paging"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hasNext = Result.has("uri_next");
    }

    public PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    public void setPagingHandlerModel(PagingHandlerModel pagingHandlerModel) {
        this.pagingHandlerModel = pagingHandlerModel;
    }

    public void setPage(int Page) {
        this.Page = Page;
    }

    public void resetPage() {
        Page = 1;
    }

    public void nextPage() {
        Page = Page + 1;
    }

    public void setHasNext(boolean HasNext) {
        hasNext = HasNext;
    }

    public int getPage() {
        return Page;
    }

    /**
     * since 11-11-2015, paging has change its structure
     * please change to @see PagingHandler#setNewParameter
     *
     * @param Result {@link JSONObject}
     */
    @Deprecated
    public void setParameter(JSONObject Result) {
        this.result = Result;
        JSONObject Paging = null;
        if (!result.has("paging")) {
            hasNext = false;
            return;
        }
        try {
            if (result.getString("paging").equals("null")) {
                hasNext = false;
                return;
            }
            Paging = new JSONObject(result.getString("paging"));
            if (Paging.has("uri_next")) {
                hasNext = !Paging.getString("uri_next").equals("");
            }

            if (Paging.has("has_next")) {
                hasNext = (Paging.getInt("has_next") != 0);
            }

        } catch (JSONException e) {
            hasNext = false;
            e.printStackTrace();
        }

    }

    @Override
    public void onSavedInstanceState(Bundle saved) {
        saved.putInt(PAGING_INDEX, getPage());
        saved.putBoolean(PAGING_HASNEXT, hasNext);
        saved.putParcelable(PAGING_ACE, pagingHandlerModel);
    }

    @Override
    public int onCreate(Bundle retrieved) {
        setPage(retrieved.getInt(PAGING_INDEX, defaultPagingIndex));
        setHasNext(retrieved.getBoolean(PAGING_HASNEXT));
        PagingHandlerModel p = retrieved.getParcelable(PAGING_ACE);
        setPagingHandlerModel(p);
        return retrieved.getInt(PAGING_INDEX, defaultPagingIndex);
    }

    @Override
    public int onCreateView(Bundle retrieved) {
        setPage(retrieved.getInt(PAGING_INDEX, defaultPagingIndex));
        setHasNext(retrieved.getBoolean(PAGING_HASNEXT));
        PagingHandlerModel p = (retrieved.getParcelable(PAGING_ACE));
        setPagingHandlerModel(p);
        return retrieved.getInt(PAGING_INDEX, defaultPagingIndex);
    }

    public int getNextPage() {
        return Integer.parseInt(Uri.parse(pagingHandlerModel.getUriNext()).getQueryParameter("page"));
    }
}
