package com.tokopedia.core.newgallery.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.myproduct.presenter.ImageGallery;
import com.tokopedia.core.util.Pair;

import java.util.ArrayList;
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
 */
public class ImageGalleryImpl implements ImageGallery {
    public static final String FILE_PATH = "file://";
    ImageGalleryView imageGalleryView;
    public ImageGalleryImpl(ImageGalleryView imageGalleryView){
        this.imageGalleryView = imageGalleryView;
    }

    @Override
    public List<FolderModel> fetchImageUsingDb(Context context, final String folderName) {
        final List<FolderModel> result = new ArrayList<>();
        Observable.just(new Pair<Context, String>(context, folderName))
                .map(new Func1<Pair<Context, String>, List<FolderModel>>() {
                    @Override
                    public List<FolderModel> call(Pair<Context, String> pair) {
                        ArrayList<FolderModel> datas = new ArrayList<FolderModel>();
                        String[] projection = new String[]{MediaStore.Images.Media._ID,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_TAKEN,
                                MediaStore.Images.Media.DATA};
                        String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";
                        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        // Cursor cur = managedQuery(images,
                        // projection, // Which columns to return
                        // BUCKET_GROUP_BY, // Which rows to return (all rows)
                        // null, // Selection arguments (none)
                        // BUCKET_ORDER_BY // Ordering
                        // );

                        Cursor cur = pair.getModel1().getContentResolver().query(images, projection,
                                null, null, BUCKET_ORDER_BY);

                        if (cur.moveToFirst()) {
                            Map<String, List<String>> data = new HashMap<String, List<String>>();
                            int bucketColumn = cur
                                    .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
                            do {
                                // Get the field values
                                String bucketName = cur.getString(bucketColumn);
                                String path = cur.getString(dataColumn);
                                // Do something with the values.
//                                Log.d(TAG, messageTAG + bucketName + " : " + path);
                                List<String> value;
                                if(data.get(bucketName)==null){
                                    value = new ArrayList<String>() {};
                                }else{
                                    value = data.get(bucketName);
                                }
                                value.add(path);// FILE_PATH +
                                data.put(bucketName, value);
                            } while (cur.moveToNext());
                            for(Iterator<Map.Entry<String, List<String>>> iterator = data.entrySet().iterator();iterator.hasNext();){
                                Map.Entry<String, List<String>> entry = iterator.next();
                                datas.add(new FolderModel(entry.getKey(), entry.getValue()));
                            }
                        }

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
                        if(imageGalleryView!=null){
                            imageGalleryView.loadData(folderModels);
                        }
                    }
                })
        ;


        return result;
    }

    @Override
    public List<FolderModel> fetchImageUsingDb(Context context) {
        return fetchImageUsingDb(context,"");
    }
}
