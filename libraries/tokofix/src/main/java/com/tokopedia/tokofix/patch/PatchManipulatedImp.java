package com.tokopedia.tokofix.patch;

import android.content.Context;
import android.util.Log;

import com.meituan.robust.Patch;
import com.meituan.robust.PatchManipulate;
import com.meituan.robust.RobustApkHashUtils;
import com.meituan.robust.RobustCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author errysuprayogi on 01,February,2020
 */
public class PatchManipulatedImp extends PatchManipulate {
    /***
     * connect to the network ,get the latest patches
     * @param context
     *
     * @return
     */

    private final RobustCallBack robustCallBack;

    public PatchManipulatedImp(RobustCallBack robustCallBack) {
        this.robustCallBack = robustCallBack;
    }

    @Override
    protected List<Patch> fetchPatchList(final Context context) {
        // Report the app's own robustApkHash to the server, and the server distinguishes each apk build to issue a patch to the app based on the robustApkHash
        // apkhash is the unique identifier for apk, so you cannnot patch wrong apk.
        String robustApkHash = RobustApkHashUtils.readRobustApkHash(context);
        Log.w("robust", "robustApkHash :" + robustApkHash);

        Patch patch = new Patch();
        patch.setName("robust-patch");
        patch.setPatchesInfoImplClassFullName("com.meituan.robust.patch.PatchesInfoImpl");
        List patches = new ArrayList<Patch>();
        patch.setLocalPath(context.getDir("patch", Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "patch");
        patches.add(patch);
        robustCallBack.onPatchFetched(true, true, patch);
        return patches;
    }

    /**
     * @param context
     * @param patch
     * @return you can verify your patches here
     */
    @Override

    protected boolean verifyPatch(Context context, Patch patch) {
        //do your verification, put the real patch to patch
        patch.setTempPath(context.getCacheDir() + File.separator + "robust" + File.separator + "patch");
        try {
            copy(patch.getLocalPath(), patch.getTempPath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("copy source patch to local patch error, no patch execute in path " + patch.getTempPath());
        }

        return true;
    }

    public void copy(String srcPath, String dstPath) throws IOException {
        File src = new File(srcPath);
        if (!src.exists()) {
            throw new RuntimeException("source patch does not exist ");
        }
        File dst = new File(dstPath);
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    /**
     * @param patch
     * @return you may download your patches here, you can check whether patch is in the phone
     */
    @Override
    protected boolean ensurePatchExist(Patch patch) {
        return true;
    }
}
