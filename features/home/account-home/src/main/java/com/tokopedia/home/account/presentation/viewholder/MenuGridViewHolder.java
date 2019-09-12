package com.tokopedia.home.account.presentation.viewholder;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.adapter.MenuGridAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.util.MenuGridSpacingDecoration;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridViewHolder extends AbstractViewHolder<MenuGridViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_menu_grid;

    private TextView textTitle;
    private TextView textLink;
    private RecyclerView recyclerCategory;
    private MenuGridAdapter adapter;
    private AccountItemListener listener;
    private RecyclerView.RecycledViewPool viewPool;

    public MenuGridViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        this.listener = listener;
        textTitle = itemView.findViewById(R.id.text_title);
        textLink = itemView.findViewById(R.id.text_link);
        recyclerCategory = itemView.findViewById(R.id.recycler_category);
        adapter = new MenuGridAdapter(listener);
        recyclerCategory.setAdapter(adapter);
        recyclerCategory.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4, LinearLayoutManager.VERTICAL, false));
        recyclerCategory.addItemDecoration(new MenuGridSpacingDecoration(4, 0, 2, false));
    }

    @Override
    public void bind(MenuGridViewModel element) {
        if (!TextUtils.isEmpty(element.getTitle())) {
            textTitle.setText(element.getTitle());
        }

        if (!TextUtils.isEmpty(element.getLinkText())) {
            textLink.setText(element.getLinkText());
            textLink.setOnClickListener(v -> listener.onMenuGridLinkClicked(element));
        }

        adapter.setNewData(element.getItems());
    }
}
