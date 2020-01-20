package com.tokopedia.referral;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.referral.domain.GetReferralDataUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Util {
    public static RequestParams getPostRequestBody(UserSession userSession){
        RequestParams params = RequestParams.create();
        if(userSession.isLoggedIn()) {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty(GetReferralDataUseCase.Companion.getUserId(), Integer.parseInt(userSession.getUserId()));
            requestBody.addProperty(GetReferralDataUseCase.Companion.getMsisdn(), userSession.getPhoneNumber());
            params.getParameters().put(GetReferralDataUseCase.Companion.getData(), requestBody);
        }
        return params;
    }

    // this is share data method from core module -> ShareSocmedHandler class
    public static void shareData(Activity context, String packageName, String targetType, String shareTxt, String ProductUri, Bitmap image, String altUrl) {
        boolean Resolved = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(targetType);
        File f = null;
        if (image != null)
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                CheckTempDirectory();
                f = new File(Environment.getExternalStorageDirectory() + File.separator + "tkpdtemp" + File.separator + uniqueCode() + ".jpg");
                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (image != null) {
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, f));
        }
        share.putExtra(Intent.EXTRA_REFERRER, ProductUri);
        share.putExtra(Intent.EXTRA_TEXT, shareTxt);

        if (context != null) {
            if (context.getPackageManager() != null) {
                List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);

                for (ResolveInfo info : resInfo) {
                    if (info.activityInfo.packageName.equals(packageName)) {
                        Resolved = true;
                        share.setPackage(info.activityInfo.packageName);
                    }
                }
            }

            if (Resolved) {
                context.startActivity(share);
            } else if (altUrl != null) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(altUrl)));
            } else
                Toast.makeText(context, context.getString(R.string.error_apps_not_installed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cek direktori temporari untuk menyimpan gambar ada atau tidak
     */

    public static void CheckTempDirectory() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "tkpdtemp" + File.separator;
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {
            Log.v("FILES", "EXIST");
            File[] fs = f.listFiles();
            if (fs != null && fs.length > 5) // Hapus jika jumlah gambar temporary > 5
                for (File file : fs) {
                    file.delete();
                }
        } else {
            Log.v("FILES", "DONT EXIST");
            f.mkdir(); // create directory jika direktori tidak ada
        }
    }

    public static String uniqueCode() {
        String IDunique = UUID.randomUUID().toString();
        String id = IDunique.replaceAll("-", "");
        String code = id.substring(0, 16);
        return code;
    }
}
