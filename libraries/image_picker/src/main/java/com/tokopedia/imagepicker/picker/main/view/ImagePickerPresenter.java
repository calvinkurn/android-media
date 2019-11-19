package com.tokopedia.imagepicker.picker.main.view;

import android.content.Context;
import androidx.annotation.NonNull;
import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.common.exception.FileSizeAboveMaximumException;
import com.tokopedia.imagepicker.common.util.ImageUtils;

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

    }

    public void detachView() {
        super.detachView();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    public void resizeImage(List<String> imagePath, final long maxFileSize) {

        Subscription subscription = Observable.from(imagePath)
                .concatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String path) {

                        if (ImageUtils.getFileSizeInKb(path) > maxFileSize) {
                            if (ImageUtils.isImageType(getView().getContext(), path)) {
                                //resize image
                                String pathResult = ImageUtils.resizeBitmap(path, ImageUtils.DEF_WIDTH, ImageUtils.DEF_HEIGHT,
                                        true, ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_EDIT_RESULT);
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
                                    .downloadOnly(ImageUtils.DEF_WIDTH, ImageUtils.DEF_HEIGHT);
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
