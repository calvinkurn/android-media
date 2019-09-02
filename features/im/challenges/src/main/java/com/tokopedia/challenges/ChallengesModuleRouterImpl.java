package com.tokopedia.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareData;
import com.tokopedia.linker.model.LinkerShareResult;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.user.session.UserSession;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ChallengesModuleRouterImpl {

    public static Intent getLoginIntent(Context context) {
        Intent intent = LoginActivity.DeepLinkIntents.getCallingIntent(context);
        return intent;
    }


    public static Intent getHomeIntent(Context context) {
        return null;//MainParentActiviy.start(context);
    }

    public static void generateBranchUrlForChallenge(Activity context, String url, String title, String channel, String og_url, String og_title, String og_desc, String og_image, String deepLink, BranchLinkGenerateListener branchLinkGenerateListener) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.INDI_CHALLENGE_TYPE)
                .setName(title)
                .setUri(url)
                .setSource(channel)
                .setOgUrl(og_url)
                .setOgTitle(og_title)
                .setDescription(og_desc)
                .setOgImageUrl(og_image)
                .setDeepLink(deepLink)
                .build();

        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                getLinkerShareData(shareData), new ShareCallback() {
                    @Override
                    public void urlCreated(LinkerShareResult linkerShareData) {
                        branchLinkGenerateListener.onGenerateLink(linkerShareData.getShareContents(), linkerShareData.getShareUri());
                    }

                    @Override
                    public void onError(LinkerError linkerError) {

                    }
                }));
    }


    public static void shareBranchUrlForChallenge(Activity context, String packageName, String url, String shareContents) {
        shareData(context, packageName, "text/plain", url, shareContents,null,null);
    }


    public static String getStringRemoteConfig(Context context, String key) {
        return  new FirebaseRemoteConfigImpl(context).getString(key, "");
    }

    public static boolean getBooleanRemoteConfig(Context context, String key, boolean defaultValue) {
        return new FirebaseRemoteConfigImpl(context).getBoolean(key, defaultValue);
    }

    public static LinkerShareData getLinkerShareData(LinkerData linkerData){
        UserSession userSession = new UserSession(LinkerManager.getInstance().getContext());

        UserData userData = new UserData();
        userData.setName(userSession.getName());
        userData.setPhoneNumber(userSession.getPhoneNumber());
        userData.setUserId(userSession.getUserId());
        userData.setEmail(userSession.getEmail());
        userData.setFirstTimeUser(userSession.isFirstTimeUser());
        userData.setLoggedin(userSession.isLoggedIn());

        LinkerShareData linkerShareData = new LinkerShareData();
        linkerShareData.setLinkerData(linkerData);
        linkerShareData.setUserData(userData);

        return linkerShareData;
    }

   public interface BranchLinkGenerateListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

    private static void shareData(Activity context, String packageName, String targetType, String ProductUri,String shareTxt,  Bitmap image, String altUrl) {
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
            share.putExtra(Intent.EXTRA_STREAM, getUri(context, f));
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
                Toast.makeText(context, "error_apps_not_installed", Toast.LENGTH_SHORT).show();
        }
    }
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

    public static Uri getUri(Context context, File outputMediaFile) {
        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", outputMediaFile);
        } else {
            return Uri.fromFile(outputMediaFile);
        }
    }

    public static String uniqueCode() {
        String IDunique = UUID.randomUUID().toString();
        String id = IDunique.replaceAll("-", "");
        String code = id.substring(0, 16);
        return code;
    }
}
