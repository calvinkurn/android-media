package com.tokopedia.train.search.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.label.selection.SelectionItem;
import com.tokopedia.design.label.selection.text.SelectionTextLabelView;
import com.tokopedia.design.text.RangeInputView;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.search.presentation.contract.BaseTrainFilterListener;
import com.tokopedia.train.search.presentation.contract.FilterSearchActionView;
import com.tokopedia.train.search.presentation.model.FilterSearchData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 3/20/18.
 */

public class TrainFilterSearchFragment extends BaseDaggerFragment implements BaseTrainFilterListener {

    private static final String FILTER_SEARCH_DATA = "filter_search_data";

    private FilterSearchActionView listener;
    private FilterSearchData filterSearchData;
    private CurrencyTextWatcher minCurrencyTextWatcher;
    private CurrencyTextWatcher maxCurrencyTextWatcher;

    public static TrainFilterSearchFragment newInstance() {
        TrainFilterSearchFragment fragment = new TrainFilterSearchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_filter_search, container,
                false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            filterSearchData = savedInstanceState.getParcelable(FILTER_SEARCH_DATA);
        } else {
            filterSearchData = listener.getFilterSearchData();
        }

        listener.setTitleToolbar(getString(R.string.train_search_filter));
        listener.setCloseButton(true);

        populateView(view);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FILTER_SEARCH_DATA, filterSearchData);
    }

    private void populateView(View view) {
        renderPriceRangeFilter(view);
        renderTrainNameFilter(view);
        renderTrainDepartureFilter(view);
        renderTrainClassFilter(view);
    }

    private void renderTrainNameFilter(View view) {
        SelectionTextLabelView selectionTextLabelViewName = view.findViewById(R.id.selection_label_train_name);
        final List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        if (filterSearchData.getSelectedTrains() != null) {
            for (int i = 0; i < filterSearchData.getSelectedTrains().size(); i++) {
                SelectionItem<String> selectionItem = new SelectionItem<>();
                selectionItem.setKey(filterSearchData.getSelectedTrains().get(i));
                selectionItem.setValue(filterSearchData.getSelectedTrains().get(i));
                selectionItemList.add(selectionItem);
            }
        }
        selectionTextLabelViewName.setItemList(selectionItemList);
        selectionTextLabelViewName.setOnDeleteListener(selectionItem -> {
            for (int i = 0; i < filterSearchData.getSelectedTrains().size(); i++) {
                if (filterSearchData.getSelectedTrains().get(i).equals(selectionItem.getKey())) {
                    filterSearchData.getSelectedTrains().remove(i);
                }
            }
            listener.onChangeFilterSearchData(filterSearchData);
        });
        selectionTextLabelViewName.setOnClickListener(v -> listener.onNameFilterSearchTrainClicked());
    }

    private void renderTrainDepartureFilter(View view) {
        SelectionTextLabelView selectionTextLabelViewName = view.findViewById(R.id.selection_label_time_departure);
        final List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        if (filterSearchData.getSelectedDepartureTimeList() != null) {
            for (int i = 0; i < filterSearchData.getSelectedDepartureTimeList().size(); i++) {
                SelectionItem<String> selectionItem = new SelectionItem<>();
                selectionItem.setKey(filterSearchData.getSelectedDepartureTimeList().get(i));
                selectionItem.setValue(filterSearchData.getSelectedDepartureTimeList().get(i));
                selectionItemList.add(selectionItem);
            }
        }
        selectionTextLabelViewName.setItemList(selectionItemList);
        selectionTextLabelViewName.setOnDeleteListener(selectionItem -> {
            for (int i = 0; i < filterSearchData.getSelectedDepartureTimeList().size(); i++) {
                if (filterSearchData.getSelectedDepartureTimeList().get(i).equals(selectionItem.getKey())) {
                    filterSearchData.getSelectedDepartureTimeList().remove(i);
                }
            }
            listener.onChangeFilterSearchData(filterSearchData);
        });
        selectionTextLabelViewName.setOnClickListener(v -> listener.onDepartureFilterSearchTrainClicked());
    }

    private void renderTrainClassFilter(View view) {
        SelectionTextLabelView selectionTextLabelViewName = view.findViewById(R.id.selection_label_class_name);
        final List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        if (filterSearchData.getSelectedTrainClass() != null) {
            for (int i = 0; i < filterSearchData.getSelectedTrainClass().size(); i++) {
                SelectionItem<String> selectionItem = new SelectionItem<>();
                selectionItem.setKey(filterSearchData.getSelectedTrainClass().get(i));
                selectionItem.setValue(filterSearchData.getSelectedTrainClass().get(i));
                selectionItemList.add(selectionItem);
            }
        }
        selectionTextLabelViewName.setItemList(selectionItemList);
        selectionTextLabelViewName.setOnDeleteListener(selectionItem -> {
            for (int i = 0; i < filterSearchData.getSelectedTrainClass().size(); i++) {
                if (filterSearchData.getSelectedTrainClass().get(i).equals(selectionItem.getKey())) {
                    filterSearchData.getSelectedTrainClass().remove(i);
                }
            }
            listener.onChangeFilterSearchData(filterSearchData);
        });
        selectionTextLabelViewName.setOnClickListener(v -> listener.onClassFilterSearchTrainClicked());
    }

    private void renderPriceRangeFilter(View view) {
        RangeInputView rangeInputView = view.findViewById(R.id.price_filter_search);
        EditText minPriceEditText = rangeInputView.getMinValueEditText();
        EditText maxPriceEditText = rangeInputView.getMaxValueEditText();
        long minPrice = filterSearchData.getSelectedMinPrice() > 0 ? filterSearchData.getSelectedMinPrice() :
                filterSearchData.getMinPrice();
        long maxPrice = filterSearchData.getSelectedMaxPrice() > 0 ? filterSearchData.getSelectedMaxPrice() :
                filterSearchData.getMaxPrice();

        if (minCurrencyTextWatcher != null) {
            minPriceEditText.removeTextChangedListener(minCurrencyTextWatcher);
        }
        minCurrencyTextWatcher = new CurrencyTextWatcher(minPriceEditText, CurrencyEnum.RP);
        minPriceEditText.addTextChangedListener(minCurrencyTextWatcher);

        if (maxCurrencyTextWatcher != null) {
            maxPriceEditText.removeTextChangedListener(maxCurrencyTextWatcher);
        }
        maxCurrencyTextWatcher = new CurrencyTextWatcher(maxPriceEditText, CurrencyEnum.RP);
        maxPriceEditText.addTextChangedListener(maxCurrencyTextWatcher);

        rangeInputView.setPower(1);
        rangeInputView.setData((int) filterSearchData.getMinPrice(), (int) filterSearchData.getMaxPrice(),
                (int) minPrice, (int) maxPrice);
        rangeInputView.setOnValueChangedListener((minValue, maxValue, minBound, maxBound) -> {
            filterSearchData.setSelectedMinPrice(minValue);
            filterSearchData.setSelectedMaxPrice(maxValue);
            listener.onChangeFilterSearchData(filterSearchData);
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (FilterSearchActionView) context;
    }

    @Override
    public void resetFilter() {
        View view = getView();
        if (view == null) {
            return;
        }

        FilterSearchData filterSearchDataExisting = listener.getFilterSearchData();
        filterSearchData = filterSearchDataExisting.resetSelectedValue();
        populateView(view);
        listener.onChangeFilterSearchData(filterSearchData);
    }

    @Override
    public void changeFilterToOriginal() {
        // no need implementation
    }
}
