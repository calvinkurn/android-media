package com.tokopedia.topads.sdk.domain.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.UseCase;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.data.datasource.CloudTopAdsDataSource;
import com.tokopedia.topads.sdk.data.datasource.TopAdsDataSource;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsUseCase extends UseCase<TopAdsParams, AdsView> {

    private static String TAG = TopAdsUseCase.class.getSimpleName();
    private TopAdsDataSource dataSource;
    private AsyncTask<TopAdsParams, Void, TopAdsModel> task;
    private DisplayMode displayMode;
    private boolean execute = false;

    public TopAdsUseCase(Context context) {
        this.dataSource = new CloudTopAdsDataSource(context);
        this.displayMode = DisplayMode.GRID;
    }

    @Override
    public void setConfig(Config config) {
        this.dataSource.setConfig(config);
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    @Override
    public void execute(TopAdsParams params, final AdsView view) {
        if (execute) {
            Log.d(TAG, "executor already executed cancel execution");
            return;
        }
        execute = true;
        task = new AsyncTask<TopAdsParams, Void, TopAdsModel>() {

            @Override
            protected TopAdsModel doInBackground(TopAdsParams... params) {
                return dataSource.getTopAds(params[0].getParam(), params[0].getAdsPosition());
            }

            @Override
            protected void onPostExecute(TopAdsModel topAdsModel) {
                if (topAdsModel.getError() == null && topAdsModel.getStatus().getErrorCode() == 0) {
                    List<Item> visitables = new ArrayList<>();
                    for (int i = 0; i < topAdsModel.getData().size(); i++) {
                        Data data = topAdsModel.getData().get(i);
                        if (data.getProduct() != null) {
                            if (displayMode == DisplayMode.GRID) {
                                visitables.add(ModelConverter.convertToProductGridViewModel(data));
                            } else if (displayMode == DisplayMode.LIST) {
                                visitables.add(ModelConverter.convertToProductListViewModel(data));
                            } else if (displayMode == DisplayMode.FEED) {
                                visitables.add(ModelConverter.convertToProductFeedViewModel(data));
                            }
                        } else if (data.getShop() != null) {
                            if (displayMode == DisplayMode.GRID) {
                                visitables.add(ModelConverter.convertToShopGridViewModel(data));
                            } else if (displayMode == DisplayMode.LIST) {
                                visitables.add(ModelConverter.convertToShopListViewModel(data));
                            } else if (displayMode == DisplayMode.FEED) {
                                visitables.add(ModelConverter.convertToShopFeedViewModel(data, displayMode));
                            } else if (displayMode == DisplayMode.FEED_EMPTY) {
                                visitables.add(ModelConverter.convertToShopFeedViewModel(data, displayMode));
                            }
                        }
                    }
                    view.displayAds(visitables, topAdsModel.getAdsPosition());
                } else if (topAdsModel.getError() != null) {
                    view.notifyAdsErrorLoaded(topAdsModel.getError().getCode(),
                            topAdsModel.getError().getTitle());
                } else {
                    view.notifyAdsErrorLoaded(topAdsModel.getStatus().getErrorCode(),
                            topAdsModel.getStatus().getMessage());
                }
                execute = false;
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                execute = false;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }


    @Override
    public void unsubscribe() {
        if (task != null) {
            task.cancel(true);
        }
    }
}
