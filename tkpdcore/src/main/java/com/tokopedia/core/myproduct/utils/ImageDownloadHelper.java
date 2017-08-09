package com.tokopedia.core.myproduct.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.newgallery.GalleryActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Class to separate the logic download - from image urls to tokopedia local paths
 * Created by hendry on 8/8/2017.
 */

public class ImageDownloadHelper {

    private WeakReference<Context> contextWeakReference;

    private static final int WIDTH_DOWNLOAD = 2048;
    private static final int DEF_WIDTH_CMPR = 2048;
    private static final int DEF_QLTY_COMPRESS = 70;

    private boolean needCompressTkpd = false;

    private WeakReference<OnImageDownloadListener> onImageDownloadListenerWeakReference;
    public interface OnImageDownloadListener{
        void onError(Throwable e);
        void onSuccess(ArrayList<String> localPaths);
    }
    public ImageDownloadHelper(Context context){
        this.contextWeakReference = new WeakReference<>(context);
    }
    
    public void convertHttpPathToLocalPath(List<String> urlsToDownload,
                                           boolean needCompressTkpd,
                                           OnImageDownloadListener onImageDownloadListener) {
        this.needCompressTkpd = needCompressTkpd;
        onImageDownloadListenerWeakReference= new WeakReference<>(onImageDownloadListener);
        downloadImages(urlsToDownload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<List<File>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (onImageDownloadListenerWeakReference.get()!= null) {
                                    onImageDownloadListenerWeakReference.get().onError(e);
                                }
                            }

                            @Override
                            public void onNext(List<File> files) {
                                ArrayList<String> localPaths = new ArrayList<>();
                                if (files == null || files.size() == 0) {
                                    throw new NullPointerException();
                                }
                                for (int i = 0, sizei = files.size(); i < sizei; i++) {
                                    localPaths.add(files.get(i).getAbsolutePath());
                                }
                                if (onImageDownloadListenerWeakReference.get()!= null) {
                                    onImageDownloadListenerWeakReference.get().onSuccess(localPaths);
                                }
                            }
                        }
                );
    }

    private Observable<List<File>> downloadImages(final List<String> urls) {
        return Observable.from(urls)
                .flatMap(new Func1<String, Observable<File>>() {
                    @Override
                    public Observable<File> call(String url) {
                        return downloadObservable(url);
                    }
                }).toList();
    }

    @NonNull
    private Observable<File> downloadObservable(String url) {
        return Observable.just(url)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String url) {
                        if (contextWeakReference.get() == null ){
                            return null;
                        }
                        FutureTarget<File> future = Glide.with(contextWeakReference.get())
                                .load(url)
                                .downloadOnly(WIDTH_DOWNLOAD, WIDTH_DOWNLOAD);
                        try {
                            File cacheFile = future.get();
                            String cacheFilePath = cacheFile.getAbsolutePath();
                            if (needCompressTkpd) {
                                String fileNameToMove = FileUtils.generateUniqueFileName(cacheFilePath);
                                File photo = FileUtils.writeImageToTkpdPath(
                                        FileUtils.compressImage(
                                                cacheFilePath, DEF_WIDTH_CMPR, DEF_WIDTH_CMPR, DEF_QLTY_COMPRESS),
                                        fileNameToMove);
                                if (photo != null) {
                                    return photo;
                                }
                            } else {
                                return writeImageToTkpdPath(cacheFile);
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        }
                        return null;
                    }
                });
    }

    private File writeImageToTkpdPath(File source) {
        InputStream inStream;
        OutputStream outStream;
        File dest = null;
        try {

            File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            dest = new File(directory.getAbsolutePath() + "/image.jpg");

            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;
    }
}
