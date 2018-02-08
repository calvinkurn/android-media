package com.tokopedia.design.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.list.item.SectionDividedItem;

import java.util.ArrayList;
import java.util.List;


public abstract class SectionDividedItemAdapter<T extends SectionDividedItem>
        extends RecyclerView.Adapter<SectionDividedItemAdapter.ViewHolder<T>> implements SectionIndexer {

    protected List<T> itemList = new ArrayList<>();

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View parentView = inflater.inflate(R.layout.alphabetical_section_list_item, parent, false);
        FrameLayout itemContainer = (FrameLayout) parentView.findViewById(R.id.item_container);
        View itemView = inflater.inflate(getItemLayout(), parent, false);
        itemContainer.addView(itemView);
        return getViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        holder.bind(itemList.get(position), position > 0 ? itemList.get(position - 1) : null);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public int getPositionForSection(int sectionId) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getSectionId() == sectionId) {
                return i;
            }
        }
        return -1;
    }

    public int getSectionForPosition(int arg0) {
        return 0;
    }

    public Object[] getSections() {
        return null;
    }

    protected abstract int getItemLayout();
    protected abstract ViewHolder<T> getViewHolder(View itemView);

    public abstract static class ViewHolder<T extends SectionDividedItem>
            extends RecyclerView.ViewHolder {

        private TextView sectionHeader;
        private SectionTitleDictionary sectionTitleDictionary;

        public ViewHolder(View itemView, SectionTitleDictionary sectionTitleDictionary) {
            super(itemView);
            sectionHeader = (TextView) itemView.findViewById(R.id.section_header);
            this.sectionTitleDictionary = sectionTitleDictionary;
            initItem(itemView);
        }

        public void bind(T item, T prevItem) {
            int sectionId = item.getSectionId();

            if (prevItem == null) {
                showSectionHeader(sectionId);
            } else {
                if (sectionId != prevItem.getSectionId()) {
                    showSectionHeader(sectionId);
                } else {
                    hideSectionHeader();
                }
            }
            bindItem(item);
        }

        private void showSectionHeader(int sectionId) {
            sectionHeader.setVisibility(View.VISIBLE);
            sectionHeader.setText(sectionTitleDictionary.getSectionTitle(sectionId));
        }

        private void hideSectionHeader() {
            sectionHeader.setVisibility(View.GONE);
        }

        protected abstract void initItem(View itemView);
        protected abstract void bindItem(T item);
    }
}