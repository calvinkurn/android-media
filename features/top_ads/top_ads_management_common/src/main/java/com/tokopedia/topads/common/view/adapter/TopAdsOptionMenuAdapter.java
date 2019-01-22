package com.tokopedia.topads.common.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.common.R;
import com.tokopedia.topads.common.data.model.TopAdsOptionMenu;
import com.tokopedia.topads.common.TopAdsMenuBottomSheets;

import java.util.List;

/**
 * Created by hadi.putra on 24/05/18.
 */

public class TopAdsOptionMenuAdapter extends RecyclerView.Adapter<TopAdsOptionMenuViewHolder> {
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_CHECKABLE = 1;
    private boolean isCheckEnable;
    private List<TopAdsOptionMenu> optionMenus;
    private int selectedId;

    private TopAdsMenuBottomSheets.OnMenuItemSelected onMenuItemSelected;

    public TopAdsOptionMenuAdapter(int mode, List<TopAdsOptionMenu> optionMenus) {
        isCheckEnable = mode == MODE_CHECKABLE;
        this.optionMenus = optionMenus;
        if (optionMenus != null && optionMenus.size() > 0){
            selectedId = optionMenus.get(0).getId();
        }
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public void setOptionMenuItemSelected(TopAdsMenuBottomSheets.OnMenuItemSelected onMenuItemSelected) {
        this.onMenuItemSelected = onMenuItemSelected;
    }

    @Override
    public TopAdsOptionMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topads_menu_option, parent, false);
        return new TopAdsOptionMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopAdsOptionMenuViewHolder holder, final int position) {
        final TopAdsOptionMenu optionMenu = optionMenus.get(position);
        holder.onBind(optionMenu, isCheckEnable && (selectedId == optionMenu.getId()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckEnable){
                    selectedId = position;
                    notifyDataSetChanged();
                }
                if (onMenuItemSelected != null){
                    onMenuItemSelected.onItemSelected(optionMenu.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionMenus.size();
    }
}
