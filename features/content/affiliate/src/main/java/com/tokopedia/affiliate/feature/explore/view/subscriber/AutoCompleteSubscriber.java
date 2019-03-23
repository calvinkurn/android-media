package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.affiliate.feature.explore.data.pojo.AutoCompletePojo;
import com.tokopedia.affiliate.feature.explore.data.pojo.AutoCompleteQuery;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.AutoCompleteViewModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by yfsx on 12/10/18.
 */
public class AutoCompleteSubscriber extends Subscriber<GraphqlResponse> {
    private ExploreContract.View mainView;

    public AutoCompleteSubscriber(ExploreContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(GraphqlResponse response) {
        AutoCompleteQuery query = response.getData(AutoCompleteQuery.class);
        List<AutoCompleteViewModel> autoCompleteViewModels = mappingData(query);

        mainView.hideLoading();
        if (!autoCompleteViewModels.isEmpty()) {
            mainView.onSuccessGetAutoComplete(autoCompleteViewModels);
        }
    }

    private List<AutoCompleteViewModel> mappingData(AutoCompleteQuery query) {
        List<AutoCompleteViewModel> modelList = new ArrayList<>();
        for (AutoCompletePojo pojo : query.getData().getMatch()) {
            AutoCompleteViewModel model = new AutoCompleteViewModel(
                    pojo.getText(),
                    pojo.getFormatted());
            modelList.add(model);
        }
        return modelList;
    }
}
