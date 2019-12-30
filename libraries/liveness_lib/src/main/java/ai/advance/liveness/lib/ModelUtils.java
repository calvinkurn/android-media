package ai.advance.liveness.lib;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ai.advance.common.utils.FileUtil;
import ai.advance.common.utils.LogUtil;


/**
 * createTime:2019-07-17
 *
 * @author fan.zhang@advance.ai
 */
class ModelUtils {

    static final String MODEL_PATH = GuardianLivenessDetectionSDK.getApplicationContext().getApplicationContext().getFilesDir()
            .getAbsolutePath() + "/model";

    /**
     * Copy all files in the model directory to the data / data directory
     */
    static boolean copyModelsToData() {
        try {
            String modelAssetsVersionFileName = "liveness_mv";
            String localModelVersion = FileUtil.readFile(GuardianLivenessDetectionSDK.getApplicationContext(), modelAssetsVersionFileName);
            if (!BuildConfig.MODEL_ASSETS_VERSION.equals(localModelVersion)) {
                File[] files = new File(MODEL_PATH).listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        files[i].delete();
                    }
                }
                String[] assetsFiles = GuardianLivenessDetectionSDK.getApplicationContext().getAssets().list("model");
                if (assetsFiles != null) {
                    for (int i = 0; i < assetsFiles.length; i++) {
                        copyAssetsToData(assetsFiles[i]);
                    }
                }
                FileUtil.saveFile(GuardianLivenessDetectionSDK.getApplicationContext(), modelAssetsVersionFileName, BuildConfig.MODEL_ASSETS_VERSION);
            }
        } catch (Exception e) {
            LogUtil.sdkLogE(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Copy the files in the assets directory to the app data directory
     *
     * @param assetsName assets file name
     */
    private static void copyAssetsToData(String assetsName) {
        InputStream in = null;
        FileOutputStream out = null;

        File modelPathFile = new File(MODEL_PATH);
        if (!modelPathFile.exists()) {
            modelPathFile.mkdirs();
        }
        String path = MODEL_PATH + "/" + assetsName; // data/data directory
        File file = new File(path);
        if (!file.exists()) {
            try {
                in = GuardianLivenessDetectionSDK.getApplicationContext().getAssets().open("model/" + assetsName);
                out = new FileOutputStream(file);
                int length = -1;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
