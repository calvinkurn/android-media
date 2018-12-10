package com.tokopedia.discovery.autocomplete.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.ShopSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.util.AutoCompleteTracking;

import java.util.Locale;

public class ShopViewHolder extends AbstractViewHolder<ShopSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_shop_auto_complete;
    private final Context context;
    private final String tabName;

    private TextView titleTextView;
    private TextView location;
    private ImageView badgesOfficialStore;
    private ImageView shopAvatar;
    private ImageView iconCopyTextView;

    private final ItemClickListener listener;

    public ShopViewHolder(View itemView, ItemClickListener clickListener, String tabName) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = clickListener;
        this.tabName = tabName;
        titleTextView = itemView.findViewById(R.id.titleTextView);
        location = itemView.findViewById(R.id.subTitleTextView);
        badgesOfficialStore = itemView.findViewById(R.id.badgesOfficialStore);
        shopAvatar = itemView.findViewById(R.id.shop_avatar);
        iconCopyTextView = itemView.findViewById(R.id.icon);
    }

    @Override
    public void bind(final ShopSearch element) {
        int startIndex = indexOfSearchQuery(element.getKeyword(), element.getSearchTerm());
        if(startIndex == -1){
            titleTextView.setText(element.getKeyword().toLowerCase());
        } else {
            SpannableString highlightedTitle = new SpannableString(element.getKeyword());
            highlightedTitle.setSpan(new TextAppearanceSpan(context, R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlightedTitle.setSpan(new TextAppearanceSpan(context, R.style.searchTextHiglight),
                    startIndex + element.getSearchTerm().length(),
                    element.getKeyword().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleTextView.setText(highlightedTitle);
        }

        Glide.with(context).load(element.getImageUrl()).into(shopAvatar);
        if(element.isOfficial()){
            badgesOfficialStore.setVisibility(View.VISIBLE);
        } else {
            badgesOfficialStore.setVisibility(View.GONE);
        }

        location.setText(element.getLocation());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTracking.eventClickShopSearch(
                        itemView.getContext(),
                        String.format(
                                "keyword: %s - shop: %s - applink: %s",
                                element.getSearchTerm(),
                                element.getKeyword(),
                                element.getApplink()
                        ),
                        tabName
                );
                listener.onItemClicked(element.getApplink(), element.getUrl());
            }
        });

        iconCopyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.copyTextToSearchView(element.getKeyword());
            }
        });
    }

    private int indexOfSearchQuery(String displayName, String searchTerm) {
        if (!TextUtils.isEmpty(searchTerm)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }
}
