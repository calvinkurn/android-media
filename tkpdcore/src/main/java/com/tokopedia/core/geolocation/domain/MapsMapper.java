package com.tokopedia.core.geolocation.domain;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.geolocation.model.autocomplete.Data;
import com.tokopedia.core.geolocation.model.autocomplete.Prediction;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.PredictionResult;
import com.tokopedia.core.geolocation.model.coordinate.CoordinateModel;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public class MapsMapper implements IMapsMapper{

    @Override
    public AutoCompleteViewModel convertAutoCompleteModel(Data autoCompleteData, String query) {
        AutoCompleteViewModel autoCompleteViewModel = new AutoCompleteViewModel();
        List<PredictionResult> predictionResults = new ArrayList<>();
        for (Prediction predictions : autoCompleteData.getPredictions()) {
            PredictionResult simplifiedResult = new PredictionResult();
            String mainText = predictions.getStructuredFormatting().getMainText();
            String secondaryText = predictions.getStructuredFormatting().getSecondaryText();
            String mainTextToShow = mainText.replaceAll(query, "<b>" + query + "</b>");
            String secondaryTextToShow = secondaryText.replaceAll(query, "<b>" + query + "</b>");
            simplifiedResult.setMainTextFormatted(mainTextToShow);
            simplifiedResult.setSecondaryTextFormatted(secondaryTextToShow);
            simplifiedResult.setMainText(mainText);
            simplifiedResult.setSecondaryText(secondaryText);
            simplifiedResult.setPlaceId(predictions.getPlaceId());
            predictionResults.add(simplifiedResult);
        }
        autoCompleteViewModel.setListOfPredictionResults(predictionResults);
        return autoCompleteViewModel;
    }

    @Override
    public CoordinateViewModel convertAutoCompleteLocationId(CoordinateModel coordinateModel) {
        CoordinateViewModel coordinateViewModel = new CoordinateViewModel();
        LatLng coordinate = new LatLng(
                coordinateModel.getGeometry().getLocation().getLat(),
                coordinateModel.getGeometry().getLocation().getLng()
                );
        coordinateViewModel.setCoordinate(coordinate);
        return coordinateViewModel;
    }
}
