package com.tokopedia.challenges.view.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
import com.tokopedia.challenges.view.fragments.submit.ChallengesSubmitPresenter;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.challenges.view.utils.Utils;

import javax.inject.Inject;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class UploadChallengeService extends Service implements IUploadChallengeServiceContract.UploadChallengeListener {
    public static final String TAG = "UploadChallengeService";
    public static final String UPLOAD_FINGERPRINT_KEY = "UploadChallengeService";
    public static final String UPLOAD_CHALLENGE_ID = "UploadChallengeID";
    public static final String UPLOAD_FILE_PATH = "UploadFilePath";
    public static final String UPLOAD_POST_ID = "Uploadpostid";
    private String GENERAL = "ANDROID_GENERAL_CHANNEL";


    @Inject
    UploadChallengePresenterImpl presenter;

    UploadFingerprints uploadFingerprints;
    private String challengeID;
    private String uploadFilePath;
    private NotificationManager notificationManager;
    private String postId;

    public static Intent getIntent(Context context, UploadFingerprints uploadFingerprints, String challengeId, String uploadFilePath, String postId) {
        Intent intent = new Intent(context, UploadChallengeService.class);
        intent.putExtra(UPLOAD_FINGERPRINT_KEY, uploadFingerprints);
        intent.putExtra(UPLOAD_CHALLENGE_ID, challengeId);
        intent.putExtra(UPLOAD_FILE_PATH, uploadFilePath);
        intent.putExtra(UPLOAD_POST_ID, postId);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        ChallengesComponent component = DaggerChallengesComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
        presenter.attach(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uploadFingerprints = (UploadFingerprints) intent.getSerializableExtra(UPLOAD_FINGERPRINT_KEY);
        challengeID = intent.getStringExtra(UPLOAD_CHALLENGE_ID);
        uploadFilePath = intent.getStringExtra(UPLOAD_FILE_PATH);
        postId = intent.getStringExtra(UPLOAD_POST_ID);
        presenter.uploadChallange();
        createNotification();
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    Integer notificationID = 100;
    NotificationCompat.Builder builder;

    public void createNotification() {
        builder = buildBaseNotification();
        Notification notification = builder
                .setContentText("Sedang mengunggah")
                .setAutoCancel(false)
                .setOngoing(true)
                .setProgress(uploadFingerprints.getTotalParts(), 0, true)
                .build();
        notificationManager.notify(TAG, notificationID, notification);
    }


    private NotificationCompat.Builder buildBaseNotification() {

        int largeIconRes = R.drawable.ic_big_notif_customerapp;
        return new NotificationCompat.Builder(this, GENERAL)
                .setContentTitle("Tokopedia Challenges")
                .setSmallIcon(R.drawable.ic_status_bar_notif_customerapp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), largeIconRes))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
    }


    @Override
    public UploadFingerprints getUploadFingerprints() {
        return uploadFingerprints;
    }

    @Override
    public void setProgress(int progress, int total) {
        builder.setProgress(total, progress, false);
        Notification notification = builder.build();
        notificationManager.notify(TAG, notificationID, notification);
    }

    @Override
    public void onProgressComplete() {
        notificationManager.cancel(TAG, notificationID);
        builder = buildBaseNotification();
        Intent intent = RouteManager.getIntent(UploadChallengeService.this, Utils.getApplinkPathWithPrefix(ChallengesUrl.AppLink.SUBMISSION_DETAILS, postId));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.setContentText(getString(R.string.ch_submitted_success)).build();
        notificationManager.notify(TAG, notificationID, notification);
        Intent intent1 = new Intent(ChallengesSubmitPresenter.ACTION_UPLOAD_COMPLETE);
        intent1.putExtra(Utils.QUERY_PARAM_SUBMISSION_ID, uploadFingerprints.getNewPostId());
        intent1.putExtra(Utils.QUERY_PARAM_FILE_PATH, getUploadFilePath());
        sendBroadcast(intent1);
        stopSelf();

    }

    @Override
    public String getChallengeId() {
        return challengeID;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    @Override
    public void onProgressFail() {
        notificationManager.cancel(TAG, notificationID);
        builder = buildBaseNotification();
        Notification notification = builder.setContentText(getString(R.string.ch_submit_fail)).build();
        notificationManager.notify(TAG, notificationID, notification);
        sendBroadcast(new Intent(ChallengesSubmitPresenter.ACTION_UPLOAD_FAIL));
        stopSelf();
    }

}