package com.tokopedia.autocomplete.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.analytics.AutocompleteTracking;
import com.tokopedia.autocomplete.viewmodel.ShopSearch;

import java.util.Locale;

public class ShopViewHolder extends AbstractViewHolder<ShopSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_shop_auto_complete;
    private final Context context;
    private final String tabName;

    private TextView titleTextView;
    private TextView location;
    private ImageView imageViewShopBadge;
    private ImageView shopAvatar;

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
        imageViewShopBadge = itemView.findViewById(R.id.imageViewShopBadge);
        shopAvatar = itemView.findViewById(R.id.shop_avatar);
    }

    @Override
    public void bind(final ShopSearch element) {
        boundedShopSearch = element;

        setSearchQueryStartIndexInKeyword();

        setTitle();
        setSubtitle();
        loadImageIntoShopAvatar();
        setShopBadges();
        setItemViewOnClickListener();
    }

    private void setSearchQueryStartIndexInKeyword() {
        String displayName = boundedShopSearch.getKeyword();
        String searchTerm = boundedShopSearch.getSearchTerm();

        searchQueryStartIndexInKeyword = !TextUtils.isEmpty(searchTerm) ?
                displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault())) : -1;
    }

    private void setTitle() {
        if(searchQueryStartIndexInKeyword == -1){
            titleTextView.setText(boundedShopSearch.getKeyword());
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

    private void setShopBadges() {
        if (imageViewShopBadge != null) {
            if (!boundedShopSearch.getShopBadgeIconUrl().isEmpty()) {
                imageViewShopBadge.setVisibility(View.VISIBLE);
                ImageHandler.loadImageWithoutPlaceholder(imageViewShopBadge, boundedShopSearch.getShopBadgeIconUrl());
            }
            else {
                imageViewShopBadge.setVisibility(View.GONE);
            }
        }
    }

    private void setItemViewOnClickListener() {
        itemView.setOnClickListener(v -> {
            AutocompleteTracking.eventClickShopSearch(
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
}
