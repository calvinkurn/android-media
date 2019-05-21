package com.tokopedia.instantloan.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.data.model.response.LoanPeriodType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLoanParamActivity extends BaseActivity {

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_SELECTED_SORT = "EXTRA_SELECTED_SORT";
    public static final String EXTRA_SELECTED_NAME = "EXTRA_SELECTED_NAME";

    RecyclerView recyclerView;
    View buttonClose;
    private TextView topBarTitle;
    private ListAdapter adapter;
    public static final String SORT_ACTION_INTENT = "com.tokopedia.core" + ".SORT";
    private static final String TAG = SelectLoanParamActivity.class.getSimpleName();
    private ArrayList<LoanPeriodType> data;
    private int selectedKey;
    private String selectedValue;

    public static Intent createInstance(Context context, ArrayList<LoanPeriodType> sort, HashMap<String, String> selectedSort) {
        Intent intent = new Intent(context, SelectLoanParamActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_DATA, sort);
        if (selectedSort != null) {
            intent.putExtra(EXTRA_SELECTED_SORT, selectedSort);
        }
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SORT_PRODUCT;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_loan_param);
        topBarTitle = (TextView) findViewById(R.id.top_bar_title);
        topBarTitle.setText(getString(R.string.title_sort_but));
        recyclerView = (RecyclerView) findViewById(R.id.list);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        data = getIntent().getExtras().getParcelableArrayList(EXTRA_DATA);
        generateSelectedKeyValue((HashMap<String, String>) getIntent().getSerializableExtra(EXTRA_SELECTED_SORT));
        adapter = new ListAdapter(data, selectedKey, selectedValue, new OnItemClickListener() {
            @Override
            public void onItemClicked(int sort, String ob, String label) {
                Intent intent = new Intent();
                HashMap<String, String> params = new HashMap<>();
                params.put(String.valueOf(sort), ob);
                intent.putExtra(EXTRA_SELECTED_SORT, params);
                intent.putExtra(EXTRA_SELECTED_NAME, label);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(adapter);

    }

    private void generateSelectedKeyValue(HashMap<String, String> selectedSort) {
        if (selectedSort == null) {
            return;
        }

        for (Map.Entry<String, String> entry : selectedSort.entrySet()) {
            selectedKey = Integer.parseInt(entry.getKey());
            selectedValue = entry.getValue();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.tokopedia.core2.R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.tokopedia.core2.R.id.action_close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, com.tokopedia.core2.R.anim.push_down);
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private int selectedKey;
        private String selectedValue;
        List<LoanPeriodType> sortList;
        OnItemClickListener clickListener;

        public ListAdapter(List<LoanPeriodType> sortList, int selectedKey, String selectedValue, OnItemClickListener clickListener) {
            if (sortList == null) {
                this.sortList = new ArrayList<>();
            } else {
                this.sortList = sortList;
            }
            this.selectedKey = selectedKey;
            this.selectedValue = selectedValue;
            this.clickListener = clickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.core2.R.layout.sort_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.title.setText(sortList.get(position).getLabel());
            holder.title.setTag(sortList.get(position).getValue());
            if (selectedValue == null) {
                if (position == 0) {
                    holder.title.setSelected(true);
                }
            } else {
                if (sortList.get(position).getLabel().equals(selectedKey)
                        && sortList.get(position).getValue().equals(selectedValue)) {
                    holder.title.setSelected(true);
                } else {
                    holder.title.setSelected(false);
                }
            }
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) {
                        return;
                    }
                    selectedKey = sortList.get(holder.getAdapterPosition()).getId();
                    selectedValue = sortList.get(holder.getAdapterPosition()).getValue();
                    String selectedName = sortList.get(holder.getAdapterPosition()).getLabel();

                    clickListener.onItemClicked(selectedKey, selectedValue, selectedName);

                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return sortList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // each data item is just a string in this case
            public TextView title;

            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(com.tokopedia.core2.R.id.title);
                title.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                textView.setSelected(true);
                clickListener.onItemClicked(sortList.get(getAdapterPosition()).getId(),
                        sortList.get(getAdapterPosition()).getValue(),
                        sortList.get(getAdapterPosition()).getLabel());
                notifyDataSetChanged();
            }

        }

    }

    private interface OnItemClickListener {
        void onItemClicked(int id, String ob, String label);
    }
}