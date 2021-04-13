package com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.uimodel;

import java.util.List;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public class AutoCompleteUiModel {

    private List<PredictionResult> listOfPredictionResults;

    public List<PredictionResult> getListOfPredictionResults() {
        return listOfPredictionResults;
    }

    public void setListOfPredictionResults(List<PredictionResult> listOfPredictionResults) {
        this.listOfPredictionResults = listOfPredictionResults;
    }
}
