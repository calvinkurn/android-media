package com.tokopedia.topads.common;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.topads.common.data.model.TopAdsOptionMenu;
import com.tokopedia.topads.common.view.adapter.TopAdsOptionMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi.putra on 24/05/18.
 */

public class TopAdsMenuBottomSheets extends BottomSheets {
    private String title;
    private List<TopAdsOptionMenu> optionMenus = new ArrayList<>();
    private int mode;
    private int selectedId;
    private OnMenuItemSelected menuItemSelected;
    private Context mContext;

    public TopAdsMenuBottomSheets setTitle(String title) {
        this.title = title;
        return this;
    }

    public TopAdsMenuBottomSheets setContext(Context context) {
        this.mContext = context;
        return this;
    }

    public TopAdsMenuBottomSheets setMode(int mode) {
        this.mode = mode;
        return this;
    }

    public TopAdsMenuBottomSheets setMenuItemSelected(OnMenuItemSelected menuItemSelected) {
        this.menuItemSelected = menuItemSelected;
        return this;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void initView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        TopAdsOptionMenuAdapter adapter = new TopAdsOptionMenuAdapter(mode, optionMenus);
        adapter.setOptionMenuItemSelected(menuItemSelected);
        if (mode == TopAdsOptionMenuAdapter.MODE_CHECKABLE){
            adapter.setSelectedId(selectedId);
        }
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        ImageButton btnClose = getDialog().findViewById(com.tokopedia.design.R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected String title() {
        return title;
    }

    public void addItem(int id, String title, boolean isSelected) {
        if (isSelected){
            selectedId = id;
        }
        optionMenus.add(new TopAdsOptionMenu(title, id));
    }

    public TopAdsMenuBottomSheets setMenu(@MenuRes int menuId) {
        Menu menu = new MenuBuilder(mContext);
        new SupportMenuInflater(mContext).inflate(menuId, menu);

        for (int i = 0; i < menu.size(); ++i){
            MenuItem item = menu.getItem(i);
            addItem(item.getItemId(), item.getTitle().toString(), false);
        }

        return this;
    }

    public interface OnMenuItemSelected{
        void onItemSelected(int itemId);
    }
}
