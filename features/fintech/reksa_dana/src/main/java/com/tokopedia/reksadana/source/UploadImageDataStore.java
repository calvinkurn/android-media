package com.tokopedia.reksadana.source;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.reksadana.source.api.UploadImageApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

public class UploadImageDataStore {
    private UploadImageApi uploadImageApi;
    public UploadImageDataStore(UploadImageApi uploadImageApi) {
        this.uploadImageApi = uploadImageApi;

    }

    public Observable<ResponseBody> uploadImage(String  url, String filePath) {
        File file = new File(filePath);

        InputStream in = null;
        byte[] buf = {};
        try {
            in = new FileInputStream(file);
            buf = new byte[in.available()];
            while (in.read(buf) != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), buf);

        CommonUtils.dumper("in cloud ="+url);
        /*try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return uploadImageApi.postImage(url, reqFile);
    }
}
