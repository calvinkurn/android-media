package com.tokopedia.tkpd.campaign.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.exception.ServerErrorRequestDeniedException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorMaintenanceException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorTimeZoneException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.configuration.WavRecorder;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.data.model.CampaignException;
import com.tokopedia.tkpd.campaign.domain.audio.PostAudioDataUseCase;
import com.tokopedia.tkpd.campaign.view.ShakeDetectManager;
import com.tokopedia.usecase.RequestParams;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.tkpd.campaign.domain.audio.PostAudioDataUseCase.AUDIO_PATH;
import static com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase.IS_AUDIO;
import static com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase.SCREEN_NAME;

/**
 * Created by sandeepgoyal on 21/02/18.
 */

public class AudioShakeDetectPresenter extends ShakeDetectPresenter implements WavRecorder.RecordCompleteListener {


    PostAudioDataUseCase postShakeDetectUseCase;
    private static final int AUDIO_RECORD_LENGTH = 10000;
    private static final int VIBRATION_PERIOD = 500;


    @Inject
    public AudioShakeDetectPresenter(PostAudioDataUseCase shakeDetectUseCase, @ApplicationContext Context context) {
        super(shakeDetectUseCase, context);
        this.postShakeDetectUseCase = shakeDetectUseCase;
    }

    @Override
    public void onShakeDetect() {
        try {
            startRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    WavRecorder recorder;

    private void startRecording() throws IOException {
        recorder = new WavRecorder();
        recorder.startRecording(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recorder.stopRecording();
            }
        }, AUDIO_RECORD_LENGTH);
    }


    @Override
    public void onRecordComplete() {
        getView().showErrorNetwork("Record Complete");
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(IS_AUDIO, "true");
        requestParams.putString(SCREEN_NAME, ShakeDetectManager.sTopActivity);
        requestParams.putString(AUDIO_PATH, WavRecorder.getFilePath());
        postShakeDetectUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
            @Override
            public void onCompleted() {
                getView().finish();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof CampaignException) {
                    getView().showErrorNetwork(context.getString(R.string.msg_dialog_wrong_scan));
                } else if (e instanceof ResponseDataNullException) {
                    getView().showErrorNetwork(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().showErrorNetwork(e.getMessage());
                } else if (e instanceof ServerErrorException) {
                    getView().showErrorNetwork(ErrorHandler.getErrorMessage(context, e));
                } else {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
                intent.putExtra("isSuccess", false);
                getView().sendBroadcast(intent);
                v.vibrate(VIBRATION_PERIOD);
                getView().finish();
            }

            @Override
            public void onNext(final CampaignResponseEntity s) {

                Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
                intent.putExtra("isSuccess", true);
                intent.putExtra("data", s.getUrl());
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VIBRATION_PERIOD);
                getView().sendBroadcast(intent);

                //Open next activity based upon the result from server
            }
        });
    }

}
