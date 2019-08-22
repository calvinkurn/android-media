package com.tokopedia.home.account.presentation.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewholder.MenuGridItemViewHolder;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridAdapter extends RecyclerView.Adapter<MenuGridItemViewHolder> {
    private List<MenuGridItemViewModel> categories;
    private AccountItemListener listener;

    public MenuGridAdapter(AccountItemListener listener) {
        this.categories = new ArrayList<>();
        this.listener = listener;

    }

    @NonNull
    @Override
    public MenuGridItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_grid_item, parent, false);
        return new MenuGridItemViewHolder(parent.getContext(), view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuGridItemViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setNewData(List<MenuGridItemViewModel> items) {
        if(items != null) {
            categories.clear();
            categories.addAll(items);
            notifyDataSetChanged();
        }
    }
}
