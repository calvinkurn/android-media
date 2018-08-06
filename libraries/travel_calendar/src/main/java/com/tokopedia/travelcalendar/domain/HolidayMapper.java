package com.tokopedia.travelcalendar.domain;

import com.tokopedia.travelcalendar.data.entity.HolidayResultEntity;
import com.tokopedia.travelcalendar.view.model.HolidayDetail;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class HolidayMapper implements Func1<List<HolidayResultEntity>, List<HolidayResult>> {

    @Inject
    public HolidayMapper() {
    }

    @Override
    public List<HolidayResult> call(List<HolidayResultEntity> holidayResultEntities) {
        List<HolidayResult> holidayResultList = new ArrayList<>();
        for (HolidayResultEntity holidayResultEntity: holidayResultEntities) {
            HolidayResult holidayResult = new HolidayResult();
            holidayResult.setId(holidayResultEntity.getId());

            HolidayDetail holidayDetail = new HolidayDetail();
            holidayDetail.setDate(holidayResultEntity.getAttributes().getDate());
            holidayDetail.setLabel(holidayResultEntity.getAttributes().getLabel());

            holidayResult.setAttributes(holidayDetail);

            holidayResultList.add(holidayResult);
        }
        return holidayResultList;
    }
}
