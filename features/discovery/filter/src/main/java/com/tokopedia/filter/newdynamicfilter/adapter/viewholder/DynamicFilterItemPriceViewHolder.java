package com.tokopedia.filter.newdynamicfilter.adapter.viewholder;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.design.price.PriceRangeInputView;
import com.tokopedia.design.text.RangeInputView;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.common.helper.NumberParseHelper;
import com.tokopedia.filter.newdynamicfilter.adapter.ExpandableItemSelectedListAdapter;
import com.tokopedia.filter.newdynamicfilter.adapter.PricePillsAdapter;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.decoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterItemPriceViewHolder extends DynamicFilterViewHolder {

    private TextView wholesaleTitle;
    private SwitchCompat wholesaleToggle;
    private View wholesaleContainer;
    private PriceRangeInputView priceRangeInputView;
    private final DynamicFilterView dynamicFilterView;
    private Option priceMinOption;
    private Option priceMaxOption;
    private Option priceMinMaxOption;
    private RecyclerView pricePillsRecyclerView;
    private PricePillsAdapter pricePillsAdapter;
    private int minBound = 0;
    private int maxBound = 0;

    public DynamicFilterItemPriceViewHolder(View itemView, final DynamicFilterView dynamicFilterView) {
        super(itemView);
        this.dynamicFilterView = dynamicFilterView;

        wholesaleTitle = itemView.findViewById(R.id.wholesale_title);
        wholesaleToggle = itemView.findViewById(R.id.wholesale_toggle);
        wholesaleContainer = itemView.findViewById(R.id.wholesale_container);
        priceRangeInputView = itemView.findViewById(R.id.price_range_input_view);
        pricePillsRecyclerView = itemView.findViewById(R.id.price_pill_recycler_view);
    }

    @Override
    public void bind(Filter filter) {
        String maxLabel = "";
        String minLabel = "";

        wholesaleContainer.setVisibility(View.GONE);

        int lastMinValue = 0;
        int lastMaxValue = 0;
        List<Option> priceRangeList = new ArrayList<>();

        for (Option option : filter.getOptions()) {
            String optionValue = dynamicFilterView.getFilterValue(option.getKey());

            if (Option.KEY_PRICE_MIN_MAX_RANGE.equals(option.getKey())) {
                minBound = TextUtils.isEmpty(option.getValMin()) ? 0 : Integer.parseInt(option.getValMin());
                maxBound = TextUtils.isEmpty(option.getValMax()) ? 0 : Integer.parseInt(option.getValMax());
                priceMinMaxOption = option;
            }

            if (Option.KEY_PRICE_MIN.equals(option.getKey())) {
                minLabel = option.getName();
                lastMinValue = TextUtils.isEmpty(optionValue) ? 0 : Integer.parseInt(optionValue);
                priceMinOption = option;
            }

            if (Option.KEY_PRICE_MAX.equals(option.getKey())) {
                maxLabel = option.getName();
                lastMaxValue = TextUtils.isEmpty(optionValue) ? 0 : Integer.parseInt(optionValue);
                priceMaxOption = option;
            }

            if (Option.KEY_PRICE_WHOLESALE.equals(option.getKey())) {
                bindWholesaleOptionItem(option);
            }

            if (Option.KEY_PRICE_RANGE_1.equals(option.getKey())
                    || Option.KEY_PRICE_RANGE_2.equals(option.getKey())
                    || Option.KEY_PRICE_RANGE_3.equals(option.getKey())) {
                priceRangeList.add(option);
            }
        }

        populatePricePills(priceRangeList);

        if ((lastMinValue != 0 && lastMinValue > maxBound)) {
            maxBound = lastMinValue;
        }

        if(lastMaxValue!= 0 && lastMaxValue > maxBound){
            maxBound = lastMaxValue;
        }

        if(lastMinValue != 0 && lastMinValue < minBound){
            minBound = lastMinValue;
        }

        if (lastMaxValue != 0 && lastMaxValue < minBound) {
            minBound = lastMaxValue;
        }

        int defaultMinValue;
        if (lastMinValue == 0) {
            defaultMinValue = minBound;
        } else {
            defaultMinValue = lastMinValue;
        }

        int defaultMaxValue;
        if (lastMaxValue == 0) {
            defaultMaxValue = maxBound;
        } else {
            defaultMaxValue = lastMaxValue;
        }

        priceRangeInputView.setGestureListener(getPriceRangeInputViewGestureListener());

        priceRangeInputView.setOnValueChangedListener(getPriceRangeInputViewOnValueChangeListener());

        priceRangeInputView.setData(minLabel, maxLabel, minBound, maxBound,
                defaultMinValue, defaultMaxValue);
    }

    private void populatePricePills(List<Option> priceRangeList) {
        if (priceRangeList.isEmpty()) {
            pricePillsRecyclerView.setVisibility(View.GONE);
            return;
        }

        pricePillsRecyclerView.setVisibility(View.VISIBLE);

        if (pricePillsRecyclerView.getLayoutManager() == null) {
            pricePillsRecyclerView.setLayoutManager(
                new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        int spacingBetween = itemView.getContext().getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8);
        int edgeMargin = itemView.getContext().getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16);

        if (pricePillsRecyclerView.getItemDecorationCount() == 0) {
            pricePillsRecyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(spacingBetween, edgeMargin));
        }

        pricePillsAdapter = new PricePillsAdapter(new PricePillsAdapter.Callback() {
            @Override
            public void onPriceRangeSelected(int minValue, int maxValue) {
                priceRangeInputView.setData(minBound, maxBound, minValue, maxValue);
                refreshPricePills();
                dynamicFilterView.onPriceRangeClicked();
            }

            @Override
            public void onPriceRangeRemoved() {
                priceRangeInputView.setData(minBound, maxBound, minBound, maxBound);
                refreshPricePills();
                dynamicFilterView.onPriceRangeClicked();
            }

            @Override
            public int getCurrentPriceMin() {
                return NumberParseHelper.safeParseInt(dynamicFilterView.getFilterValue(Option.KEY_PRICE_MIN));
            }

            @Override
            public int getCurrentPriceMax() {
                return NumberParseHelper.safeParseInt(dynamicFilterView.getFilterValue(Option.KEY_PRICE_MAX));
            }
        });
        pricePillsAdapter.setPricePills(priceRangeList);
        pricePillsRecyclerView.setAdapter(pricePillsAdapter);
    }

    private void refreshPricePills() {
        if (pricePillsAdapter != null) {
            pricePillsAdapter.refreshData();
        }
    }

    private PriceRangeInputView.GestureListener getPriceRangeInputViewGestureListener() {
        return new RangeInputView.GestureListener() {
            @Override
            public void onButtonRelease(int minValue, int maxValue) {
                refreshPricePills();
                dynamicFilterView.onPriceSliderRelease(minValue, maxValue);
            }

            @Override
            public void onButtonPressed(int minValue, int maxValue) {
                dynamicFilterView.onPriceSliderPressed(minValue, maxValue);
            }

            @Override
            public void onValueEditedFromTextInput(int minValue, int maxValue) {
                refreshPricePills();
                dynamicFilterView.onPriceEditedFromTextInput(minValue, maxValue);
            }
        };
    }

    private PriceRangeInputView.OnValueChangedListener getPriceRangeInputViewOnValueChangeListener() {
        return (minValue, maxValue, minBound, maxBound) -> {
            applyMinValueFilter(minValue, minBound);

            applyMaxValueFilter(maxValue, maxBound);
        };
    }

    private void applyMinValueFilter(int minValue, int minBound) {
        if (minValue == minBound) {
            dynamicFilterView.removeSavedTextInput(priceMinOption.getUniqueId());
        } else {
            dynamicFilterView.saveTextInput(priceMinOption.getUniqueId(), String.valueOf(minValue));
        }
    }

    private void applyMaxValueFilter(int maxValue, int maxBound) {
        if (maxValue == maxBound) {
            dynamicFilterView.removeSavedTextInput(priceMaxOption.getUniqueId());
        } else {
            dynamicFilterView.saveTextInput(priceMaxOption.getUniqueId(), String.valueOf(maxValue));
        }
    }

    private void bindWholesaleOptionItem(final Option option) {
        wholesaleContainer.setVisibility(View.VISIBLE);
        wholesaleTitle.setText(option.getName());

        wholesaleTitle.setOnClickListener(v -> wholesaleToggle.setChecked(!wholesaleToggle.isChecked()));

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                (buttonView, isChecked) -> dynamicFilterView.saveCheckedState(option, isChecked);

        bindSwitch(wholesaleToggle,
                dynamicFilterView.loadLastCheckedState(option),
                onCheckedChangeListener);
    }
}
