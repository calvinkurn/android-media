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
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
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

    private ShopSearch boundedShopSearch;
    private int searchQueryStartIndexInKeyword = -1;

    public ShopViewHolder(View itemView, ItemClickListener clickListener, String tabName) {
        super(itemView);

        this.context = itemView.getContext();
        this.listener = clickListener;
        this.tabName = tabName;

        initView();
    }

    private void initView() {
        titleTextView = itemView.findViewById(R.id.titleTextView);
        location = itemView.findViewById(R.id.subTitleTextView);
        badgesOfficialStore = itemView.findViewById(R.id.badgesOfficialStore);
        shopAvatar = itemView.findViewById(R.id.shop_avatar);
        iconCopyTextView = itemView.findViewById(R.id.icon);
    }

    @Override
    public void bind(final ShopSearch element) {
        boundedShopSearch = element;

        setSearchQueryStartIndexInKeyword();

        setTitle();
        setSubtitle();
        loadImageIntoShopAvatar();
        setBadgesOfficialStoreIfOfficial();
        setItemViewOnClickListener();
        setIconCopyTextViewOnClickListener();
    }

    private void setSearchQueryStartIndexInKeyword() {
        String displayName = boundedShopSearch.getKeyword();
        String searchTerm = boundedShopSearch.getSearchTerm();

        searchQueryStartIndexInKeyword = !TextUtils.isEmpty(searchTerm) ?
                displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault())) : -1;
    }

    private void setTitle() {
        if(searchQueryStartIndexInKeyword == -1){
            titleTextView.setText(boundedShopSearch.getKeyword().toLowerCase());
        } else {
            titleTextView.setText(getHighlightedTitle());
        }
    }

    private SpannableString getHighlightedTitle() {
        SpannableString highlightedTitle = new SpannableString(boundedShopSearch.getKeyword());

        highlightTitleBeforeKeyword(highlightedTitle);

        highlightTitleAfterKeyword(highlightedTitle);

        return highlightedTitle;
    }

    private void highlightTitleBeforeKeyword(SpannableString highlightedTitle) {
        highlightedTitle.setSpan(new TextAppearanceSpan(context, R.style.searchTextHiglight),
                0, searchQueryStartIndexInKeyword, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void highlightTitleAfterKeyword(SpannableString highlightedTitle) {
        int highlightAfterKeywordStartIndex = searchQueryStartIndexInKeyword + boundedShopSearch.getSearchTerm().length();
        int highlightAfterKeywordEndIndex = boundedShopSearch.getKeyword().length();

        highlightedTitle.setSpan(
                new TextAppearanceSpan(context, R.style.searchTextHiglight),
                highlightAfterKeywordStartIndex, highlightAfterKeywordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void setSubtitle() {
        location.setText(boundedShopSearch.getLocation());
    }

    private void loadImageIntoShopAvatar() {
        ImageHandler.loadImageCircle2(context, shopAvatar, boundedShopSearch.getImageUrl());
    }

    private void setBadgesOfficialStoreIfOfficial() {
        if(boundedShopSearch.isOfficial()){
            badgesOfficialStore.setVisibility(View.VISIBLE);
        } else {
            badgesOfficialStore.setVisibility(View.GONE);
        }
    }

    private void setItemViewOnClickListener() {
        itemView.setOnClickListener(v -> {
            AutoCompleteTracking.eventClickShopSearch(
                    itemView.getContext(),
                    String.format(
                            "keyword: %s - shop: %s - applink: %s",
                            boundedShopSearch.getSearchTerm(),
                            boundedShopSearch.getKeyword(),
                            boundedShopSearch.getApplink()
                    ),
                    tabName
            );
            listener.onItemClicked(boundedShopSearch.getApplink(), boundedShopSearch.getUrl());
        });
    }

    private void setIconCopyTextViewOnClickListener() {
        iconCopyTextView.setOnClickListener(v -> listener.copyTextToSearchView(boundedShopSearch.getKeyword()));
    }
}
