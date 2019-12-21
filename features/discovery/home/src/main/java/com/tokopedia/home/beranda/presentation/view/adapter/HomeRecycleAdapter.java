package com.tokopedia.home.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel;
import com.tokopedia.dynamicbanner.entity.PlayCardHome;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.RetryModel;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends HomeBaseAdapter<HomeAdapterFactory> {
    //without ticker
    static public final int POSITION_GEOLOCATION_WITHOUT_TICKER = 3;
    static public final int POSITION_HEADER_WITHOUT_TICKER = 1;

    //with ticker
    static public final int POSITION_GEOLOCATION_WITH_TICKER = 4;
    static public final int POSITION_HEADER_WITH_TICKER = 2;

    static public final int POSITION_UNDEFINED = -1;

    protected HomeAdapterFactory typeFactory;
    private RetryModel retryModel;

    public HomeRecycleAdapter(DiffUtil.ItemCallback<HomeVisitable> diffUtilCallback, HomeAdapterFactory adapterTypeFactory,
                              List<Visitable> visitables) {
        super(diffUtilCallback, adapterTypeFactory, visitables);
        typeFactory = adapterTypeFactory;
        this.visitables = visitables;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type(typeFactory);
    }

    //    @Override
//    public void onBindViewHolder(AbstractViewHolder holder, int position) {
//        holder.bind(visitables.get(position));
//        //check if visitable is homerecommendation, we will set newData = false after bind
//        //because newData = true will force viewholder to recreate tab and viewpager
//        if (visitables.get(position) instanceof HomeRecommendationFeedViewModel) {
//            ((HomeRecommendationFeedViewModel) visitables.get(position)).setNewData(false);
//        }
//    }

    public List<Visitable> getItems() {
        return visitables;
    }

    public void clearItems() {
        visitables.clear();
    }

    public void showRetry() {
//        if (this.visitables.contains(retryModel)) {
//            return;
//        }
//        int positionStart = getItemCount();
//        this.visitables.add(retryModel);
//        notifyItemRangeInserted(positionStart, 1);
    }

    public void removeRetry() {
//        int index = this.visitables.indexOf(retryModel);
//        this.visitables.remove(retryModel);
//        notifyItemRemoved(index);
    }

    //mapping another visitable to visitables from home_query
//    public void setItems(List<Visitable> visitables) {
//        this.visitables = visitables;
//        notifyDataSetChanged();
//    }

    public int hasReview() {
        for (int i = 0; i < visitables.size(); i++) {
            if (visitables.get(i) instanceof ReviewViewModel) {
                return i;
            }
        }
        return -1;
    }

    public void updateReviewItem(SuggestedProductReview suggestedProductReview) {
        if (visitables.get(hasReview()) instanceof ReviewViewModel && hasReview() != -1) {
            ((ReviewViewModel) visitables.get(hasReview())).setSuggestedProductReview(suggestedProductReview);
            notifyItemChanged(hasReview());
        }
    }

    public void updateHomeQueryItems(List<Visitable> newVisitable) {
        clearItems();
        this.visitables = newVisitable;
        notifyDataSetChanged();
    }

    public void removeGeolocationViewModel() {
        int removedPosition = removeGeolocation();
        if (removedPosition != -1) {
            notifyItemRemoved(removedPosition);
        }
    }

    public void removeReviewViewModel() {
        int reviewPosition = removeReview();
        if (reviewPosition != -1) {
            notifyItemRemoved(reviewPosition);
        }
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
            if (this.visitables.size() > POSITION_HEADER_WITHOUT_TICKER &&
                    this.visitables.get(POSITION_HEADER_WITHOUT_TICKER) instanceof HeaderViewModel) {
                return POSITION_HEADER_WITHOUT_TICKER;
            } else if (this.visitables.size() > POSITION_HEADER_WITH_TICKER &&
                    this.visitables.get(POSITION_HEADER_WITH_TICKER) instanceof HeaderViewModel) {
                return POSITION_HEADER_WITH_TICKER;
            } else {
                return POSITION_UNDEFINED;
            }
        }
        return POSITION_UNDEFINED;
    }

    private int hasGeolocationViewModel() {
        if (this.visitables != null && this.visitables.size() > 0) {
            if (this.visitables.size() > POSITION_GEOLOCATION_WITHOUT_TICKER &&
                    this.visitables.get(POSITION_GEOLOCATION_WITHOUT_TICKER) instanceof GeolocationPromptViewModel) {
                return POSITION_GEOLOCATION_WITHOUT_TICKER;
            } else if (this.visitables.size() > POSITION_GEOLOCATION_WITH_TICKER &&
                    this.visitables.get(POSITION_GEOLOCATION_WITH_TICKER) instanceof GeolocationPromptViewModel) {
                return POSITION_GEOLOCATION_WITH_TICKER;
            } else {
                return POSITION_UNDEFINED;
            }
        }
        return POSITION_UNDEFINED;
    }

    private int removeGeolocation() {
        for(int i=0; i<visitables.size(); i++){
            if(visitables.get(i) instanceof GeolocationPromptViewModel){
                visitables.remove(i);
                return i;
            }
        }
        return POSITION_UNDEFINED;
    }

    private int removeReview() {
        for(int i=0; i<visitables.size(); i++){
            if(visitables.get(i) instanceof ReviewViewModel){
                visitables.remove(i);
                return i;
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

    public void setPlayData(PlayCardHome playContentBanner, int adapterPosition) {
        if (visitables.get(adapterPosition) instanceof PlayCardViewModel) {
            ((PlayCardViewModel) visitables.get(adapterPosition)).setPlayCardHome(playContentBanner);
        }
        notifyItemChanged(adapterPosition);
    }

}
