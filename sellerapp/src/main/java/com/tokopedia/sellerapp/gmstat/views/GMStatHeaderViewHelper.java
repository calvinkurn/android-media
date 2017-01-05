package com.tokopedia.sellerapp.gmstat.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.View;

import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.library.LoaderImageView;
import com.tokopedia.sellerapp.gmstat.library.LoaderTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.sellerapp.gmstat.views.GMStatActivity.IS_GOLD_MERCHANT;
import static com.tokopedia.sellerapp.gmstat.views.GMStatActivityFragment.getDateWithYear;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.CUSTOM_END_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.CUSTOM_START_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.PERIOD_TYPE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.SELECTION_PERIOD;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.SELECTION_TYPE;

/**
 * Created by normansyahputa on 11/21/16.
 */

public class GMStatHeaderViewHelper {

    @BindArray(R.array.month_names_abrev)
    String[] monthNamesAbrev;

//    @BindArray(R.array.month_names)
//    String[] monthNames;

    @BindView(R.id.calendar_range)
    LoaderTextView calendarRange;

    @BindView(R.id.calendar_arrow_icon)
    LoaderImageView calendarArrowIcon;

    @BindView(R.id.calendar_icon)
    LoaderImageView calendarIcon;

    @BindColor(R.color.grey_400)
    int gredyColor;

    @BindColor(R.color.arrow_up)
    int greenColor;

    private static final Locale locale = new Locale("in","ID");


    public static final int MOVE_TO_SET_DATE = 1;
    private View itemView;
    private boolean isGmStat;
    private long sDate;
    private long eDate;
    private int lastSelection;
    private int selectionType = PERIOD_TYPE;
    boolean isLoading = false;

    public GMStatHeaderViewHelper(View itemView, boolean isGmStat){
        this.itemView = itemView;
        this.isGmStat = isGmStat;
        ButterKnife.bind(this, itemView);

        resetToLoading();
    }

    public void resetToLoading(){
        calendarRange.resetLoader();
        calendarArrowIcon.resetLoader();
        calendarIcon.resetLoader();

        isLoading = true;
    }

    public void bindData(List<Integer> dateGraph, int lastSelection) {

        this.lastSelection = lastSelection;

        resetToLoading();

        if(dateGraph == null || dateGraph.size() <=0)
            return;

        String startDate = getDateWithYear(dateGraph.get(0), monthNamesAbrev);
        this.sDate = getDateWithYear(dateGraph.get(0));

        int lastIndex = (dateGraph.size()>7)?6:dateGraph.size()-1;
        String endDate = getDateWithYear(dateGraph.get(lastIndex), monthNamesAbrev);
        this.eDate = getDateWithYear(dateGraph.get(lastIndex));


        calendarRange.setText(startDate+" - "+endDate);

        setImageIcon();

        stopLoading();

        if(!isGmStat){
            calendarRange.setTextColor(gredyColor);
            calendarArrowIcon.setVisibility(View.GONE);
        }else{
            calendarRange.setTextColor(greenColor);
            calendarArrowIcon.setVisibility(View.VISIBLE);
        }
    }

    protected void setImageIcon() {
        Drawable setDateNext = AppCompatDrawableManager.get().getDrawable(itemView.getContext()
                , R.drawable.ic_set_date_next);
        calendarArrowIcon.setImageDrawable(setDateNext);
        calendarIcon.setImageResource(R.mipmap.ic_icon_calendar_02);
    }

    public void stopLoading() {
        calendarRange.stopLoading();
        calendarArrowIcon.stopLoading();
        calendarIcon.stopLoading();

        isLoading = true;
    }

    public void bindDate(long sDate, long eDate, int lastSelectionPeriod, int selectionType){
        this.sDate = sDate;
        this.eDate = eDate;
        this.lastSelection = lastSelectionPeriod;
        this.selectionType = selectionType;

        String startDate = null;
        if(sDate != -1){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", locale);
            startDate = dateFormat.format(cal.getTime());
            startDate = getDateWithYear(Integer.parseInt(startDate), monthNamesAbrev);
        }

        String endDate = null;
        if(eDate != -1){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(eDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", locale);
            endDate = dateFormat.format(cal.getTime());
            int end = Integer.parseInt(endDate);
            Log.d("MNORMANSYAH", "endDate "+endDate+" int "+Integer.parseInt(endDate)+" end "+end);
            endDate = getDateWithYear(endDate, monthNamesAbrev);
        }

        calendarRange.setText(startDate+" - "+endDate);

        setImageIcon();
        stopLoading();
    }

    public static List<String> getDates(List<Integer> dateGraph, String[] monthNames){
        if(dateGraph == null || dateGraph.size() <=0)
            return null;

        String startDate = getDateWithYear(dateGraph.get(0), monthNames);

        int lastIndex = (dateGraph.size()>7)?6:dateGraph.size()-1;
        String endDate = getDateWithYear(dateGraph.get(lastIndex), monthNames);

        List<String> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);

        return dates;
    }

    public void onClick(GMStatActivityFragment gmStatActivityFragment){
        if(!isLoading){
            return;
        }

        // prevent to set date if non gold merchant.
        if(!isGmStat)
            return;

        Intent moveToSetDate = new Intent(gmStatActivityFragment.getActivity(), SetDateActivity.class);
        moveToSetDate.putExtra(IS_GOLD_MERCHANT, isGmStat);
        moveToSetDate.putExtra(SELECTION_PERIOD, lastSelection);
        moveToSetDate.putExtra(SELECTION_TYPE, selectionType);
        moveToSetDate.putExtra(CUSTOM_START_DATE, sDate);
        moveToSetDate.putExtra(CUSTOM_END_DATE, eDate);
        gmStatActivityFragment.getActivity().startActivityForResult(moveToSetDate, MOVE_TO_SET_DATE);
    }
}
