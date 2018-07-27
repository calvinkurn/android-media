package com.tokopedia.flight.airport.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.PersistableBundle;

import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerBackgroundUseCase;

import javax.inject.Inject;

import rx.Subscriber;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class GetAirportListJobService extends JobService {
    public static final String AIRPORT_VERSION = "version";
    @Inject
    FlightAirportPickerBackgroundUseCase flightAirportPickerBackgroundUseCase;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        PersistableBundle bundle = jobParameters.getExtras();
        DaggerFlightAirportComponent
                .builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .flightAirportModule(new FlightAirportModule())
                .build().inject(this);
        flightAirportPickerBackgroundUseCase.execute(
                flightAirportPickerBackgroundUseCase.createRequestParams(bundle.getLong(AIRPORT_VERSION, 1)),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        jobFinished(jobParameters, true);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        jobFinished(jobParameters, !aBoolean);
                    }
                });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (flightAirportPickerBackgroundUseCase != null)
            flightAirportPickerBackgroundUseCase.unsubscribe();
        return true;
    }
}
