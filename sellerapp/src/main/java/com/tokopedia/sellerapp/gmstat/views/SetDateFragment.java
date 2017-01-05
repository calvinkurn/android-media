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
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.sellerapp.R;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tokopedia.sellerapp.gmstat.views.GMStatActivityFragment.getDateFormat;
import static com.tokopedia.sellerapp.gmstat.views.GMStatActivityFragment.getDateWithYear;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.CUSTOM_END_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.CUSTOM_START_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.CUSTOM_TYPE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.PERIOD_TYPE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.SELECTION_PERIOD;
import static com.tokopedia.sellerapp.gmstat.views.SetDateFragment.StartOrEndPeriodModel.YESTERDAY;

/**
 * Created by normansyahputa on 11/25/16.
 */

public class SetDateFragment extends Fragment {
    private SetDate setDate;
    private static final Locale locale = new Locale("in","ID");

    public interface SetDate{
        void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType);
        boolean isGMStat();
        int selectionPeriod();
        int selectionType();
        long sDate();
        long eDate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context != null && context instanceof SetDate){
            setDate = (SetDate) context;
        }
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
        setDatePagerAdapter = new SetDatePagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity(), setDate.isGMStat(), setDate.selectionPeriod(),
                setDate.sDate(), setDate.eDate());
        setDateViewPager.setAdapter(setDatePagerAdapter);
        slidingTabs.setupWithViewPager(setDateViewPager);

        switch (setDate.selectionType()){
            case PERIOD_TYPE:
                setDateViewPager.setCurrentItem(0);
                break;
            case CUSTOM_TYPE:
                setDateViewPager.setCurrentItem(1);
                break;
        }
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
        private boolean isGM;
        private int lastSelectionPeriod;
        private long sDate;
        private long eDate;

        public SetDatePagerAdapter(FragmentManager fm, Context context, boolean isGM,
                                   int lastSelectionPeriod, long sDate, long eDate) {
            super(fm);
            this.context = context;
            this.isGM = isGM;
            this.lastSelectionPeriod = lastSelectionPeriod;
            this.sDate = sDate;
            this.eDate = eDate;
            Log.d("MNORMANSYAH", "sDate "+getDateFormat(sDate)+" eDate "+getDateFormat(eDate));
        }

        @Override
        public int getCount() {
            if(!isGM)
                return 1;
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PeriodFragment.newInstance(lastSelectionPeriod);
                case 1:
                default:
                    return CustomFragment.newInstance(sDate,eDate);
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
        private long sDate, eDate;

        @OnClick(R.id.save_date)
        public void saveDate(){
            if(getActivity() != null && getActivity() instanceof SetDate){
                long sDate = periodAdapter.datePickerRules.sDate;
                long eDate = periodAdapter.datePickerRules.eDate;
                ((SetDate)getActivity()).returnStartAndEndDate(sDate, eDate, -1, CUSTOM_TYPE);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            if(bundle != null){
                sDate = bundle.getLong(CUSTOM_START_DATE, -1);
                eDate = bundle.getLong(CUSTOM_END_DATE, -1);
            }
            View rootView = inflater.inflate(R.layout.period_layout, container, false);
            unbinder = ButterKnife.bind(this, rootView);

            periodLinLay.setVisibility(View.GONE);
            periodRecyclerView.setVisibility(View.VISIBLE);
            periodAdapter = new PeriodAdapter(rootView, sDate, eDate);

            List<BasePeriodModel> basePeriodModels = new ArrayList<>();
            StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
            startOrEndPeriodModel.setStartDate(sDate);
            basePeriodModels.add(startOrEndPeriodModel);
            startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
            startOrEndPeriodModel.setEndDate(eDate);
            basePeriodModels.add(startOrEndPeriodModel);

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

        public static Fragment newInstance(long sDate, long eDate){
            Fragment fragment = new CustomFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(CUSTOM_START_DATE, sDate);
            bundle.putLong(CUSTOM_END_DATE, eDate);
            fragment.setArguments(bundle);
            return fragment;
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

        @OnClick(R.id.overlay_set_date)
        public void onCheckForOther(){
            if(periodListener.isAllNone(!checkBoxPeriod.isChecked(), position)){
                return;
            }
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

            if(periodRangeModel.range == 1 && !periodRangeModel.isRange){
                periodHeader.setText(R.string.yesterday);
            }else if(periodRangeModel.isRange){
                if(periodRangeModel.range==7){
                    periodHeader.setText(R.string.seven_days_ago);
                }else if(periodRangeModel.range == 31){
                    periodHeader.setText(R.string.thirty_days_ago);
                }
            }
//            periodHeader.setText(periodRangeModel.headerText);

            String description = periodRangeModel.getDescription();
            Log.d("MNORMANSYAH", "description : "+description);
            String[] range = description.split("-");
            int[] split = new int[range.length];
            int i;
            for(i=0;i<range.length;i++){
                String[] split1 = range[i].split(" ");
                split[i] = Integer.parseInt(reverseDate(split1));
            }

            if(split.length  >1 ){
                String res = String.format("%s - %s", getDateWithYear(split[0], monthNamesAbrev), getDateWithYear(split[1], monthNamesAbrev));
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
                        ((SetDate)getActivity()).returnStartAndEndDate(sDate, eDate, i, PERIOD_TYPE);
                    }
                }

            }
        }

        public static Fragment newInstance(int lastSelectionPeriod){
            Fragment fragment = new PeriodFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SELECTION_PERIOD, lastSelectionPeriod);
            fragment.setArguments(bundle);
            return fragment;
        }

        List<BasePeriodModel> basePeriodModels;

        PeriodListener periodListener = new PeriodListener() {
            @Override
            public void updateCheck(boolean checked, int index) {

                // dont unselect period.
                if(isAllNone(checked, index))
                    return;

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

            @Override
            public boolean isAllNone(boolean isChecked, int index) {
                int isNoneAll = 0;
                for(int i=0;i<basePeriodModels.size();i++){
                    if(i==index && !isChecked){
                        isNoneAll++;
                        continue;
                    }
                    if(!((PeriodRangeModel)basePeriodModels.get(i)).isChecked){
                        isNoneAll++;
                    }
                }

                // dont unselect period.
                return isNoneAll == basePeriodModels.size();
            }
        };

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.period_layout, container, false);

            int lastSelection = 1;
            Bundle bundle = getArguments();
            if(bundle != null){
                lastSelection = bundle.getInt(SELECTION_PERIOD, 1);
            }

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

            //[START] set last selection
            PeriodRangeModel periodRangeModel = (PeriodRangeModel) basePeriodModels.get(lastSelection);
            periodRangeModel.isChecked = true;
            basePeriodModels.set(lastSelection, periodRangeModel);
            //[END] set last selection

            periodAdapter.setBasePeriodModels(basePeriodModels);

            periodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            periodRecyclerView.setAdapter(periodAdapter);
            //[END] old code

            periodChooseViewHelpers = new ArrayList<>();
            for (int i=0;i<basePeriodModels.size();i++){
                @SuppressWarnings("ConstantConditions") View view = LayoutInflater.from(container.getContext()).inflate(R.layout.periode_layout, periodLinLay, false);
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

    @SuppressWarnings("deprecation")
    public static class PeriodAdapter extends RecyclerView.Adapter{
        DatePickerRules datePickerRules;
        List<BasePeriodModel> basePeriodModels;
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);

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

            @Override
            public boolean isAllNone(boolean isChecked, int index) {
                return false;
            }
        };

        @Deprecated
        public PeriodAdapter(){
            basePeriodModels = new ArrayList<>();
        }

        public PeriodAdapter(final View itemView, long sDate, long eDate){
            basePeriodModels = new ArrayList<>();

            Calendar instance = Calendar.getInstance();
//            instance.add(Calendar.DATE, -1);
            long tomorrow = instance.getTimeInMillis();

            instance = Calendar.getInstance();
            instance.set(2015, 6, 25);
            long minLimit = instance.getTimeInMillis();

            instance = Calendar.getInstance();
            instance.add(Calendar.DATE, -1);
            long yesterday = instance.getTimeInMillis();

            Log.d("MNORMANSYAH", "max limit ## "+dateFormat.format(tomorrow)+
                    " minLimit "+ dateFormat.format(minLimit) +
                    " max End Date "+dateFormat.format(yesterday));

            datePickerRules = new DatePickerRules(tomorrow, minLimit, 60, yesterday);
            datePickerRules.setDatePickerRulesListener(new DatePickerRules.DatePickerRulesListener() {
                @Override
                public void exceedSDate() {
                    Toast.makeText(itemView.getContext(), "exceed start date", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void exceedEDate() {
                    Toast.makeText(itemView.getContext(), "exceed end date", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void resetToSDate(long sDate, long eDate) {
                    basePeriodModels.clear();
                    StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                    startOrEndPeriodModel.setStartDate(sDate);
                    basePeriodModels.add(startOrEndPeriodModel);
                    startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                    startOrEndPeriodModel.setEndDate(eDate);
                    basePeriodModels.add(startOrEndPeriodModel);

                    PeriodAdapter.this.notifyDataSetChanged();
                }

                @Override
                public void resetToEDate(long sDate, long eDate) {
                    basePeriodModels.clear();
                    StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                    startOrEndPeriodModel.setStartDate(sDate);
                    basePeriodModels.add(startOrEndPeriodModel);
                    startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                    startOrEndPeriodModel.setEndDate(eDate);
                    basePeriodModels.add(startOrEndPeriodModel);

                    PeriodAdapter.this.notifyDataSetChanged();
                }

                @Override
                public void successSDate(long sDate, long eDate) {
                    basePeriodModels.clear();
                    StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                    startOrEndPeriodModel.setStartDate(sDate);
                    basePeriodModels.add(startOrEndPeriodModel);

                    if(eDate == -1) {
                        PeriodAdapter.this.notifyDataSetChanged();
                        return;
                    }

                    startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                    startOrEndPeriodModel.setEndDate(eDate);
                    basePeriodModels.add(startOrEndPeriodModel);
                    PeriodAdapter.this.notifyDataSetChanged();
                }

                @Override
                public void successEDate(long sDate, long eDate) {
                    basePeriodModels.clear();
                    StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                    startOrEndPeriodModel.setStartDate(sDate);
                    basePeriodModels.add(startOrEndPeriodModel);

                    if(sDate == -1) {
                        PeriodAdapter.this.notifyDataSetChanged();
                        return;
                    }
                    startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                    startOrEndPeriodModel.setEndDate(eDate);
                    basePeriodModels.add(startOrEndPeriodModel);

                    PeriodAdapter.this.notifyDataSetChanged();
                }

                @Override
                public void promptUserExceedLimit() {
                    Toast.makeText(itemView.getContext(), "exceed range date", Toast.LENGTH_SHORT).show();
                }
            });
            datePickerRules.seteDate(eDate);
            datePickerRules.setsDate(sDate);
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
                    CustomViewHolder customViewHolder = (CustomViewHolder) holder;
//                    customViewHolder.setDateValidationListener(dateValidationListener);
                    customViewHolder.setDatePickerRules(datePickerRules);
                    customViewHolder.bindData((StartOrEndPeriodModel) basePeriodModels.get(position));
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

//        DateValidationListener dateValidationListener;

        DatePickerRules datePickerRules;

        public void setDatePickerRules(DatePickerRules datePickerRules) {
            this.datePickerRules = datePickerRules;
        }

        public void setDateValidationListener(DateValidationListener dateValidationListener) {
//            this.dateValidationListener = dateValidationListener;
        }

        private DatePickerDialog fromDatePickerDialog;
        private DatePickerDialog toDatePickerDialog;
        private StartOrEndPeriodModel startOrEndPeriodModel;

        @OnClick(R.id.custom_header)
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

                @SuppressWarnings("StatementWithEmptyBody")
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    Log.d("MNORMANSYAH", "year : "+year+" monthOfYear "+monthOfYear+ " dayOfMonth "+dayOfMonth);
                    String month = ((monthOfYear+1 < 10)?("0"+(monthOfYear+1)):(monthOfYear+1)+"");
                    String day = ((dayOfMonth < 10)?("0"+dayOfMonth):dayOfMonth+"");
                    String data = year+""+month+""+day;
                    Log.d("MNORMANSYAH", "data : "+data);


                    datePickerRules.setsDate(newDate.getTimeInMillis());
//                    if(dateValidationListener.addSDate(newDate.getTimeInMillis())) {
//                        startOrEndPeriodModel.startDate = newDate.getTimeInMillis();
//                        customDate.setText(getDateWithYear(Integer.parseInt(data), monthNamesAbrev));
//                    }else{
//                        // maximal 60 hari
//                    }

                }

            },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar minDate = Calendar.getInstance();
            minDate.set(2015, 6, 25);
            fromDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.DATE, -1);
            fromDatePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

            fromDatePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            toDatePickerDialog = new DatePickerDialog(this.itemView.getContext(), new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String month = ((monthOfYear+1 < 10)?("0"+(monthOfYear+1)):(monthOfYear+1)+"");
                    String day = ((dayOfMonth < 10)?("0"+dayOfMonth):dayOfMonth+"");
                    String data = year+""+month+""+day;
                    Log.d("MNORMANSYAH", "data : "+data);

                    datePickerRules.seteDate(newDate.getTimeInMillis());
//                    if(dateValidationListener.addEDate(newDate.getTimeInMillis())){
//                        startOrEndPeriodModel.endDate = newDate.getTimeInMillis();
//                        customDate.setText(getDateWithYear(Integer.parseInt(data), monthNamesAbrev));
//                    }
                }

            },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            toDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            toDatePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            toDatePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        public void bindData(StartOrEndPeriodModel startOrEndPeriodModel){
            this.startOrEndPeriodModel = startOrEndPeriodModel;
            customHeader.setText(startOrEndPeriodModel.textHeader);
            if(startOrEndPeriodModel.isEndDate) {
                String endDate = startOrEndPeriodModel.getEndDate();
                String[] split = endDate.split(" ");
                customDate.setText(getDateWithYear(Integer.parseInt(reverseDate(split)), monthNamesAbrev));

//                dateValidationListener.addEDate(startOrEndPeriodModel.endDate);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startOrEndPeriodModel.endDate);

                toDatePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, YESTERDAY);
                toDatePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            }
            if(startOrEndPeriodModel.isStartDate) {
                String startDate = startOrEndPeriodModel.getStartDate();
                String[] split = startDate.split(" ");
                customDate.setText(getDateWithYear(Integer.parseInt(reverseDate(split)), monthNamesAbrev));

//                dateValidationListener.addSDate(startOrEndPeriodModel.startDate);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startOrEndPeriodModel.startDate);

                fromDatePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            }
        }
    }

    public interface DateValidationListener{
        boolean addSDate(long sDate);
        boolean addEDate(long eDate);
    }

    public interface PeriodListener{
        void updateCheck(boolean checked, int index);
        boolean isAllNone(boolean checked, int index);
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

            if(periodRangeModel.range == 1 && !periodRangeModel.isRange){
                periodHeader.setText(R.string.yesterday);
            }else if(periodRangeModel.isRange){
                if(periodRangeModel.range==7){
                    periodHeader.setText(R.string.seven_days_ago);
                }else if(periodRangeModel.range == 31){
                    periodHeader.setText(R.string.thirty_days_ago);
                }
            }
//            periodHeader.setText(periodRangeModel.headerText);

            String description = periodRangeModel.getDescription();
            Log.d("MNORMANSYAH", "description : "+description);
            String[] range = description.split("-");
            int[] split = new int[range.length];
            int i;
            for(i=0;i<range.length;i++){
                String[] split1 = range[i].split(" ");
                split[i] = Integer.parseInt(reverseDate(split1));
            }
            if(split.length  >1 ){
                String res = String.format("%s - %s", getDateWithYear(split[0], monthNamesAbrev), getDateWithYear(split[1], monthNamesAbrev));
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
            DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
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
        public static final int SEVEN_AGO = -8;
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

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public String getStartDate(){
            if(isStartDate) {
                Calendar cal = Calendar.getInstance();
                if(startDate == -1){
                    cal.add(Calendar.DATE, SEVEN_AGO);
                    startDate = cal.getTimeInMillis();
                }else{
                    cal.setTimeInMillis(startDate);
                }
                System.out.println("Yesterday's date = " + cal.getTime());
                DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
                return dateFormat.format(cal.getTime());
            }else
                return "21/01/1992";
        }

        public String getEndDate(){
            if(isEndDate){
                Calendar cal = Calendar.getInstance();
                if(endDate== -1) {
                    cal.add(Calendar.DATE, YESTERDAY);
                    endDate = cal.getTimeInMillis();
                }else{
                    cal.setTimeInMillis(endDate);
                }
                System.out.println("Yesterday's date = " + cal.getTime());
                DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
                return dateFormat.format(cal.getTime());
            }else{
                return "21/01/1992";
            }
        }
    }

    public static class DatePickerRules{
        public long maxSDate;
        private long maxLimit;
        private long minLimit;
        private int rangeLimit;
        public long maxEDate;

        private long sDate = -1;
        private long eDate = -1;

        public interface DatePickerRulesListener{
            void exceedSDate();
            void exceedEDate();
            void resetToSDate(long sDate, long eDate);
            void resetToEDate(long sDate, long eDate);
            void successSDate(long sDate, long eDate);
            void successEDate(long sDate, long eDate);
            void promptUserExceedLimit();
        }

        DatePickerRulesListener datePickerRulesListener;
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);

        public DatePickerRules(long maxLimit, long minLimit, int rangeLimit, long maxEDate){
            this.maxLimit = maxLimit;
            this.minLimit = minLimit;
            this.rangeLimit = rangeLimit;
            this.maxEDate = maxEDate;

            Calendar instance = getInstance();
            instance.setTimeInMillis(maxEDate);
            instance.add(Calendar.DATE, (-1*rangeLimit));
            this.maxSDate = instance.getTimeInMillis();
        }

        public void setDatePickerRulesListener(DatePickerRulesListener datePickerRulesListener) {
            this.datePickerRulesListener = datePickerRulesListener;
        }

        private Calendar getInstance(){
            return Calendar.getInstance();
        }

        public long getsDate() {
            return sDate;
        }

        public void setsDate(long sDate) {
            Log.d("MNORMANSYAH", "# "+getDateFormat(sDate)+" & "+getDateFormat(maxLimit)+" & "+ getDateFormat(minLimit));
            if(sDate > maxLimit || sDate < minLimit ) {
                if(datePickerRulesListener != null){
                    datePickerRulesListener.promptUserExceedLimit();
                }
                return;
            }

            if(sDate < maxSDate){
                maxSDate = sDate;
                this.sDate = sDate;

                Calendar instance = getInstance();
                instance.setTimeInMillis(sDate);
                instance.add(Calendar.DATE, rangeLimit);

                if(instance.getTimeInMillis() >= maxLimit){
                    instance.setTimeInMillis(maxLimit);
                }

                if(instance.getTimeInMillis() <= minLimit){
                    instance.setTimeInMillis(minLimit);
                }

                maxEDate = instance.getTimeInMillis();
                eDate = maxEDate;

                if(datePickerRulesListener != null){
                    datePickerRulesListener.resetToSDate(sDate, eDate);
                }
                return;
            }

            if(sDate == eDate){
                this.eDate = sDate;
                this.sDate = sDate;
                if(datePickerRulesListener != null){
                    datePickerRulesListener.successSDate(sDate, eDate);
                }
                return;
            }

            if(eDate != -1){
                if(eDate > sDate){
                    this.sDate = sDate;
                    if(datePickerRulesListener != null){
                        datePickerRulesListener.successSDate(sDate, eDate);
                    }
                }else{
                    // when start date bigger than end date
                    // then start date and upper become new range
                    Calendar instance = getInstance();
                    instance.setTimeInMillis(sDate);
                    instance.add(Calendar.DATE, rangeLimit);
                    long eDates = instance.getTimeInMillis();

                    instance = getInstance();
                    instance.setTimeInMillis(eDates);

                    Calendar instance2 = getInstance();
                    instance2.setTimeInMillis(maxLimit);
                    Log.d("MNORMANSYAH", dateFormat.format(instance.getTime())+" & "+dateFormat.format(instance2.getTime()));

                    if(eDates > maxLimit){
                        instance = getInstance();
                        instance.setTimeInMillis(maxLimit);
                        instance.add(Calendar.DATE, -1);
                        Log.d("MNORMANSYAH", "set end date exceed ## "+dateFormat.format(instance.getTimeInMillis()));
                        eDate = instance.getTimeInMillis();
                        maxEDate = eDate;
                    }else{
                        Log.d("MNORMANSYAH", "set end date normal ## "+dateFormat.format(eDates));
                        eDate = eDates;
                        maxEDate = eDates;
                    }

                    this.sDate = sDate;
                    this.maxSDate = sDate;
                }
            }

//            if(sDate > eDate && eDate != -1){
//                if(datePickerRulesListener != null){
//                    datePickerRulesListener.exceedSDate();
//                }
//                return;
//            }else

            Log.d("MNORMANSYAH ", "eDate "+getDateFormat(eDate)+" maxSDate "+getDateFormat(maxSDate)+" eDate"+ getDateFormat(maxEDate));
            if(sDate >= maxSDate && sDate <= maxEDate){
                this.sDate = sDate;
                if(datePickerRulesListener != null){
                    datePickerRulesListener.successSDate(sDate, eDate);
                }
            }
        }

        public long geteDate() {
            return eDate;
        }

        public void seteDate(long eDate) {
            if(eDate > maxLimit || eDate < minLimit ) {
                if(datePickerRulesListener != null){
                    datePickerRulesListener.promptUserExceedLimit();
                }
                return;
            }

            if(eDate > maxEDate){
                maxEDate = eDate;
                this.eDate = eDate;

                Calendar instance = getInstance();
                instance.setTimeInMillis(eDate);
                instance.add(Calendar.DATE, -1*rangeLimit);

                if(instance.getTimeInMillis() >= maxLimit){
                    instance.setTimeInMillis(maxLimit);
                }

                if(instance.getTimeInMillis() <= minLimit){
                    instance.setTimeInMillis(minLimit);
                }

                maxSDate = instance.getTimeInMillis();
                sDate = maxSDate;

                if(datePickerRulesListener != null){
                    datePickerRulesListener.resetToEDate(sDate, eDate);
                }
                return;
            }

            if(sDate == eDate){
                this.eDate = sDate;
                this.sDate = sDate;
                if(datePickerRulesListener != null){
                    datePickerRulesListener.successSDate(sDate, eDate);
                }
                return;
            }

            if(sDate != -1){
                if(eDate > sDate){
                    this.eDate = eDate;
                    if(datePickerRulesListener != null){
                        datePickerRulesListener.successEDate(sDate, eDate);
                    }
                }else{
                    // when end date lower than start date
                    // then end date and lower become new range
                    Calendar instance = getInstance();
                    instance.setTimeInMillis(eDate);
                    instance.add(Calendar.DATE, -rangeLimit);
                    long sDates = instance.getTimeInMillis();

                    instance = getInstance();
                    instance.setTimeInMillis(sDates);

                    Calendar instance2 = getInstance();
                    instance2.setTimeInMillis(minLimit);
                    DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
                    Log.d("MNORMANSYAH", dateFormat.format(instance.getTime())+" & "+dateFormat.format(instance2.getTime()));

                    if(sDates < minLimit){
                        instance = getInstance();
                        instance.setTimeInMillis(minLimit);
//                        instance.add(Calendar.DATE, 1);
                        sDate = instance.getTimeInMillis();
                        maxSDate = sDate;
                    }else{
                        sDate = sDates;
                        maxSDate = sDates;
                    }

                    this.eDate = eDate;
                    this.maxEDate= eDate;
                }
            }

//            if(sDate > eDate && sDate != -1){
//                if(datePickerRulesListener != null){
//                    datePickerRulesListener.exceedEDate();
//                }
//                return;
//            }else

            Log.d("MNORMANSYAH ", "eDate "+getDateFormat(eDate)+" maxSDate "+getDateFormat(maxSDate)+" eDate"+ getDateFormat(maxEDate));
            if(eDate >= maxSDate && eDate <= maxEDate){
                this.eDate = eDate;
                if(datePickerRulesListener != null){
                    datePickerRulesListener.successEDate(sDate, eDate);
                }
            }
        }
    }
}
