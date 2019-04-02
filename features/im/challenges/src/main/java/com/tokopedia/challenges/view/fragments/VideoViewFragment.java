package com.tokopedia.challenges.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;

/**
 * @author lalit.singh
 */
public class VideoViewFragment extends DialogFragment implements CustomVideoPlayer.CustomVideoPlayerListener, View.OnClickListener {

    private static VideoViewFragment instance;

    private SubmissionResult submissionResult;

    CustomVideoPlayer customVideoPlayer;
    private int currentVideoProgress;

    public static Fragment createInstance(SubmissionResult submissionResult) {
        if (instance == null) {
            instance = new VideoViewFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(Utils.QUERY_PARAM_SUBMISSION_RESULT, submissionResult);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Utils.QUERY_PARAM_SUBMISSION_RESULT)) {
            submissionResult = getArguments().getParcelable(Utils.QUERY_PARAM_SUBMISSION_RESULT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_view, container, false);
        rootView.findViewById(R.id.cl_videoViewParent).setOnClickListener(this);
        rootView.findViewById(R.id.iv_close).setOnClickListener(this);
        customVideoPlayer = rootView.findViewById(R.id.video_player);
        setVideoThumb();
        return rootView;
    }

    public void setVideoThumb() {
        customVideoPlayer.setVideoThumbNail(submissionResult.getThumbnailUrl(),
                submissionResult.getMedia().get(0).getVideo().getSources().get(1).getSource(),
                false, this, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //if(currentVideoProgress!= 0){
        customVideoPlayer.startPlay(currentVideoProgress, true);
        //}
    }

    @Override
    public void onPause() {
        super.onPause();
        if (customVideoPlayer != null && customVideoPlayer.isVideoPlaying()) {
            currentVideoProgress = customVideoPlayer.getPosition();
            customVideoPlayer.pause();
        }
    }


    @Override
    public void OnVideoStart() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_close) {
            getActivity().onBackPressed();
        }
    }
}


/*else if (model.getMedia() != null && model.getMedia().get(0).getVideo() != null && model.getMedia().get(0).getVideo().getSources() != null) {
            getView().setChallengeImage(model.getThumbnailUrl(), model.getMedia().get(0).getVideo().getSources().get(1).getSource());
        }*/