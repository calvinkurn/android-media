package com.tokopedia.topchat.attachinvoice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Hendri on 28/03/18.
 */

public class GetInvoicePostRequest {

    public static final String START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @SerializedName("message_id")
    @Expose
    int messageId;
    @SerializedName("user_id")
    @Expose
    int userId;
    @SerializedName("show_all")
    @Expose
    boolean isShowAll;
    @SerializedName("page")
    @Expose
    int page;
    @SerializedName("limit")
    @Expose
    int limit;
    @SerializedName("start_time")
    @Expose
    String startTime;

    public GetInvoicePostRequest(int messageId, int userId, boolean isShowAll, int page, int
            limit) {
        this.messageId = messageId;
        this.userId = userId;
        this.isShowAll = isShowAll;
        this.page = page;
        this.limit = limit;
        this.startTime = generateStartTime();
    }

    public int getMessageId() {
        return messageId;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isShowAll() {
        return isShowAll;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public String getStartTime() {
        return startTime;
    }

    public String generateStartTime() {
        SimpleDateFormat date = new SimpleDateFormat(
                START_TIME_FORMAT, Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        return date.format(Calendar.getInstance().getTime());
    }
}
