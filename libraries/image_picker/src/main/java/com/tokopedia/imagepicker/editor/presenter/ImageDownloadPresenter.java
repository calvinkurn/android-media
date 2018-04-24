package com.tokopedia.imagepicker.editor.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.imagepicker.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ImageDownloadPresenter {

    private static final int WIDTH_DOWNLOAD = 2048;

    private CompositeSubscription compositeSubscription;

    private OnImageDownloadListener onImageDownloadListener;
    public interface OnImageDownloadListener{
        Context getContext();
        void onErrorDownloadImageToLocal(Throwable e);
        void onSuccessDownloadImageToLocal(ArrayList<String> localPaths);
    }

    public void attachView(OnImageDownloadListener onImageDownloadListener){
        this.onImageDownloadListener = onImageDownloadListener;
    }

    public void detachView(){
        onImageDownloadListener = null;
        compositeSubscription.unsubscribe();
    }

    public void convertHttpPathToLocalPath(List<String> urlsToDownload) {

        Subscription subscription = downloadImages(urlsToDownload)
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
                                if (ImageDownloadPresenter.this.onImageDownloadListener!= null) {
                                    ImageDownloadPresenter.this.onImageDownloadListener.onErrorDownloadImageToLocal(e);
                                }
                            }

                            @Override
                            public void onNext(List<File> files) {
                                ArrayList<String> resultLocalPaths = new ArrayList<>();
                                if (files == null || files.size() == 0) {
                                    throw new NullPointerException();
                                }
                                for (int i = 0, sizei = files.size(); i < sizei; i++) {
                                    resultLocalPaths.add(files.get(i).getAbsolutePath());
                                }
                                if (ImageDownloadPresenter.this.onImageDownloadListener != null) {
                                    ImageDownloadPresenter.this.onImageDownloadListener.onSuccessDownloadImageToLocal(resultLocalPaths);
                                }
                            }
                        }
                );
        if (compositeSubscription== null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    private Observable<List<File>> downloadImages(final List<String> urls) {
        // use concat map to preserve the ordering
        return Observable.from(urls)
                .concatMap(new Func1<String, Observable<File>>() {
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
                        if (URLUtil.isNetworkUrl(url)) {
                            if (onImageDownloadListener== null || onImageDownloadListener.getContext() == null) {
                                return null;
                            }
                            FutureTarget<File> future = Glide.with(onImageDownloadListener.getContext())
                                    .load(url)
                                    .downloadOnly(WIDTH_DOWNLOAD, WIDTH_DOWNLOAD);
                            try {
                                File cacheFile = future.get();
                                String cacheFilePath = cacheFile.getAbsolutePath();
                                File photo;
                                photo = ImageUtils.writeImageToTkpdPath(cacheFilePath);
                                if (photo != null) {
                                    return photo;
                                }
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage());
                            }
                            return null;
                        } else {
                            return new File(url);
                        }
                    }
                });
    }

}
