package com.tokopedia.contactus.inboxticket2.view.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.BackgroundColorSpan;
import android.util.TypedValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    private Context mContext;
    private Locale mLocale;

    public String UPLOAD_URL = "https://u12.tokopedia.net";
    public String CLOSED = "closed";
    public String OPEN = "open";
    public String SOLVED = "solved";

    public Utils(Context context) {
        mContext = context;
    }

    public boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    private List<Integer> findOccurrences(String what, String src) {
        ArrayList<Integer> occurences = new ArrayList<>();
        String regex = "\\b" + what + "\\w*";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            occurences.add(matcher.start());
        }
        return occurences;
    }

    public SpannableString getHighlightText(String what, String src) {
        SpannableString spannableString = new SpannableString(src);
        if (what.length() > 0) {
            List<Integer> occurences = findOccurrences(what, src);
            for (Integer start : occurences) {
                BackgroundColorSpan styleSpan = new BackgroundColorSpan(Color.parseColor("#ecdb77"));
                spannableString.setSpan(styleSpan, start, start + what.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    public SpannableString getStatusTitle(String src, int background, int textColor, int textSizeSp) {
        SpannableString spannableString = new SpannableString(src);
        int start = src.lastIndexOf(".") + 4;
        RoundedBackgroundSpan roundedBackgroundSpan = new RoundedBackgroundSpan(background, textColor, convertSpToPx(textSizeSp),
                convertDpToPx(8), convertDpToPx(4), convertDpToPx(2), convertDpToPx(2));
        spannableString.setSpan(roundedBackgroundSpan, start, src.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public String getDateTime(String isoTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", getLocale());
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM 'pukul' HH:mm", getLocale());
        dateFormat.setTimeZone(TimeZone.getDefault());
        try {
            Date date = inputFormat.parse(isoTime);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

    }

    public String getDateTimeCurrent() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM 'pukul' HH:mm", getLocale());
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }

    public String getDateTimeYear(String isoTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", getLocale());
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", getLocale());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm 'WIB'", getLocale());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        try {
            Date date = inputFormat.parse(isoTime);
            if (DateUtils.isToday(date.getTime()))
                return timeFormat.format(date);
            else if (isYesterday(date.getTime())) {
                return "Kemarin";
            } else {
                return dateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

    }

    private float convertDpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    private float convertSpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, mContext.getResources().getDisplayMetrics());
    }

    private Locale getLocale() {
        if (mLocale == null)
            mLocale = new Locale("in", "ID", "");
        return mLocale;
    }

    private boolean isYesterday(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay - 1);
    }
}

