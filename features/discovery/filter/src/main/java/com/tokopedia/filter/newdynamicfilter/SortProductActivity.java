package com.tokopedia.filter.newdynamicfilter;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erry on 7/12/2016.
 */
@SuppressWarnings("unchecked")
public class SortProductActivity extends BaseActivity {

    public static final String EXTRA_SORT_DATA = "EXTRA_SORT_DATA";
    public static final String EXTRA_SELECTED_SORT = "EXTRA_SELECTED_SORT";
    public static final String EXTRA_AUTO_APPLY_FILTER = "EXTRA_AUTO_APPLY_FILTER";
    public static final String EXTRA_SELECTED_SORT_NAME = "EXTRA_SELECTED_SORT_NAME";
    public static final String SCREEN_SORT_PRODUCT = "Sort Produk Activity";

    RecyclerView recyclerView;
    View buttonClose;
    private TextView topBarTitle;
    private ListAdapter adapter;
    private ArrayList<Sort> data;
    private String selectedKey;
    private String selectedValue;

    @Override
    public String getScreenName() {
        return SCREEN_SORT_PRODUCT;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sort);
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
        data = getIntent().getExtras().getParcelableArrayList(EXTRA_SORT_DATA);
        generateSelectedKeyValue((HashMap<String, String>) getIntent().getSerializableExtra(EXTRA_SELECTED_SORT));
        adapter = new ListAdapter(data, selectedKey, selectedValue, new OnItemClickListener() {
            @Override
            public void onItemClicked(Sort item) {
                Intent intent = new Intent();
                HashMap<String, String> params = new HashMap<>();
                params.put(item.getKey(), item.getValue());
                intent.putExtra(EXTRA_SELECTED_SORT, params);
                intent.putExtra(EXTRA_AUTO_APPLY_FILTER, item.getApplyFilter());
                intent.putExtra(EXTRA_SELECTED_SORT_NAME, item.getName());
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
            selectedKey = entry.getKey();
            selectedValue = entry.getValue();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_down);
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private String selectedKey;
        private String selectedValue;
        List<Sort> sortList;
        OnItemClickListener clickListener;

        public ListAdapter(List<Sort> sortList, String selectedKey, String selectedValue, OnItemClickListener clickListener) {
            if(sortList==null){
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_sort_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.title.setText(sortList.get(position).getName());
            holder.title.setTag(sortList.get(position).getValue());
            if (selectedKey == null && selectedValue == null) {
                if (position == 0) {
                    holder.title.setSelected(true);
                }
            } else {
                if (sortList.get(position).getKey().equals(selectedKey)
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
                    selectedKey = sortList.get(holder.getAdapterPosition()).getKey();
                    selectedValue = sortList.get(holder.getAdapterPosition()).getValue();

                    clickListener.onItemClicked(sortList.get(holder.getAdapterPosition()));

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
                title = (TextView) v.findViewById(R.id.title);
                title.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                textView.setSelected(true);
                clickListener.onItemClicked(sortList.get(getAdapterPosition()));
                notifyDataSetChanged();
            }

        }

    }

    private interface OnItemClickListener {
        void onItemClicked(Sort sortItem);
    }
}
