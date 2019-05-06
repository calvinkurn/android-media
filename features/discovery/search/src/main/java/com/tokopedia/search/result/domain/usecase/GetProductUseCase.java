package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;
import java.util.Objects;

import rx.Observable;

public final class GetProductUseCase extends UseCase<SearchProductModel> {
    private static final String PARAMETER_ROWS = "8";
    private static final String APP_CHANGE_PARAMETER_ROW = "mainapp_change_parameter_row";
    
    private boolean changeParamRow;

    GetProductUseCase(RemoteConfig remoteConfig) {
        this.changeParamRow = remoteConfig.getBoolean(APP_CHANGE_PARAMETER_ROW, false);
    }
    
    @Override
    public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
        return null;
    }

    public RequestParams createInitializeSearchParam(Map<String, String> searchParameter) {
        return createInitializeSearchParam(searchParameter, false);
    }

    public RequestParams createInitializeSearchParam(Map<String, String> searchParameter, boolean forceSearch) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putAllString(searchParameter);
        putRequestParamsOtherParameters(requestParams, searchParameter, forceSearch);

        return requestParams;
    }

    private void putRequestParamsOtherParameters(RequestParams requestParams, Map<String, String> searchParameter, boolean forceSearch) {
        putRequestParamsSearchParameters(requestParams, searchParameter, forceSearch);

        putRequestParamsTopAdsParameters(requestParams, searchParameter);

        putRequestParamsDepartmentIdIfNotEmpty(requestParams, searchParameter);
    }

    private void putRequestParamsSearchParameters(RequestParams requestParams, Map<String, String> searchParameter, boolean forceSearch) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.ROWS, getSearchRows());
        requestParams.putString(SearchApiConst.OB, getSearchSort(searchParameter));
        requestParams.putString(SearchApiConst.START, getSearchStart(searchParameter));
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(SearchApiConst.Q, omitNewline(getSearchQuery(searchParameter)));
        requestParams.putString(SearchApiConst.UNIQUE_ID, searchParameter.get(SearchApiConst.UNIQUE_ID));
        requestParams.putBoolean(SearchApiConst.REFINED, forceSearch);
    }

    private String getSearchRows() {
        return (changeParamRow) ? PARAMETER_ROWS : SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS;
    }

    private String getSearchSort(Map<String, String> searchParameter) {
        String sort = searchParameter.get(SearchApiConst.OB);

        return !textIsEmpty(sort) ? sort : SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT;
    }

    private String getSearchStart(Map<String, String> searchParameter) {
        return String.valueOf(searchParameter.get(SearchApiConst.START));
    }

    private String getSearchQuery(Map<String, String> searchParameter) {
        return omitNewline(searchParameter.get(SearchApiConst.Q));
    }

    private String omitNewline(String text) {
        return String.valueOf(text).replace("\n", "");
    }

    private void putRequestParamsTopAdsParameters(RequestParams requestParams, Map<String, String> searchParameter) {
        requestParams.putInt(TopAdsParams.KEY_ITEM, 2);
        requestParams.putString(TopAdsParams.KEY_EP, TopAdsParams.DEFAULT_KEY_EP);
        requestParams.putString(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putBoolean(TopAdsParams.KEY_WITH_TEMPLATE, true);
        requestParams.putInt(TopAdsParams.KEY_PAGE, getTopAdsKeyPage(searchParameter));
    }

    private int getTopAdsKeyPage(Map<String, String> searchParameter) {
        try {
            String startString = searchParameter.get(SearchApiConst.START);
            startString = textIsEmpty(startString) ? "" : startString;

            int start = Integer.parseInt(Objects.requireNonNull(startString));
            int defaultValueStart = Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS);

            return (start / defaultValueStart + 1);
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void putRequestParamsDepartmentIdIfNotEmpty(RequestParams requestParams, Map<String, String> searchParameter) {
        String departmentId = searchParameter.get(SearchApiConst.SC);

        if (!textIsEmpty(departmentId)) {
            requestParams.putString(SearchApiConst.SC, departmentId);
            requestParams.putString(TopAdsParams.KEY_DEPARTEMENT_ID, departmentId);
        }
    }

    private boolean textIsEmpty(String text) {
        return text == null || text.length() == 0;
    }
}
