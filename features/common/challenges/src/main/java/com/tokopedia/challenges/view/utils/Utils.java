package com.tokopedia.challenges.view.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.challengesubmission.Awards;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class Utils {
    private static Utils singleInstance;
    public static final String QUERY_PARAM_CHALLENGE_ID = "challenge-id";
    public static final String QUERY_PARAM_SUBMISSION_ID = "submission-id";
    public static final String QUERY_PARAM_KEY_START = "start";
    public static final String QUERY_PARAM_KEY_SIZE = "size";
    public static final String QUERY_PARAM_KEY_SORT = "sort";
    public static final String QUERY_PARAM_KEY_SORT_RECENT = "recent";
    public static final String QUERY_PARAM_KEY_SORT_POINTS = "points";
    public static final String QUERY_PARAM_IS_PAST_CHALLENGE = "isPastChallenge";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_REJECTED = "Rejected";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_PARTICIPATED = "Participated";
    public static final String STATUS_DECLINED = "Declined";
    public static final String STATUS_WAITING = "Waiting";
    public static boolean FROMNOCACHE;


    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
    }

    public static String convertUTCToString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Date d = null;
        String formattedTime = null;
        try {
            d = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("d MMMM yyyy", new Locale("in", "ID", ""));
            sdf2.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            formattedTime = sdf2.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return " " + formattedTime;
    }

    public static long convertUTCToMillis(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d != null)
            return d.getTime();
        else
            return -1L;
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
        return RequestBody.create(MediaType.parse("image/*"), file);
    }


    public static RequestBody generateImageRequestBodySlice(String path, int start, int end) {
        return RequestBody.create(MediaType.parse("image/*"), sliceFile(path, start, end));
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

    public static int KB_1 = 1024;
    public static int KB_10 = 10 * KB_1;
    public static int MB_1 = 1000 * KB_1;
    public static int MB_10 = 10 * MB_1;


    public static byte[] get10KBFile(String path) {
        return sliceFile(path, 0, KB_10);
    }

    public static byte[] sliceFile(String path, int start, int end) {
        File file = new File(path);
        int upperBound = end > file.length() ? (int) file.length() : end;
        byte[] bytesArray = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray);
            //read file into bytes[]
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Arrays.copyOfRange(bytesArray, start, upperBound);

    }

    public static String getApplinkPathForBranch(String url, String id) {
        if (url != null && id != null) {
            url = url.replace(Constants.Schemes.APPLINKS + "://", "");
            url = url.replaceFirst("\\{.*?\\} ?", id == null ? "" : id);
        }
        return url;
    }

    public static String getApplinkPathWithPrefix(String url, String id) {
        if (url != null && id != null) {
            url = url.replaceFirst("\\{.*?\\} ?", id == null ? "" : id);
        }
        return url;
    }

    public static int getWinnerPosition(List<Awards> awards) {
        int position;
        int finalPosition = Integer.MAX_VALUE;
        if (awards != null) {
            for (Awards award : awards) {
                if (award != null) {
                    if (!TextUtils.isEmpty(award.getType())) {
                        if (award.getType().equalsIgnoreCase("JudgesPick")) {
                            finalPosition = 1;
                            break;
                        } else if (award.getType().equalsIgnoreCase("FirstPlace")) {
                            position = 2;
                            if (finalPosition > position)
                                finalPosition = position;

                        } else if (award.getType().equalsIgnoreCase("SecondPlace")) {
                            position = 3;
                            if (finalPosition > position)
                                finalPosition = position;
                        }
                    }
                }
            }
        }
        if (finalPosition == Integer.MAX_VALUE)
            return -1;
        else
            return finalPosition;
    }

    public static void setTextViewBackground(Context context, TextView tvStatus, String status) {
        tvStatus.setText(status);
        if (STATUS_APPROVED.equalsIgnoreCase(status) || STATUS_PARTICIPATED.equalsIgnoreCase(status)) {
            if (STATUS_PARTICIPATED.equalsIgnoreCase(status)) {
                tvStatus.setText(context.getResources().getString(R.string.participated));
            } else {
                tvStatus.setText(context.getResources().getString(R.string.approved));
            }
            tvStatus.setBackgroundResource(R.drawable.bg_round_solid_green_radius_huge);
            tvStatus.setTextColor(context.getResources().getColor(R.color.tkpd_main_green));
        } else if (STATUS_DECLINED.equalsIgnoreCase(status)) {
            tvStatus.setText(context.getResources().getString(R.string.rejected));
            tvStatus.setBackgroundResource(R.drawable.bg_round_solid_red_radius_huge);
            tvStatus.setTextColor(context.getResources().getColor(R.color.declined_red_textcolor));

        } else if (STATUS_WAITING.equalsIgnoreCase(status)) {
            tvStatus.setText(context.getResources().getString(R.string.pending));
            tvStatus.setBackgroundResource(R.drawable.bg_round_solid_yellow_radius_huge);
            tvStatus.setTextColor(context.getResources().getColor(R.color.pending_yellow_textcolor));
        } else if (STATUS_COMPLETED.equalsIgnoreCase(status)) {
            tvStatus.setText(context.getResources().getString(R.string.completed));
            tvStatus.setBackgroundResource(R.drawable.bg_round_solid_gray_radius_huge);
            tvStatus.setTextColor(context.getResources().getColor(R.color.black_38));
        }
    }

    public static String getImageUrl(String url) {
        return url + "?height=200";
    }

    public static String getImageUrlForSubmission(String url) {
        return url + "?height=200";
    }
}
