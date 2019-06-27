package com.tokopedia.topads.common.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Subscriber;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsBaseListPresenter<T extends CustomerView> extends BaseDaggerPresenter<T> {
    private final TopAdsDatePickerInteractor topAdsDatePickerInteractor;
    private final TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;
    protected final UserSessionInterface userSession;

    public TopAdsBaseListPresenter(TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                   TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                   UserSessionInterface userSession) {
        this.topAdsDatePickerInteractor = topAdsDatePickerInteractor;
        this.topAdsAddSourceTaggingUseCase = topAdsAddSourceTaggingUseCase;
        this.userSession = userSession;
    }

    public int getLastSelectionDatePickerIndex() {
        return topAdsDatePickerInteractor.getLastSelectionDatePickerIndex();
    }

    public int getLastSelectionDatePickerType() {
        return topAdsDatePickerInteractor.getLastSelectionDatePickerType();
    }

    public boolean isDateUpdated(Date startDate, Date endDate){
        if (startDate == null || endDate == null) {
            return true;
        }
        String dateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String dateTextCache = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getStartDate());
        if (!dateText.equalsIgnoreCase(dateTextCache)) {
            return true;
        }
        dateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate);
        dateTextCache = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getEndDate());
        if (!dateText.equalsIgnoreCase(dateTextCache)) {
            return true;
        }
        return false;
    }

    public Date getEndDate() {
        Calendar endCalendar = Calendar.getInstance();
        return topAdsDatePickerInteractor.getEndDate(endCalendar.getTime());
    }

    public Date getStartDate() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK);
        return topAdsDatePickerInteractor.getStartDate(startCalendar.getTime());
    }

    public void saveDate(Date startDate, Date endDate) {
        topAdsDatePickerInteractor.saveDate(startDate, endDate);
    }

    public void saveSelectionDatePicker(int selectionType, int lastSelection) {
        topAdsDatePickerInteractor.saveSelectionDatePicker(selectionType, lastSelection);
    }

    public void saveSourceTagging(@TopAdsSourceOption String source) {
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source),
                new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Void aVoid) {
                        //do nothing
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsAddSourceTaggingUseCase.unsubscribe();
    }

    protected String getShopId(){
        return userSession.getShopId();
    }
}
