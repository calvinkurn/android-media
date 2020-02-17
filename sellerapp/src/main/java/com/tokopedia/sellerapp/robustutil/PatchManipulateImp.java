package com.tokopedia.sellerapp.robustutil;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.meituan.robust.Patch;
import com.meituan.robust.PatchManipulate;
import com.meituan.robust.RobustApkHashUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mivanzhang on 17/2/27.
 *
 * We recommend you rewrite your own PatchManipulate class ,adding your special patch Strategy，in the demo we just load the patch directly
 *
 * <br>
 *   Pay attention to the difference of patch's LocalPath and patch's TempPath
 *
 *     <br>
 *    We recommend LocalPath store the origin patch.jar which may be encrypted,while TempPath is the true runnable jar
 *<br>
 *<br>
 *    We recommend inheriting PatchManipulate to implement your app ’s unique A patch loading strategy, where setLocalPath sets the original path of the patch, the patch stored in this path is encrypted, setTempPath stores the decrypted patch, and is a executable jar file
 *     <br>
 *     setTempPath removes patches immediately after loading. If encryption and decryption patches are not needed, there is no difference between the two.
 */

public class PatchManipulateImp extends PatchManipulate {
    /***
     * connect to the network ,get the latest patches
     * l联网获取最新的补丁
     * @param context
     *
     * @return
     */
    @Override
    protected List<Patch> fetchPatchList(Context context) {
        String robustApkHash = RobustApkHashUtils.readRobustApkHash(context);
        Log.w("robust","robustApkHash :" + robustApkHash);
        Patch patch = new Patch();
        patch.setName("robust-patch");
        patch.setLocalPath(Environment.getExternalStorageDirectory().getPath()+ File.separator+"robust"+ File.separator + "patch");

        patch.setPatchesInfoImplClassFullName("com.meituan.robust.patch.PatchesInfoImpl");
        List patches = new ArrayList<Patch>();
        patches.add(patch);
        return patches;
    }

    /**
     *
     * @param context
     * @param patch
     * @return
     *
     * you can verify your patches here
     */
    @Override

    protected boolean verifyPatch(Context context, Patch patch) {
        //do your verification, put the real patch to patch
        patch.setTempPath(context.getCacheDir()+ File.separator+"robust"+ File.separator + "patch");
        //in the sample we just copy the file
        try {
            copy(patch.getLocalPath(), patch.getTempPath());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("copy source patch to local patch error, no patch execute in path "+patch.getTempPath());
        }

        return true;
    }
    public void copy(String srcPath, String dstPath) throws IOException {
        File src=new File(srcPath);
        if(!src.exists()){
            throw new RuntimeException("source patch does not exist ");
        }
        File dst=new File(dstPath);
        if(!dst.getParentFile().exists()){
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
     *
     * @param patch
     * @return
     *
     * you may download your patches here, you can check whether patch is in the phone
     */
    @Override
    protected boolean ensurePatchExist(Patch patch) {
        return true;
    }
}
