package com.tokopedia.core.newgallery.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.nytimes.android.external.cache.Stopwatch;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.myproduct.presenter.ImageGallery;
import com.tokopedia.core.newgallery.constant.Constants;
import com.tokopedia.core.newgallery.model.ImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by m.normansyah on 03/12/2015.
 *
 * this class is implemented presenter.
 * updated using better flow {get necessary data only }
 */
public class ImageGalleryImpl implements ImageGallery {

    private final ContentResolver contentResolver;
    private ImageGalleryView imageGalleryView;
    private ArrayList<ImageModel> dataAlbum = new ArrayList<>();

    private Map<String, List<String>> data = new HashMap<>();
    private boolean isGetItemAlbum = false;
    private String tmpFolderPath = null;

    public ImageGalleryImpl(ImageGalleryView imageGalleryView){
        this.imageGalleryView = imageGalleryView;
        if (imageGalleryView.getContentResolver() == null) {
            throw new RuntimeException("this should be implemented to Activity or Fragment !!");
        }
        contentResolver = imageGalleryView.getContentResolver();
    }

    @Override
    public void getItemAlbum() {
        if (imageGalleryView != null && (dataAlbum.size()>0)) {
            imageGalleryView.retrieveData(dataAlbum);
        }else {
            fetchImageUsingDb();
        }
    }

    public String getFolderPath(int position) {
        String folderPath = "";
        if (dataAlbum != null && position >= 0 && position < dataAlbum.size()) {
            folderPath = dataAlbum.get(position).getName();
        }
        return folderPath;
    }

    @Override
    public void getItemListAlbum(String folderPath) {
        tmpFolderPath = folderPath;
        if (imageGalleryView != null) {
            List<String> pathFiles = data.get(folderPath);

            if(pathFiles == null){
                isGetItemAlbum = true;
                fetchImageUsingDb();
                return;
            }

            ArrayList<ImageModel> imageModels = new ArrayList<>();
            for (String pathFile : pathFiles) {
                imageModels.add(new ImageModel(null, pathFile, null));
            }
            imageGalleryView.retrieveItemData(imageModels);
        }
    }

    @Override
    public void detach() {
        imageGalleryView = null;
        data = null;
    }

    @Override
    public List<FolderModel> fetchImageUsingDb(final String folderName) {
        final List<FolderModel> result = new ArrayList<>();
        Observable.just(folderName)
                .map(new Func1<String, List<FolderModel>>() {
                    @Override
                    public List<FolderModel> call(String s) {
                        ArrayList<FolderModel> datas = new ArrayList<>();
                        String[] projection = new String[]{MediaStore.Images.Media._ID,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_TAKEN,
                                MediaStore.Images.Media.DATA};
                        String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";
                        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                        Cursor cur = contentResolver.query(images, projection,
                                null, null, BUCKET_ORDER_BY);

                        if (cur.moveToFirst()) {
                            data = new HashMap<>();
                            int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
                            do {
                                String bucketName = cur.getString(bucketColumn);
                                String path = cur.getString(dataColumn);
                                List<String> value;
                                if(data.get(bucketName)==null){
                                    value = new ArrayList<>();

                                    ImageGalleryImpl.this.dataAlbum.add(new ImageModel(bucketName, path,  new File(path).getParent()));
                                }else{
                                    value = data.get(bucketName);
                                }
                                value.add(path);
                                data.put(bucketName, value);
                            } while (cur.moveToNext());

                            for(Iterator<Map.Entry<String, List<String>>> iterator = data.entrySet().iterator();iterator.hasNext();){
                                Map.Entry<String, List<String>> entry = iterator.next();
                                datas.add(new FolderModel(entry.getKey(), entry.getValue()));
                            }
                        }

                        cur.close();

                        return datas;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<FolderModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<FolderModel> folderModels) {
                        result.addAll(folderModels);
                        if(isGetItemAlbum && tmpFolderPath != null && !tmpFolderPath.isEmpty()){
                            List<String> pathFiles = data.get(tmpFolderPath);
                            ArrayList<ImageModel> imageModels = new ArrayList<>();
                            for (String pathFile : pathFiles) {
                                imageModels.add(new ImageModel(null, pathFile, null));
                            }
                            imageGalleryView.retrieveItemData(imageModels);
                            isGetItemAlbum = false;
                            return;
                        }

                        // this to support old code
                        if(imageGalleryView!=null){
                            imageGalleryView.loadData(folderModels);
                        }

                        // this for new code
                        if (imageGalleryView != null && (dataAlbum.size()>0)) {
                            imageGalleryView.retrieveData(dataAlbum);
                        }
                    }
                });


        return result;
    }

    @Override
    public List<FolderModel> fetchImageUsingDb() {
        return fetchImageUsingDb("");
    }
}
