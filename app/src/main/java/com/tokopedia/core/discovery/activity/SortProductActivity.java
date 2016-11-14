package com.tokopedia.core.discovery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.discovery.fragment.browseparent.BrowseParentFragment;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.widgets.DividerItemDecoration;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Erry on 7/12/2016.
 */
public class SortProductActivity extends TActivity {

    @Bind(R2.id.toolbar)
    Toolbar toolbar;
    @Bind(R2.id.list)
    RecyclerView recyclerView;
    private ListAdapter adapter;
    public static final String SORT_ACTION_INTENT = BuildConfig.APPLICATION_ID + ".SORT";
    private static final String TAG = SortProductActivity.class.getSimpleName();
    private DynamicFilterModel.Data data;
    private String source;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SORT_PRODUCT;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sort);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        data = Parcels.unwrap(getIntent().getParcelableExtra(BrowseProductActivity.EXTRA_DATA));
        source = getIntent().getStringExtra(BrowseProductActivity.EXTRA_SOURCE);
        adapter = new ListAdapter(data.getSort(), new OnItemClickListener() {
            @Override
            public void onItemClicked(String sort, String ob) {
                data.setSelected(sort);
                data.setSelectedOb(ob);
                Intent intent = new Intent(SORT_ACTION_INTENT);
                intent.putExtra(BrowseParentFragment.SORT_EXTRA, Parcels.wrap(data));
                intent.putExtra(BrowseParentFragment.SOURCE_EXTRA, source);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R2.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_down);
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        List<DynamicFilterModel.Sort> sortList;
        OnItemClickListener clickListener;

        public ListAdapter(List<DynamicFilterModel.Sort> sortList, OnItemClickListener clickListener) {
            if(sortList==null){
                this.sortList = new ArrayList<>();
            } else {
                this.sortList = sortList;
            }
            this.clickListener = clickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d(TAG, "selected item "+data.getSelected());
            holder.title.setText(sortList.get(position).getName());
            holder.title.setTag(sortList.get(position).getValue());
            if (data.getSelected() != null) {
                if (holder.title.getText().equals(data.getSelected())) {
                    holder.title.setSelected(true);
                } else {
                    holder.title.setSelected(false);
                }
            }
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
                clickListener.onItemClicked(sortList.get(getAdapterPosition()).getName(), sortList.get(getAdapterPosition()).getValue());
                notifyDataSetChanged();
            }

        }

    }

    private interface OnItemClickListener {
        void onItemClicked(String sort, String ob);
    }
}
