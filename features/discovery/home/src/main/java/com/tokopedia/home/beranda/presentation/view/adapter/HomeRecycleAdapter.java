package com.tokopedia.home.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.GeolocationPromptViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeRecommendationFeedViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.RetryModel;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends BaseAdapter<HomeAdapterFactory> {

    //without ticker
    static public final int POSITION_GEOLOCATION_WITHOUT_TICKER = 3;
    static public final int POSITION_HEADER_WITHOUT_TICKER = 1;

    //with ticker
    static public final int POSITION_GEOLOCATION_WITH_TICKER = 4;
    static public final int POSITION_HEADER_WITH_TICKER = 2;

    static public final int POSITION_UNDEFINED = -1;

    protected HomeAdapterFactory typeFactory;
    private RetryModel retryModel;

    public HomeRecycleAdapter(HomeAdapterFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        this.typeFactory = adapterTypeFactory;
        this.retryModel = new RetryModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(visitables.get(position));
        //check if visitable is homerecommendation, we will set newData = false after bind
        //because newData = true will force viewholder to recreate tab and viewpager
        if (visitables.get(position) instanceof HomeRecommendationFeedViewModel) {
            ((HomeRecommendationFeedViewModel) visitables.get(position)).setNewData(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return visitables.size();
    }

    public Visitable getItem(int pos) {
        return visitables.get(pos);
    }

    public List<Visitable> getItems() {
        return visitables;
    }

    public void clearItems() {
        visitables.clear();
    }

    public void showRetry() {
        if (this.visitables.contains(retryModel)) {
            return;
        }
        int positionStart = getItemCount();
        this.visitables.add(retryModel);
        notifyItemRangeInserted(positionStart, 1);
    }

    public void removeRetry() {
        int index = this.visitables.indexOf(retryModel);
        this.visitables.remove(retryModel);
        notifyItemRemoved(index);
    }

    //mapping another visitable to visitables from home_query
    public void setItems(List<Visitable> visitables, HeaderViewModel headerViewModel) {
        this.visitables = visitables;
        addHomeHeaderViewModel(headerViewModel);
        notifyDataSetChanged();
    }

    public void updateHomeQueryItems(List<Visitable> newVisitable) {
        int headerHomePosition = hasHomeHeaderViewModel();
        if (headerHomePosition != POSITION_UNDEFINED) {
            newVisitable.add(headerHomePosition, getItems().get(headerHomePosition));
        }
        clearItems();
        this.visitables = newVisitable;

        notifyDataSetChanged();
    }

    public void removeGeolocationViewModel() {
        int removedPosition = removeGeolocation();
        notifyItemRemoved(removedPosition);
    }

    public void setHomeHeaderViewModel(HeaderViewModel homeHeaderViewModel) {
        int changedPosition = setHomeHeader(homeHeaderViewModel);
        if (changedPosition != POSITION_UNDEFINED) {
            notifyItemChanged(changedPosition);
        }
    }

    public void addHomeHeaderViewModel(HeaderViewModel homeHeaderViewModel) {
        int addedPosition = addHomeHeader(homeHeaderViewModel);
        if (addedPosition != POSITION_UNDEFINED) {
            notifyItemInserted(addedPosition);
        }
    }

    public void setGeolocationViewModel(GeolocationPromptViewModel geolocationViewModel) {
        int addedPosition = setGeolocation(geolocationViewModel);
        notifyItemInserted(addedPosition);
    }

    private boolean hasTicker() {
        return getItems().size() > 1 && getItems().get(1) instanceof TickerViewModel;
    }

    private int hasHomeHeaderViewModel() {
        if (this.visitables != null && this.visitables.size() > 0) {
            if (this.visitables.get(POSITION_HEADER_WITHOUT_TICKER) instanceof HeaderViewModel) {
                return POSITION_HEADER_WITHOUT_TICKER;
            } else if (this.visitables.get(POSITION_HEADER_WITH_TICKER) instanceof HeaderViewModel) {
                return POSITION_HEADER_WITH_TICKER;
            } else {
                return POSITION_UNDEFINED;
            }
        }
        return POSITION_UNDEFINED;
    }

    private int hasGeolocationViewModel() {
        if (this.visitables != null && this.visitables.size() > 0) {
            if (this.visitables.get(POSITION_GEOLOCATION_WITHOUT_TICKER) instanceof GeolocationPromptViewModel) {
                return POSITION_GEOLOCATION_WITHOUT_TICKER;
            } else if (this.visitables.get(POSITION_GEOLOCATION_WITH_TICKER) instanceof GeolocationPromptViewModel) {
                return POSITION_GEOLOCATION_WITH_TICKER;
            } else {
                return POSITION_UNDEFINED;
            }
        }
        return POSITION_UNDEFINED;
    }

    private int removeGeolocation() {
        switch (hasGeolocationViewModel()) {
            case POSITION_GEOLOCATION_WITH_TICKER: {
                this.visitables.remove(POSITION_GEOLOCATION_WITH_TICKER);
                return POSITION_GEOLOCATION_WITH_TICKER;
            }
            case POSITION_GEOLOCATION_WITHOUT_TICKER: {
                this.visitables.remove(POSITION_GEOLOCATION_WITHOUT_TICKER);
                return POSITION_GEOLOCATION_WITHOUT_TICKER;
            }
        }
        return POSITION_UNDEFINED;
    }

    private int setGeolocation(GeolocationPromptViewModel geolocationPromptViewModel) {
        switch (hasGeolocationViewModel()) {
            case POSITION_GEOLOCATION_WITH_TICKER: {
                this.visitables.set(POSITION_GEOLOCATION_WITH_TICKER, geolocationPromptViewModel);
                return POSITION_GEOLOCATION_WITH_TICKER;
            }
            case POSITION_GEOLOCATION_WITHOUT_TICKER: {
                this.visitables.set(POSITION_GEOLOCATION_WITHOUT_TICKER, geolocationPromptViewModel);
                return POSITION_GEOLOCATION_WITHOUT_TICKER;
            }
            case POSITION_UNDEFINED: {
                if (hasTicker()) {
                    this.visitables.add(
                            POSITION_GEOLOCATION_WITH_TICKER,
                            geolocationPromptViewModel
                    );
                    return POSITION_HEADER_WITH_TICKER;
                } else {
                    this.visitables.add(
                            POSITION_GEOLOCATION_WITHOUT_TICKER,
                            geolocationPromptViewModel
                    );
                    return POSITION_GEOLOCATION_WITHOUT_TICKER;
                }
            }
        }
        return POSITION_UNDEFINED;
    }

    //update and return updated position
    private int setHomeHeader(HeaderViewModel homeHeaderViewModel) {
        switch (hasHomeHeaderViewModel()) {
            case POSITION_HEADER_WITH_TICKER: {
                this.visitables.set(POSITION_HEADER_WITH_TICKER, homeHeaderViewModel);
                return POSITION_HEADER_WITH_TICKER;
            }
            case POSITION_HEADER_WITHOUT_TICKER: {
                this.visitables.set(POSITION_HEADER_WITHOUT_TICKER, homeHeaderViewModel);
                return POSITION_HEADER_WITHOUT_TICKER;
            }
        }
        return POSITION_UNDEFINED;
    }

    private int addHomeHeader(HeaderViewModel homeHeaderViewModel) {
        if (hasTicker()) {
            this.visitables.add(POSITION_HEADER_WITH_TICKER, homeHeaderViewModel);
            return POSITION_HEADER_WITH_TICKER;
        } else {
            this.visitables.add(POSITION_HEADER_WITHOUT_TICKER, homeHeaderViewModel);
            return POSITION_HEADER_WITHOUT_TICKER;
        }
    }

    public int getRecommendationFeedSectionPosition() {
        return visitables.size()-1;
    }

    public boolean isRetryShown() {
        return visitables.contains(retryModel);
    }
}
