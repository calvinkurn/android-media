package com.tokopedia.logisticaddaddress.domain;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.uimodel.AutoCompleteUiModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.uimodel.PredictionResult;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel;
import com.tokopedia.logisticaddaddress.di.GeolocationScope;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.Data;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.Prediction;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.CoordinateModel;

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
    public AutoCompleteUiModel convertAutoCompleteModel(Data autoCompleteData, String query) {
        AutoCompleteUiModel autoCompleteUiModel = new AutoCompleteUiModel();
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
        autoCompleteUiModel.setListOfPredictionResults(predictionResults);
        return autoCompleteUiModel;
    }

    @Override
    public CoordinateUiModel convertAutoCompleteLocationId(CoordinateModel coordinateModel) {
        CoordinateUiModel coordinateUiModel = new CoordinateUiModel();
        LatLng coordinate = new LatLng(
                coordinateModel.getGeometry().getLocation().getLat(),
                coordinateModel.getGeometry().getLocation().getLng()
        );
        coordinateUiModel.setCoordinate(coordinate);
        coordinateUiModel.setTitle(coordinateModel.getName());
        coordinateUiModel.setAddress(coordinateModel.getFormattedAddress());
        coordinateUiModel.setPlaceId(coordinateModel.getPlaceId());
        return coordinateUiModel;
    }

    private String modifiedString(String text, String query) {
        return text.replaceAll(query, "<b>" + query + "</b>");
    }
}
