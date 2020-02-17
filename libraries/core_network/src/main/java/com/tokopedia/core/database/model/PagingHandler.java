package com.tokopedia.core.database.model;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 27/11/2015
 * move code to RxJava and Retrofit 2 style.
 */
@Deprecated
public class PagingHandler implements PaginHandlerRotation {
    public static final String PAGING_ACE = "PAGING_ACE";
    private JSONObject result;
    protected boolean hasNext = false;
    private int Page = 1;
    //	private int NextPage = -1;
    private PagingHandlerModel pagingHandlerModel;

    public boolean CheckNextPage() {
        return hasNext;
    }

	@Parcel
	public static class PagingHandlerModel implements Parcelable {

        // use this for api based
        int startIndex;

        @SerializedName("uri_next")
        @Expose
        public String uriNext;
        @SerializedName("uri_previous")
        @Expose
		String uriPrevious;
        @SerializedName("current")
        @Expose
        public String uriCurrent;

		public PagingHandlerModel() {
		}

        protected PagingHandlerModel(android.os.Parcel in) {
			startIndex = in.readInt();
			uriNext = in.readString();
			uriPrevious = in.readString();
			uriCurrent = in.readString();
		}

		public static final Creator<PagingHandlerModel> CREATOR = new Creator<PagingHandlerModel>() {
			@Override
			public PagingHandlerModel createFromParcel(android.os.Parcel in) {
				return new PagingHandlerModel(in);
			}

			@Override
			public PagingHandlerModel[] newArray(int size) {
				return new PagingHandlerModel[size];
			}
		};

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

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(android.os.Parcel dest, int flags) {
			dest.writeInt(startIndex);
			dest.writeString(uriNext);
			dest.writeString(uriPrevious);
			dest.writeString(uriCurrent);
		}
	}

    public static boolean CheckHasNext(PagingHandlerModel pagingHandlerModel) {
        return CheckHasNext(pagingHandlerModel.uriNext);
    }

	public static boolean CheckHasNext(String uriNext){
		if(uriNext!=null&&!uriNext.equals("0")&& !uriNext.equals("")){
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
	 * @param Result {@link JSONObject}
	 * @param paths valid path
	 */
	@Deprecated
	public void setParameter(JSONObject Result, String... paths){
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
        // this cause bug
//		hasNext = false;
        // this cause bug
    }

    public void setHasNext(boolean HasNext) {
//		this.NextPage = NextPage;
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
        saved.putParcelable(PAGING_ACE, Parcels.wrap(pagingHandlerModel));
    }

    @Override
    public int onCreate(Bundle retrieved) {
        setPage(retrieved.getInt(PAGING_INDEX, defaultPagingIndex));
        setHasNext(retrieved.getBoolean(PAGING_HASNEXT));
        PagingHandlerModel p = Parcels.unwrap(retrieved.getParcelable(PAGING_ACE));
        setPagingHandlerModel(p);
        return retrieved.getInt(PAGING_INDEX, defaultPagingIndex);
    }

	@Override
	public int onCreateView(Bundle retrieved) {
		setPage(retrieved.getInt(PAGING_INDEX, defaultPagingIndex));
		setHasNext(retrieved.getBoolean(PAGING_HASNEXT));
		PagingHandlerModel p = Parcels.unwrap(retrieved.getParcelable(PAGING_ACE));
		setPagingHandlerModel(p);
		return retrieved.getInt(PAGING_INDEX, defaultPagingIndex);
	}

	public int getNextPage() {
		return Integer.parseInt(Uri.parse(pagingHandlerModel.getUriNext()).getQueryParameter("page"));
	}
}
