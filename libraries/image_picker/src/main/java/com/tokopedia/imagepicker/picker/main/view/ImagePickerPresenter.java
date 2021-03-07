package com.tokopedia.imagepicker.picker.main.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.common.exception.FileSizeAboveMaximumException;
import com.tokopedia.utils.file.FileUtil;
import com.tokopedia.utils.image.ImageProcessingUtil;

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

public class ImagePickerPresenter extends BaseDaggerPresenter<ImagePickerPresenter.ImagePickerView> {

    private CompositeSubscription compositeSubscription;

    public interface ImagePickerView extends CustomerView {
        Context getContext();

        void onErrorDownloadImageToLocal(Throwable e);

        void onSuccessDownloadImageToLocal(ArrayList<String> localPaths);

        void onErrorResizeImage(Throwable e);

        void onSuccessResizeImage(ArrayList<String> resultPaths);

        void onErrorConvertFormatImage(Throwable e);

        void onSuccessConvertFormatImage(ArrayList<String> resultPaths);

    }

    public void detachView() {
        super.detachView();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    public void resizeImage(List<String> imagePath, final long maxFileSize,
                            boolean recheckSizeAfterResize, boolean convertToWebp) {

        Subscription subscription = Observable.from(imagePath)
                .concatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String path) {
                        if (FileUtil.getFileSizeInKb(path) > maxFileSize) {
                            if (FileUtil.isImageType(getView().getContext(), path)) {
                                String pathResult = "";
                                //resize image
                                if (convertToWebp) {
                                    pathResult = ImageProcessingUtil.resizeBitmap(path,
                                            ImageProcessingUtil.DEF_WIDTH,
                                            ImageProcessingUtil.DEF_HEIGHT,
                                            true,
                                            Bitmap.CompressFormat.WEBP);
                                } else {
                                    pathResult = ImageProcessingUtil.resizeBitmap(path,
                                            ImageProcessingUtil.DEF_WIDTH,
                                            ImageProcessingUtil.DEF_HEIGHT,
                                            true,
                                            ImageProcessingUtil.getCompressFormat(path));
                                }

                                if (recheckSizeAfterResize && FileUtil.getFileSizeInKb(pathResult) > maxFileSize) {
                                    throw new FileSizeAboveMaximumException();
                                }
                                return Observable.just(pathResult);
                            } else {
                                throw new FileSizeAboveMaximumException();
                            }
                        } else {
                            return Observable.just(path);
                        }
                    }
                }).toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<List<String>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().onErrorResizeImage(e);
                                }
                            }

                            @Override
                            public void onNext(List<String> paths) {
                                ArrayList<String> resultLocalPaths = new ArrayList<>();
                                if (paths == null || paths.size() == 0) {
                                    throw new NullPointerException();
                                }
                                for (int i = 0, sizei = paths.size(); i < sizei; i++) {
                                    resultLocalPaths.add(paths.get(i));
                                }
                                if (isViewAttached()) {
                                    getView().onSuccessResizeImage(resultLocalPaths);
                                }
                            }
                        }
                );
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void convertFormatImage(List<String> imagePath, boolean convertToWebp) {

        Subscription subscription = Observable.from(imagePath)
                .concatMap((Func1<String, Observable<String>>) path -> {
                    String resultPath = path;
                    boolean isFileImage = FileUtil.isImageType(getView().getContext(), path);
                    boolean isWebp = ImageProcessingUtil.isWebp(path);

                    if (convertToWebp && isFileImage && !isWebp) {
                        resultPath = ImageProcessingUtil.resizeBitmap(path,
                                ImageProcessingUtil.DEF_WIDTH,
                                ImageProcessingUtil.DEF_HEIGHT,
                                true,
                                Bitmap.CompressFormat.WEBP);
                    }
                    return Observable.just(resultPath);
                }).toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<List<String>>() {
                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().onErrorConvertFormatImage(e);
                                }
                            }

                            @Override
                            public void onNext(List<String> paths) {
                                ArrayList<String> resultLocalPaths = new ArrayList<>();
                                if (paths == null || paths.size() == 0) {
                                    throw new NullPointerException();
                                }
                                for (int i = 0, sizei = paths.size(); i < sizei; i++) {
                                    resultLocalPaths.add(paths.get(i));
                                }
                                if (isViewAttached()) {
                                    getView().onSuccessConvertFormatImage(resultLocalPaths);
                                }
                            }
                        }
                );
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
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
                                if (isViewAttached()) {
                                    getView().onErrorDownloadImageToLocal(e);
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
                                if (isViewAttached()) {
                                    getView().onSuccessDownloadImageToLocal(resultLocalPaths);
                                }
                            }
                        }
                );
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
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
                            if (!isViewAttached()) {
                                return null;
                            }
                            FutureTarget<File> future = Glide.with(getView().getContext())
                                    .load(url)
                                    .downloadOnly(ImageProcessingUtil.DEF_WIDTH, ImageProcessingUtil.DEF_HEIGHT);
                            try {
                                return future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage());
                            }
                        } else {
                            return new File(url);
                        }
                    }
                });
    }

}
