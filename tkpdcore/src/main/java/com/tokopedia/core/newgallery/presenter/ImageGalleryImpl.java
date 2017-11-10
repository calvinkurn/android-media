package com.tokopedia.core.newgallery.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

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
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayList<ImageModel> dataAlbum = new ArrayList<>();
    private ArrayList<ImageModel> dataListPhoto = new ArrayList<>();

    public ImageGalleryImpl(ImageGalleryView imageGalleryView){
        this.imageGalleryView = imageGalleryView;
        if (imageGalleryView.getContentResolver() == null) {
            throw new RuntimeException("this should be implemented to Activity or Fragment !!");
        }
        contentResolver = imageGalleryView.getContentResolver();
    }

    @Override
    public void getItemAlbum() {
        new GetItemAlbum().execute();
    }

    public String getFolderPath(int position) {
        return dataAlbum.get(position).getPathFolder();
    }

    public void getItemListAlbum(int position) {
        new GetItemListAlbum(getFolderPath(position)).execute();
    }

    @Override
    public void getItemListAlbum(String folderPath) {
        new GetItemListAlbum(folderPath).execute();
    }

    private boolean Check(String a, ArrayList<String> list) {
        return !list.isEmpty() && list.contains(a);
    }

    private boolean checkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return true;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        boolean isCheck = false;
        for (int k = 0; k < Constants.FORMAT_IMAGE.size(); k++) {
            if (name.endsWith(Constants.FORMAT_IMAGE.get(k))) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
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
                            Map<String, List<String>> data = new HashMap<String, List<String>>();
                            int bucketColumn = cur
                                    .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
                            do {
                                String bucketName = cur.getString(bucketColumn);
                                String path = cur.getString(dataColumn);
                                List<String> value;
                                if(data.get(bucketName)==null){
                                    value = new ArrayList<String>() {};
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
                });


        return result;
    }

    @Override
    public List<FolderModel> fetchImageUsingDb() {
        return fetchImageUsingDb("");
    }

    private class GetItemAlbum extends AsyncTask<Void, Void, String> {
        private GetItemAlbum() {
        }

        protected String doInBackground(Void... params) {
            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, null, null, null);
            if (cursor != null) {
                int column_index_data = cursor.getColumnIndexOrThrow("_data");
                while (cursor.moveToNext()) {
                    String pathFile = cursor.getString(column_index_data);
                    File file = new File(pathFile);
                    if (file.exists()) {
                        boolean check = ImageGalleryImpl.this.checkFile(file);
                        if (!ImageGalleryImpl.this.Check(file.getParent(), ImageGalleryImpl.this.pathList) && check) {
                            ImageGalleryImpl.this.pathList.add(file.getParent());
                            ImageGalleryImpl.this.dataAlbum.add(new ImageModel(file.getParentFile().getName(), pathFile, file.getParent()));
                        }
                    }
                }
                cursor.close();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            if (imageGalleryView != null) {
                imageGalleryView.retrieveData(dataAlbum, pathList);
            }
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetItemListAlbum extends AsyncTask<Void, Void, String> {
        String pathAlbum;

        GetItemListAlbum(String pathAlbum) {
            this.pathAlbum = pathAlbum;
        }

        protected String doInBackground(Void... params) {
            File file = new File(this.pathAlbum);
            if (file.isDirectory()) {
                for (File fileTmp : file.listFiles()) {
                    if (fileTmp.exists()) {
                        boolean check = ImageGalleryImpl.this.checkFile(fileTmp);
                        if (!fileTmp.isDirectory() && check) {
                            ImageGalleryImpl.this.dataListPhoto.add(new ImageModel(fileTmp.getName(), fileTmp.getAbsolutePath(), fileTmp.getAbsolutePath()));
                            publishProgress();
                        }
                    }
                }
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                Collections.sort(ImageGalleryImpl.this.dataListPhoto, new Comparator<ImageModel>() {
                    @Override
                    public int compare(ImageModel item, ImageModel t1) {
                        File fileI = new File(item.getPathFolder());
                        File fileJ = new File(t1.getPathFolder());
                        if (fileI.lastModified() > fileJ.lastModified()) {
                            return -1;
                        }
                        if (fileI.lastModified() < fileJ.lastModified()) {
                            return 1;
                        }
                        return 0;
                    }
                });
            } catch (Exception e) {
            }

            if (imageGalleryView != null) {
                imageGalleryView.retrieveItemData(dataListPhoto, pathList);
            }
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }
}
