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
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;

import java.util.List;



public class TopEventsSuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SearchViewModel> categoryItems;
    private DealsSearchPresenter mPresenter;
    private String highLightText;
    private String lowerhighlight;
    private String upperhighlight;
    private boolean isFooterAdded = false;

    private static final int ITEM = 1;
    private static final int FOOTER = 2;

    public TopEventsSuggestionsAdapter(Context context, List<SearchViewModel> categoryItems, DealsSearchPresenter presenter) {
        this.mContext = context;
        this.mPresenter = presenter;
        this.categoryItems = categoryItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.simple_recycler_item, parent, false);
                holder = new DealsTitleHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
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
                ((DealsTitleHolder) holder).setEventTitle(position, categoryItems.get(position));
                break;
            case FOOTER:
                break;
            default:
                break;
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
            add(new SearchViewModel());
        }
    }

    private SearchViewModel getItem(int position) {
        return categoryItems.get(position);
    }

    public void add(SearchViewModel item) {
        categoryItems.add(item);
        notifyItemInserted(categoryItems.size() - 1);
    }

    public void addAll(List<SearchViewModel> items) {
        for (SearchViewModel item : items) {
            add(item);
        }
    }

    private void remove(SearchViewModel item) {
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
            SearchViewModel item = getItem(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }


    public class DealsTitleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvDealTitle;

        View itemView;
        SearchViewModel valueItem;
        int mPosition;

        private DealsTitleHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            itemView.setOnClickListener(this);
            tvDealTitle =itemView.findViewById(R.id.tv_simple_item);
        }

        private void setEventTitle(int position, SearchViewModel value) {
            this.valueItem = value;
            this.mPosition = position;
            SpannableString spannableString = new SpannableString(valueItem.getTitle());
            if (highLightText != null && !highLightText.isEmpty() && Utils.containsIgnoreCase(valueItem.getTitle(), highLightText)) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                int fromindex = valueItem.getTitle().indexOf(highLightText);
                if (fromindex == -1) {
                    fromindex = valueItem.getTitle().indexOf(lowerhighlight);
                }
                if (fromindex == -1) {
                    fromindex = valueItem.getTitle().indexOf(upperhighlight);
                }
                int toIndex = fromindex + highLightText.length();
                spannableString.setSpan(styleSpan, fromindex, toIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvDealTitle.setText(spannableString);
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
            loadingLayout=itemView.findViewById(R.id.loading_fl);
        }
    }

}
