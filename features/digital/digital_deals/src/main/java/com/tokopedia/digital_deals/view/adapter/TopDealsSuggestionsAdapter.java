package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.presenter.DealsSearchPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.model.ProductItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TopDealsSuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ProductItem> categoryItems;
    private DealsSearchPresenter mPresenter;
    private String highLightText;
    private String lowerhighlight;
    private String upperhighlight;
    private boolean isFooterAdded = false;

    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private final int HEADER = 3;
    private boolean isHeaderAdded = false;

    public TopDealsSuggestionsAdapter(List<ProductItem> categoryItems, DealsSearchPresenter presenter) {
        this.mPresenter = presenter;
        if (categoryItems != null) {
            this.categoryItems=new ArrayList<ProductItem>(categoryItems);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.item_top_suggestions, parent, false);
                holder = new DealsTitleHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            case HEADER:
                v = inflater.inflate(R.layout.header_layout_trending_deals, parent, false);
                holder = new HeaderViewHolder(v);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                ((DealsTitleHolder) holder).setDealTitle(position, categoryItems.get(position));
                break;
            case FOOTER:
                break;
            case HEADER:
                break;
            default:
                break;
        }
    }

    public void addHeader() {
        if (!isHeaderAdded) {
            isHeaderAdded = true;
            categoryItems.add(0, new ProductItem());
            notifyItemInserted(0);
        }
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public void setHighLightText(String text) {
        if (text != null && text.length() > 0) {
            String first = text.substring(0, 1).toUpperCase();
            lowerhighlight = text.toLowerCase();
            upperhighlight = text.toUpperCase();
            highLightText = first + text.substring(1).toLowerCase();
        }
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new ProductItem());
        }
    }

    private ProductItem getItem(int position) {
        return categoryItems.get(position);
    }

    public void add(ProductItem item) {
        categoryItems.add(item);
        notifyItemInserted(categoryItems.size() - 1);
    }

    public void addAll(List<ProductItem> items) {
        for (ProductItem item : items) {
            add(item);
        }
    }

    private void remove(ProductItem item) {
        int position = categoryItems.indexOf(item);
        if (position > -1) {
            categoryItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            ProductItem item = getItem(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && isHeaderAdded) ? HEADER : (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }


    public class DealsTitleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvDealTitle;
        TextView tvBrandName;
        View itemView;
        ProductItem valueItem;
        int mPosition;

        private DealsTitleHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            tvDealTitle = itemView.findViewById(R.id.tv_simple_item);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
        }

        private void setDealTitle(int position, ProductItem value) {
            this.valueItem = value;
            this.mPosition = position;
            SpannableString spannableString = new SpannableString(valueItem.getDisplayName());
            if (highLightText != null && !highLightText.isEmpty() && Utils.containsIgnoreCase(valueItem.getDisplayName(), highLightText)) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                int fromindex = valueItem.getDisplayName().toLowerCase().indexOf(highLightText.toLowerCase());
                if (fromindex == -1) {
                    fromindex = valueItem.getDisplayName().toLowerCase().indexOf(lowerhighlight.toLowerCase());
                }
                if (fromindex == -1) {
                    fromindex = valueItem.getDisplayName().toLowerCase().indexOf(upperhighlight.toLowerCase());
                }
                int toIndex = fromindex + highLightText.length();
                spannableString.setSpan(styleSpan, fromindex, toIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvDealTitle.setText(spannableString);
            tvBrandName.setText(value.getBrand().getTitle());
        }


        @Override
        public void onClick(View v) {
            mPresenter.onSearchResultClick(valueItem);
            notifyItemChanged(mPosition);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            loadingLayout = itemView.findViewById(R.id.loading_fl);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {


        private HeaderViewHolder(View itemView) {
            super(itemView);
        }

    }

}
