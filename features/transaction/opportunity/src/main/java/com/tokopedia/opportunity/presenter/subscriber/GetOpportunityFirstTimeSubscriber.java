package com.tokopedia.opportunity.presenter.subscriber;

import com.tokopedia.opportunity.data.entity.opportunitycategorydata.FilterData;
import com.tokopedia.opportunity.data.entity.opportunitycategorydata.OpportunityCategoryData;
import com.tokopedia.opportunity.data.entity.opportunitycategorydata.OptionItem;
import com.tokopedia.opportunity.data.entity.opportunitycategorydata.SearchData;
import com.tokopedia.opportunity.data.entity.opportunitycategorydata.SortData;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.opportunity.data.OpportunityFilterModel;
import com.tokopedia.opportunity.domain.model.OpportunityFirstTimeModel;
import com.tokopedia.opportunity.listener.OpportunityListView;
import com.tokopedia.opportunity.viewmodel.FilterViewModel;
import com.tokopedia.opportunity.viewmodel.OptionViewModel;
import com.tokopedia.opportunity.viewmodel.SearchViewModel;
import com.tokopedia.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.opportunity.viewmodel.opportunitylist.OpportunityFilterViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 6/2/17.
 */

public class GetOpportunityFirstTimeSubscriber extends Subscriber<OpportunityFirstTimeModel> {

    private final OpportunityListView viewListener;

    public GetOpportunityFirstTimeSubscriber(OpportunityListView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorFirstTime(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(OpportunityFirstTimeModel opportunityFirstTimeModel) {
        viewListener.onSuccessFirstTime(
                GetOpportunitySubscriber.mappingToViewModel(
                        opportunityFirstTimeModel.getOpportunityModel()), 
                mappingToViewModel(
                        opportunityFirstTimeModel.getOpportunityFilterModel()));
    }


    private OpportunityFilterViewModel mappingToViewModel(OpportunityFilterModel opportunityFilterModel) {
        OpportunityCategoryData opportunityCategoryData = opportunityFilterModel.getOpportunityCategoryData();
        OpportunityFilterViewModel viewModel = new OpportunityFilterViewModel();
        viewModel.setListFilter(mapplingFilterToViewModel(opportunityCategoryData.getFilter()));
        viewModel.setListSortingType(mappingSortToViewModel(opportunityCategoryData.getSort()));
        return viewModel;
    }

    private ArrayList<SortingTypeViewModel> mappingSortToViewModel(ArrayList<SortData> sort) {
        ArrayList<SortingTypeViewModel> list = new ArrayList<>();

        for (SortData sortData : sort) {
            SortingTypeViewModel sortingTypeViewModel = new SortingTypeViewModel();
            sortingTypeViewModel.setName(sortData.getName());
            sortingTypeViewModel.setValue(sortData.getValue());
            sortingTypeViewModel.setKey(sortData.getKey());
            sortingTypeViewModel.setSelected(sortData.isDefault());
            list.add(sortingTypeViewModel);
        }
        return list;
    }

    private ArrayList<FilterViewModel> mapplingFilterToViewModel(
            ArrayList<FilterData> filterDatas) {
        ArrayList<FilterViewModel> list = new ArrayList<>();

        for (FilterData filterData : filterDatas) {
            FilterViewModel filterViewModel = new FilterViewModel();
            filterViewModel.setName(filterData.getTitle());
            filterViewModel.setListChild(mappingListOption(filterData.getOptionItemList()));
            filterViewModel.setSearchViewModel(mappingSearch(filterData.getSearchData()));
            list.add(filterViewModel);
        }
        return list;
    }

    private SearchViewModel mappingSearch(SearchData searchData) {
        SearchViewModel searchViewModel = new SearchViewModel();
        searchViewModel.setIsSearchable(searchData.getSearchable());
        searchViewModel.setPlaceholder(searchData.getPlaceholder());
        return searchViewModel;
    }

    private ArrayList<OptionViewModel> mappingListOption(List<OptionItem> optionItemList) {
        ArrayList<OptionViewModel> list = new ArrayList<>();
        for (OptionItem optionItem : optionItemList) {
            OptionViewModel viewModel = new OptionViewModel();
            viewModel.setName(optionItem.getName());
            viewModel.setIdentifier(optionItem.getIdentifier());
            viewModel.setIsHidden(optionItem.getHidden());
            viewModel.setKey(optionItem.getKey());
            viewModel.setParent(optionItem.getParent());
            viewModel.setValue(optionItem.getValue());
            viewModel.setTreeLevel(optionItem.getTree());
            if (optionItem.getListChild() != null)
                viewModel.setListChild(mappingListOption(optionItem.getListChild()));
            else
                viewModel.setListChild(new ArrayList<OptionViewModel>());
            list.add(viewModel);
        }
        return list;
    }
}
