package com.tokopedia.challenges.view.service;

import com.tokopedia.challenges.view.model.upload.UploadFingerprints;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */
public interface IUploadChallengeServiceContract {

    public interface UploadChallengeListener {
        public UploadFingerprints getUploadFingerprints();

        public void setProgress(int progress, int total);

        public void onProgressComplete();

        public String getChallengeId();

        public String getUploadFilePath();

        void onProgressFail();
    }

    public interface UploadChallengePresenter {
        public void uploadChallange();
    }
}
