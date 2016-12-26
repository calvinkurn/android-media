package com.tokopedia.discovery.dynamicfilter.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Filter;
import android.widget.Filterable;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.discovery.dynamicfilter.adapter.DynamicFilterOtherAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 7/12/16.
 */
public class DynamicFilterOtherPresenterImpl extends DynamicFilterOtherPresenter implements Filterable {
    com.tokopedia.core.discovery.model.Filter dynamicFilter;
    private Filter filter;
    private int maxListItem = 25;
    private int dataSize = 0;
    private static final String TAG = DynamicFilterOtherPresenterImpl.class.getSimpleName();
    private List<Option> options;

    public DynamicFilterOtherPresenterImpl(DynamicFilterOtherView view) {
        super(view);
    }

    @Override
    public String getMessageTAG() {
        return "DynamicFilterOtherPresenter";
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return "DynamicFilterOtherPresenter";
    }

    @Override
    public void initData(@NonNull Context context) {
        if (!isAfterRotate) {
            options = dynamicFilter.getOptions();
            boolean status = dynamicFilter.getSearch().getSearchable() == 1;
            view.toggleSearch(status, dynamicFilter.getSearch().getPlaceholder());
            List<RecyclerViewItem> viewItems = new ArrayList<>();
            if (options.size() > maxListItem) {
                for (int i = 0; i < maxListItem; i++) {
                    viewItems.add(DynamicFilterOtherAdapter.convertTo(options.get(i)));
                }
                view.setupAdapter(viewItems);
                view.setIsLoading(true);
            } else {
                for (Option option : options) {
                    viewItems.add(DynamicFilterOtherAdapter.convertTo(option));
                }
                view.setupAdapter(viewItems);
                view.setIsLoading(false);
            }
            dataSize += maxListItem;
            view.setupRecylerView();
        }
    }

    @Override
    public void loadMore(FragmentActivity activity) {
        int nextItem = dataSize + maxListItem;
        List<RecyclerViewItem> viewItems = new ArrayList<>();
        for (int i = dataSize; i < nextItem; i++) {
            if (i >= options.size()) {
                view.setIsLoading(false);
                break;
            }
            viewItems.add(DynamicFilterOtherAdapter.convertTo(options.get(i)));
        }
        view.addListItem(viewItems);
        dataSize += maxListItem;
        if (nextItem > dataSize) {
            view.setIsLoading(false);
        }
    }

    @Override
    public void fetchArguments(Bundle argument) {
        if (!isAfterRotate && argument != null) {
            dynamicFilter = Parcels.unwrap(argument.getParcelable(FILTER_DATA));
        }
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotate) {

        }
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new DataFilter<Option>(options);
        return filter;
    }

    public class DataFilter<T> extends Filter {
        private ArrayList<T> sourceObjects;

        public DataFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterSeq = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();
                for (T object : sourceObjects) {
                    Option option = (Option) object;
                    if (option.getName().toLowerCase().contains(filterSeq)) {
                        filter.add(object);
                    }
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            options = (ArrayList<Option>) results.values;
            List<RecyclerViewItem> viewItems = new ArrayList<>();
            if (options.size() > maxListItem) {
                for (int i = 0; i < maxListItem; i++) {
                    viewItems.add(DynamicFilterOtherAdapter.convertTo(options.get(i)));
                }
                view.setIsLoading(true);
            } else {
                for (int i = 0; i < options.size(); i++) {
                    viewItems.add(DynamicFilterOtherAdapter.convertTo(options.get(i)));
                }
                view.setIsLoading(false);
            }
            dataSize += maxListItem;
            view.setListItem(viewItems);
        }
    }

}
