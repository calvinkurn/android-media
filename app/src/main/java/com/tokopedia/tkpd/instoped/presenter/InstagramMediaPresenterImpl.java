package com.tokopedia.tkpd.instoped.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;

import com.google.gson.GsonBuilder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.instoped.InstopedService;
import com.tokopedia.tkpd.instoped.model.InstagramMediaModel;
import com.tokopedia.tkpd.instoped.model.InstagramUserModel;
import com.tokopedia.tkpd.instoped.model.rawMediaModel.Caption;
import com.tokopedia.tkpd.instoped.model.rawMediaModel.Datum;
import com.tokopedia.tkpd.instoped.model.rawMediaModel.Images;
import com.tokopedia.tkpd.instoped.model.rawMediaModel.RawMediaModel;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Toped18 on 5/18/2016.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class InstagramMediaPresenterImpl implements InstagramMedia {

    public static final int FAILED_LOAD_IMAGE = -1;
    public static final int MAXIMUM_IMAGE_SELECTED = -2;
    private int maxItemCount = -1;
    private int itemCount = 0;
    private SparseArray<InstagramMediaModel> selectedModel = new SparseArray<>();
    private InstagramMediaFragmentView instagramMediaView;
    private InstopedService service = new InstopedService();
    private List<RecyclerViewItem> mediaModels;
    private static InstagramUserModel model;
    private String nextMaxId = "";
    private Context context;
    private boolean hasLoadMore;
    private boolean isLoading;

    public InstagramMediaPresenterImpl(InstagramMediaFragmentView instagramMediaView) {
        this.instagramMediaView = instagramMediaView;
    }

    @Override
    public void initIntagramMediaInstances(Context context) {
        this.context = context;
        if (!isAfterRotate()) {
            mediaModels = new ArrayList<RecyclerViewItem>();
        }
        instagramMediaView.initHolder();

    }

    @Override
    public void initData() {
        requestMedia();
    }

    @Override
    public void loadMore() {
        if (!isLoading) {
            isLoading = true;
            requestMedia();
        }
    }

    @Override
    public void fetchHotListData() {

    }

    @Override
    public List<RecyclerViewItem> parseJSON(JSONObject Result) {
        return null;
    }

    @Override
    public void resetToPageOne() {

    }

    @Override
    public void onSaveDataBeforeRotate(Bundle outState) {

    }

    @Override
    public void onFetchDataAfterRotate(Bundle outState) {

    }

    @Override
    public boolean isAfterRotate() {
        return false;
    }

    @Override
    public void moveToOtherActivity(RecyclerViewItem data) {

    }

    @Override
    public void setRetryListener() {

    }

    @Override
    public void setData(List<RecyclerViewItem> items, boolean hasNext, int nextPage) {

    }

    @Override
    public void ariseRetry() {

    }

    @Override
    public void onMessageError(String text) {

    }

    @Override
    public void onNetworkError(String text) {

    }

    @Override
    public void updateViewData(int type, Object... data) {

    }

    @Override
    public void initDataAfterRotate() {

    }

    @Override
    public int getDataSize() {
        return selectedModel.size();
    }

    @Override
    public int getItemDataType(int position) {
        return 0;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    private void requestMedia() {
        instagramMediaView.loadingShow(true);
        Observable<Response<String>> observable = service.getApi().getSelfMedia(model.accessToken, nextMaxId, ITEM_COUNT_GETTER);
        observable.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetUserMediaSubscriber());
    }

    private Subscriber<Response<String>> onGetUserMediaSubscriber() {
        return new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                instagramMediaView.loadingShow(false);
                String messageError = "Gagal mengambil gambar dari Instagram";
                onInvalidAccessToken(messageError);
            }

            @Override
            public void onNext(Response<String> stringResponse) {
                instagramMediaView.loadingShow(false);
                if (stringResponse.code() == 200)
                    onRequestMediaSuccess(stringResponse.body());
                else {
                    String messageError = "Gagal mengambil gambar dari Instagram";
                    onInvalidAccessToken(messageError);
                }
            }
        };
    }

    private void onRequestMediaSuccess(String response) {
        RawMediaModel data = new GsonBuilder()
                .create()
                .fromJson(response,
                        RawMediaModel.class);
        nextMaxId = data.getPagination().getNextMaxId();
        if (getMediaModelsFromResult(data).isEmpty()) {
            instagramMediaView.showEmptyData();
        } else {
            isLoading = false;
            mediaModels.addAll(getMediaModelsFromResult(data));
            instagramMediaView.initAdapter(mediaModels);
            if (maxItemCount == -1) {
                //[START] depends on how much it fetch the data
//            updateMaxCount(data.getData().size());
                //[END] depends on how much it fetch the data

                updateMaxCount(20);
            }
            if (nextMaxId == null) {
                hasLoadMore = false;
            } else {
                hasLoadMore = true;
            }
            instagramMediaView.loadDataChange();
        }
    }


    @Override
    public void updateMaxCount(int instaMediacount) {
        maxItemCount = instaMediacount;
        instagramMediaView.updateTitleView(itemCount, maxItemCount);
    }

    @Override
    public void updateItemSelection(boolean increment, int pos) {
        if (increment) {
            if (itemCount == maxItemCount) {
                instagramMediaView.onMessageError(MAXIMUM_IMAGE_SELECTED, context.getString(R.string.maximum_instoped_limit));
            } else {
                itemCount++;
                selectedModel.put(pos, (InstagramMediaModel) mediaModels.get(pos));
            }
        } else {
            itemCount--;
            selectedModel.remove(pos);
        }
        instagramMediaView.updateTitleView(itemCount, maxItemCount);
        instagramMediaView.loadDataChange();


    }

    private List<InstagramMediaModel> getMediaModelsFromResult(RawMediaModel response) {
        List<InstagramMediaModel> models = new ArrayList<>();
        List<Datum> dataList = response.getData();
        Datum data;
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            InstagramMediaModel model = new InstagramMediaModel();
            data = dataList.get(i);
            Caption caption = data.getCaption();
            Images images = data.getImages();
            model.link = data.getLink();
            model.captionText = caption.getText();
            model.filter = data.getFilter();
            model.standardResolution = images.getStandardResolution().getUrl();
            model.thumbnail = images.getThumbnail().getUrl();
            models.add(model);
        }
        return models;
    }

    @Override
    public SparseArray<InstagramMediaModel> getSelectedModel() {
        return selectedModel;
    }

    @Override
    public void clearSelectedModel() {
        selectedModel.clear();
    }

    @Override
    public void setModel(InstagramUserModel model) {
        this.model = model;
    }

    @Override
    public boolean isSelected(int position) {
        return selectedModel.get(position) != null;
    }

    @Override
    public boolean getHasLoadMore() {
        return hasLoadMore;
    }

    public static void removeToken() {
        if (model != null && model.accessToken != null) {
            model.accessToken = null;
        }
    }


    private void onInvalidAccessToken(String error) {
        instagramMediaView.onMessageError(FAILED_LOAD_IMAGE, error);
//        onRequestMediaListener.onRequestAccessToken(getFragmentManager());
    }

}
