package ai.advance.liveness.lib

import ai.advance.common.utils.FileUtil
import ai.advance.common.utils.LogUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object ModelUtils {

    @JvmStatic
    val MODEL_PATH = GuardianLivenessDetectionSDK.applicationContext.applicationContext.filesDir
            .absolutePath + "/model"

    fun copyModelsToData(): Boolean {
        try {
            val modelAssetsVersionFileName = "liveness_mv"
            val localModelVersion = FileUtil.readFile(GuardianLivenessDetectionSDK.applicationContext, modelAssetsVersionFileName)
            if (BuildConfig.MODEL_ASSETS_VERSION != localModelVersion) {
                val files = File(MODEL_PATH).listFiles()
                if (files != null) {
                    for (i in files.indices) {
                        files[i].delete()
                    }
                }
                val assetsFiles = GuardianLivenessDetectionSDK.applicationContext.assets.list("model")
                if (assetsFiles != null) {
                    for (i in assetsFiles.indices) {
                        copyAssetsToData(assetsFiles[i])
                    }
                }
                FileUtil.saveFile(GuardianLivenessDetectionSDK.applicationContext, modelAssetsVersionFileName, BuildConfig.MODEL_ASSETS_VERSION)
            }
        } catch (e: Exception) {
            LogUtil.sdkLogE(e.message)
            return false
        }

        return true
    }

    /**
     * Copy the files in the assets directory to the app data directory
     *
     * @param assetsName assets file name
     */
    private fun copyAssetsToData(assetsName: String) {
        var `in`: InputStream? = null
        var out: FileOutputStream? = null

        val modelPathFile = File(MODEL_PATH)
        if (!modelPathFile.exists()) {
            modelPathFile.mkdirs()
        }
        val path = "$MODEL_PATH/$assetsName" // data/data directory
        val file = File(path)
        if (!file.exists()) {
            try {
                `in` = GuardianLivenessDetectionSDK.applicationContext.assets.open("model/$assetsName")
                out = FileOutputStream(file)
                val buf = ByteArray(1024)
                var length = `in`.read(buf)
                while (length != -1) {
                    out.write(buf, 0, length)
                    length = `in`.read(buf)
                }
                out.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }

                }
                if (out != null) {
                    try {
                        out.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }

                }
            }
        }
    }
}
