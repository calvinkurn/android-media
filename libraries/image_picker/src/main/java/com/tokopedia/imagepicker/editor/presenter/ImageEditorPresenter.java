package com.tokopedia.imagepicker.editor.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
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
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ImageEditorPresenter extends BaseDaggerPresenter<ImageEditorPresenter.ImageEditorView> {

    private CompositeSubscription compositeSubscription;

    public interface ImageEditorView extends CustomerView {
        Context getContext();

        void onErrorDownloadImageToLocal(Throwable e);

        void onSuccessDownloadImageToLocal(ArrayList<String> localPaths);

        void onErrorCropImageToRatio(Throwable e);

        void onSuccessCropImageToRatio(ArrayList<String> cropppedImagePaths);
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

    public void cropBitmapToExpectedRatio(List<String> oriImagePaths,
                                          List<String> step0ImagePaths,
                                          final List<String> localImagePaths, final int ratioX, final int ratioY) {
        Subscription subscription =
                Observable.zip(
                        Observable.from(oriImagePaths),
                        Observable.from(step0ImagePaths),
                        Observable.from(localImagePaths),
                        new Func3<String, String, String, String>() {
                            @Override
                            public String call(String oriImagePath, String step0, String imagePath) {
                                // if image paths is not step0, it has been processed by edit, so just return it
                                if (!imagePath.equals(step0)) {
                                    return imagePath;
                                }
                                //below is the step0 path
                                System.gc();
                                // if it is step0, need to check the dimension
                                // if the dimension is not expected dimension, crop it
                                // then delete the step0 file
                                float expectedRatio = (float) ratioX / ratioY;
                                int[] widthHeight = ImageUtils.getWidthAndHeight(imagePath);
                                int width = widthHeight[0];
                                int height = widthHeight[1];
                                float currentRatio = (float) width / height;
                                if (expectedRatio == currentRatio) {
                                    return imagePath;
                                } else {
                                    String outputPath;
                                    outputPath = trimBitmap(imagePath, expectedRatio, currentRatio);
                                    return outputPath;
                                }
                            }
                        }
                ).toList()
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
                                            getView().onErrorCropImageToRatio(e);
                                        }
                                    }

                                    @Override
                                    public void onNext(List<String> croppedImagedPath) {
                                        if (isViewAttached()) {
                                            ArrayList<String> resultLocalPaths = new ArrayList<>();
                                            for (int i = 0, sizei = croppedImagedPath.size(); i < sizei; i++) {
                                                resultLocalPaths.add(croppedImagedPath.get(i));
                                            }
                                            getView().onSuccessCropImageToRatio(resultLocalPaths);
                                        }
                                    }
                                }
                        );
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    private String trimBitmap(String imagePath, float expectedRatio, float currentRatio) {
        Bitmap bitmapToEdit = ImageUtils.getBitmapFromPath(imagePath, ImageUtils.DEF_WIDTH,
                ImageUtils.DEF_HEIGHT, false);
        int width = bitmapToEdit.getWidth();
        int height = bitmapToEdit.getHeight();
        int left = 0, right = width, top = 0, bottom = height;
        int expectedWidth = width, expectedHeight = height;
        if (expectedRatio < currentRatio) { // trim left and right
            expectedWidth = (int) (expectedRatio * height);
            left = ((width - expectedWidth) / 2);
            right = (left + expectedWidth);
        } else { // trim top and bottom
            expectedHeight = (int) (width / expectedRatio);
            top = ((height - expectedHeight) / 2);
            bottom = (top + expectedHeight);
        }

        boolean isPng = ImageUtils.isPng(imagePath);

        Bitmap outputBitmap;
        outputBitmap = Bitmap.createBitmap(expectedWidth, expectedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawBitmap(bitmapToEdit, new Rect(left, top, right, bottom),
                new Rect(0, 0, expectedWidth, expectedHeight), null);
        File file = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE,
                outputBitmap, isPng);
        bitmapToEdit.recycle();
        outputBitmap.recycle();

        System.gc();

        return file.getAbsolutePath();
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
