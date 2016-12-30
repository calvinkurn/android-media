package com.tokopedia.core.myproduct.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.myproduct.api.UploadImageProduct;
import com.tokopedia.core.myproduct.model.GenerateHostModel;
import com.tokopedia.core.myproduct.model.UploadProductImageData;
import com.tokopedia.core.newgallery.presenter.ImageGalleryImpl;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.normansyah on 18/12/2015.
 * still using old network handler, just to make sure retrofit 2 upload work
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class UploadPhotoTask extends AsyncTask<byte[], String, String> {
    public static final String NAME = "name";
    public static final String NEW_ADD = "new_add";
    public static final String PRODUCT_ID = "product_id";
    public static final String SERVER_ID = "server_id";
    public static final String TOKEN = "token";
    public static final String TOKEN_DEFAULT_VALUE = "";
    public static final String PRODUCT_ID_DEFAULT_VALUE = "";
    public static final String GOLANG = "2";
    public static final String NAME_DEFAULT_IMAGE = "sembarangan aja";
    public static final String TAG = "MNORMANSYAH";
    public static final String messageTAG = UploadPhotoTask.class.getSimpleName() + " : ";
    String path;
    GenerateHostModel.GenerateHost generateHost;
    String token;
    String userId;
    String deviceId;

    public UploadPhotoTask(String userId, String deviceId, GenerateHostModel.GenerateHost generateHost, String path, String token) {
        this(generateHost, path, token);
        this.userId = userId;
        this.deviceId = deviceId;
    }

    @Deprecated
    public UploadPhotoTask(GenerateHostModel.GenerateHost generateHost, String path, String token) {
        this.path = path;
        this.generateHost = generateHost;
        this.token = token;
    }

    @Override
    protected String doInBackground(byte[]... jpeg) {
        Log.d(TAG, messageTAG + FileUtils.getPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath(), "image", "jpg"));
//        File photo=new File(Environment.getExternalStorageDirectory(), "image.jpg");// old
        File photo = writeImageToTkpdPath(jpeg[0]);

        //[START] save to db for images
        ImageGalleryImpl.Pair<Integer, Integer> resolution = null;
        try {
            resolution = MetadataUtil.getWidthFromImage(photo.getAbsolutePath());
            int width = resolution.getModel1();
            int height = resolution.getModel2();
            PictureDB pictureDB = new PictureDB(path, width, height);
            pictureDB.save();
            Log.d(TAG, messageTAG + " hasil save ke db : " + pictureDB.Id);
        } catch (MetadataUtil.UnSupportedimageFormatException e) {
            e.printStackTrace();
        }
        //[END] save to db for images

        return photo.getAbsolutePath();
    }

    public static File writeImageToTkpdPath(InputStream source) {
        OutputStream outStream = null;
        File dest = null;
        try {

            File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            dest = new File(directory.getAbsolutePath() + "/image.jpg");

            outStream = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = source.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            source.close();
            outStream.close();

            Log.d(TAG, "File is copied successful!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;
    }

    public static File writeImageToTkpdPath(File source) {
        InputStream inStream = null;
        OutputStream outStream = null;
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

            Log.d(TAG, "File is copied successful!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;
    }

    public static File writeImageToTkpdPath(byte[] buffer) {
        if (buffer != null) {
            File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File photo = new File(directory.getAbsolutePath() + "/image.jpg");

            if (photo.exists()) {
                photo.delete();
            }

            try {
                FileOutputStream fos = new FileOutputStream(photo.getPath());

                fos.write(buffer);
                fos.close();
            } catch (java.io.IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
                return null;
            }
            return photo;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        File file = new File(s);// fileLoc[i]

        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, userId, deviceId,
                "https://" + generateHost.getUploadHost() + "/web-service/v4/action/upload-image/upload_product_image.pl", false)
                .setIdentity()
                .addParam(NAME, NAME_DEFAULT_IMAGE)
                .addParam(NEW_ADD, GOLANG)
                .addParam(PRODUCT_ID, PRODUCT_ID_DEFAULT_VALUE)
                .addParam(SERVER_ID, generateHost.getServerId())
                .addParam(TOKEN, TOKEN_DEFAULT_VALUE)
                .compileAllParam()
                .finish();

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NAME));
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NEW_ADD));
        RequestBody productId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(PRODUCT_ID));
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(SERVER_ID));
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(TOKEN));

        RetrofitUtils.createRetrofit("https://" + generateHost.getUploadHost() + "/web-service/v4/action/upload-image/upload_product_image.pl/")
                .create(UploadImageProduct.class)
                .uploadProductV4(
                        "https://" + generateHost.getUploadHost() + "/web-service/v4/action/upload-image/upload_product_image.pl/",
                        networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                        networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                        networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                        networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                        userId,
                        deviceId,
                        hash,
                        deviceTime,
                        fileToUpload,
                        name,
                        newAdd,
                        productId,
                        token,
                        serverId
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<UploadProductImageData>() {
                            @Override
                            public void onCompleted() {
                                Log.d("MNORMANSYAH", "success upload image!!!");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("MNORMANSYAH", "Hassil Upload Retrofit : " + e.getLocalizedMessage());
                            }

                            @Override
                            public void onNext(UploadProductImageData uploadProductImageData) {
                                Log.d("MNORMANSYAH", "Hasil Upload Retrofit " + uploadProductImageData.toString() + "");
                            }
                        }
                );


        //[START] Old ws-new
        // MediaType.parse("application/octet-stream"), ba);
//        RequestBody fbody = RequestBody.create(MediaType.parse("image/jpg"), file);
//        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), generateHost.getServerId());
//        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), token);
//        Retrofit retrofit = RetrofitUtils.createRetrofit(generateHost.getUploadHost());
//        retrofit
//                .create(UploadImageProduct.class)
//                .uploadProduct(
//                        generateHost.getUploadHost(),
//                        fbody,
//                        serverId,
//                        userId
//                ).enqueue(new Callback<UploadProductImageData>() {
//            @Override
//            public void onResponse(Response<UploadProductImageData> response, Retrofit retrofit) {
//                Log.d("MNORMANSYAH", "Hasil Upload Retrofit " + response.body() + "");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e("MNORMANSYAH", "Hassil Upload Retrofit : " + t.getLocalizedMessage());
//            }
//        });
        //[END] Old ws-new
    }
}
