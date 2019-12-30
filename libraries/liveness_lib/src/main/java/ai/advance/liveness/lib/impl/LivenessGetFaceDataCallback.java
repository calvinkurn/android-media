package ai.advance.liveness.lib.impl;


import ai.advance.common.entity.BaseResultEntity;

/**
 * createTime:2019/4/24
 *
 * @author fan.zhang@advance.ai
 */
public interface LivenessGetFaceDataCallback {
    /**
     * get face data start
     */
    void onGetFaceDataStart();

    /**
     * get face data success
     */
    void onGetFaceDataSuccess(BaseResultEntity entity);

    /**
     * get face data failed
     */
    void onGetFaceDataFailed(BaseResultEntity entity);
}
