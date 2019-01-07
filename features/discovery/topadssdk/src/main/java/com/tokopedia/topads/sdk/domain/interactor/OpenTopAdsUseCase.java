package com.tokopedia.topads.sdk.domain.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.UseCase;
import com.tokopedia.topads.sdk.data.datasource.CloudTopAdsDataSource;
import com.tokopedia.topads.sdk.data.datasource.TopAdsDataSource;
import com.tokopedia.topads.sdk.view.AdsView;

/**
 * @author by errysuprayogi on 3/31/17.
 */

public class OpenTopAdsUseCase extends UseCase<String, AdsView> {

    private TopAdsDataSource dataSource;
    private AsyncTask<String, Void, String> task;

    public OpenTopAdsUseCase(Context context) {
        this.dataSource = new CloudTopAdsDataSource(context);
    }

    @Override
    public void setConfig(Config config) {

    }

    public void execute(String requestParams) {
        execute(requestParams, null);
    }

    @Override
    public void execute(String requestParams, AdsView view) {
        task = new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... params) {
                return dataSource.clickTopAdsUrl(params[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestParams);
        } else {
            task.execute(requestParams);
        }
    }

    @Override
    public void unsubscribe() {
        if(task!=null) {
            task.cancel(true);
        }
    }
}
