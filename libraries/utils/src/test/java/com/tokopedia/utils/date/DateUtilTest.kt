package com.tokopedia.utils.date

import org.junit.Test
import java.util.*

/**
 * @author by furqan on 29/06/2021
 */
class DateUtilTest {

    @Test
    fun getDayDiff() {
        val today = DateUtil.getCurrentDate()
        val tomorrow = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.DATE, 2)

        val todayString = today.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
        val tomorrowString = tomorrow.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        val dayDiff = DateUtil.getDayDiff(todayString, tomorrowString)

        assert(dayDiff == 2L)
    }

    @Test
    fun getDayDiffDateTwoDaysFromToday() {
        val theDayAfterTomorrow = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.DATE, 2)

        val dayDiff = theDayAfterTomorrow.getDayDiffFromToday()

        assert(dayDiff == 2L)
    }

    @Test
    fun getDayDiffStringTwoDaysFromToday() {
        val theDayAfterTomorrow = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.DATE, 2)

        val theDayAfterTomorrowString = theDayAfterTomorrow.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        val dayDiff = DateUtil.getDayDiffFromToday(theDayAfterTomorrowString)

        assert(dayDiff == 2L)
    }

    @Test
    fun getDayDiffOneDayFromToday() {
        val tomorrow = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.DATE, 1)

        val tomorrowString = tomorrow.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        val dayDiff = DateUtil.getDayDiffFromToday(tomorrowString)

        assert(dayDiff == 1L)
    }

    @Test
    fun getDayDiffOneYearFromToday() {
        val oneYearLater = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.YEAR, 1)

        val oneYearLaterString = oneYearLater.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        val dayDiff = DateUtil.getDayDiffFromToday(oneYearLaterString)

        assert(dayDiff == 365L)
    }

    @Test
    fun getDayDiffOneWeekFromToday() {
        val oneWeekLater = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.DATE, 7)

        val oneWeekLaterString = oneWeekLater.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        val dayDiff = DateUtil.getDayDiffFromToday(oneWeekLaterString)

        assert(dayDiff == 7L)
    }

    @Test
    fun getCurrentCalendar() {
        val currentCalendar = Calendar.getInstance()

        val dateUtilCurrentCalendar = DateUtil.getCurrentCalendar()

        assert(currentCalendar.time.trimDate() == dateUtilCurrentCalendar.time.trimDate())
    }

    @Test
    fun addTimeToSpesificDate() {
        val currentCalendar = DateUtil.getCurrentCalendar()
        currentCalendar.add(Calendar.YEAR, 1)

        val dateUtilAddTime = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.YEAR, 1)

        assert(currentCalendar.time.trimDate() == dateUtilAddTime.trimDate())
    }

    @Test
    fun removeTime() {
        val dateAfterRemove = DateUtil.getCurrentDate().removeTime()
        val calendar = Calendar.getInstance()
        calendar.time = dateAfterRemove

        assert(calendar.get(Calendar.HOUR_OF_DAY) == 0)
        assert(calendar.get(Calendar.MINUTE) == 0)
        assert(calendar.get(Calendar.SECOND) == 0)
        assert(calendar.get(Calendar.MILLISECOND) == 0)
    }

    @Test
    fun formatDateByUserTimeZone() {
        val date1 = DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD)

        val newDate = DateUtil.formatDateByUsersTimezone(
                DateUtil.YYYY_MM_DD,
                DateUtil.YYYY_MM_DD,
                DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS)
        )

        assert(date1 == newDate)
    }

    @Test
    fun formatDateWithDefaultLocale() {
        val date1 = DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD)

        val newDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD,
                DateUtil.YYYY_MM_DD,
                DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS)
        )

        assert(date1 == newDate)
    }

    @Test
    fun formatDateWithLocale() {
        val formattedDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.YYYY_MM_DD,
                DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                Locale("in", "IN")
        )
        val comparationDate = DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD)

        assert(formattedDate == comparationDate)
    }

    @Test
    fun formatDateWithWrongFormatShouldReturnGivenDate() {
        val formattedDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.YYYY_MM_DD,
                "dummy date"
        )

        assert(formattedDate == "dummy date")
    }

    @Test
    fun formatDateWithDefaultLocaleBetweenTimezone() {
        val date1 = DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        val newDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                date1,
                TimeZone.getTimeZone("UTC"),
                TimeZone.getTimeZone("GMT+7")
        )

        val dateWithAdditional7Hours = date1.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
                .addTimeToSpesificDate(Calendar.HOUR_OF_DAY, 7)
                .toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        assert(dateWithAdditional7Hours == newDate)
    }

    @Test
    fun formatDateWithLocaleBetweenTimeZone() {
        val date1 = DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)

        val formattedDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                TimeZone.getTimeZone("UTC"),
                TimeZone.getTimeZone("GMT+7"),
                Locale("in", "IN")
        )

        val dateWithAdditional7Hours = date1.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
                .addTimeToSpesificDate(Calendar.HOUR_OF_DAY, 7)
                .toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)


        assert(formattedDate == dateWithAdditional7Hours)
    }

    @Test
    fun formatDateWithLocaleBetweenTimeZoneWithWrongFormatShouldReturnGivenDate() {
        val formattedDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                "dummy date",
                TimeZone.getTimeZone("UTC"),
                TimeZone.getTimeZone("GMT+7"),
                Locale("in", "IN")
        )

        assert(formattedDate == "dummy date")
    }

    @Test
    fun formatToUi() {
        val today = DateUtil.getCurrentDate()
        val todayYYYYMMDDString = today.toString(DateUtil.YYYY_MM_DD)
        val todayViewString = today.toString(DateUtil.DEFAULT_VIEW_FORMAT)

        val formattedDate = DateUtil.formatToUi(todayYYYYMMDDString)

        assert(formattedDate == todayViewString)
    }

    @Test(expected = RuntimeException::class)
    fun nullStringToDate() {
        val date: String? = null
        date.toDate(DateUtil.YYYY_MM_DD)
    }

    @Test(expected = RuntimeException::class)
    fun wrongFormatStringToDate() {
        val date = "10:10:10"
        date.toDate(DateUtil.YYYY_MM_DD)
    }

}