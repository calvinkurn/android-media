package com.tokopedia.discovery.activity;

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

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.discovery.model.Sort;
import com.tokopedia.core.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erry on 7/12/2016.
 */
@SuppressWarnings("unchecked")
public class SortProductActivity extends TActivity {

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_SELECTED_SORT = "EXTRA_SELECTED_SORT";
    public static final String EXTRA_SELECTED_NAME = "EXTRA_SELECTED_NAME";

    RecyclerView recyclerView;
    View buttonClose;
    private TextView topBarTitle;
    private ListAdapter adapter;
    public static final String SORT_ACTION_INTENT = "com.tokopedia.core" + ".SORT";
    private static final String TAG = SortProductActivity.class.getSimpleName();
    private ArrayList<Sort> data;
    private String selectedKey;
    private String selectedValue;

    public static Intent createInstance(Context context, ArrayList<Sort> sort, HashMap<String, String> selectedSort) {
        Intent intent = new Intent(context, SortProductActivity.class);
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
        setContentView(com.tokopedia.discovery.R.layout.activity_product_sort);
        topBarTitle = (TextView) findViewById(com.tokopedia.discovery.R.id.top_bar_title);
        topBarTitle.setText(getString(R.string.title_sort_but));
        recyclerView = (RecyclerView) findViewById(com.tokopedia.discovery.R.id.list);
        buttonClose = findViewById(com.tokopedia.discovery.R.id.top_bar_close_button);
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
            public void onItemClicked(String sort, String ob, String label) {
                Intent intent = new Intent();
                HashMap<String, String> params = new HashMap<>();
                params.put(sort, ob);
                intent.putExtra(EXTRA_SELECTED_SORT, params);
                intent.putExtra(EXTRA_SELECTED_NAME, label);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(adapter);

        getWindow()
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
        getMenuInflater().inflate(R.menu.menu_sort, menu);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_list_item, parent, false);
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
                    String selectedName = sortList.get(holder.getAdapterPosition()).getName();

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
                title = (TextView) v.findViewById(R.id.title);
                title.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                textView.setSelected(true);
                clickListener.onItemClicked(sortList.get(getAdapterPosition()).getKey(),
                        sortList.get(getAdapterPosition()).getValue(),
                        sortList.get(getAdapterPosition()).getName());
                notifyDataSetChanged();
            }

        }

    }

    private interface OnItemClickListener {
        void onItemClicked(String sort, String ob, String label);
    }
}
