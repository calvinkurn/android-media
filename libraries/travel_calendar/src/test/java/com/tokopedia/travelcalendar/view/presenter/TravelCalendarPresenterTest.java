package com.tokopedia.travelcalendar.view.presenter;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.travelcalendar.domain.GetHolidayUseCase;
import com.tokopedia.travelcalendar.domain.TravelCalendarTestScheduler;
import com.tokopedia.travelcalendar.view.TravelCalendarContract;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/08/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class TravelCalendarPresenterTest {

    @Mock
    GetHolidayUseCase getHolidayUseCase;
    @Mock
    TravelCalendarContract.View view;

    private TravelCalendarPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TravelCalendarPresenter(getHolidayUseCase, new TravelCalendarTestScheduler());
        presenter.attachView(view);
    }

    @Test
    public void showMonthsBasedMinDateAndMaxDate() {
        //given
        String minDateString = "07-12-2017";
        String maxDateString = "07-02-2019";
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", new Locale("in", "ID"));
        Date minDate, maxDate;
        try {
            minDate = formatter.parse(minDateString);
            maxDate = formatter.parse(maxDateString);

            //when
            presenter.getMonthsCalendarList(minDate, maxDate);

            //then
            Mockito.verify(view).renderCalendarMonthList(11, 2017, 15);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void showErrorGetHolidayDatesWhenFailed() throws Exception {
        //given
        String message = "Terjadi kesalahan. Ulangi beberapa saat lagi";
        Mockito.when(getHolidayUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.error(new MessageErrorException(message)));

        //when
        presenter.getHolidayEvents();
        //then
        Mockito.verify(view).renderErrorMessage(Mockito.anyObject());
    }

    @Test
    public void showHolidayDatesWhenSuccess() throws Exception {
        //given
        List<HolidayResult> holidayResultList = new ArrayList<>();
        holidayResultList.add(new HolidayResult());

        Mockito.when(getHolidayUseCase.createObservable(Mockito.anyObject()))
                .thenReturn(Observable.just(holidayResultList));

        //when
        presenter.getHolidayEvents();
        //then
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).renderAllHolidayEvent(Mockito.anyList());
    }
}