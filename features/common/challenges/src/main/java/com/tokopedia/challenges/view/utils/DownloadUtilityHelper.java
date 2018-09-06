package com.tokopedia.challenges.view.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.tokopedia.challenges.R;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadUtilityHelper {
    public static long DownloadData(Uri uri, Context context, boolean isVideo) {

        long downloadReference;

        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        if (isVideo)
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "AndroidTutorialPoint.mp3");
        else
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "AndroidTutorialPoint.jpg");

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

//        Button DownloadStatus = (Button) findViewById(R.id.DownloadStatus);
//        DownloadStatus.setEnabled(true);
//        Button CancelDownload = (Button) findViewById(R.id.CancelDownload);
//        CancelDownload.setEnabled(true);

        return downloadReference;
    }
}
