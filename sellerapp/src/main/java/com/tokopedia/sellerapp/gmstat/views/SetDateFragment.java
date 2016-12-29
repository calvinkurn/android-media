package com.tokopedia.sellerapp.gmstat.views;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.sellerapp.R;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tokopedia.sellerapp.gmstat.views.GMStatActivityFragment.getDateWithYear;
import static com.tokopedia.sellerapp.gmstat.views.SetDateFragment.StartOrEndPeriodModel.YESTERDAY;

/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment extends Fragment {
    public interface SetDate{
        void returnStartAndEndDate(long startDate, long endDate);
    }

    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;

    @BindView(R.id.set_date_viewpager)
    ViewPager setDateViewPager;
    SetDatePagerAdapter setDatePagerAdapter;

    Unbinder bind;

    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.set_date_layout, container, false);
        bind = ButterKnife.bind(this, rootView);
        setDatePagerAdapter = new SetDatePagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        setDateViewPager.setAdapter(setDatePagerAdapter);
        slidingTabs.setupWithViewPager(setDateViewPager);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    public static class SetDatePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "PERIODE", "KUSTOM" };
        private Context context;

        public SetDatePagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PeriodFragment.newInstance();
                case 1:
                default:
                    return CustomFragment.newInstance();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    public static class CustomFragment extends Fragment {
        @BindView(R.id.period_recyclerview)
        RecyclerView periodRecyclerView;
        private Unbinder unbinder;
        private PeriodAdapter periodAdapter;
        @BindView(R.id.period_linlay)
        LinearLayout periodLinLay;

        @OnClick(R.id.save_date)
        public void saveDate(){
            if(getActivity() != null && getActivity() instanceof SetDate){
                long sDate = ((StartOrEndPeriodModel)periodAdapter.getBasePeriodModels().get(0)).startDate;
                long eDate = ((StartOrEndPeriodModel)periodAdapter.getBasePeriodModels().get(1)).endDate;
                ((SetDate)getActivity()).returnStartAndEndDate(sDate, eDate);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.period_layout, container, false);
            unbinder = ButterKnife.bind(this, rootView);

            periodLinLay.setVisibility(View.GONE);
            periodRecyclerView.setVisibility(View.VISIBLE);
            periodAdapter = new PeriodAdapter();

            List<BasePeriodModel> basePeriodModels = new ArrayList<>();
            basePeriodModels.add(new StartOrEndPeriodModel(true, false,"Tanggal Mulai"));
            basePeriodModels.add(new StartOrEndPeriodModel(false, true, "Tanggal Selesai"));

            periodAdapter.setBasePeriodModels(basePeriodModels);

            periodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            periodRecyclerView.setAdapter(periodAdapter);

            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        public static Fragment newInstance() {
            return new CustomFragment();
        }
    }


    public static class PeriodChooseViewHelper {

        private View itemView;
        private int position;

        public PeriodChooseViewHelper(View itemView, int position){
            this.itemView = itemView;
            this.position = position;
            ButterKnife.bind(this, itemView);
        }

        @BindView(R.id.checkbox_period)
        CheckBox checkBoxPeriod;

        @BindView(R.id.period_header)
        TextView periodHeader;

        @BindView(R.id.period_date)
        TextView periodDate;
        private PeriodRangeModel periodRangeModel;
        PeriodListener periodListener;

//        @BindArray(R.array.month_names)
//        String[] monthNames;

        @BindArray(R.array.month_names_abrev)
        String[] monthNamesAbrev;

        public void setPeriodListener(PeriodListener periodListener) {
            this.periodListener = periodListener;
        }

        @OnClick({R.id.overlay_set_date, R.id.period_date, R.id.period_header})
        public void onCheckForOther(){
            checkBoxPeriod.setChecked(!checkBoxPeriod.isChecked());
            onCheckBoxPeriod(!checkBoxPeriod.isChecked());
        }

//        @OnCheckedChanged(R.id.checkbox_period)
        public void onCheckBoxPeriod(boolean checked){
            periodRangeModel.isChecked = checked;
            if(periodListener != null){
                periodListener.updateCheck(checked, position);
            }
        }

        public void resetToFalse(){
            checkBoxPeriod.setChecked(false);
        }

        public void bindData(PeriodRangeModel periodRangeModel){
            this.periodRangeModel = periodRangeModel;
            if(periodRangeModel.isChecked){
                checkBoxPeriod.setChecked(true);
            }else{
                checkBoxPeriod.setChecked(false);
            }

            if(periodRangeModel.range == 1 && periodRangeModel.isRange == false){
                periodHeader.setText("Kemarin");
            }else if(periodRangeModel.isRange = true){
                if(periodRangeModel.range==7){
                    periodHeader.setText("7 hari terakhir");
                }else if(periodRangeModel.range == 31){
                    periodHeader.setText("30 hari terakhir");
                }
            }
//            periodHeader.setText(periodRangeModel.headerText);

            String description = periodRangeModel.getDescription();
            Log.d("MNORMANSYAH", "description : "+description);
            String[] range = description.split("-");
            int[] split = new int[range.length];
            int i=0;
            for(i=0;i<range.length;i++){
                String[] split1 = range[i].split(" ");
                split[i] = Integer.parseInt(reverseDate(split1));
            }

            if(split.length  >1 ){
                String res = String.format("%s-%s", getDateWithYear(split[0], monthNamesAbrev), getDateWithYear(split[1], monthNamesAbrev));
                periodDate.setText(res);
            }

            if(split.length  ==1 ){
                String res = String.format("%s", getDateWithYear(split[0], monthNamesAbrev));
                periodDate.setText(res);
            }
        }
    }

    public static class PeriodFragment extends Fragment {
        @BindView(R.id.period_recyclerview)
        RecyclerView periodRecyclerView;
        private Unbinder unbinder;
        private PeriodAdapter periodAdapter;

        @BindView(R.id.save_date)
        Button saveDate;

        @BindView(R.id.period_linlay)
        LinearLayout periodLinLay;
        List<PeriodChooseViewHelper> periodChooseViewHelpers;

        @OnClick(R.id.save_date)
        public void saveDate(){
            if(getActivity() != null && getActivity() instanceof SetDate){
                for(int i=0;i<basePeriodModels.size();i++){
                    PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                    if(prm.isChecked){
                        long sDate = prm.startDate;
                        long eDate = prm.endDate;
                        ((SetDate)getActivity()).returnStartAndEndDate(sDate, eDate);
                    }
                }

            }
        }

        List<BasePeriodModel> basePeriodModels;

        PeriodListener periodListener = new PeriodListener() {
            @Override
            public void updateCheck(boolean checked, int index) {

                for(int i=0;i<basePeriodModels.size();i++){
                    if(index != i){
                        if(basePeriodModels.get(i) instanceof PeriodRangeModel ){
                            PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                            prm.isChecked = false;
                            basePeriodModels.set(i, prm);

                            periodChooseViewHelpers.get(i).resetToFalse();
                        }
                    }else{
                        if(basePeriodModels.get(i) instanceof PeriodRangeModel ){
                            PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                            prm.isChecked = true;
                            basePeriodModels.set(i, prm);
                        }
                    }
                }
            }
        };

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.period_layout, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            //[START] old code
            periodAdapter = new PeriodAdapter();

            basePeriodModels = new ArrayList<>();
            PeriodRangeModel e = new PeriodRangeModel(false, 1);
            e.headerText = "Kemarin";
            basePeriodModels.add(e);
            e = new PeriodRangeModel(true, 7);
            e.headerText = "7 hari terakhir";
            basePeriodModels.add(e);
            e = new PeriodRangeModel(true, 31);
            e.headerText = "30 hari terakhir";
            basePeriodModels.add(e);

            periodAdapter.setBasePeriodModels(basePeriodModels);

            periodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            periodRecyclerView.setAdapter(periodAdapter);
            //[END] old code

            periodChooseViewHelpers = new ArrayList<>();
            for (int i=0;i<basePeriodModels.size();i++){
                View view = LayoutInflater.from(container.getContext()).inflate(R.layout.periode_layout, periodLinLay, false);
                PeriodChooseViewHelper periodChooseViewHelper = new PeriodChooseViewHelper(view, i);
                periodChooseViewHelper.bindData((PeriodRangeModel) basePeriodModels.get(i));
                periodChooseViewHelper.setPeriodListener(periodListener);
                periodChooseViewHelpers.add(periodChooseViewHelper);
                periodLinLay.addView(view);
            }
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        public static Fragment newInstance() {
            return new PeriodFragment();
        }
    }

    public static class PeriodAdapter extends RecyclerView.Adapter{
        List<BasePeriodModel> basePeriodModels;

        PeriodListener periodListener = new PeriodListener() {
            @Override
            public void updateCheck(boolean checked, int index) {
                for(int i=0;i<basePeriodModels.size();i++){
                    if(index != i){
                        if(basePeriodModels.get(i) instanceof PeriodRangeModel ){
                            PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                            prm.isChecked = false;
                        }
                    }
                }

                if (mRecyclerView != null && !mRecyclerView.isComputingLayout()) {
                    PeriodAdapter.this.notifyDataSetChanged();
                }
            }
        };

        public PeriodAdapter(){
            basePeriodModels = new ArrayList<>();
        }

        private RecyclerView mRecyclerView;

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            mRecyclerView = recyclerView;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case PeriodRangeModel.TYPE:
                    return new BasePeriodViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.periode_layout, parent, false));
                case StartOrEndPeriodModel.TYPE:
                default:
//                    return null;
                    return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.custom_layout, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (basePeriodModels.get(position).type){
                case PeriodRangeModel.TYPE:
                    BasePeriodViewHolder bpvh = ((BasePeriodViewHolder)holder);
                    bpvh.setPeriodListener(periodListener);
                    bpvh.bindData((PeriodRangeModel) basePeriodModels.get(position));
                    break;
                case StartOrEndPeriodModel.TYPE:
                    ((CustomViewHolder)holder).bindData((StartOrEndPeriodModel) basePeriodModels.get(position));
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            switch (basePeriodModels.get(position).type){
                case PeriodRangeModel.TYPE:
                case StartOrEndPeriodModel.TYPE:
                    return basePeriodModels.get(position).type;
            }
            throw new RuntimeException("please register type to PeriodAdapter");
//            return super.getItemViewType(position);
        }

        public List<BasePeriodModel> getBasePeriodModels() {
            return basePeriodModels;
        }

        public void setBasePeriodModels(List<BasePeriodModel> basePeriodModels) {
            this.basePeriodModels = basePeriodModels;
        }

        @Override
        public int getItemCount() {
            return basePeriodModels.size();
        }
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{

//        @BindArray(R.array.month_names)
//        String[] monthNames;

        @BindArray(R.array.month_names_abrev)
        String[] monthNamesAbrev;

        @BindView(R.id.custom_header)
        TextView customHeader;

        @BindView(R.id.custom_date)
        TextView customDate;

        @BindView(R.id.custom_drop_down)
        ImageView customDropDown;

        private DatePickerDialog fromDatePickerDialog;
        private DatePickerDialog toDatePickerDialog;
        private StartOrEndPeriodModel startOrEndPeriodModel;

        @OnClick(R.id.custom_layout_container)
        public void onChooseDate(){
            if(startOrEndPeriodModel == null)
                return;

            if(startOrEndPeriodModel.isEndDate
                    && toDatePickerDialog != null
                    && !toDatePickerDialog.isShowing()){
                toDatePickerDialog.show();
            }

            if(startOrEndPeriodModel.isStartDate
                    && fromDatePickerDialog != null
                    && !fromDatePickerDialog.isShowing()){
                fromDatePickerDialog.show();
            }
        }

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            Calendar newCalendar = Calendar.getInstance();
            fromDatePickerDialog = new DatePickerDialog(this.itemView.getContext(), new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String month = ((monthOfYear < 10)?("0"+(monthOfYear+1)):monthOfYear+1+"");
                    String day = ((dayOfMonth < 10)?("0"+dayOfMonth):dayOfMonth+"");
                    String data = year+""+month+""+day;
                    customDate.setText(getDateWithYear(Integer.parseInt(data), monthNamesAbrev));

                    startOrEndPeriodModel.startDate = newDate.getTimeInMillis();
                }

            },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar minDate = Calendar.getInstance();
            minDate.set(2015, 6, 25);
            fromDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

            toDatePickerDialog = new DatePickerDialog(this.itemView.getContext(), new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String month = ((monthOfYear < 10)?("0"+(monthOfYear+1)):monthOfYear+1+"");
                    String day = ((dayOfMonth < 10)?("0"+dayOfMonth):dayOfMonth+"");
                    String data = year+""+month+""+day;
                    Log.d("MNORMANSYAH", "data : "+data);
                    customDate.setText(getDateWithYear(Integer.parseInt(data), monthNamesAbrev));

                    startOrEndPeriodModel.endDate = newDate.getTimeInMillis();
                }

            },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        }

        public void bindData(StartOrEndPeriodModel startOrEndPeriodModel){
            this.startOrEndPeriodModel = startOrEndPeriodModel;
            customHeader.setText(startOrEndPeriodModel.textHeader);
            if(startOrEndPeriodModel.isEndDate) {
                String endDate = startOrEndPeriodModel.getEndDate();
                String[] split = endDate.split(" ");
                customDate.setText(getDateWithYear(Integer.parseInt(reverseDate(split)), monthNamesAbrev));

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startOrEndPeriodModel.endDate);

                toDatePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            }
            if(startOrEndPeriodModel.isStartDate) {
                String startDate = startOrEndPeriodModel.getStartDate();
                String[] split = startDate.split(" ");
                customDate.setText(getDateWithYear(Integer.parseInt(reverseDate(split)), monthNamesAbrev));

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startOrEndPeriodModel.startDate);

                fromDatePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            }
        }
    }

    public interface PeriodListener{
        void updateCheck(boolean checked, int index);
    }

    @Deprecated
    public static class BasePeriodViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox_period)
        CheckBox checkBoxPeriod;

        @BindView(R.id.period_header)
        TextView periodHeader;

        @BindView(R.id.period_date)
        TextView periodDate;
        private PeriodRangeModel periodRangeModel;
        PeriodListener periodListener;

//        @BindArray(R.array.month_names)
//        String[] monthNames;

        @BindArray(R.array.month_names_abrev)
        String[] monthNamesAbrev;

        public void setPeriodListener(PeriodListener periodListener) {
            this.periodListener = periodListener;
        }

        @OnCheckedChanged(R.id.checkbox_period)
        public void onCheckBoxPeriod(boolean checked){
            periodRangeModel.isChecked = checked;
            if(periodListener != null){
                periodListener.updateCheck(checked, getLayoutPosition());
            }
        }

        public BasePeriodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(PeriodRangeModel periodRangeModel){
            this.periodRangeModel = periodRangeModel;
            if(periodRangeModel.isChecked){
                checkBoxPeriod.setChecked(true);
            }else{
                checkBoxPeriod.setChecked(false);
            }

            if(periodRangeModel.range == 1 && periodRangeModel.isRange == false){
                periodHeader.setText("Kemarin");
            }else if(periodRangeModel.isRange = true){
                if(periodRangeModel.range==7){
                    periodHeader.setText("7 hari terakhir");
                }else if(periodRangeModel.range == 31){
                    periodHeader.setText("30 hari terakhir");
                }
            }
//            periodHeader.setText(periodRangeModel.headerText);

            String description = periodRangeModel.getDescription();
            Log.d("MNORMANSYAH", "description : "+description);
            String[] range = description.split("-");
            int[] split = new int[range.length];
            int i=0;
            for(i=0;i<range.length;i++){
                String[] split1 = range[i].split(" ");
                split[i] = Integer.parseInt(reverseDate(split1));
            }

            if(split.length  >1 ){
                String res = String.format("%s-%s", getDateWithYear(split[0], monthNamesAbrev), getDateWithYear(split[1], monthNamesAbrev));
                periodDate.setText(res);
            }

            if(split.length  ==1 ){
                String res = String.format("%s", getDateWithYear(split[0], monthNamesAbrev));
                periodDate.setText(res);
            }
        }
    }

    public static String reverseDate(String[] split) {
        String reverse = "";
        for(int i=split.length-1;i>=0;i--){
            reverse += split[i];
        }
        return reverse;
    }

    public static class BasePeriodModel {
        int type = -1;

        public BasePeriodModel(int type) {
            this.type = type;
        }
    }

    @Parcel
    public static class PeriodRangeModel extends BasePeriodModel{

        public static final int TYPE = 1;
        public boolean isChecked;
        String headerText;
        String formatText = "%s - %s";
        boolean isRange;
        int range;
        long startDate = -1, endDate = -1;

        public PeriodRangeModel() {
            super(TYPE);
        }

        public PeriodRangeModel(boolean isRange, int range){
            this();
            this.isRange = isRange;
            this.range = range;
        }

        public String getDescription(){
            DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
            if(isRange) {
                Calendar sDate = calculateCalendar(YESTERDAY);
                endDate = sDate.getTimeInMillis();
                String yesterday = dateFormat.format(sDate.getTime());

                Calendar eDate = calculateCalendar(-range);
                startDate = eDate.getTimeInMillis();
                String startDate = dateFormat.format(eDate.getTime());

                return headerText = String.format(formatText, startDate, yesterday);
            }else{
                Calendar sDate = calculateCalendar(-range);
                Calendar eDate = sDate;
                startDate = sDate.getTimeInMillis();
                endDate = sDate.getTimeInMillis();
                return dateFormat.format(sDate.getTime());
            }
        }

        private Calendar calculateCalendar(int daysAgo){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, daysAgo);
            return cal;
        }
    }

    public static class StartOrEndPeriodModel extends BasePeriodModel{

        public static final int TYPE = 2;
        public static final int YESTERDAY = -1;
        public static final int SIXTY_DAYS_AGO = -61;
        /**
         * isEndDate 60 hari sebelum hari ini
         */
        boolean isStartDate,
        /**
         * yesterday
         */
                isEndDate;

        String textHeader;

        long startDate = -1, endDate = -1;

        private StartOrEndPeriodModel() {
            super(TYPE);
        }

        public StartOrEndPeriodModel(boolean isStartDate, boolean isEndDate, String textHeader) {
            this();
            this.isStartDate = isStartDate;
            this.isEndDate = isEndDate;
            this.textHeader = textHeader;
        }

        public String getStartDate(){
            if(isStartDate) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, SIXTY_DAYS_AGO);
                startDate = cal.getTimeInMillis();
                System.out.println("Yesterday's date = " + cal.getTime());
                DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                return dateFormat.format(cal.getTime());
            }else
                return "21/01/1992";
        }

        public String getEndDate(){
            if(isEndDate){
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, YESTERDAY);
                endDate = cal.getTimeInMillis();
                System.out.println("Yesterday's date = " + cal.getTime());
                DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                return dateFormat.format(cal.getTime());
            }else{
                return "21/01/1992";
            }
        }
    }
}
