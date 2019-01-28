package com.tokopedia.logisticgeolocation.domain;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.Data;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.Prediction;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel.PredictionResult;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.CoordinateModel;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.logisticgeolocation.di.GeolocationScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
@GeolocationScope
public class MapsMapper implements IMapsMapper{

    @Inject
    public MapsMapper() {
    }

    @Override
    public AutoCompleteViewModel convertAutoCompleteModel(Data autoCompleteData, String query) {
        AutoCompleteViewModel autoCompleteViewModel = new AutoCompleteViewModel();
        List<PredictionResult> predictionResults = new ArrayList<>();
        for (Prediction predictions : autoCompleteData.getPredictions()) {
            PredictionResult simplifiedResult = new PredictionResult();
            String mainText = predictions.getStructuredFormatting().getMainText();
            String secondaryText = predictions.getStructuredFormatting().getSecondaryText();
            String mainTextToShow = modifiedString(mainText, query);
            String secondaryTextToShow = modifiedString(secondaryText, query);
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
        coordinateViewModel.setTitle(coordinateModel.getName());
        coordinateViewModel.setAddress(coordinateModel.getFormattedAddress());
        coordinateViewModel.setPlaceId(coordinateModel.getPlaceId());
        return coordinateViewModel;
    }

    private String modifiedString(String text, String query) {
        return text.replaceAll(query, "<b>" + query + "</b>");
    }
}
