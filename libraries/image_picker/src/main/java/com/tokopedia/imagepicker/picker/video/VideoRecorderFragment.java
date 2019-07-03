package com.tokopedia.imagepicker.picker.video;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.cameraview.Audio;
import com.tokopedia.cameraview.CameraListener;
import com.tokopedia.cameraview.CameraOptions;
import com.tokopedia.cameraview.CameraView;
import com.tokopedia.cameraview.Flash;
import com.tokopedia.cameraview.Gesture;
import com.tokopedia.cameraview.GestureAction;
import com.tokopedia.cameraview.Mode;
import com.tokopedia.cameraview.VideoResult;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.util.VideoUtils;
import com.tokopedia.imagepicker.picker.main.builder.StateRecorderType;
import com.tokopedia.imagepicker.picker.widget.VideoPlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VideoRecorderFragment extends TkpdBaseV4Fragment {
    private static final String SAVED_FLASH_INDEX = "saved_flash_index";
    private static final int DURATION_MAX = 60000;

    private List<Flash> flashList = new ArrayList<>();
    private int flashIndex = 0;
    private Timer timer;
    private VideoPickerCallback videoCallback;

    private CameraView cameraView;
    private ImageButton btnFlash;
    private ImageButton btnFlip;
    private ProgressBar progress;
    private TextView txtDuration;
    private View vwRecord;
    private View containerRecorder;
    private View containerPreview;
    private View recapture;
    private View useVideo;

    private VideoPlayerView videoPreview;
    private String videoPath = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            flashIndex = savedInstanceState.getInt(SAVED_FLASH_INDEX, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_recorder, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof VideoPickerCallback))
            throw new RuntimeException("Please Attach VideoPickerCallback");
        videoCallback = (VideoPickerCallback) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAttribute(view);
        cameraPrepared();
        progress.setMax(DURATION_MAX);
        btnFlip.setOnClickListener(v -> cameraSwitchFacing());
        btnFlash.setOnClickListener(v -> {
            if (flashList.size() > 0) {
                flashIndex = (flashIndex + 1) % flashList.size();
                setCameraFlash();
            }
        });
        view.findViewById(R.id.btnRecord).setOnClickListener(v -> recording());
        recapture.setOnClickListener(v -> showRecorder());
        useVideo.setOnClickListener(v -> {
            if (videoCallback != null) {
                videoCallback.onVideoTaken(videoPath);
                videoPath = null;
            }
            showRecorder();
        });
    }

    private void showRecorder() {
        containerPreview.setVisibility(View.GONE);
        containerRecorder.setVisibility(View.VISIBLE);
        if (videoCallback != null)
            videoCallback.onVideoRecorderVisible();
    }

    private void cameraPrepared() {
        cameraView.setMode(Mode.VIDEO);
        cameraView.setAudio(Audio.ON);
        cameraView.clearCameraListeners();
        cameraView.addCameraListener(cameraListener());
        cameraView.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            cameraView.open();
        } catch (Throwable t){
            if (GlobalConfig.isAllowDebuggingTools())
                t.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            cameraView.close();
        } catch (Throwable t){
            if (GlobalConfig.isAllowDebuggingTools())
                t.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            cameraView.destroy();
            timer.cancel();
        } catch (Throwable t){
            if (GlobalConfig.isAllowDebuggingTools())
                t.printStackTrace();
        }
        super.onDestroy();
    }

    private CameraListener cameraListener() {
        return new CameraListener() {
            @Override
            public void onCameraOpened(@NonNull CameraOptions options) {
                super.onCameraOpened(options);
                initCameraFlash();
            }

            @Override
            public void onVideoTaken(@NonNull VideoResult result){
                super.onVideoTaken(result);
                videoPath = result.getFile().getAbsolutePath();
                videoPreview.setVideoURI(Uri.parse(result.getFile().getAbsolutePath()));
                videoPreview.setOnCompletionListener(mp -> {
                    videoPreview.stopPlayback();
                });
                videoPreview.setOnPreparedListener(mp -> {
                    mp.setLooping(true);
                    playPreview(result.getFile().getAbsolutePath());
                });
                showPreviewVideo();
            }
        };
    }

    private void showPreviewVideo() {
        containerPreview.setVisibility(View.VISIBLE);
        containerRecorder.setVisibility(View.GONE);
        if (videoCallback != null)
            videoCallback.onVideoPreviewVisible();
    }

    private void playPreview(String videoPath) {
        if (videoPreview.isPlaying()) return;

        if (new File(videoPath).exists()){
            videoPreview.start();
        }
    }

    private void recording(){
        videoCallback.onVideoRecorder(StateRecorderType.START);

        //set default value
        progress.setProgress(0);
        long[] countDownMills = {DURATION_MAX};
        txtDuration.setText(getString(R.string.vidpick_duration_default));

        if (cameraView.isTakingVideo()) {
            vwRecord.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            btnFlash.setVisibility(View.VISIBLE);
            btnFlip.setVisibility(View.VISIBLE);
            cameraView.stopVideo();
            timer.cancel();
        } else {
            vwRecord.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            btnFlip.setVisibility(View.GONE);
            btnFlash.setVisibility(View.GONE);
            File file = VideoUtils.getVideoPath();
            cameraView.takeVideo(file, DURATION_MAX);
            //progress and duration countdown
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                           @Override
                           public void run() {
                               if (cameraView != null && cameraView.isTakingVideo()) {
                                   if (getActivity() != null) {
                                       getActivity().runOnUiThread(() -> {
                                           long minutes = TimeUnit.MILLISECONDS.toMinutes(countDownMills[0]);
                                           long seconds = TimeUnit.MILLISECONDS.toSeconds(countDownMills[0]) - TimeUnit.MINUTES.toSeconds(minutes);
                                           txtDuration.setText(getString(R.string.vidpick_duration_format,
                                                   String.format(Locale.getDefault(), "%02d",minutes),
                                                   String.format(Locale.getDefault(), "%02d", seconds)));
                                           progress.setProgress(progress.getProgress() + 1000);
                                           countDownMills[0] -= 1000;
                                       });
                                   }
                               }
                           }
                       },1, 1000);
        }
    }

    private void initCameraFlash() {
        if (cameraView == null || cameraView.getCameraOptions() == null) return;

        if (cameraView.getCameraOptions() != null){
            Collection<Flash> supportedFlash = cameraView.getCameraOptions().getSupportedFlash();
            for (Flash flash : supportedFlash){
                if (flash != Flash.TORCH)
                    flashList.add(flash);
            }
        }

        if (flashList.size() > 0){
            btnFlash.setVisibility(View.VISIBLE);
            setCameraFlash();
        } else {
            btnFlash.setVisibility(View.GONE);
        }
    }

    private void setCameraFlash() {
        if (flashIndex < 0 || flashList.size() <= flashIndex) return;

        Flash flash = flashList.get(flashIndex);

        if (flash.ordinal() == Flash.TORCH.ordinal()) {
            flashIndex = (flashIndex + 1) % flashList.size();
            flash = flashList.get(flashIndex);
        }

        cameraView.set(flash);
        setUIFlashCamera(flash.ordinal());
    }

    private void cameraSwitchFacing() {
        if (cameraView.isTakingVideo()) return;
        cameraView.toggleFacing();
    }

    private void setUIFlashCamera(int flashEnum) {
        if (flashEnum == Flash.AUTO.ordinal()){
            btnFlash.setImageDrawable(MethodChecker.getDrawable(btnFlash.getContext(),R.drawable.ic_auto_flash));
        } else if (flashEnum == Flash.ON.ordinal()){
            btnFlash.setImageDrawable(MethodChecker.getDrawable(btnFlash.getContext(),R.drawable.ic_on_flash));
        } else if (flashEnum == Flash.OFF.ordinal()){
            btnFlash.setImageDrawable(MethodChecker.getDrawable(btnFlash.getContext(),R.drawable.ic_off_flash));
        }
    }

    private void initializeAttribute(View view) {
        cameraView = view.findViewById(R.id.cameraView);
        btnFlash = view.findViewById(R.id.btnFlash);
        progress = view.findViewById(R.id.progress);
        btnFlip = view.findViewById(R.id.btnFlip);
        txtDuration = view.findViewById(R.id.txtDuration);
        vwRecord = view.findViewById(R.id.vwRecord);
        videoPreview = view.findViewById(R.id.videoPreview);
        containerPreview = view.findViewById(R.id.container_preview);
        containerRecorder = view.findViewById(R.id.layout_video_recorder);
        recapture = view.findViewById(R.id.layout_recapture);
        useVideo = view.findViewById(R.id.layout_use);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_FLASH_INDEX, flashIndex);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public interface VideoPickerCallback{
        void onVideoTaken(String filePath);
        void onVideoRecorder(@StateRecorderType int state);
        void onVideoPreviewVisible();
        void onVideoRecorderVisible();
    }
}
