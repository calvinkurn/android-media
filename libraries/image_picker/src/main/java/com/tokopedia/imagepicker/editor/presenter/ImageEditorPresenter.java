package com.tokopedia.imagepicker.editor.presenter;

import android.content.Context;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.common.exception.FileSizeAboveMaximumException;
import com.tokopedia.imagepicker.common.util.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ImageEditorPresenter extends BaseDaggerPresenter<ImageEditorPresenter.ImageEditorView> {

    private CompositeSubscription compositeSubscription;

    public interface ImageEditorView extends CustomerView {
        Context getContext();

        void onErrorDownloadImageToLocal(Throwable e);

        void onSuccessDownloadImageToLocal(ArrayList<String> localPaths);

        void onErrorCropImageToRatio(Throwable e);

        void onSuccessCropImageToRatio(ArrayList<String> cropppedImagePaths, ArrayList<Boolean> isEditted);

        void onErrorResizeImage(Throwable e);

        void onSuccessResizeImage(ArrayList<String> resultPaths);
    }

    public void detachView() {
        super.detachView();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
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

    public void cropBitmapToExpectedRatio(final List<String> localImagePaths, final int ratioX, final int ratioY) {
        if (ratioX <= 0 || ratioY <= 0 ) {
            getView().onSuccessCropImageToRatio((ArrayList<String>) localImagePaths, new ArrayList<Boolean>(localImagePaths.size()));
        }
        Subscription subscription =
                Observable.from(localImagePaths)
                        .concatMap(new Func1<String, Observable<String>>() {
                            @Override
                            public Observable<String> call(String imagePath) {
                                System.gc();
                                // if the dimension is not expected dimension, crop it
                                float expectedRatio = (float) ratioX / ratioY;
                                int[] widthHeight = ImageUtils.getWidthAndHeight(imagePath);
                                int defaultOrientation;
                                try {
                                    defaultOrientation = ImageUtils.getOrientation(imagePath);
                                } catch (IOException e) {
                                    defaultOrientation = ExifInterface.ORIENTATION_NORMAL;
                                }
                                int width;
                                int height;
                                if (defaultOrientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                                        defaultOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                                    width = widthHeight[1];
                                    height = widthHeight[0];
                                } else {
                                    width = widthHeight[0];
                                    height = widthHeight[1];
                                }
                                float currentRatio = (float) width / height;
                                if (expectedRatio == currentRatio) {
                                    return Observable.just(imagePath);
                                } else {
                                    String outputPath;
                                    outputPath = ImageUtils.trimBitmap(imagePath, expectedRatio, currentRatio, true);
                                    return Observable.just(outputPath);
                                }
                            }
                        })
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<List<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().onErrorCropImageToRatio(e);
                                }
                            }

                            @Override
                            public void onNext(List<String> croppedImagedPath) {
                                if (isViewAttached()) {
                                    ArrayList<String> resultLocalPaths = new ArrayList<>();
                                    ArrayList<Boolean> isEdittedList = new ArrayList<>();
                                    for (int i = 0, sizei = croppedImagedPath.size(); i < sizei; i++) {
                                        String result = croppedImagedPath.get(i);
                                        resultLocalPaths.add(result);
                                        if (result.equals(localImagePaths.get(i))) {
                                            isEdittedList.add(false);
                                        } else {
                                            isEdittedList.add(true);
                                        }
                                    }
                                    getView().onSuccessCropImageToRatio(resultLocalPaths, isEdittedList);
                                }
                            }
                        });

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

}
