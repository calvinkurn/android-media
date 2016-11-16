package com.tokopedia.core.manage.shop.notes.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nisie on 10/26/16.
 */

public class ShopNotesAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_NOTE = 100;
    public static final String RETURN_POLICY = "2";

    public interface ActionShopNotesListener {
        void onDeleteNote(ShopNote shopNote);

        void onEditNote(ShopNote shopNote);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.notes_name)
        TextView title;

        @Bind(R2.id.delete_notes)
        ImageView deleteButton;

        @Bind(R2.id.edit_notes)
        ImageView editButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<ShopNote> list;
    private final Context context;
    ActionShopNotesListener listener;

    public ShopNotesAdapter(Context context, ActionShopNotesListener listener) {
        this.context = context;
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    public static ShopNotesAdapter createInstance(Context context, ActionShopNotesListener listener) {
        return new ShopNotesAdapter(context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_NOTE:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_shop_notes, null);
                ViewHolder holder = new ShopNotesAdapter.ViewHolder(itemLayoutView);
                ImageHandler.loadImageWithId(holder.deleteButton, R.drawable.remove);
                ImageHandler.loadImageWithId(holder.editButton, R.drawable.ic_action_edit);
                return holder;
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_NOTE:
                bindShopNote((ShopNotesAdapter.ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindShopNote(ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getNoteTitle());
        if (list.get(position).getNoteStatus().equals(RETURN_POLICY)) {
            holder.title.setTypeface(null, Typeface.BOLD);
        } else {
            holder.title.setTypeface(null, Typeface.NORMAL);
        }
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setPosition(position);
                listener.onDeleteNote(list.get(position));
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setPosition(position);
                listener.onEditNote(list.get(position));
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_NOTE;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    public ArrayList<ShopNote> getList() {
        return list;
    }

    public void addList(List<ShopNote> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean hasReturnablePolicy() {
        for (ShopNote shopNote : list) {
            if (shopNote.getNoteStatus().equals(RETURN_POLICY))
                return true;
        }
        return false;
    }
}
