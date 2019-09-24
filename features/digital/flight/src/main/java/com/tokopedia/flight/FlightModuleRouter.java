package com.tokopedia.flight;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;

import java.io.File;

import rx.Observable;

public interface FlightModuleRouter {

    Observable<ProfileInfo> getProfile();

    File writeImage(String filePath, int qualityProcentage);

}
