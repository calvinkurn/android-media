package com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel;

import java.util.List;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public class AutoCompleteViewModel {

    private List<PredictionResult> listOfPredictionResults;

    public List<PredictionResult> getListOfPredictionResults() {
        return listOfPredictionResults;
    }

    public void setListOfPredictionResults(List<PredictionResult> listOfPredictionResults) {
        this.listOfPredictionResults = listOfPredictionResults;
    }
}
