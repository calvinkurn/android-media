package com.tokopedia.tkpdtrain;

import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.homepage.presentation.listener.TrainHomepageView;
import com.tokopedia.train.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.train.homepage.presentation.presenter.TrainHomepagePresenterImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Rizky on 22/02/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TrainDateUtil.class)
public class TrainHomepagePresenterImplTest {

    private Calendar fakeCurrentCalendar;

    private TrainHomepageViewModel trainHomepageViewModel;
    private TrainHomepageView trainHomepageView;

    private TrainHomepagePresenterImpl trainHomepagePresenterImpl;

    @Before
    public void setupTrainHomepagePresenterTest() {
        trainHomepageViewModel = mock(TrainHomepageViewModel.class);
        trainHomepageView = mock(TrainHomepageView.class);

        PowerMockito.mockStatic(TrainDateUtil.class);

        // setup today's date
        int fakeYear = 1994;
        int fakeMonth = 9;
        int fakeDate = 17;
        fakeCurrentCalendar = setupFakeCalendar(fakeYear, fakeMonth, fakeDate);
        when(TrainDateUtil.getCurrentCalendar()).thenReturn(fakeCurrentCalendar);
        Date fakeCurrentDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.getCurrentDate()).thenReturn(fakeCurrentDate); // 17-9-1994

//        trainHomepagePresenterImpl = new TrainHomepagePresenterImpl();
//        trainHomepagePresenterImpl.attachView(trainHomepageView);
    }

    @Test
    public void singleTrip() {
        trainHomepagePresenterImpl.singleTrip();

        verify(trainHomepageViewModel).setOneWay(true);
        verify(trainHomepageView).renderSingleTripView(trainHomepageViewModel);
    }

    @Test
    public void roundTrip() {
        trainHomepagePresenterImpl.roundTrip();

        verify(trainHomepageViewModel).setOneWay(false);
        verify(trainHomepageView).renderRoundTripView(trainHomepageViewModel);
    }

    @Test
    public void onDepartureDateButtonClicked() {
        Date departureDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 1);
        String departureDateString = TrainDateUtil.dateToString(departureDate, TrainDateUtil.DEFAULT_FORMAT);

        when(trainHomepageViewModel.getDepartureDate()).thenReturn(departureDateString);

        Date minDate = TrainDateUtil.getCurrentDate();
        Date maxDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100);
        Date selectedDate = TrainDateUtil.stringToDate(trainHomepageViewModel.getDepartureDate());

        trainHomepagePresenterImpl.onDepartureDateButtonClicked();

        verify(trainHomepageView).showDepartureDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Test
    public void onReturnDateButtonClicked() {
        Date departureDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 1);
        String departureDateString = TrainDateUtil.dateToString(departureDate, TrainDateUtil.DEFAULT_FORMAT);
        Date returnDate = TrainDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, 2);
        String returnDateString = TrainDateUtil.dateToString(returnDate, TrainDateUtil.DEFAULT_FORMAT);

        when(trainHomepageViewModel.getReturnDate()).thenReturn(returnDateString);
        when(trainHomepageViewModel.getDepartureDate()).thenReturn(departureDateString);

        Date selectedDate = TrainDateUtil.stringToDate(trainHomepageViewModel.getReturnDate());
        Date minDate = TrainDateUtil.stringToDate(trainHomepageViewModel.getDepartureDate());
        Date maxDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100);

        trainHomepagePresenterImpl.onReturnDateButtonClicked();

        verify(trainHomepageView).showReturnDatePickerDialog(selectedDate, minDate, maxDate);
    }

    // today's date   = 17 Oct 1994
    // departure date = 18 Oct 1994
    @Test
    public void onDepartureDateChange_SelectedDateValid_OneWayTrip_RenderSingleTripView() {
        // set limit date: today's date + 100
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 25-1-1995

        when(trainHomepageViewModel.isOneWay()).thenReturn(true);

        int newFakeDepartureYear = 1994;
        int newFakeDepartureMonth = 9;
        int newFakeDepartureDate = 18;

        trainHomepagePresenterImpl.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        verify(trainHomepageView).renderSingleTripView(trainHomepageViewModel);
    }

    // today's date   = 17 Oct 1994
    // departure date = 18 Oct 1994
    // return date    = 22 Oct 1994
    @Test
    public void onDepartureDateChange_SelectedDateValid_RoundTrip_RenderRoundTripView() {
        // set limit date: today's date + 100
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 25-1-1995

        // set return date
        int fakeReturnYear = 1994;
        int fakeReturnMonth = 9;
        int fakeReturnDate = 22;
        Calendar returnCalendar = setupFakeCalendar(fakeReturnYear, fakeReturnMonth, fakeReturnDate);
        Date returnDate = returnCalendar.getTime();
        when(trainHomepageViewModel.getReturnDate()).thenReturn("22-09-1994");
        when(TrainDateUtil.stringToDate("22-09-1994")).thenReturn(returnDate); // 22-9-1994

        when(trainHomepageViewModel.isOneWay()).thenReturn(false);

        int newFakeDepartureYear = 1994;
        int newFakeDepartureMonth = 9;
        int newFakeDepartureDate = 18;

        trainHomepagePresenterImpl.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        verify(trainHomepageView).renderRoundTripView(trainHomepageViewModel);
    }

    // today's date   = 17 Oct 1994
    // departure date = 26 Feb 1995
    @Test
    public void onDepartureDateChange_SelectedDepartureDateGreaterThan100Days_ShowError() {
        //given
        // set limit date: today's date + 100
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 25-1-1995

        int newFakeDepartureYear = 1995;
        int newFakeDepartureMonth = 1;
        int newFakeDepartureDate = 26;

        //when
        trainHomepagePresenterImpl.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        //then
        verify(trainHomepageView).showDepartureDateMax90Days(R.string.kai_homepage_departure_max_90_days_from_today_error);
    }

    // today's date   = 17 Oct 1994
    // departure date = 16 Oct 1994
    @Test
    public void onDepartureDateChange_SelecedDepartureDateSmallerThanToday_ShowError() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date fakeLimitDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(fakeLimitDate); // 25-1-1995

        int newFakeDepartureYear = 1994;
        int newFakeDepartureMonth = 9;
        int newFakeDepartureDate = 16;

        trainHomepagePresenterImpl.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        verify(trainHomepageView).showDepartureDateShouldAtLeastToday(R.string.kai_homepage_departure_should_atleast_today_error);
    }

    // today's date   = 17 Oct 1994
    // departure date = 19 Oct 1994
    // return date    = 21 Oct 1994
    @Test
    public void onReturnDateChange_SelectedDateValid_RenderRoundTripView() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 17-9-1994

        // set departure date
        Calendar fakeDepartureCalendar = setupFakeCalendar(1994, 9, 19);
        Date fakeDepartureDate = fakeDepartureCalendar.getTime();
        when(trainHomepageViewModel.getDepartureDate()).thenReturn("19-09-1994");
        when(TrainDateUtil.stringToDate("19-09-1994")).thenReturn(fakeDepartureDate);

        int newFakeReturnYear = 1994;
        int newFakeReturnMonth = 9;
        int newFakeReturnDate = 21;

        trainHomepagePresenterImpl.onReturnDateChange(newFakeReturnYear, newFakeReturnMonth, newFakeReturnDate);

        verify(trainHomepageView).renderRoundTripView(trainHomepageViewModel);
    }

    // today's date   = 17 Oct 1994
    // departure date = 19 Oct 1994
    // return date    = 26 Feb 1995
    @Test
    public void onReturnDateChange_SelectedReturnDateGreaterThan100Days_ShowError() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 17-9-1994

        // set departure date
        Calendar fakeDepartureCalendar = setupFakeCalendar(1994, 9, 19);
        Date fakeDepartureDate = fakeDepartureCalendar.getTime();
        when(trainHomepageViewModel.getDepartureDate()).thenReturn("19-09-1994");
        when(TrainDateUtil.stringToDate("19-09-1994")).thenReturn(fakeDepartureDate);

        int newFakeReturnYear = 1995;
        int newFakeReturnMonth = 9;
        int newFakeReturnDate = 26;

        trainHomepagePresenterImpl.onReturnDateChange(newFakeReturnYear, newFakeReturnMonth, newFakeReturnDate);

        verify(trainHomepageView).showReturnDateMax100Days(R.string.kai_homepage_return_max_100_days_from_today_error);
    }

    // today's date   = 17 Oct 1994
    // departure date = 19 Oct 1994
    // return date    = 18 Oct 1994
    @Test
    public void onReturnDateChange_SelectedReturnDateSmallerThanDepartureDate_ShowError() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 17-9-1994

        // set departure date
        Calendar fakeDepartureCalendar = setupFakeCalendar(1994, 9, 19);
        Date fakeDepartureDate = fakeDepartureCalendar.getTime();
        when(trainHomepageViewModel.getDepartureDate()).thenReturn("19-09-1994");
        when(TrainDateUtil.stringToDate("19-09-1994")).thenReturn(fakeDepartureDate);

        int newFakeReturnYear = 1994;
        int newFakeReturnMonth = 9;
        int newFakeReturnDate = 18;

        trainHomepagePresenterImpl.onReturnDateChange(newFakeReturnYear, newFakeReturnMonth, newFakeReturnDate);

        verify(trainHomepageView).showReturnDateShouldGreaterOrEqual(R.string.kai_homepage_return_should_greater_equal_error);
    }

    private Calendar setupFakeCalendar(int year, int month, int date) {
        Calendar fakeCalendar = Calendar.getInstance();
        fakeCalendar.set(Calendar.YEAR, year);
        fakeCalendar.set(Calendar.MONTH, month);
        fakeCalendar.set(Calendar.DATE, date);
        return fakeCalendar;
    }

}
