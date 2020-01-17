package ai.advance.liveness.lib.impl


import ai.advance.common.entity.BaseResultEntity

/**
 * createTime:2019/4/24
 *
 * @author fan.zhang@advance.ai
 */
interface LivenessGetFaceDataCallback {
    /**
     * get face data start
     */
    fun onGetFaceDataStart()

    /**
     * get face data success
     */
    fun onGetFaceDataSuccess(entity: BaseResultEntity)

    /**
     * get face data failed
     */
    fun onGetFaceDataFailed(entity: BaseResultEntity)
}
