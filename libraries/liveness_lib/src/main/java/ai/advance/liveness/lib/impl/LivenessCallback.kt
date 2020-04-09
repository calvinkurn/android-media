package ai.advance.liveness.lib.impl

import ai.advance.liveness.lib.Detector

/**
 * createTime:2019/4/24
 *
 * @author fan.zhang@advance.ai
 */
interface LivenessCallback : Detector.DetectorInitCallback {
    /**
     * action success callback
     */
    fun onDetectionSuccess()

    /**
     * frame status callback
     *
     * @param warnCode current frame status
     */
    fun onDetectionFrameStateChanged(warnCode: Detector.WarnCode)

    /**
     * Remaining time of current action
     *
     * @param remainingTimeMills ms
     */
    fun onActionRemainingTimeChanged(remainingTimeMills: Long)

    /**
     * detection failed
     */
    fun onDetectionFailed(failedType: Detector.DetectionFailedType, detectionType: Detector.DetectionType)

    /**
     * detection success
     */
    fun onDetectionActionChanged()
}
