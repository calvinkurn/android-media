package com.tokopedia.travelcalendar.data;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.data.entity.HolidayEntity;
import com.tokopedia.travelcalendar.data.entity.HolidayResultEntity;
import com.tokopedia.travelcalendar.network.TravelCalendarApi;
import com.tokopedia.usecase.RequestParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarCloudDataStore implements TravelCalendarDataStore {

    public static final String KEY_CALENDAR_HOLIDAY = "calendar_holiday";
    private static final String QUERY_KEY = "query";
    private static final long DURATION_SAVE_TO_CACHE = TimeUnit.DAYS.toSeconds(7);

    private Context context;
    private TravelCalendarApi travelCalendarApi;
    private CacheManager cacheManager;

    public TravelCalendarCloudDataStore(CacheManager cacheManager, Context context, TravelCalendarApi travelCalendarApi) {
        this.context = context;
        this.travelCalendarApi = travelCalendarApi;
        this.cacheManager = cacheManager;
    }

    @Override
    public Observable<List<HolidayResultEntity>> getHolidayResults() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(QUERY_KEY, getRequestGetHolidayPayload());
        return travelCalendarApi.getHolidayEntity(requestParams.getParameters())
                .doOnNext(new Action1<GraphqlResponse<HolidayEntity>>() {
                    @Override
                    public void call(GraphqlResponse<HolidayEntity> holidayEntityGraphqlResponse) {
                        if (holidayEntityGraphqlResponse.getData() != null &&
                                holidayEntityGraphqlResponse.getData().getHolidayResultEntities() != null) {
                            String jsonString = CacheUtil.convertModelToString(holidayEntityGraphqlResponse.getData(),
                                    new TypeToken<HolidayEntity>() {}.getType());
                            cacheManager.save(KEY_CALENDAR_HOLIDAY, jsonString, DURATION_SAVE_TO_CACHE);
                        }
                    }
                })
                .map(new Func1<GraphqlResponse<HolidayEntity>, List<HolidayResultEntity>>() {
                    @Override
                    public List<HolidayResultEntity> call(GraphqlResponse<HolidayEntity> holidayEntityGraphqlResponse) {
                        return holidayEntityGraphqlResponse.getData().getHolidayResultEntities();
                    }
                });
    }

    private String getRequestGetHolidayPayload() {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.holiday_calendar_query);
    }
}
