package com.tokopedia.sellerapp.gmstat.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;

import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.library.LoaderImageView;
import com.tokopedia.sellerapp.gmstat.library.LoaderTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.sellerapp.gmstat.views.GMStatActivityFragment.getDateWithYear;

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


    public static final int MOVE_TO_SET_DATE = 1;
    private View itemView;

    public GMStatHeaderViewHelper(View itemView){
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);

        resetToLoading();
    }

    public void resetToLoading(){
        calendarRange.resetLoader();
        calendarArrowIcon.resetLoader();
        calendarIcon.resetLoader();
    }

    public void bindData(List<Integer> dateGraph) {

        calendarRange.resetLoader();
        calendarArrowIcon.resetLoader();
        calendarIcon.resetLoader();

        if(dateGraph == null || dateGraph.size() <=0)
            return;

        String startDate = getDateWithYear(dateGraph.get(0), monthNamesAbrev);

        int lastIndex = (dateGraph.size()>7)?6:dateGraph.size()-1;
        String endDate = getDateWithYear(dateGraph.get(lastIndex), monthNamesAbrev);

        calendarRange.setText(startDate+" - "+endDate);

        Drawable setDateNext = AppCompatDrawableManager.get().getDrawable(itemView.getContext()
                , R.drawable.ic_set_date_next);
        calendarArrowIcon.setImageDrawable(setDateNext);
        calendarIcon.setImageResource(R.mipmap.ic_icon_calendar_02);


        calendarRange.stopLoading();
        calendarArrowIcon.stopLoading();
        calendarIcon.stopLoading();
    }

    public void bindDate(long sDate, long eDate){
        String startDate = null;
        if(sDate != -1){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            startDate = dateFormat.format(cal.getTime());
            startDate = getDateWithYear(Integer.parseInt(startDate), monthNamesAbrev);
        }

        String endDate = null;
        if(eDate != -1){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(eDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            endDate = dateFormat.format(cal.getTime());
            endDate = getDateWithYear(Integer.parseInt(endDate), monthNamesAbrev);
        }

        calendarRange.setText(startDate+" - "+endDate);
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
        Intent moveToSetDate = new Intent(gmStatActivityFragment.getActivity(), SetDateActivity.class);
        gmStatActivityFragment.getActivity().startActivityForResult(moveToSetDate, MOVE_TO_SET_DATE);
    }
}
