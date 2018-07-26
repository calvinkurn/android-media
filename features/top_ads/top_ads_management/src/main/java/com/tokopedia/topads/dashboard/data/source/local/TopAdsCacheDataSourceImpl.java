package com.tokopedia.topads.dashboard.data.source.local;

import android.content.Context;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class TopAdsCacheDataSourceImpl implements TopAdsCacheDataSource {

    private static final String REQUEST_DATE_FORMAT = TopAdsConstant.REQUEST_DATE_FORMAT;
    private static final String LOCAL_CACHE_NAME = "top_ads_cache";

    private static final String PARAM_START_DATE = "PARAM_START_DATE";
    private static final String PARAM_END_DATE = "PARAM_END_DATE";
    private static final String PARAM_LAST_SELCTION_DATE_PICKER_TYPE = "PARAM_LAST_SELCTION_DATE_PICKER_TYPE";
    private static final String PARAM_LAST_SELCTION_DATE_PICKER_INDEX = "PARAM_LAST_SELCTION_DATE_PICKER_INDEX";

    private LocalCacheHandler localCacheHandler;

    public TopAdsCacheDataSourceImpl(Context context) {
        localCacheHandler = new LocalCacheHandler(context, LOCAL_CACHE_NAME);
    }

    @Override
    public void resetDate() {
        localCacheHandler.putString(PARAM_START_DATE, null);
        localCacheHandler.putString(PARAM_END_DATE, null);
        localCacheHandler.putInt(PARAM_LAST_SELCTION_DATE_PICKER_TYPE, 0);
        localCacheHandler.putInt(PARAM_LAST_SELCTION_DATE_PICKER_INDEX, 2);
        localCacheHandler.applyEditor();
    }

    @Override
    public void saveDate(Date startDate, Date endDate) {
        if (startDate != null) {
            localCacheHandler.putString(PARAM_START_DATE, new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
            localCacheHandler.applyEditor();
        }
        if (endDate != null) {
            localCacheHandler.putString(PARAM_END_DATE, new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
            localCacheHandler.applyEditor();
        }
    }

    @Override
    public Date getStartDate(Date defaultDate) {
        Date date = defaultDate;
        try {
            String tempDate = localCacheHandler.getString(PARAM_START_DATE);
            if (!TextUtils.isEmpty(tempDate)) {
                date = new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).parse(tempDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public Date getEndDate(Date defaultDate) {
        Date date = defaultDate;
        try {
            String tempDate = localCacheHandler.getString(PARAM_END_DATE);
            if (!TextUtils.isEmpty(tempDate)) {
                date = new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).parse(tempDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void updateLastInsertStatistic() {
        localCacheHandler.setExpire(TopAdsConstant.CACHE_EXPIRED_TIME);
        localCacheHandler.applyEditor();
    }

    @Override
    public boolean isStatisticDataExpired() {
        return localCacheHandler.isExpired();
    }

    @Override
    public void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex) {
        localCacheHandler.putInt(PARAM_LAST_SELCTION_DATE_PICKER_TYPE, selectionDatePickerType);
        localCacheHandler.putInt(PARAM_LAST_SELCTION_DATE_PICKER_INDEX, selectionDatePeriodIndex);
        localCacheHandler.applyEditor();
    }

    @Override
    public int getLastSelectionDatePickerType() {
        return localCacheHandler.getInt(PARAM_LAST_SELCTION_DATE_PICKER_TYPE);
    }

    @Override
    public int getLastSelectionDatePickerIndex() {
        return localCacheHandler.getInt(PARAM_LAST_SELCTION_DATE_PICKER_INDEX);
    }
}