package com.tokopedia.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author EkaCipta
 *         Class untuk share dan menangkap shareintent
 */
public class ShareSocmedHandler {
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private Activity activity;
    private String url = null;
    private FacebookInterface fbinterface;
//    private OnPublishListener onPublishListener = new OnPublishListener() {
//
//        @Override
//        public void onFail(String reason) {
//            // insure that you are logged in before publishing
//            fbinterface.onShareFailed();
//        }
//
//        @Override
//        public void onException(Throwable throwable) {
//            CommonUtils.dumper("hangman sharefb exception " + throwable.toString());
//            fbinterface.onShareFailed();
//        }
//
//        @Override
//        public void onThinking() {
//
//        }
//
//        @Override
//        public void onComplete(String postId) {
//            CommonUtils.dumper("SUCCESS!" + postId);
//            fbinterface.onShareComplete();
//        }
//    };
//

    public interface FacebookInterface {
        public void onShareComplete();

        public void onShareFailed();
    }

    /**
     * @param context
     * @param result  Hasil exclusion list dari web
     * @param uri     Alamat yang diterima dari intent
     * @return 0 Jika terdapat dalam exclude, GetShareIntent jika tidak terdapat dalam exclude
     */
    public static int GetNewExclusionList(final Activity context, JSONObject result, final Uri uri) {
        String link = uri.toString();
        String ExclusionList[];
        int temp;
        try {
            JSONArray data = new JSONArray(result.getString("data"));
            temp = data.length();
            ExclusionList = new String[temp];
            for (int i = 0; i < temp; i++) {
                ExclusionList[i] = data.getString(i);
            }

            LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.EXCLUSION);
            cache.putArrayString(TkpdCache.Key.URL, ExclusionList);
            cache.applyEditor(); // Update exclusion

            for (String string : ExclusionList) {
                if (link.contains(string)) {
                    PackageManager pm = context.getPackageManager();
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tokopedia.com"));
                    ActivityInfo af = myIntent.resolveActivityInfo(pm, 0);
                    Intent launchIntent = new Intent(Intent.ACTION_MAIN, Uri.parse(link));
                    launchIntent.setClassName(af.packageName, af.name);
                    context.startActivity(launchIntent);
                    context.finish();
                    return 0; // Terdapat dalam daftar exclude dari web, redirect ke browser
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GetShareIntent(context, uri); // Tidak terdapat exclusion apa2
    }

    public static void SaveExclusionList(Activity context, String[] exclusionList) {

    }


    /**
     * @param context
     * @param data    Alamat untuk di cek
     * @return 0 jika terdapat dalam exclusion dari cache, 1 masuk ke cek exclusion dari web
     */
    public static int GetExclusionList(Activity context, Uri data) {
        String link = data.toString();
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.EXCLUSION);
        String ExclusionList[] = cache.getArrayString(TkpdCache.Key.URL);
        // Mengambil daftar exlcude dari cache yang telah disimpan
        if (ExclusionList != null)
            for (String string : ExclusionList) {
                if (link.contains(string)) {
                    PackageManager pm = context.getPackageManager();
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                    ActivityInfo af = myIntent.resolveActivityInfo(pm, 0);
                    Intent launchIntent = new Intent(Intent.ACTION_MAIN, Uri.parse(link));
                    launchIntent.setClassName(af.packageName, af.name);
                    context.startActivity(launchIntent);
                    context.finish();
                    return 0; // Terdapat dalam daftar exclude dari cache, redirect ke browser
                }
            }
        return 1; // Tidak terdapat dalam exclude, masuk ke fungsi cek exclusion dari web
    }

    /**
     * @param context
     * @param data    URI yang didapat dari intent
     * @return 1 Link valid, 2 Link tidak valid
     */
    public static int GetShareIntent(Activity context, Uri data) {
        String link = data.toString();

        // Untuk memproses URL jika berupa Hot, atau search atau link toko/produk
        if (link.contains("/hot/")) {
            link = link.replace("http://www.tokopedia.com/hot/", "");
            link = link.replace("http://m.tokopedia.com/hot/", "");
            link = link.replace("www.tokopedia.com/hot/", "");
            link = link.replace("m.tokopedia.com/hot/", "");
            context.startActivity(BrowseProductRouter.getBrowseProductIntent(context, link));
            return 1; // 1 untuk pindah ke activity lain 2 untuk buka home
        } else if (link.contains("/search?")) {
            link = link.replace("http://www.tokopedia.com/search?", "");
            link = link.replace("http://m.tokopedia.com/search?", "");
            link = link.replace("www.tokopedia.com/hot/", "");
            link = link.replace("m.tokopedia.com/hot/", "");
            //TODO change to latest code
//            Intent intent = new Intent(context, BrowseCategory.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("querry_data", link);
//            bundle.putInt("state", 4);
//            intent.putExtras(bundle);
//            context.startActivity(intent);
            return 1;
        } else {
            link = link.replace("http://www.tokopedia.com/", "");
            link = link.replace("https://www.tokopedia.com/", "");
            link = link.replace("http://m.tokopedia.com/", "");
            link = link.replace("https://m.tokopedia.com/", "");
            link = link.replace("www.tokopedia.com/hot/", "");
            link = link.replace("m.tokopedia.com/hot/", "");
            String[] div = link.split("/");
            if (div.length == 1) {
                Intent intent = new Intent(context, ShopInfoActivity.class);
                intent.putExtras(ShopInfoActivity.createBundle("", div[0]));
                context.startActivity(intent);
                return 1;
            } else if (div.length == 2) {
                Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
                Bundle bundle = new Bundle();
                bundle.putString("shop_domain", div[0]);
                bundle.putString("product_key", div[1]);
                intent.putExtras(bundle);
                context.startActivity(intent);
                return 1;
            } else
                return 2;
        }
    }

    /**
     * Untuk Product, untuk share shop see ShareSpecificShop
     *
     * @param packageName nama package diambil di TkpdState.PackageName.*
     * @param targetType  jenis apakah gambar/textonly diambil di TkpdState.PackageName.Type_
     * @param altUrl      URL tempat share apabila apps tidak diinstal pada device, disimpan di TkpdState.PackageName.
     * @author EkaCipta
     */

    public static void ShareSpecific(final ShareData data, final Activity context, final String packageName, final String targetType, final Bitmap image, final String altUrl) {
        BranchSdkUtils.generateBranchLink(data, context, new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                ShareData(context, packageName, targetType, shareContents, shareUri, image, altUrl);
            }
        });
    }

    private static void ShareData(Activity context, String packageName, String targetType, String shareTxt, String ProductUri, Bitmap image, String altUrl) {
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
     * Untuk data uri (tidak perlu konversi dari bitmap ke uri)
     *
     * @param packageName nama package diambil di TkpdState.PackageName.*
     * @param targetType  jenis apakah gambar/textonly diambil di TkpdState.PackageName.Type_
     * @param altUrl      URL tempat share apabila apps tidak diinstal pada device, disimpan di TkpdState.PackageName.
     * @author EkaCipta
     */

    public static void ShareSpecificUri(final ShareData data, final Activity context, final String packageName, final String targetType, final String image, final String altUrl) {
        Observable.just(image)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String image) {
                        File photo = null;
                        if (image != null) {
                            FutureTarget<File> future = Glide.with(context)
                                    .load(image)
                                    .downloadOnly(4096, 2160);
                            try {
                                photo = future.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                        return photo;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<File>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(final File file) {
                                BranchSdkUtils.generateBranchLink(data, context, new BranchSdkUtils.GenerateShareContents() {
                                    @Override
                                    public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                                        ShareDataWithSpecificUri(file, targetType, image, context, shareContents, shareUri, packageName, altUrl);

                                    }
                                });
                            }
                        }
                );

    }

    private static void ShareDataWithSpecificUri(File file, String targetType, String image, Activity context, String shareTxt, String ProductUri, String packageName, String altUrl) {
        boolean Resolved = false;
        final Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(targetType);

        if (image != null) {
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, file));
        }
        share.putExtra(Intent.EXTRA_REFERRER, ProductUri);
        share.putExtra(Intent.EXTRA_HTML_TEXT, ProductUri);
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

    public static void ShareIntentImage(Activity context, String title, String shareTxt,
                                        String ProductUri, Bitmap icon) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        CheckTempDirectory();
        File f = new File(Environment.getExternalStorageDirectory() + File.separator
                + "tkpdtemp" + File.separator + uniqueCode() + ".jpg");
        if (icon != null)
            try {
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (icon != null && f.exists()) {

            Uri uri = MethodChecker.getUri(context, f);
            Intent chooserIntent = createIntent(context, title, shareTxt, uri, true);
            context.startActivity(chooserIntent);
        } else {
            Intent chooserIntent = createIntent(context, title, shareTxt, null, false);
            context.startActivity(chooserIntent);
        }
    }

    private static Intent createIntent(Context context, String title, String shareTxt, Uri uri,
                                       boolean fileExists) {
        Intent share = new Intent(Intent.ACTION_SEND);
        String shareTitle;
        if (fileExists) {
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_TEXT, shareTxt);
            shareTitle = "Share Image!";
        } else {
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, title);
            share.putExtra(Intent.EXTRA_TEXT, shareTxt);
            shareTitle = "Share Link!";
        }


        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);

        boolean isShop = false;
        if (SessionHandler.isV4Login(context)) {
            if (SessionHandler.isUserHasShop(context)) {
                isShop = true;
            }
        }

        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                if (fileExists) {
                    targetedShare.setType("image/*");
                    targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt);
                    targetedShare.putExtra(Intent.EXTRA_STREAM, uri);
                } else {
                    targetedShare.setType("text/plain");
                    targetedShare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    targetedShare.putExtra(Intent.EXTRA_SUBJECT, title);
                    targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt);
                }

                String appsName = info.activityInfo.packageName;
                if (appsName.equalsIgnoreCase(context.getPackageName())) {
                    if (isShop) {
                        targetedShare.setPackage(appsName);
                        targetedShareIntents.add(targetedShare);
                    }
                } else {
                    targetedShare.setPackage(appsName);
                    targetedShareIntents.add(targetedShare);
                }
            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0),
                    shareTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toArray(new Parcelable[]{}));
            return chooserIntent;
        }
        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
        shareContent.putExtra(Intent.EXTRA_TEXT, shareTxt);
        Intent chooserIntent = Intent.createChooser(shareContent,
                shareTitle);
        return chooserIntent;
    }

    public static void ShareIntentImageUri(final ShareData data, final Activity context, final String title, String imageUri) {

        BranchSdkUtils.generateBranchLink(data, context, new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                if (title != null) share.putExtra(Intent.EXTRA_SUBJECT, title);
                share.putExtra(Intent.EXTRA_TEXT, shareContents);
                context.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });

    }

    /**
     * @param context
     * @param ProductUri bisa diganti url product
     * @param icon       kasih null untuk Shop
     * @author EkaCipta
     */

    public static void ShareIntentImage(Activity context, String shareTxt, String ProductUri, Bitmap icon) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        CheckTempDirectory();
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "tkpdtemp" + File.separator + uniqueCode() + ".jpg");
        if (icon != null)
            try {
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }


//		Intent shareIntent = new Intent();
//		 shareIntent.setAction(Intent.ACTION_SEND);
//		 shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
//		 shareIntent.putExtra(Intent.EXTRA_TEXT, "Shareing Prodak nih: ");
//		 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subjecct");
//		 shareIntent.setType("image/jpeg");
//		 shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//		 context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        try {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("image/*");
            List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, PackageManager.MATCH_DEFAULT_ONLY);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    try {
                        Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
//						  if (info.activityInfo.packageName.equals("com.google.android.gm") || info.activityInfo.packageName.equals("com.google.android.keep") || info.activityInfo.packageName.equals("com.google.android.apps.plus") || info.activityInfo.packageName.equals("com.google.android.talk")) {
                        targetedShare.setType("text/plain"); // put here your mime type
                        //targetedShare.putExtra(Intent.EXTRA_SUBJECT,"Aplikasi Tokopedia");
//							  targetedShare.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.message_share_prod_info) + " " + ProductUri);
                        if (icon != null && !info.activityInfo.packageName.equals("com.bbm")) {
                            targetedShare.putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, f));
                            targetedShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }

                        targetedShare.setPackage(info.activityInfo.packageName);
//						  else{
//							  targetedShare.setType("text/plain"); // put here your mime type
//							  targetedShare.setPackage(info.activityInfo.packageName);
//						  }
                        targetedShare.putExtra(Intent.EXTRA_REFERRER, ProductUri);
                        targetedShare.putExtra(Intent.EXTRA_HTML_TEXT, ProductUri);
                        targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt);
                        targetedShareIntents.add(targetedShare);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                Intent chooserIntent = Intent.createChooser(share, "Select app to share");
                context.startActivity(chooserIntent);
            }
        } catch (Exception e) {
            Log.v("VM", "Exception while sending image on " + e.getMessage());
        }
    }

    public static void ShareIntent(Activity context, String tagLine, String ProductUri) {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                targetedShareIntent.setType("text/plain");
                String packageName = resolveInfo.activityInfo.packageName;
                //targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "App Tokopedia");
                // Untuk beda2in pada masing2 platform
                if (packageName.equals("com.facebook.katana")) {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, ProductUri);
                } else {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, tagLine + " " + ProductUri);
                    //targetedShareIntents.add(targetedShareIntent);
                }
                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);
//                if (packageName.equals("com.facebook.katana")){
//                	targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just shared this product "+ProductUri);
//                    targetedShareIntent.setPackage(packageName);
//                    targetedShareIntents.add(targetedShareIntent);
//                }
//                else{
//                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just shared this product "+ProductUri);
//                }
            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");

            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

            context.startActivity(chooserIntent);
        }
    }


    public ShareSocmedHandler(String url, Activity activity) {
        this.activity = activity;
        this.url = url;
        fbinterface = (FacebookInterface) activity;
    }
}

