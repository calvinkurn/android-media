package com.tokopedia.design.component;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseBottomSheetView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 2/13/18.
 */

public class Menus extends BaseBottomSheetView {

    public interface OnItemMenuClickListener {
        void onClick(ItemMenus itemMenus, int pos);
    }

    public Menus(@NonNull Context context) {
        super(context);
    }

    public Menus(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected Menus(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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

    private MenusAdapter menusAdapter;

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        menusAdapter.setTitle(this.getContext().getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        menusAdapter.setTitle(title == null ? null : title.toString());
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

    private class MenusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private int TYPE_HEADER = 0;
        private int TYPE_FOOTER = 1;
        private int TYPE_ITEM = 2;

        private List<ItemMenus> itemMenusList;

        private OnItemMenuClickListener onItemMenuClickListener;
        private String btnActionText;
        private String title;
        private View.OnClickListener btnActionClickListener;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public ImageView icon, iconEnd;

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.tv_title_menu);
                icon = view.findViewById(R.id.iv_icon_menu);
                iconEnd = view.findViewById(R.id.iv_icon_menu_end);
            }
        }

        private class FooterViewHolder extends RecyclerView.ViewHolder {

            private Button button;

            FooterViewHolder(View view) {
                super(view);
                button = view.findViewById(R.id.btn_action_menus);
            }
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {

            private TextView tvTitle;
            private View buttonCLose;

            HeaderViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tv_title);
                buttonCLose = view.findViewById(R.id.btn_close);
            }
        }

        private MenusAdapter() {
            itemMenusList = new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_menu_item, parent, false);
                return new ViewHolder(itemView);
            } else if (viewType == TYPE_HEADER) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_menu_item_title, parent, false);
                return new HeaderViewHolder(itemView);
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
            } else if (viewHolder instanceof HeaderViewHolder) {
                final HeaderViewHolder header = (HeaderViewHolder) viewHolder;
                header.buttonCLose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                header.tvTitle.setText(title);
            } else if (viewHolder instanceof ViewHolder) {
                final ItemMenus itemMenus = itemMenusList.get(i - headerCount());
                final ViewHolder holder = (ViewHolder) viewHolder;
                 if (itemMenus.icon > 0) {
                    holder.icon.setImageResource(itemMenus.icon);
                    holder.icon.setVisibility(View.VISIBLE);
                } else {
                    holder.icon.setVisibility(View.GONE);
                }
                if (itemMenus.iconEnd > 0) {
                    holder.iconEnd.setImageResource(itemMenus.iconEnd);
                    holder.iconEnd.setVisibility(View.VISIBLE);
                } else {
                    holder.iconEnd.setVisibility(View.GONE);
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

        private void setTitle(String title) {
            this.title = title;
        }

        private void setOnActionClickListener(View.OnClickListener listener) {
            this.btnActionClickListener = listener;
        }

        private void setOnItemMenuClickListener(OnItemMenuClickListener onItemMenuClickListener) {
            this.onItemMenuClickListener = onItemMenuClickListener;
        }

        private boolean hasFooter() {
            return !TextUtils.isEmpty(btnActionText);
        }

        private int footerCount() {
            return hasFooter() ? 1 : 0;
        }

        private int headerCount() {
            return hasTitle() ? 1 : 0;
        }

        private boolean hasTitle() {
            return !TextUtils.isEmpty(title);
        }

        @Override
        public int getItemCount() {
            return itemMenusList.size() + footerCount() + headerCount();
        }

        @Override
        public int getItemViewType(int position) {
            if (hasTitle() && position == 0) {
                return TYPE_HEADER;
            }
            if (hasFooter() && position == (itemMenusList.size() + headerCount())) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
    }

    public static class ItemMenus {

        public String title;
        public int icon;
        public @DrawableRes  int iconEnd;

        public ItemMenus(String title) {
            this.title = title;
        }

        public ItemMenus(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
    }
}