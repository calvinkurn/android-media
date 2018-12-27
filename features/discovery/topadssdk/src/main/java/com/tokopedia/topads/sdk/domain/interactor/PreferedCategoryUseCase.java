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
import com.tokopedia.topads.sdk.domain.model.PreferedCategory;
import com.tokopedia.topads.sdk.listener.PreferedCategoryListener;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.topads.sdk.view.AdsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class PreferedCategoryUseCase extends UseCase<TopAdsParams, AdsView> {

    private static String TAG = PreferedCategoryUseCase.class.getSimpleName();
    private TopAdsDataSource dataSource;
    private AsyncTask<TopAdsParams, Void, PreferedCategory> task;
    private boolean execute = false;
    private final PreferedCategoryListener listener;
    private final CacheHandler cacheHandler;
    private Random random;



    public PreferedCategoryUseCase(
            Context context, PreferedCategoryListener listener) {
        this.dataSource = new CloudTopAdsDataSource(context);
        this.listener = listener;
        this.cacheHandler = new CacheHandler(context, CacheHandler.TOP_ADS_CACHE);
        this.random = new Random();
    }

    @Override
    public void setConfig(Config config) {
        this.dataSource.setConfig(config);
    }

    @Override
    public void execute(TopAdsParams params, final AdsView view) {
        if (cacheHandler.isExpired()) {
            if (execute) {
                Log.d(TAG, "executor already executed cancel execution");
                return;
            }
            execute = true;
            task = new AsyncTask<TopAdsParams, Void, PreferedCategory>() {

                @Override
                protected PreferedCategory doInBackground(TopAdsParams... params) {
                    return dataSource.getPreferenceCategory();
                }

                @Override
                protected void onPostExecute(PreferedCategory preferedCategory) {
                    if (preferedCategory.getErrorMessage() == null && preferedCategory.getUserType() != 1) {
                        listener.onSuccessLoadPrefered(getRandomId(preferedCategory.getUserCategoriesId()));
                        saveToCache(preferedCategory);
                    } else {
                        listener.onErrorLoadPrefered();
                        view.notifyAdsErrorLoaded(Config.ERROR_CODE_INVALID_RESPONSE,
                                preferedCategory.getErrorMessage());
                    }
                    execute = false;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                }
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            } else {
                task.execute(params);
            }
        } else {
            ArrayList<Integer> preferredCacheList
                    = cacheHandler.getArrayListInteger(CacheHandler.KEY_PREFERRED_CATEGORY);
            listener.onSuccessLoadPrefered(getRandomId(preferredCacheList));
        }
    }

    private void saveToCache(PreferedCategory preferedCategory) {
        ArrayList<Integer> userCategoriesId
                = (ArrayList<Integer>) preferedCategory.getUserCategoriesId();

        cacheHandler.putArrayListInteger(
                CacheHandler.KEY_PREFERRED_CATEGORY, userCategoriesId);
        int fifteenMinuteInsecond = 900;
        cacheHandler.setExpire(fifteenMinuteInsecond);
    }


    @Override
    public void unsubscribe() {
        if (task != null) {
            task.cancel(true);
        }
    }

    private int getRandomId(List<Integer> ids) {
        if (ids.size() > 0) {
            return ids.get(random.nextInt(ids.size()));
        } else {
            return 0;
        }
    }
}
