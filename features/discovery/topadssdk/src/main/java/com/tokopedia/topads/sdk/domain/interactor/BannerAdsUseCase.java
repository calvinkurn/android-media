package com.tokopedia.topads.sdk.domain.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.UseCase;
import com.tokopedia.topads.sdk.data.datasource.CloudTopAdsDataSource;
import com.tokopedia.topads.sdk.data.datasource.TopAdsDataSource;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.view.BannerAdsContract;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class BannerAdsUseCase extends UseCase<TopAdsParams, BannerAdsContract.View> {

    private static String TAG = BannerAdsUseCase.class.getSimpleName();
    private AsyncTask<TopAdsParams, Void, CpmModel> task;
    private TopAdsDataSource dataSource;
    private boolean execute = false;

    public BannerAdsUseCase(Context context) {
        this.dataSource = new CloudTopAdsDataSource(context);
    }

    @Override
    public void setConfig(Config config) {
        this.dataSource.setConfig(config);
    }

    @Override
    public void execute(TopAdsParams params, final BannerAdsContract.View view) {
        if (execute) {
            Log.d(TAG, "executor already executed cancel execution");
            return;
        }
        execute = true;
        task = new AsyncTask<TopAdsParams, Void, CpmModel>() {

            @Override
            protected void onPreExecute() {
                if (view == null) return;

                view.showLoading();
            }

            @Override
            protected CpmModel doInBackground(TopAdsParams... topAdsParams) {
                return dataSource.getTopAdsBanner(topAdsParams[0].getParam());
            }

            @Override
            protected void onPostExecute(CpmModel cpmModel) {
                if (view == null) return;

                view.hideLoading();
                if (cpmModel.getStatus().getErrorCode() == 0) {
                    view.displayAds(cpmModel);
                } else if (cpmModel.getError() != null) {
                    view.notifyAdsErrorLoaded(cpmModel.getError().getCode(),
                            cpmModel.getError().getTitle());
                } else {
                    view.notifyAdsErrorLoaded(cpmModel.getStatus().getErrorCode(),
                            cpmModel.getStatus().getMessage());
                }
                execute = false;
            }

            @Override
            protected void onCancelled() {
                if (view == null) return;
                view.onCanceled();
                view.hideLoading();
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
            task = null;
        }
    }
}
