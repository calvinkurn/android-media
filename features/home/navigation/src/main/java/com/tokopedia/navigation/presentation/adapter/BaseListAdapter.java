package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.navigation.presentation.adapter.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> items;
    protected Context context;

    protected boolean withFooter = false;

    protected OnItemClickListener onItemClickListener;

    public BaseListAdapter(Context context) {
        this.context = context;
        items = new ArrayList<T>();
    }

    @Override
    public int getItemCount() {
        try {
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    protected View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(getItemResourceLayout(viewType), parent, false);
    }

    protected abstract int getItemResourceLayout(int viewType);

    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (items != null && items.size() > 0) holder.bind(items.get(position));
    }

    public T getItem(int position) {
        try {
            return items.get(position);
        } catch (IndexOutOfBoundsException e) { /* ignore */ }
        return null;
    }

    public List<T> getItems() {
        return items;
    }

    public void add(T item) {
        items.add(item);
        notifyItemInserted(withFooter ? items.size() : items.size() - 1);
    }

    public void add(T item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void addAll(List<T> items) {
        for (T item : items) {
            add(item);
        }
    }

    public void addOrUpdate(T item) {
        int i = items.indexOf(item);
        if (i >= 0) {
            items.set(i, item);
            notifyItemChanged(i);
        } else {
            add(item);
        }
    }

    public void addOrUpdate(List<T> items) {
        int size = items.size();
        for (int i = 0; i < size; i++) {
            T item = items.get(i);
            int x = items.indexOf(item);
            if (x >= 0) {
                items.set(x, item);
            } else {
                add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void remove(T item) {
        int position = items.indexOf(item);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void remove(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void insert(T item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void clear() {
        if (items != null && !items.isEmpty()) {
            notifyItemRangeRemoved(0, getItemCount());
            items.clear();
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
