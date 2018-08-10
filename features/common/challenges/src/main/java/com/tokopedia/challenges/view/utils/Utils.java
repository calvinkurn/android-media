package com.tokopedia.challenges.view.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class Utils {
    private static Utils singleInstance;
    public static final String QUERY_PARAM_CHALLENGE_ID="challenge-id";
    public static final String QUERY_PARAM_KEY_START="start";
    public static final String QUERY_PARAM_KEY_SIZE="size";
    public static final String QUERY_PARAM_KEY_SORT="sort";
    public static final String QUERY_PARAM_KEY_SORT_RECENT="recent";
    public static final String QUERY_PARAM_KEY_SORT_POINTS="points";

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
    }

    public static String convertUTCToString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date d = null;
        String formattedTime=null;
        try {
            d = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("d MM yyyy", new Locale("in", "ID", ""));
            sdf2.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            formattedTime = sdf2.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return " "+formattedTime;
    }

    public static RequestBody generateRequestPlainTextBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"),
                value);
    }
    public static RequestBody generateRequestBlobBody(byte[] value) {
        return RequestBody.create(MediaType.parse("text/plain"),
                value);
    }

    public static RequestBody generateImageRequestBody(String path) {
        File file = new File(path);
        return RequestBody.create(MediaType.parse("images/*"), file);
    }


    public static MultipartBody.Part generateRequestImages(String name, String path) {
        File file = new File(path);
        RequestBody requestBody = generateImageRequestBody(path);
        return MultipartBody.Part.createFormData(name, file.getName(), requestBody);
    }

    public static MultipartBody.Part generateRequestVideo(String name, String path) {

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/wav"), file);

        return MultipartBody.Part.createFormData(name, file.getName(), requestBody);

    }

    private static  int KB_1 = 1024;
    private static  int KB_10 = 10 * KB_1;
    private static int MB_1 = 1000 * KB_1;
    private static int MB_10 = 10 * MB_1;


    public static byte[] get10KBFile(String path)  {
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

    public static byte[] sliceFile(String path,int start,int end) {
        File file = new File(path);
        byte[] bytesArray = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Arrays.copyOfRange(bytesArray, start, end);

    }

}
