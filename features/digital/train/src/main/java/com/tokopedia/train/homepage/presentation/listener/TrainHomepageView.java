package com.tokopedia.train.homepage.presentation.listener;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.train.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;

import java.util.Date;
import java.util.List;

/**
 * @author Rizky on 21/02/18.
 */

public interface TrainHomepageView extends CustomerView {

    void renderSingleTripView(TrainHomepageViewModel trainHomepageViewModel);

    void renderRoundTripView(TrainHomepageViewModel trainHomepageViewModel);

    void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

    void showReturnDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

    void showDepartureDateShouldAtLeastToday(@StringRes int resId);

    void showDepartureDateMax90Days(@StringRes int resId);

    void showReturnDateShouldGreaterOrEqual(@StringRes int resId);

    void showReturnDateMax100Days(@StringRes int resId);

    void setHomepageViewModel(TrainHomepageViewModel viewModel);

    void navigateToSearchPage(TrainSearchPassDataViewModel passDataViewModel);

    TrainHomepageViewModel getHomepageViewModel();

    Activity getActivity();

    void showOriginStationEmptyError(@StringRes int resId);

    void showDestinationStationEmptyError(@StringRes int resId);

    void showOriginAndDestinationShouldNotSameError(@StringRes int resId);

    void showOriginAndDestinationIslandShouldBeTheSame(@StringRes int resId);

    void navigateToLoginPage();

    void closePage();

    void renderPromoList(List<TrainPromoViewModel> trainPromoViewModelList);

    void hidePromoList();

    void navigateToKaiWebView();

    void stopTrace();

    void showPromoList();
}
