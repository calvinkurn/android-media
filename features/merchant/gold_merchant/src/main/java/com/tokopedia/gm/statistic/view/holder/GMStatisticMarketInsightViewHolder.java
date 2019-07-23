package com.tokopedia.gm.statistic.view.holder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.view.adapter.GMMarketInsightAdapter;
import com.tokopedia.gm.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.gm.common.constant.GMCommonConstantKt.IMG_URL_ICON_LOCK_WHITE_GREEN;

/**
 * Created by normansyahputa on 11/23/16.
 */

public class GMStatisticMarketInsightViewHolder implements GMStatisticViewHolder {

    public interface Listener {
        void onButtonRedirectToClicked();

        void onReadMoreClicked();
    }

    private static final String DEFAULT_CATEGORY = "kaos";

    private View itemView;
    private TextView tvMarketInsightFooter;
    private TitleCardView titleCardView;
    private GMMarketInsightAdapter GMMarketInsightAdapter;
    private View overlayWarningView;
    private Button buttonRedirectTo;
    private TextView tvOverlayDescription;
    private ImageView imageViewLock;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public GMStatisticMarketInsightViewHolder(View view) {
        itemView = view;
        titleCardView = (TitleCardView) itemView.findViewById(R.id.market_insight_card_view);
        overlayWarningView = titleCardView.getContentView().findViewById(R.id.vg_market_insight_not_gm);
        buttonRedirectTo = titleCardView.getContentView().findViewById(R.id.button_redirect_to);
        tvOverlayDescription = titleCardView.getContentView().findViewById(R.id.text_view_overlay_description);
        imageViewLock = titleCardView.getContentView().findViewById(R.id.image_view_lock);
        tvOverlayDescription.setMovementMethod(LinkMovementMethod.getInstance());
        tvOverlayDescription.setText(
                createDescriptionWithSpannable(
                        itemView.getContext().getString(R.string.gm_statistic_get_access_to_see_gm_stat),
                        itemView.getContext().getString(R.string.gm_statistic_read_more)
                )
        );
        buttonRedirectTo.setText(view.getContext().getString(R.string.gm_statistic_upgrade_shop));
        buttonRedirectTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickGMStatBuyGMDetailTransaction(v.getContext());
                if (listener != null) {
                    listener.onButtonRedirectToClicked();
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tvMarketInsightFooter = (TextView) view.findViewById(R.id.market_insight_footer);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                titleCardView.getContext(), LinearLayoutManager.VERTICAL, false));
        GMMarketInsightAdapter = new GMMarketInsightAdapter(new ArrayList<GetKeyword.SearchKeyword>());
        recyclerView.setAdapter(GMMarketInsightAdapter);
        ImageHandler imageHandler = new ImageHandler(view.getContext());
        imageHandler.loadImage(imageViewLock, IMG_URL_ICON_LOCK_WHITE_GREEN);
    }

    private SpannableStringBuilder createDescriptionWithSpannable(
            String originalText,
            String readMoreText
    ) {
        SpannableString spannableText = new SpannableString(readMoreText);
        int startIndex = 0;
        int endIndex = spannableText.length();
        int color = itemView.getResources().getColor(R.color.merchant_green);
        spannableText.setSpan(color, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View view) {
                listener.onReadMoreClicked();
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
            }
        };
        spannableText.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannableText.setSpan(
                new StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return new SpannableStringBuilder(originalText).append(" ").append(spannableText);
    }

    /**
     * set category for footer
     */
    public void bindCategory(String categoryName) {
        if (TextUtils.isEmpty(categoryName)) {
            tvMarketInsightFooter.setVisibility(View.INVISIBLE);
        } else {
            tvMarketInsightFooter.setText(MethodChecker.fromHtml(
                    tvMarketInsightFooter.getContext().getString(
                            R.string.gm_statistic_these_keywords_are_based_on_category_x, categoryName)));
            tvMarketInsightFooter.setVisibility(View.VISIBLE);
        }
    }

    public void bindData(List<GetKeyword> getKeywords, boolean isGoldMerchant) {
        if (!isGoldMerchant) {
            displayOverlay();
            return;
        }

        if (getKeywords == null || getKeywords.size() <= 0) {
            displayEmptyState();
            return;
        }

        overlayWarningView.setVisibility(View.GONE);
        //[START] check whether all is empty
        int isNotEmpty = 0;
        for (GetKeyword getKeyword : getKeywords) {
            if (getKeyword.getSearchKeyword() == null || getKeyword.getSearchKeyword().isEmpty())
                isNotEmpty++;
        }

        // if all keyword empty
        if (isNotEmpty == getKeywords.size()) {
            displayEmptyState();
            return;
        }

        // remove null or empty
        for (int i = 0; i < getKeywords.size(); i++) {
            if (getKeywords.get(i) == null
                    || getKeywords.get(i).getSearchKeyword() == null
                    || getKeywords.get(i).getSearchKeyword().isEmpty())
                getKeywords.remove(i);
        }

        GetKeyword getKeyword = getKeywords.get(0);

        List<GetKeyword.SearchKeyword> searchKeyword = getKeyword.getSearchKeyword();
        GMMarketInsightAdapter.setSearchKeywords(searchKeyword);
        GMMarketInsightAdapter.notifyDataSetChanged();
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    public void bindNoShopCategory(boolean goldMerchant, boolean idlePowerMerchant) {
        if (goldMerchant) {
            if (idlePowerMerchant)
                displayOverlay();
            else
                displayEmptyState();
        } else {
            displayOverlay();
        }
    }

    private void displayOverlay() {
        titleCardView.setViewState(LoadingStateView.VIEW_CONTENT);
        overlayWarningView.setVisibility(View.VISIBLE);
        displayDummyContentKeyword();
    }

    private void displayDummyContentKeyword() {
        // create dummy data as replacement for non gold merchant user.
        List<GetKeyword.SearchKeyword> searchKeyword = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            GetKeyword.SearchKeyword searchKeyword1 = new GetKeyword.SearchKeyword();
            searchKeyword1.setFrequency(1000);
            searchKeyword1.setKeyword(
                    String.format(
                            titleCardView.getContext().getString(R.string.market_insight_item_non_gm_text),
                            Integer.toString(i)
                    )
            );
            searchKeyword.add(searchKeyword1);
        }

        bindCategory(DEFAULT_CATEGORY);

        GMMarketInsightAdapter.setSearchKeywords(searchKeyword);
        GMMarketInsightAdapter.notifyDataSetChanged();
    }

    private void displayEmptyState() {
        titleCardView.setEmptyViewRes(R.layout.partial_gm_statistic_market_insight_empty_state);
        titleCardView.setViewState(LoadingStateView.VIEW_EMPTY);
    }

    @Override
    public void setViewState(int viewState) {
        titleCardView.setViewState(viewState);
    }
}