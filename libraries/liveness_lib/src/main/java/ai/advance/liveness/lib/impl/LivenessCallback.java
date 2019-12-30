package ai.advance.liveness.lib.impl;

import ai.advance.liveness.lib.Detector;

/**
 * createTime:2019/4/24
 *
 * @author fan.zhang@advance.ai
 */
public interface LivenessCallback extends Detector.DetectorInitCallback {
    /**
     * action success callback
     */
    void onDetectionSuccess();

    /**
     * frame status callback
     *
     * @param warnCode current frame status
     */
    void onDetectionFrameStateChanged(Detector.WarnCode warnCode);

    /**
     * Remaining time of current action
     *
     * @param remainingTimeMills ms
     */
    void onActionRemainingTimeChanged(long remainingTimeMills);

    /**
     * detection failed
     */
    void onDetectionFailed(Detector.DetectionFailedType failedType, Detector.DetectionType detectionType);

    /**
     * detection success
     */
    void onDetectionActionChanged();
}
