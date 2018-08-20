package com.tokopedia.challenges.view.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
import com.tokopedia.challenges.view.fragments.submit.ChallengesSubmitPresenter;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.core.app.BaseService;
import com.tokopedia.core.gcm.utils.NotificationChannelId;

import javax.inject.Inject;

public class UploadChallengeService extends BaseService implements IUploadChallengeServiceContract.UploadChallengeListener {
    public static final String TAG = "UploadChallengeService";
    public static final String UPLOAD_FINGERPRINT_KEY = "UploadChallengeService";
    public static final String UPLOAD_CHALLENGE_ID = "UploadChallengeID";
    public static final String UPLOAD_FILE_PATH = "UploadFilePath";




    @Inject
    UploadChallengePresenterImpl presenter;

    UploadFingerprints uploadFingerprints;
    String challengeID;
    String uploadFilePath;
    private NotificationManager notificationManager;

    public static Intent getIntent(Context context, UploadFingerprints uploadFingerprints,String challengeId,String uploadFilePath) {
        Intent intent = new Intent(context, UploadChallengeService.class);
        intent.putExtra(UPLOAD_FINGERPRINT_KEY, uploadFingerprints);
        intent.putExtra(UPLOAD_CHALLENGE_ID, challengeId);
        intent.putExtra(UPLOAD_FILE_PATH, uploadFilePath);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        ChallengesComponent component = DaggerChallengesComponent.builder()
                .baseAppComponent(((BaseMainApplication)getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
        presenter.attach(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uploadFingerprints = (UploadFingerprints) intent.getSerializableExtra(UPLOAD_FINGERPRINT_KEY);
        challengeID = intent.getStringExtra(UPLOAD_CHALLENGE_ID);
        uploadFilePath = intent.getStringExtra(UPLOAD_FILE_PATH);
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
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Upload In Progress"))
                .setProgress(uploadFingerprints.getTotalParts(),0,true)
                .build();
        notificationManager.notify(TAG, notificationID, notification);
    }


    public void sendSuccessBroadcast() {
    }

    private NotificationCompat.Builder buildBaseNotification() {
        String title = "Challenge Uploading";
        int largeIconRes = R.drawable.ic_stat_notify2;
        return new NotificationCompat.Builder(this, NotificationChannelId.GENERAL)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_notify_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), largeIconRes))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true);
    }



    @Override
    public UploadFingerprints getUploadFingerprints() {
        return uploadFingerprints;
    }

    @Override
    public void setProgress(int progress,int total) {
        builder.setProgress(total, progress, false);
        Notification notification = builder.build();
        notificationManager.notify(TAG,notificationID, notification);
    }

    @Override
    public void onProgressComplete() {
        notificationManager.cancel(TAG,notificationID);
        sendBroadcast(new Intent(ChallengesSubmitPresenter.ACTION_UPLOAD_COMPLETE));
        stopSelf();

    }

    @Override
    public String getChallengeId() {
        return challengeID;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

}