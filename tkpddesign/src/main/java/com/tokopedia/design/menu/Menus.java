package com.tokopedia.design.menu;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.bottomsheet.BaseBottomSheetView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 2/13/18.
 *
 * How to use?
 *
 * Menus menus = new Menus(context);
 * menus.setItemMenuList(string array / String[] / List<ItemMenus>);
 * menus.setActionText("Button Action");
 * menus.setOnActionClickListener(View.OnClickListener);
 * menus.setOnItemMenuClickListener(Menus.OnItemMenuClickListener);
 * menus.show();
 * menus.dismiss();
 */

public class Menus extends BaseBottomSheetView {

    private MenusAdapter menusAdapter;

    public Menus(@NonNull Context context) {
        super(context);
    }

    public Menus(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected Menus(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.widget_menu;
    }

    @Override
    protected void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_menu);

        menusAdapter = new MenusAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(menusAdapter);
    }

    public List<ItemMenus> getItemMenuList() {
        return menusAdapter.itemMenusList;
    }

    public ItemMenus getItemMenu(int position) {
        return menusAdapter.itemMenusList.get(position);
    }

    public void setItemMenuList(List<ItemMenus> itemMenusList) {
        this.menusAdapter.itemMenusList = itemMenusList;
        menusAdapter.notifyDataSetChanged();
    }

    public void setItemMenuList(@ArrayRes int stringArray) {
        String[] menus = this.getContext().getResources().getStringArray(stringArray);
        setItemMenuList(menus);
    }

    public void setItemMenuList(String[] menus) {
        List<ItemMenus> itemMenus = new ArrayList<>();
        for (String title : menus) {
            itemMenus.add(new ItemMenus(title));
        }
        this.menusAdapter.itemMenusList = itemMenus;
        menusAdapter.notifyDataSetChanged();
    }

    public void setOnItemMenuClickListener(OnItemMenuClickListener listener) {
        menusAdapter.setOnItemMenuClickListener(listener);
    }

    public void setActionText(String actionText) {
        if (menusAdapter != null) {
            menusAdapter.setButtonActionText(actionText);
        }
    }

    public void setOnActionClickListener(View.OnClickListener listener) {
        if (menusAdapter != null) {
            menusAdapter.setOnActionClickListener(listener);
        }
    }

    public interface OnItemMenuClickListener {
        void onClick(ItemMenus itemMenus, int pos);
    }

    private class MenusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private int TYPE_FOOTER = 1;
        private int TYPE_ITEM = 2;

        private List<ItemMenus> itemMenusList;

        private OnItemMenuClickListener onItemMenuClickListener;
        private String btnActionText;
        private View.OnClickListener btnActionClickListener;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public ImageView icon;

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.tv_title_menu);
                icon = view.findViewById(R.id.iv_icon_menu);
            }
        }

        private class FooterViewHolder extends RecyclerView.ViewHolder {

            private Button button;

            FooterViewHolder(View view) {
                super(view);
                button = view.findViewById(R.id.btn_action_menus);
            }
        }

        MenusAdapter() {
            itemMenusList = new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_menu_item, parent, false);
                return new ViewHolder(itemView);
            } else if (viewType == TYPE_FOOTER) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_menu_item_action, parent, false);
                return new FooterViewHolder(itemView);
            } else return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof FooterViewHolder) {
                final FooterViewHolder footer = (FooterViewHolder) viewHolder;
                footer.button.setOnClickListener(btnActionClickListener);
                footer.button.setText(btnActionText);
            } else if (viewHolder instanceof ViewHolder) {
                final ItemMenus itemMenus = itemMenusList.get(i);
                final ViewHolder holder = (ViewHolder) viewHolder;
                if (itemMenus.icon != 0) {
                    holder.icon.setImageResource(itemMenus.icon);
                    holder.icon.setVisibility(View.VISIBLE);
                } else {
                    holder.icon.setVisibility(View.GONE);
                }
                holder.title.setText(itemMenus.title);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemMenuClickListener != null)
                            onItemMenuClickListener.onClick(itemMenus, holder.getAdapterPosition());
                    }
                });
            }
        }

        private void setButtonActionText(String btnActionText) {
            this.btnActionText = btnActionText;
        }

        private void setOnActionClickListener(View.OnClickListener listener) {
            this.btnActionClickListener = listener;
        }

        private void setOnItemMenuClickListener(OnItemMenuClickListener onItemMenuClickListener) {
            this.onItemMenuClickListener = onItemMenuClickListener;
        }

        @Override
        public int getItemCount() {
            return itemMenusList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == itemMenusList.size()) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
    }
}