package com.tokopedia.developer_options.mssdk

object MsSdkOptionConst {


    val COLLECT_MODE = listOf("COLLECT_MODE_ML_MINIMIZE","COLLECT_MODE_FINANCE","COLLECT_MODE_ML_TEEN","COLLECT_MODE_HELO",
        "COLLECT_MODE_RESSO","COLLECT_MODE_TIKTOKLITE","COLLECT_MODE_TIKTOK","COLLECT_MODE_TIKTOK_BASE",
        "COLLECT_MODE_MINIMIZE","COLLECT_MODE_OV_MINIMIZE","COLLECT_MODE_ML_PGL_AL","COLLECT_MODE_TIKTOK_U13",
        "COLLECT_MODE_TIKTOK_USUNREGISTER","COLLECT_MODE_TIKTOK_GUEST","COLLECT_MODE_TIKTOK_INIT","COLLECT_MODE_TIKTOK_NONUSEA",
        "COLLECT_MODE_TIKTOK_USEA","COLLECT_MODE_DEFAULT")

    val COLLECT_MODE_SIZE = COLLECT_MODE.size

    val DEFAULT_COLLECT_MODE = Pair(COLLECT_MODE[COLLECT_MODE_SIZE-2],COLLECT_MODE_SIZE-2)

    @Volatile
    private var selectedMODE = COLLECT_MODE[COLLECT_MODE.size-2]

    @JvmStatic
    fun getIndex(value: String): Int{
        var index = 0
        for (collectMode in COLLECT_MODE){
            if(value.equals(collectMode)){
                synchronized(this){
                    return index
                }
            }
            index++
        }
        return -1
    }

    @JvmStatic
    fun getSelectMode() = synchronized(this){ selectedMODE }


    @JvmStatic
    fun getSelectModeInt(value: String) = synchronized(this){if(getIndex(value) !=-1){
        COLLECT_MODE_SIZE -2}else{
        getIndex(value)
    } }
//
//    @JvmStatic
//    fun setSelectMode(value: Int) = synchronized(this){ selectedMODE = COLLECT_MODE[value] }
//
//    @JvmStatic
//    fun setSelectMode(value: String) = synchronized(this){ selectedMODE = COLLECT_MODE[getIndex(value)] }
}
