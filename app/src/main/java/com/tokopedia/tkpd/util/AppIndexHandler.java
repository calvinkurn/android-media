package com.tokopedia.tkpd.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by ricoharisin on 8/19/15.
 */
public class AppIndexHandler {

    private GoogleApiClient GoogleClient;
    private Action ViewAction;

    public AppIndexHandler(Context context) {
        GoogleClient = new GoogleApiClient.Builder(context).addApi(AppIndex.API).build();
    }

    public void setAction(String actionType, String objectName, Uri objectAppUrl, Uri objectAppUri) {
        ViewAction = Action.newAction(actionType, objectName, objectAppUrl, objectAppUri);
    }

    public void startIndexing() {
        try {
            GoogleClient.connect();
            AppIndex.AppIndexApi.start(GoogleClient, ViewAction);
            Log.i("App Index Handler", "Start Indexing....." + ViewAction.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopIndexing() {
        try {
            AppIndex.AppIndexApi.end(GoogleClient, ViewAction);
            GoogleClient.disconnect();
            Log.i("App Index Handler", "Stop Indexing....." + ViewAction.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
