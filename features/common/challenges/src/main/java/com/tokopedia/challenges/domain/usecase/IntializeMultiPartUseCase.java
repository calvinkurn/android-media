package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class IntializeMultiPartUseCase extends RestRequestSupportInterceptorUseCase {

    private static final String FILE_NAME = "file_name";
    private static final String FILE_SIZE = "file_size";
    private static final String IMAGE_TITLE = "title";
    private static final String IMAGE_DESCRIPTION = "description";
    private static final String BLOB_DATA = "blob_data";
    HashMap<String, Object> requestBodyMap = new HashMap<>();


    @Inject
    public IntializeMultiPartUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }
    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.PRIVATE.CHALLENGE_INTIALIZE_MULTIPART, UploadFingerprints.class)
                .setRequestType(RequestType.POST_MULTIPART).setBody(requestBodyMap) .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }

     protected RequestBody generateRequestPlainTextBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"),
                value);
    }
    protected RequestBody generateRequestBlobBody(byte[] value) {
        return RequestBody.create(MediaType.parse("text/plain"),
                value);
    }

   private RequestBody generateImageRequestBody(String path) {
        File file = new File(path);
        return RequestBody.create(MediaType.parse("images/*"), file);
    }


    private MultipartBody.Part generateRequestImages(String name, String path) {
        File file = new File(path);
        RequestBody requestBody = generateImageRequestBody(path);
        return MultipartBody.Part.createFormData(name, file.getName(), requestBody);
    }

    private MultipartBody.Part generateRequestVideo(String name, String path) {

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/wav"), file);

        return MultipartBody.Part.createFormData(name, file.getName(), requestBody);

    }

    public void generateRequestParams(String title, String description, String imagePath) {
       requestBodyMap.put(IMAGE_TITLE, generateRequestPlainTextBody(title));
        requestBodyMap.put(IMAGE_DESCRIPTION, generateRequestPlainTextBody(description));
        File imageFile = new File(imagePath);
        requestBodyMap.put(FILE_NAME, generateRequestPlainTextBody(imageFile.getName()));
        requestBodyMap.put(FILE_SIZE, generateRequestPlainTextBody(String.valueOf(imageFile.length())));
        requestBodyMap.put(BLOB_DATA, generateRequestBlobBody(get10KBFile(imagePath)));
        /* requestBodyMap.put(IMAGE_TITLE, title);
        requestBodyMap.put(IMAGE_DESCRIPTION, description);
        File imageFile = new File(imagePath);
        requestBodyMap.put(FILE_NAME, imageFile.getName());
        requestBodyMap.put(FILE_SIZE, String.valueOf(imageFile.length()));
        requestBodyMap.put(BLOB_DATA, get10KBFile(imagePath));*/
    }
    private static  int KB_1 = 1024;
    private static  int KB_10 = 10 * KB_1;
    private static int MB_1 = 1000 * KB_1;
    private static int MB_10 = 10 * MB_1;


    public byte[] get10KBFile(String path)  {
        File file = new File(path);
        //init array with file length
        byte[] bytesArray = new byte[KB_10];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }

    public byte[] sliceFile(String path,int start,int end) throws IOException {
        File file = new File(path);
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();

        return Arrays.copyOfRange(bytesArray, start, end);

    }

}
