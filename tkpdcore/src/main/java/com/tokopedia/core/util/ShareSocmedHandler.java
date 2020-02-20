package com.tokopedia.core.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.core2.R;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    /**
     * Untuk Product, untuk share shop see ShareSpecificShop
     *
     * @param packageName nama package diambil di TkpdState.PackageName.*
     * @param targetType  jenis apakah gambar/textonly diambil di TkpdState.PackageName.Type_
     * @param altUrl      URL tempat share apabila apps tidak diinstal pada device, disimpan di TkpdState.PackageName.
     * @author EkaCipta
     */

    public static void ShareSpecific(final LinkerData data, final Activity context, final String packageName, final String targetType, final Bitmap image, final String altUrl) {
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0, DataMapper.getLinkerShareData(data), new ShareCallback() {
                    @Override
                    public void urlCreated(LinkerShareResult linkerShareData) {
                        ShareData(context, packageName, targetType, linkerShareData.getShareContents(),
                                linkerShareData.getShareUri(), image, altUrl);
                    }

                    @Override
                    public void onError(LinkerError linkerError) {

                    }
                })
        );
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

    public static void ShareSpecificUri(final LinkerData data, final Activity context, final String packageName, final String targetType, final String image, final String altUrl) {
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
                                LinkerManager.getInstance().executeShareRequest(
                                        LinkerUtils.createShareRequest(0, DataMapper.getLinkerShareData(data),
                                                new ShareCallback() {
                                                    @Override
                                                    public void urlCreated(LinkerShareResult linkerShareData) {
                                                        ShareDataWithSpecificUri(file, targetType, image, context,
                                                                linkerShareData.getShareContents(), linkerShareData.getShareUri(),
                                                                packageName, altUrl);
                                                    }

                                                    @Override
                                                    public void onError(LinkerError linkerError) {

                                                    }
                                                }
                                        )
                                );
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

    public static void ShareIntentImageUri(final LinkerData data, final Activity context, final String title, String imageUri) {

        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0, DataMapper.getLinkerShareData(data), new ShareCallback() {
                    @Override
                    public void urlCreated(LinkerShareResult linkerShareData) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        if (title != null) share.putExtra(Intent.EXTRA_SUBJECT, title);
                        share.putExtra(Intent.EXTRA_TEXT, linkerShareData.getShareContents());
                        context.startActivity(Intent.createChooser(share, "Share link!"));
                    }

                    @Override
                    public void onError(LinkerError linkerError) {

                    }
                })
        );
    }

    public ShareSocmedHandler(String url, Activity activity) {

    }
}

