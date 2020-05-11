package com.tokopedia.events.view.contractor;

public interface EventFilterContract {
    String TIME_RANGE = "timerange";
    String START_DATE = "startdate";
    String CATEGORY = "category";
    int REQ_OPEN_CALENDAR = 341;
    int REQ_OPEN_FILTER = 542;
    String[] CATEGORY_ARRAY = {"Hiburan", "Aktivitas", "Musik", "Olahraga", "Teater", "Seminar", "Internasional", "Open Trip"};
    String[] CATEGORY_ID = {"3", "12", "4", "5", "6", "7", "14", "16"};
    String[] TIME_ID = {"tomorrow", "today", "nextmonth", "nextweek"};
    String[] TIME_VALUES = {"Besok", "Hari Ini", "Bulan Depan", "Minggu Depan"};
    String EVERYDAY = "everyday";


    interface EventFilterView extends EventBaseContract.EventBaseView {
        void resetFilters();

        void renderFilters(String timeRange, String category);
    }

    interface EventFilterPresenter extends EventBaseContract.EventBasePresenter {
        void onClickCalendar();

        void onClickCategory();

        void onClickResetFilter();

        void setCategoryFilter(String selectedCategory);

        void deleteCalendarFilter();

        void deleteCategoryFilter();

        void setTimeRangeFilter(String timeRange, long startDate);

        void setFilters();
    }
}
